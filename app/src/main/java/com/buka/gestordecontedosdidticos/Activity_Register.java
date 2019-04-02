package com.buka.gestordecontedosdidticos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class Activity_Register extends AppCompatActivity {

    EditText edit_name;
    EditText edit_number;
    EditText edit_email_account;
    EditText edit_password_account;
    EditText edit_prove_password;
    Button btn_save_account;
    ImageView image_user;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edit_name = findViewById(R.id.edit_name);

        edit_email_account = findViewById(R.id.edit_email_account);

        edit_password_account = findViewById(R.id.edit_password_account);

        edit_prove_password = findViewById(R.id.edit_prove_password);

        btn_save_account = findViewById(R.id.btn_save_account);

        image_user = findViewById(R.id.image_user);

        edit_number = findViewById(R.id.edit_number);

        firebaseAuth = FirebaseAuth.getInstance();


        btn_save_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_account = edit_email_account.getText().toString().trim();
                String password_Account = edit_password_account.getText().toString().trim();

                RegisterUser(email_account,password_Account);



            }
        });
    }

    private void RegisterUser(String email_account, String password_account) {

        firebaseAuth.createUserWithEmailAndPassword(email_account, password_account)
                .addOnCompleteListener(Activity_Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(Activity_Register.this, "Usuário Cadastrado bem Sucedido", Toast.LENGTH_SHORT).show();

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Profile()).commit();
                            finish();

                        }else{

                            Toast.makeText(Activity_Register.this, "Erro no Cadastro", Toast.LENGTH_SHORT).show();
                    }}

                });

    }
}
