package com.buka.gestordecontedosdidticos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Activity_Add_Files extends AppCompatActivity {


    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int STORAGE_REQUEST_CODE = 200;

    public static final int IMAGE_PICK_CAMERA_CODE = 300;
    public static final int IMAGE_PICK_GALLERY_CODE = 400;

    Uri image_uri = null;

    String[] cameraPermission;
    String[] storagePermission;

    String name, email, uId, dp;

    DatabaseReference databaseReference;


    ImageView image_posted;
    EditText edit_subject, edit_theme, edit_course, edit_year;
    Button btn_upload;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_files);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Activity_Add_Files.this, Activity_Teacher_Menu.class));
                finish();
            }
        });

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        image_posted = findViewById(R.id.add_image);
        edit_subject = findViewById(R.id.edit_add_subject);
        edit_theme = findViewById(R.id.edit_add_theme);
        edit_course = findViewById(R.id.edit_add_course);
        edit_year = findViewById(R.id.edit_add_year);
        btn_upload = findViewById(R.id.btn_upload);

        pd = new ProgressDialog(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users_Teacher");
        Query query = databaseReference.orderByChild("email").equalTo(email);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    name = "" + ds.child("name").getValue();
                    email = "" + ds.child("email").getValue();
                    dp = "" + ds.child("image").getValue();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        image_posted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Subject = edit_subject.getText().toString();
                String Theme = edit_theme.getText().toString();
                String Course = edit_course.getText().toString();
                String Year = edit_year.getText().toString();

                if (image_uri == null) {

                    if (TextUtils.isEmpty(Subject)) {

                        Toast.makeText(Activity_Add_Files.this, "Digite a Área de Estudo...", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    if (TextUtils.isEmpty(Theme)) {

                        Toast.makeText(Activity_Add_Files.this, "Digite o Tema...", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    if (TextUtils.isEmpty(Course)) {

                        Toast.makeText(Activity_Add_Files.this, "Digite o Curso...", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    if (TextUtils.isEmpty(Year)) {

                        Toast.makeText(Activity_Add_Files.this, "Digite o Ano...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    uploadData(Subject, Theme, Course, Year, "noImage");

                } else {

                    uploadData(Subject, Theme, Course, Year, String.valueOf(image_uri));

                }
            }


        });
    }

    private void uploadData(final String subject, final String theme, final String course, final String year, String uri) {
        pd.setMessage("Posting image...");
        pd.show();

        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Post/" + "post" + timeStamp;

        if (!uri.equals("noImage")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            storageReference.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    String downloadUri = uriTask.getResult().toString();

                    if (uriTask.isSuccessful()) {

                        HashMap<Object, String> hashMap = new HashMap<>();
                        hashMap.put("uId", uId);
                        hashMap.put("name", name);
                        hashMap.put("email", email);
                        hashMap.put("dp", dp);
                        hashMap.put("pId", timeStamp);
                        hashMap.put("subject", subject);
                        hashMap.put("theme", theme);
                        hashMap.put("course", course);
                        hashMap.put("year", year);
                        hashMap.put("Image", downloadUri);
                        hashMap.put("Time", timeStamp);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                        reference.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                pd.dismiss();

                                Toast.makeText(Activity_Add_Files.this, "Imagem publicada...", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                pd.dismiss();
                                Toast.makeText(Activity_Add_Files.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        });
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    pd.dismiss();
                    Toast.makeText(Activity_Add_Files.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            HashMap<Object, String> hashMap = new HashMap<>();
            hashMap.put("uId", uId);
            hashMap.put("name", name);
            hashMap.put("email", email);
            hashMap.put("dp", dp);
            hashMap.put("pId", timeStamp);
            hashMap.put("subject", subject);
            hashMap.put("theme", theme);
            hashMap.put("course", course);
            hashMap.put("year", year);
            hashMap.put("Image", "noImage");
            hashMap.put("Time", timeStamp);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
            reference.child(timeStamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    pd.dismiss();

                    Toast.makeText(Activity_Add_Files.this, " publicação sem imagem...", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    pd.dismiss();
                    Toast.makeText(Activity_Add_Files.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });

        }
    }

    private void showImageDialog() {
        String[] option = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose from");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    //Camera
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                }
                if (which == 1) {
                    //Gallery
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }

                }

            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {

        Intent intent_gallery = new Intent(Intent.ACTION_PICK);
        intent_gallery.setType("image/*");
        startActivityForResult(intent_gallery, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);


    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {

                        pickFromCamera();

                    } else {
                        Toast.makeText(this, "Camera & Storage both permission are necessary", Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {

                        pickFromGallery();

                    } else {
                        Toast.makeText(this, "Storage permission is necessary", Toast.LENGTH_SHORT).show();
                    }

                } else {
                }
            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) ;

            image_uri = data.getData();

            image_posted.setImageURI(image_uri);
        } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {

            image_posted.setImageURI(image_uri);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}