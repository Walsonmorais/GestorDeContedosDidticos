package com.buka.gestordecontedosdidticos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buka.gestordecontedosdidticos.Fragments.Fragment_Add_File;
import com.buka.gestordecontedosdidticos.Fragments.Fragment_Home;
import com.buka.gestordecontedosdidticos.Fragments.Fragment_Search;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private CircleImageView nav_profile_image;
    private TextView nav_profile_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = findViewById(R.id.viewpager);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);

        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        nav_profile_image = navView.findViewById(R.id.nav_profile_image);
        nav_profile_name = navView.findViewById(R.id.nav_profile_name);

        navigationView.setNavigationItemSelectedListener(this);



    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.tab_home,
                R.drawable.tab_add_data,
                R.drawable.ic_search_files,

        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    }

    private void setupViewPager(ViewPager viewPager) {
        Activity_Menu.ViewPagerAdapter adapter = new Activity_Menu.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Fragment_Home(), "Home");
        adapter.addFrag(new Fragment_Add_File(),"Add Files");
        adapter.addFrag(new Fragment_Search(), "Search");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_profile) {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_favorite) {
            Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_books) {
            Toast.makeText(this, "Books", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share_app ){
            Toast.makeText(this, "Share App", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_about_us) {
            Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_terms_conditions) {
            Toast.makeText(this, "Terms and Conditions", Toast.LENGTH_SHORT).show();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

}
