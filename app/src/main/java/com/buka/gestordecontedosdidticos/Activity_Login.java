package com.buka.gestordecontedosdidticos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_Login extends AppCompatActivity {

    EditText edit_email;
    EditText edit_password;
    TextView text_forget_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.edit_email);

        edit_password = findViewById(R.id.edit_password);

        text_forget_password = findViewById(R.id.text_forget_password);

        btn_login = findViewById(R.id.btn_login);

    }
}
