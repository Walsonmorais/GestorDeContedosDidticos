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

    private EditText username, email, password, department;
    private TextView text_login;
    private Button btn_save_account;
    RadioGroup radioGroup_teacher;
    RadioButton radioButton_Options, rb_metroT, rb_gregorioT;

    String showRadioButtonTeacher;


    private FirebaseAuth firebaseAuthTeacher;
    private DatabaseReference reference;
    private ProgressDialog pd;

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(this, "Todos os Campos são Obrigatórios...", Toast.LENGTH_SHORT).show();

    }

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


        username = findViewById(R.id.edit_name_teacher);
        email = findViewById(R.id.edit_email_account_teacher);
        password = findViewById(R.id.edit_password_account_teacher);
        department = findViewById(R.id.edit_department_teacher);
        radioGroup_teacher = findViewById(R.id.rg_teacher_university);
        rb_metroT = findViewById(R.id.rb_metro_teacher);
        rb_gregorioT = findViewById(R.id.rb_gregorio_teacher);


        text_login = findViewById(R.id.text_login_teacher);
        btn_save_account = findViewById(R.id.btn_save_account_teacher);


        firebaseAuthTeacher = FirebaseAuth.getInstance();

        text_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Activity_Teacher_Register.this, Activity_Teacher_Login.class));

            }
        });


        btn_save_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd = new ProgressDialog(Activity_Teacher_Register.this);
                pd.setMessage("Por favor Aguarde!");
                pd.show();

                String str_username = username.getText().toString();
                String str_email = email.getText().toString();
                String str_password = password.getText().toString();
                String str_department = department.getText().toString();


                //String metro_university = rb_metro.getText().toString();
                //String gregorio_university = rb_gregorio.getText().toString();


                if (TextUtils.isEmpty(str_username)) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_put_name, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_put_email, Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_missing_some_simbol, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(str_password)) {
                    Toast.makeText(Activity_Teacher_Register.this, R.string.text_put_password, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(str_department)) {
                    Toast.makeText(Activity_Teacher_Register.this, "Digite os seus Departamentos", Toast.LENGTH_SHORT).show();
                } else {

                    radioGroup_teacher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int I) {

                            radioButton_Options = radioGroup_teacher.findViewById(I);


                            switch (I) {
                                case R.id.rb_metro_teacher:

                                    showRadioButtonTeacher = radioButton_Options.getText().toString();

                                    break;

                                case R.id.rb_gregorio_teacher:

                                    showRadioButtonTeacher = radioButton_Options.getText().toString();

                                    break;

                                default:
                            }

                        }
                    });

                    registerUser_teacher(str_username, str_email, str_password, str_department, showRadioButtonTeacher);
                }
            }
        });


    }

    private void registerUser_teacher(final String username, String email, String password, final String department, final String showRadioButton) {

        firebaseAuthTeacher.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser currentUser = firebaseAuthTeacher.getCurrentUser();
                            String userId = null;

                            if (currentUser != null) {
                                userId = currentUser.getUid();


                                reference = FirebaseDatabase.getInstance().getReference().child("Users_Teacher").child(userId);

                                HashMap<String, String> hashUsers_Teacher = new HashMap<>();
                                hashUsers_Teacher.put("id", userId);
                                hashUsers_Teacher.put("username", username);
                                hashUsers_Teacher.put("department", department);
                                hashUsers_Teacher.put("university", showRadioButton);
                                hashUsers_Teacher.put("image", "");

                                reference.setValue(hashUsers_Teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {


                                            Intent intent = new Intent(Activity_Teacher_Register.this, Activity_Teacher_Menu.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                            Toast.makeText(Activity_Teacher_Register.this, "Registado com Sucesso...", Toast.LENGTH_SHORT).show();


                                        } else {

                                            Toast.makeText(Activity_Teacher_Register.this, "Erro ao criar conta...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

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