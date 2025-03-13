package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProfilTrainigFragment extends Fragment {


    public ProfilTrainigFragment() {
        // Required empty public constructor
    }


    public static ProfilTrainigFragment newInstance() {
        ProfilTrainigFragment fragment = new ProfilTrainigFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil_trainig, container, false);
    }
}