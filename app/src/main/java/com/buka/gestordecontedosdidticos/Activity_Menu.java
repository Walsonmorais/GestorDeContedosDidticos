 package com.buka.gestordecontedosdidticos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.buka.gestordecontedosdidticos.fragments.Fragment_Home;
import com.buka.gestordecontedosdidticos.fragments.Fragment_Profile;

public class Activity_Menu extends AppCompatActivity {

    Toolbar toolbar;

    BottomNavigationView bottomNavigationView;
    FrameLayout fragment_container;
    Fragment selectedFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragment_container = findViewById(R.id.fragment_container);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new Fragment_Home();

                    break;
                case R.id.navigation_add_data:
                    Toast.makeText(Activity_Menu.this, "Add Data", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.navigation_user_profile:

                   /* SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance.getCurrentUser.getUid());
                    editor.apply();*/

                    selectedFragment = new Fragment_Profile();

                    break;
            }

            if (selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;

        }
    };

}
