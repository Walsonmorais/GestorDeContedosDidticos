package com.buka.gestordecontedosdidticos.fragments_teacher;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buka.gestordecontedosdidticos.R;
import com.buka.gestordecontedosdidticos.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_Teacher_Profile extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    TextView teacherName, departmentTeacher;

    ImageButton myFiles, myFavourites;

    String profeiled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        teacherName = view.findViewById(R.id.text_teacher_name);
        departmentTeacher = view.findViewById(R.id.department);
        myFiles = view.findViewById(R.id.my_files);
        myFavourites = view.findViewById(R.id.my_favourites);

        userInfo();

        return view;
    }

    private void userInfo() {

        String uId = null;

        if (firebaseUser != null) {

            uId = firebaseUser.getUid();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users_Teacher").child(uId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);


                if (user != null) {

                    teacherName.setText(user.getUsername());
                    departmentTeacher.setText(user.getDepartment());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main2, menu);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {

            firebaseAuth.signOut();
            return true;

        }


        return super.onOptionsItemSelected(item);
    }


}

