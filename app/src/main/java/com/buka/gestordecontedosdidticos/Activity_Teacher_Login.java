package com.buka.gestordecontedosdidticos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Teacher_Login extends AppCompatActivity {

    private EditText edit_email, edit_password;
    private TextView text_register, text_forget_password;
    private Button btn_login;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    private ProgressDialog progressDialog;
    private Dialog addItemDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);

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


        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        text_forget_password = findViewById(R.id.text_forget_password);
        btn_login = findViewById(R.id.btn_login);
        text_register = findViewById(R.id.text_register);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        text_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItemDialog = new Dialog(Activity_Teacher_Login.this);
                addItemDialog.setContentView(R.layout.layout_start_app);
                addItemDialog.setTitle(R.string.text_select_status);
                addItemDialog.show();


                final RadioButton rbt_teacher = addItemDialog.findViewById(R.id.rbt_teacher);
                final RadioButton rbt_student = addItemDialog.findViewById(R.id.rbt_student);
                final Button button_next = addItemDialog.findViewById(R.id.btn_next);


                button_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (!rbt_teacher.isChecked() && !rbt_student.isChecked()) {
                            Toast.makeText(Activity_Teacher_Login.this, R.string.text_select_status, Toast.LENGTH_SHORT).show();

                        } else if (rbt_teacher.isChecked()) {

                            Intent intent = new Intent(Activity_Teacher_Login.this, Activity_Teacher_Register.class);
                            startActivity(intent);
                        } else if (rbt_student.isChecked()) {

                            Intent intent = new Intent(Activity_Teacher_Login.this, Activity_Student_Register.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Activity_Teacher_Login.this, "Seleccione apenas um Estado", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Activity_Teacher_Login.this, R.string.text_put_email, Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Activity_Teacher_Login.this, R.string.text_missing_some_simbol, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Activity_Teacher_Login.this, R.string.text_put_password, Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog = new ProgressDialog(Activity_Teacher_Login.this);
                    progressDialog.setMessage("Por favor Aguarde!");
                    progressDialog.show();

                    UsersLogin(email, password);
                }
            }
        });


        text_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItemDialog = new Dialog(Activity_Teacher_Login.this);
                addItemDialog.setContentView(R.layout.layout_recover_password);
                addItemDialog.setTitle("Recuperar Password");

                final EditText edit_recover_password = addItemDialog.findViewById(R.id.edit_recover_password);
                final Button btn_sent_email_recover_password = addItemDialog.findViewById(R.id.btn_sent_email_recover_password);

                btn_sent_email_recover_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String email = edit_recover_password.getText().toString();

                        if (email.isEmpty()) {
                            Toast.makeText(Activity_Teacher_Login.this, "Insira o seu Email", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.setMessage("Processando");
                            progressDialog.show();
                            firebaseAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(Activity_Teacher_Login.this, "Entre no seu Email para recuperar sua conta", Toast.LENGTH_SHORT).show();
                                                addItemDialog.dismiss();

                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(Activity_Teacher_Login.this, "Falhou", Toast.LENGTH_SHORT).show();
                                                addItemDialog.dismiss();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Activity_Teacher_Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
                addItemDialog.show();


            }
        });

    }

    private void UsersLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Activity_Teacher_Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    reference = FirebaseDatabase.getInstance().getReference().child("Users_Teachers").child(firebaseAuth.getCurrentUser().getUid());

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            progressDialog.dismiss();

                            Intent intent = new Intent(Activity_Teacher_Login.this, Activity_Teacher_Menu.class);

                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressDialog.dismiss();

                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(Activity_Teacher_Login.this, "Falha na Autenticação", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}