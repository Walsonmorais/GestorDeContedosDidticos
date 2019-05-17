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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Activity_Register extends AppCompatActivity {

    EditText edit_name;
    EditText edit_number;
    EditText edit_email_account;
    EditText edit_password_account;
    EditText edit_confirm_password;
    Button btn_save_account;
    ImageView image_user;
    private FirebaseAuth firebaseAuth;

    DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_name = findViewById(R.id.edit_name);
        edit_email_account = findViewById(R.id.edit_email_account);
        edit_confirm_password = findViewById(R.id.edit_confirm_password);
        edit_password_account = findViewById(R.id.edit_password_account);
        btn_save_account = findViewById(R.id.btn_save_account);
        image_user = findViewById(R.id.image_user);
        edit_number = findViewById(R.id.edit_number);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_save_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_name = edit_name.getText().toString();
                String email_account = edit_email_account.getText().toString();
                String password_Account = edit_password_account.getText().toString();
                String confirm_password = edit_confirm_password.getText().toString();
                String user_number = edit_number.getText().toString();

                if (TextUtils.isEmpty(user_name)) {
                    Toast.makeText(Activity_Register.this, R.string.text_put_name, Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(email_account)) {
                    Toast.makeText(Activity_Register.this,R.string.text_put_email, Toast.LENGTH_SHORT).show();

                }
                else if (!email_account.contains("@")) {
                    Toast.makeText(Activity_Register.this,R.string.text_missing_some_simbol, Toast.LENGTH_SHORT).show();

                }
                else if (!email_account.contains("Gmail") && !email_account.contains("gmail")) {
                    Toast.makeText(Activity_Register.this,R.string.text_missing_Gmail, Toast.LENGTH_SHORT).show();

                }
                else if (!email_account.contains(".Com") && !email_account.contains(".com")) {
                    Toast.makeText(Activity_Register.this,R.string.text_missing_Com, Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(password_Account)) {
                    Toast.makeText(Activity_Register.this,R.string.text_put_password, Toast.LENGTH_SHORT).show();

                }
                else if (TextUtils.isEmpty(confirm_password)) {
                    Toast.makeText(Activity_Register.this,R.string.text_confirm_password, Toast.LENGTH_SHORT).show();

                }
                else if (!password_Account.equals(confirm_password)) {
                    Toast.makeText(Activity_Register.this,R.string.text_differents_password, Toast.LENGTH_SHORT).show();

                }
                else {

                    progressDialog = new ProgressDialog(Activity_Register.this);
                    progressDialog.setTitle(R.string.text_creating_account);
                    progressDialog.setMessage("Por favor Aguarde!");
                    progressDialog.show();

                    RegisterUser(user_name, user_number, email_account, password_Account);
                }
            }
        });
    }

    private void RegisterUser( final String user_name, final String user_number,String email_account, String password_account) {

        firebaseAuth.createUserWithEmailAndPassword(email_account, password_account)
                .addOnCompleteListener(Activity_Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String userId = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                        HashMap<String, String> hashUsers;
                        hashUsers = new HashMap<>();
                        hashUsers.put("id", userId);
                        hashUsers.put("username", user_name);
                        hashUsers.put("usernumber", user_number);

                        reference.setValue(hashUsers).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    Toast.makeText(Activity_Register.this,R.string.text_register_sucess, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                    Intent intent = new Intent(Activity_Register.this, Activity_Menu.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    Toast.makeText(Activity_Register.this,R.string.text_error_register, Toast.LENGTH_LONG).show();

                                }

                            }
                        });
                    }
                });
    }

}
