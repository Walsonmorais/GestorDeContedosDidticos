package com.buka.gestordecontedosdidticos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.buka.gestordecontedosdidticos.models.Model_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Profile extends AppCompatActivity {

    private RelativeLayout relative;
    private CircleImageView edit_circle_image_profile;
    private EditText edit_name_profile;
    private Button btn_edit_profile;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private final static int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("Users_Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        relative = findViewById(R.id.relative);
        edit_circle_image_profile = findViewById(R.id.edit_circle_image_profile);
        edit_name_profile = findViewById(R.id.edit_name_profile);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);

        progressDialog = new ProgressDialog(this);

        String uId = null;

        if (firebaseUser != null) {
            uId = firebaseUser.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Por favor espere");
            progressDialog.setMessage("Carregando o seu Perfil...");
            progressDialog.show();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Model_User model_user = dataSnapshot.getValue(Model_User.class);

                    if (model_user != null) {
                        edit_name_profile.setText(model_user.getUsername());
                        Picasso.get().load(model_user.getImage()).into(edit_circle_image_profile);

                    } else {
                        edit_name_profile.setText("");
                        edit_circle_image_profile.setImageResource(R.drawable.image_user_offline);
                    }

                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Activity_Profile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            edit_circle_image_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_PICK);
                }
            });

            btn_edit_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateProfile(edit_name_profile.getText().toString());
                }
            });

        } else {
            edit_circle_image_profile.setEnabled(false);
            btn_edit_profile.setEnabled(false);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser == null) {
            Snackbar.make(relative, "Faça um Login na sua Conta", Snackbar.LENGTH_LONG)
                    .setAction("Entrar", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Activity_Profile.this, Activity_Login.class);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    private void updateProfile(String edit_name_profile) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("username", edit_name_profile);

        reference.updateChildren(hashMap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                progressDialog = new ProgressDialog(Activity_Profile.this);
                progressDialog.setTitle("Por favor espere");
                progressDialog.setMessage("Atualizando sua Imagem...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Uri resultUri = result.getUri();
                String current_user_id = firebaseUser.getUid();

              /*  storageReference.child(current_user_id).putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        Log.i("Passou", "Imagem");

                        databaseReference.child("image").setValue(task.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    Log.i("Passou", "Imagem");

                                    progressDialog.dismiss();
                                    Toast.makeText(Activity_Profile.this, "Imagem atualizada com sucesso", Toast.LENGTH_LONG).show();
                                } else {

                                    Log.i("Não Passou", "Imagem");

                                    progressDialog.dismiss();
                                    Toast.makeText(Activity_Profile.this, "Imagem não atualizada", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });*/

                storageReference.child(current_user_id).putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Log.i("Passou", "Imagem");

                                databaseReference.child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Log.i("Passou", "Imagem");

                                            progressDialog.dismiss();
                                            Toast.makeText(Activity_Profile.this, "Imagem atualizada com sucesso", Toast.LENGTH_LONG).show();
                                        } else {

                                            Log.i("Não Passou", "Imagem");

                                            progressDialog.dismiss();
                                            Toast.makeText(Activity_Profile.this, "Imagem não atualizada", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_create_account_email) {
            Intent intent = new Intent(Activity_Profile.this, Activity_Register.class);
            startActivity(intent);
            return true;

        }

        if (id == R.id.menu_login) {
            Intent intent = new Intent(Activity_Profile.this, Activity_Login.class);
            startActivity(intent);
            return true;

        }

        if (id == R.id.menu_log_out) {
            firebaseAuth.signOut();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
