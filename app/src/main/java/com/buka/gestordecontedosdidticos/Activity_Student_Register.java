package com.buka.gestordecontedosdidticos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Activity_Student_Register extends AppCompatActivity {


    private EditText edit_student_name, edit_student_email, edit_student_password;
    RadioGroup rg_student_university;
    RadioGroup rg_student_course;
    RadioButton rb_options_university;
    RadioButton rb_options_course;

    String showRadioButtonUniversity;
    String showRadioButtonCourse;

    private TextView text_login_student, text_create_emai;
    private Button btn_save_student_account;

    private FirebaseAuth firebaseAuthStudent;
    private DatabaseReference databaseReference;
    private ProgressDialog pd;

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(this, "Todos os Campos são Obrigatórios...", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
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


        edit_student_name = findViewById(R.id.edit_name_student);
        edit_student_email = findViewById(R.id.edit_email_student);
        edit_student_password = findViewById(R.id.edit_password_student);
        rg_student_university = findViewById(R.id.rg_university_student);
        rg_student_course = findViewById(R.id.rg_course);

        text_login_student = findViewById(R.id.text_about_App);
        btn_save_student_account = findViewById(R.id.btn_save_student_account);


        firebaseAuthStudent = FirebaseAuth.getInstance();


        text_login_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Student_Register.this, Activity_Teacher_Login.class);
                startActivity(intent);

            }
        });


        btn_save_student_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(Activity_Student_Register.this);
                pd.setMessage("Por favor Aguarde!");
                pd.show();

                String str_studentName = edit_student_name.getText().toString();
                String str_studentEmail = edit_student_email.getText().toString();
                String str_studentPassword = edit_student_password.getText().toString();


                if (TextUtils.isEmpty(str_studentName)) {
                    Toast.makeText(Activity_Student_Register.this, R.string.text_put_name, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(str_studentEmail)) {
                    Toast.makeText(Activity_Student_Register.this, R.string.text_put_email, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(str_studentPassword)) {
                    Toast.makeText(Activity_Student_Register.this, R.string.text_put_password, Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(str_studentEmail).matches()) {
                    Toast.makeText(Activity_Student_Register.this, R.string.text_missing_some_simbol, Toast.LENGTH_SHORT).show();


                } else if (!rb_options_university.isChecked() && !rb_options_course.isChecked()) {
                    Toast.makeText(Activity_Student_Register.this, "Todos os campos são obrigatórios", Toast.LENGTH_LONG).show();

                } else if (rb_options_university.isChecked() && !rb_options_course.isChecked()) {
                    Toast.makeText(Activity_Student_Register.this, "Selecione o seu Curso", Toast.LENGTH_LONG).show();

                } else if (!rb_options_university.isChecked() && rb_options_course.isChecked()) {
                    Toast.makeText(Activity_Student_Register.this, "Selecione a sua Universidade", Toast.LENGTH_LONG).show();

                } else {


                    rg_student_university.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int K) {

                            rb_options_university = rg_student_university.findViewById(K);

                            switch (K) {

                                case R.id.rb_metro_student:
                                    showRadioButtonUniversity = rb_options_university.getText().toString();
                                    break;

                                case R.id.rb_gregorio_student:
                                    showRadioButtonUniversity = rb_options_university.getText().toString();
                                    break;

                                default:
                            }

                        }

                    });

                    rg_student_course.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int L) {

                            rb_options_course = rg_student_course.findViewById(L);

                            switch (L) {

                                case R.id.rb_arquitetura:
                                    showRadioButtonCourse = rb_options_course.getText().toString();
                                    break;

                                case R.id.rb_computacao:
                                    showRadioButtonCourse = rb_options_course.getText().toString();
                                    break;

                                case R.id.rb_econimia:
                                    showRadioButtonCourse = rb_options_course.getText().toString();
                                    break;

                                case R.id.rb_eng_civil:
                                    showRadioButtonCourse = rb_options_course.getText().toString();
                                    break;

                                default:

                            }

                        }
                    });

                    register_user_student(str_studentName, str_studentEmail, str_studentPassword, showRadioButtonUniversity, showRadioButtonCourse
                    );

                }


            }
        });


    }

    private void register_user_student(final String studentName, String studentEmail, String studentPassword, final String showRadioButtonUniversity, final String showRadioButtonCourse) {

        firebaseAuthStudent.createUserWithEmailAndPassword(studentEmail, studentPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser currentUser = firebaseAuthStudent.getCurrentUser();
                    String userStudentId = null;

                    if (currentUser != null) {

                        userStudentId = currentUser.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("User_Student").child(userStudentId);


                        HashMap<String, String> hashUsers_Student = new HashMap<>();
                        hashUsers_Student.put("id", userStudentId);
                        hashUsers_Student.put("username", studentName);
                        hashUsers_Student.put("university", showRadioButtonUniversity);
                        hashUsers_Student.put("course", showRadioButtonCourse);

                        databaseReference.setValue(hashUsers_Student).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {


                                    Intent intent = new Intent(Activity_Student_Register.this, Activity_Teacher_Menu.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    Toast.makeText(Activity_Student_Register.this, "Registado com Sucesso...", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(Activity_Student_Register.this, "Erro ao criar conta...", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(Activity_Student_Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        });


                    }


                }
            }
        });

    }

}
