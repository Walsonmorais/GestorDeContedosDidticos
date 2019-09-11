package com.buka.gestordecontedosdidticos.fragments_teacher;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.buka.gestordecontedosdidticos.Activity_Add_Files;
import com.buka.gestordecontedosdidticos.Adapter.UploadAdapter;
import com.buka.gestordecontedosdidticos.R;
import com.buka.gestordecontedosdidticos.models.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Teacher_Home extends Fragment {

    ProgressBar progressBarCircular;

    private RecyclerView recyclerView;
    private UploadAdapter mAdapter;

    private DatabaseReference mDatabaseReference;
    private List<Upload> mUploads;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_teacher_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_teacher_home);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        mUploads = new ArrayList<>();

        progressBarCircular = view.findViewById(R.id.progress_circular);

        getAllUploads();

        return view;


    }

    private void getAllUploads() {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Uploads");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot uploadSnapshot : dataSnapshot.getChildren()) {

                    Upload upload = uploadSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }

                mAdapter = new UploadAdapter(getActivity(), mUploads);
                recyclerView.setAdapter(mAdapter);
                progressBarCircular.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressBarCircular.setVisibility(View.INVISIBLE);
            }
        });


    }

}
