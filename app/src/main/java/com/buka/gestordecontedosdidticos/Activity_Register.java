package com.buka.gestordecontedosdidticos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.buka.gestordecontedosdidticos.fragments.Fragment_Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Activity_Register extends AppCompatActivity {

    EditText edit_name;
    EditText edit_number;
    EditText edit_email_account;
    EditText edit_password_account;
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
                String user_number = edit_number.getText().toString();


                if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(email_account) || TextUtils.isEmpty(password_Account) || TextUtils.isEmpty(user_number)) {

                    Toast.makeText(Activity_Register.this, "All files are required", Toast.LENGTH_SHORT).show();

                } else {

                    RegisterUser(email_account, password_Account);

                }
            }
        });
    }

    private void RegisterUser(String email_account, String password_account) {

        firebaseAuth.createUserWithEmailAndPassword(email_account, password_account).addOnCompleteListener(Activity_Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(Activity_Register.this, "Congratulations, Your account is created.", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(Activity_Register.this, "Error in Register, search all parameters.", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


}
