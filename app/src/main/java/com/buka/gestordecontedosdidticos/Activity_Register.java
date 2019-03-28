package com.buka.gestordecontedosdidticos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Activity_Register extends AppCompatActivity {

    EditText edit_name;
    EditText edit_email_account;
    EditText edit_password_account;
    EditText edit_prove_password;
    Button btn_save_account;
    ImageView image_user;


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


    }
}
