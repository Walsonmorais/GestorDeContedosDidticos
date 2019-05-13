package com.buka.gestordecontedosdidticos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText edit_email;
    EditText edit_password;
    TextView text_forget_password;
    Button btn_login;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.edit_email);

        edit_password = findViewById(R.id.edit_password);

        text_forget_password = findViewById(R.id.text_forget_password);

        btn_login = findViewById(R.id.btn_login);

        firebaseAuth = FirebaseAuth.getInstance();


        text_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();

                if (TextUtils.isEmpty(email)) {

                    Toast.makeText(LoginActivity.this, R.string.text_put_email, Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@")) {
                    Toast.makeText(LoginActivity.this, R.string.text_missing_some_simbol, Toast.LENGTH_SHORT).show();

                } else if (!email.contains("Gmail") && !email.contains("gmail")) {
                    Toast.makeText(LoginActivity.this, R.string.text_missing_Gmail, Toast.LENGTH_SHORT).show();

                } else if (!email.contains(".Com") && !email.contains(".com")) {
                    Toast.makeText(LoginActivity.this, R.string.text_missing_Com, Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, R.string.text_put_password, Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Por favor Aguarde!");
                    progressDialog.show();

                    UserLogin(email, password);
                }
            }
        });

    }

    private void UserLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            progressDialog.dismiss();

                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

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
                    Toast.makeText(LoginActivity.this, "Falha na Autenticação", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}