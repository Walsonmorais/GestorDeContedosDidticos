 package com.buka.gestordecontedosdidticos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.buka.gestordecontedosdidticos.Fragments.HomeFragment;
import com.buka.gestordecontedosdidticos.Fragments.ProfileFragment;

public class MenuActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    FrameLayout fragment_container;
    Fragment selectedFragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


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
                    selectedFragment = new HomeFragment();

                    break;
                case R.id.navigation_add_data:
                    Toast.makeText(MenuActivity.this, "Add Data", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.navigation_user_profile:
                   /* SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance.getCurrentUser.getUid());
                    editor.apply();*/
                    selectedFragment = new ProfileFragment();
                    break;
            }

            if (selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;

        }
    };

}
