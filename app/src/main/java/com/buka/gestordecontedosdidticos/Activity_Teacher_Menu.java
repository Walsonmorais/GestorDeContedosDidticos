package com.buka.gestordecontedosdidticos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.buka.gestordecontedosdidticos.fragments_teacher.Fragment_Teacher_Profile;
import com.buka.gestordecontedosdidticos.fragments_teacher.Fragment_Teacher_Home;
import com.buka.gestordecontedosdidticos.fragments_teacher.Fragment_Teacher_Search;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_Teacher_Menu extends AppCompatActivity {


    BottomNavigationView buttonNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);


        buttonNavigationView = findViewById(R.id.bottom_navigation);

        buttonNavigationView.setOnNavigationItemSelectedListener(navigationSelectedItemListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Fragment_Teacher_Home()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationSelectedItemListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {
                case R.id.icon_home:
                    selectedFragment = new Fragment_Teacher_Home();
                    break;

                case R.id.icon_search:
                    selectedFragment = new Fragment_Teacher_Search();
                    break;

                case R.id.icon_add:
                    selectedFragment = null;
                    startActivity(new Intent(Activity_Teacher_Menu.this, Activity_Add_Files.class));
                    break;

                case R.id.icon_profile:


                    /*SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();*/

                    selectedFragment = new Fragment_Teacher_Profile();
                    break;

            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            }
            return true;
        }
    };

}
