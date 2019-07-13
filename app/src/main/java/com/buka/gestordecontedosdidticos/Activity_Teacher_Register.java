package com.buka.gestordecontedosdidticos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Activity_Teacher_Register extends AppCompatActivity {

    private EditText edit_name_teacher, edit_email_account_teacher, edit_password_teacher_account, edit_confirm_password_teacher;
    private TextView text_login;
    private RadioGroup rbt_group;
    private RadioButton rbt_teacher, rbt_student;
    private AutoCompleteTextView auto_complete_teacher, auto_complete_student;
    private Button btn_save_account;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

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



        edit_name_teacher = findViewById(R.id.edit_name_teacher);
        edit_email_account_teacher = findViewById(R.id.edit_email_account_teacher);
        edit_confirm_password_teacher = findViewById(R.id.edit_confirm_password_teacher);
        edit_password_teacher_account = findViewById(R.id.edit_password_account_teacher);
        text_login = findViewById(R.id.text_login_teacher);
        btn_save_account = findViewById(R.id.btn_save_account_teacher);
        rbt_group = findViewById(R.id.rbt_group_teacher);
        rbt_teacher = findViewById(R.id.rbt_teacher);
        rbt_student = findViewById(R.id.rbt_student);
        auto_complete_teacher = findViewById(R.id.auto_complete_teacher);
        auto_complete_student = findViewById(R.id.auto_complete_student);



        firebaseAuth = FirebaseAuth.getInstance();

        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent(Activity_Teacher_Register.this, Activity_Teacher_Login.class));

            }
        });

        btn_save_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_name__teacher = edit_name_teacher.getText().toString();
                String email_account_teacher = edit_email_account_teacher.getText().toString();
                String password_Account = edit_password_teacher_account.getText().toString();
                String confirm_password = edit_confirm_password_teacher.getText().toString();
                String teacher_occupations = auto_complete_teacher.getText().toString();
                String student_occupations = auto_complete_student.getText().toString();

                if (TextUtils.isEmpty(user_name__teacher)) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_put_name, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(email_account_teacher)) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_put_email, Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email_account_teacher).matches()) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_missing_some_simbol, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(password_Account)) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_put_password, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(confirm_password)) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_confirm_password, Toast.LENGTH_SHORT).show();

                } else if (!password_Account.equals(confirm_password)) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_differents_password, Toast.LENGTH_SHORT).show();

                } else if (!rbt_teacher.isChecked() && !rbt_student.isChecked()) {
                    Toast.makeText(Activity_Teacher_Register.this, "Selecione o seu status", Toast.LENGTH_SHORT).show();
                } else if (rbt_teacher.isChecked() && teacher_occupations.isEmpty()) {
                    auto_complete_teacher.setError("Preencha a sua profissão");

                } else if (rbt_student.isChecked() && student_occupations.isEmpty()) {
                    auto_complete_student.setError("Preencha a sua profissão");
                } else {

                    progressDialog = new ProgressDialog(Activity_Teacher_Register.this);
                    progressDialog.setTitle(R.string.text_creating_account);
                    progressDialog.setMessage("Por favor Aguarde!");
                    progressDialog.show();

                    registerUser_teacher(user_name__teacher, email_account_teacher, password_Account, teacher_occupations, student_occupations);
                }
            }
        });

        rbt_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbt_teacher) {
                    auto_complete_teacher.setVisibility(View.VISIBLE);
                    auto_complete_student.setVisibility(View.GONE);

                } else if (checkedId == R.id.rbt_student) {
                    auto_complete_teacher.setVisibility(View.GONE);
                    auto_complete_student.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void registerUser_teacher(final String user_name, String email_account, String password_account, final String teacher_occupations, final String student_occupations) {
        firebaseAuth.createUserWithEmailAndPassword(email_account, password_account)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task .isSuccessful()) {

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uId = null;

                            if (currentUser != null) {
                                uId = currentUser.getUid();

                                if (rbt_teacher.isChecked()) {

                                    reference = FirebaseDatabase.getInstance().getReference().child("User Teacher").child(uId);

                                    HashMap<String, String> hashUsers_Teacher = new HashMap<>();
                                    hashUsers_Teacher.put("id", uId);
                                    hashUsers_Teacher.put("username", user_name);
                                    hashUsers_Teacher.put("teacher_occupations", teacher_occupations);
                                    hashUsers_Teacher.put("image", "");

                                    reference.setValue(hashUsers_Teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();

                                                Toast.makeText(Activity_Teacher_Register.this, "Conta criada com sucessos Docente", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getApplicationContext(), Activity_Menu.class);
                                                startActivity(intent);

                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(Activity_Teacher_Register.this, "Erro ao criar conta", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else if (rbt_student.isChecked()) {

                                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);

                                    HashMap<String, String> hashUsers_Student = new HashMap<>();
                                    hashUsers_Student.put("id", uId);
                                    hashUsers_Student.put("username", user_name);
                                    hashUsers_Student.put("student_occupations", student_occupations);
                                    hashUsers_Student.put("image", "https://firebasestorage.googleapis.com/v0/b/gestordeconteudosdidaticos.appspot.com/o/Users_Images%2Fuser.png?alt=media&token=9ca957db-6d4b-48c8-a7c3-a03925c48ebb");

                                    reference.setValue(hashUsers_Student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                progressDialog.dismiss();

                                                Toast.makeText(Activity_Teacher_Register.this, "Conta criada com sucessos Aluno", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getApplicationContext(), Activity_Menu.class);
                                                startActivity(intent);


                                            } else {

                                                progressDialog.dismiss();

                                                Toast.makeText(Activity_Teacher_Register.this, "Erro ao criar conta", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Activity_Teacher_Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}