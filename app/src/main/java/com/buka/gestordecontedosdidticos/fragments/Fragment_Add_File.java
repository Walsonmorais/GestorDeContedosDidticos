package com.buka.gestordecontedosdidticos.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buka.gestordecontedosdidticos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Add_File extends Fragment {


    public Fragment_Add_File() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_file, container, false);
    }

}