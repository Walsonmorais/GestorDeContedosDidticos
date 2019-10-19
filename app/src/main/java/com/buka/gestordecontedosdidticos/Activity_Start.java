package com.buka.gestordecontedosdidticos;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Activity_Start extends AppCompatActivity {


    private Button btn_enter_account, btn_create_account, button_next;
    private RadioButton rbt_teacher, rbt_student;
    private RadioGroup rbt_group;
    private TextView about_App;

    private FirebaseUser firebaseUser;
    private Dialog addItemDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {

            Intent intent = new Intent(Activity_Start.this, Activity_Teacher_Menu.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btn_enter_account = findViewById(R.id.btn_enter_account);
        btn_create_account = findViewById(R.id.btn_create_account);
        about_App = findViewById(R.id.text_about_App);
        rbt_group = findViewById(R.id.rbt_group_teacher);
        rbt_teacher = findViewById(R.id.rbt_teacher);
        rbt_student = findViewById(R.id.rbt_student);
        button_next = findViewById(R.id.btn_next);



        btn_enter_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Start.this, Activity_Teacher_Login.class);
                startActivity(intent);
            }
        });


        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addItemDialog = new Dialog(Activity_Start.this);
                addItemDialog.setContentView(R.layout.layout_start_app);
                addItemDialog.setTitle(R.string.text_select_status);
                addItemDialog.show();

                final RadioButton rbt_teacher = addItemDialog.findViewById(R.id.rbt_teacher);
                final RadioButton rbt_student = addItemDialog.findViewById(R.id.rbt_student);
                final Button button_next = addItemDialog.findViewById(R.id.btn_next);


                button_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if (!rbt_teacher.isChecked() && !rbt_student.isChecked()) {

                            Toast.makeText(Activity_Start.this, R.string.text_select_status, Toast.LENGTH_SHORT).show();

                        }else if (rbt_teacher.isChecked()){


                            Intent intent = new Intent(Activity_Start.this, Activity_Teacher_Register.class);
                            startActivity(intent);
                        }else if (rbt_student.isChecked()){


                            Intent intent = new Intent(Activity_Start.this, Activity_Teacher_Menu.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Activity_Start.this, "Seleccione apenas um Estado", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

    }


    }



