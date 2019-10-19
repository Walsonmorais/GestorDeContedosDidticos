package com.buka.gestordecontedosdidticos;

import android.app.Dialog;
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

public class Activity_Student_Login extends AppCompatActivity {


    private EditText edt_studentEmail, edt_studentPassword;
    private TextView txt_forget_password, txt_student_register;
    private Button btn_student_login;

    private FirebaseAuth firebaseAuthStudent;
    DatabaseReference databaseReference;

    private ProgressDialog progressdialog;
    private Dialog addItemDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);



        edt_studentEmail = findViewById(R.id.edit_email_student);
        edt_studentPassword = findViewById(R.id.edit_password_student);
        txt_forget_password = findViewById(R.id.txt_forget_password);
        txt_student_register = findViewById(R.id.txt_register_student);
        btn_student_login = findViewById(R.id.btn_login_student);

        firebaseAuthStudent = FirebaseAuth.getInstance();
        progressdialog = new ProgressDialog(this);


        txt_student_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItemDialog = new Dialog(Activity_Student_Login.this);
                addItemDialog.setContentView(R.layout.layout_start_app);
                addItemDialog.setTitle(R.string.text_select_status);
                addItemDialog.show();


                final RadioButton rbtn_teacher = addItemDialog.findViewById(R.id.rbt_teacher);
                final RadioButton rbtn_student = addItemDialog.findViewById(R.id.rbt_student);
                final Button btn_next = addItemDialog.findViewById(R.id.btn_next);


                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (!rbtn_teacher.isChecked() && !rbtn_student.isChecked()) {
                            Toast.makeText(Activity_Student_Login.this, R.string.text_select_status, Toast.LENGTH_SHORT).show();

                        } else if (rbtn_teacher.isChecked()) {

                            Intent intent = new Intent(Activity_Student_Login.this, Activity_Teacher_Register.class);
                            startActivity(intent);


                        } else if (rbtn_student.isChecked()) {

                            Intent intent = new Intent(Activity_Student_Login.this, Activity_Student_Register.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Activity_Student_Login.this, "Seleccione apenas um Estado", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                txt_forget_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addItemDialog = new Dialog(Activity_Student_Login.this);
                        addItemDialog.setContentView(R.layout.layout_recover_password);
                        addItemDialog.setTitle("Recuperar a Password");

                        final EditText edt_recoverPassword = addItemDialog.findViewById(R.id.edit_recover_password);
                        final Button button_sent_EmailRecover = addItemDialog.findViewById(R.id.btn_sent_email_recover_password);

                        button_sent_EmailRecover.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String myEmail = edt_recoverPassword.getText().toString();

                                if (myEmail.isEmpty()) {
                                    Toast.makeText(Activity_Student_Login.this, "Insira o seu Email", Toast.LENGTH_SHORT).show();
                                } else {

                                    progressdialog.setMessage("Processando");
                                    progressdialog.show();

                                    firebaseAuthStudent.sendPasswordResetEmail(myEmail)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        progressdialog.dismiss();
                                                        Toast.makeText(Activity_Student_Login.this, "Entre no seu Email para recuperar sua conta", Toast.LENGTH_SHORT).show();
                                                        addItemDialog.dismiss();

                                                    } else {
                                                        progressdialog.dismiss();
                                                        Toast.makeText(Activity_Student_Login.this, "Falhou", Toast.LENGTH_SHORT).show();
                                                        addItemDialog.dismiss();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressdialog.dismiss();
                                                    Toast.makeText(Activity_Student_Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
                        addItemDialog.show();


                    }
                });


                btn_student_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str_email = edt_studentEmail.getText().toString();
                        String str_password = edt_studentPassword.getText().toString();

                        if (TextUtils.isEmpty(str_email)) {
                            Toast.makeText(Activity_Student_Login.this, R.string.text_put_email, Toast.LENGTH_SHORT).show();

                        } else if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
                            Toast.makeText(Activity_Student_Login.this, R.string.text_missing_some_simbol, Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(str_password)) {
                            Toast.makeText(Activity_Student_Login.this, R.string.text_put_password, Toast.LENGTH_SHORT).show();

                        } else {
                            progressdialog = new ProgressDialog(Activity_Student_Login.this);
                            progressdialog.setMessage("Por favor Aguarde!");
                            progressdialog.show();

                            StudentLogin(str_email, str_password);
                        }

                    }
                });
            }
        });

    }

    private void StudentLogin(String str_email, String str_password) {

        firebaseAuthStudent.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(Activity_Student_Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users_Student").child(firebaseAuthStudent.getCurrentUser().getUid());

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            progressdialog.dismiss();

                            Intent intent = new Intent(Activity_Student_Login.this, Activity_Teacher_Menu.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            progressdialog.dismiss();

                        }
                    });
                } else {
                    progressdialog.dismiss();
                    Toast.makeText(Activity_Student_Login.this, "Falha na Autenticação", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
