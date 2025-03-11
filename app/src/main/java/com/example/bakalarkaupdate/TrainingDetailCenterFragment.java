package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

public class TrainingDetailCenterFragment extends Fragment {
    private FirebaseFirestore db;
    String centerId;
    String trainingId;
    private Button btnBoulders, btnRoutes;


    public TrainingDetailCenterFragment() {
        // Required empty public constructor
    }

    public static TrainingDetailCenterFragment newInstance(String centerId, String trainingId) {
        TrainingDetailCenterFragment fragment = new TrainingDetailCenterFragment();
        Bundle args = new Bundle();
        args.putString("centerId", centerId);
        args.putString("trainingId", trainingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            centerId = getArguments().getString("centerId");
            trainingId = getArguments().getString("trainingId");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_detail_center, container, false);

        btnBoulders = view.findViewById(R.id.BtnTrainingActivityDetailBoulders);
        btnRoutes = view.findViewById(R.id.BtnTrainingActivityDetailRoutes);

        btnBoulders.setVisibility(View.GONE);
        btnRoutes.setVisibility(View.GONE);

        checkAvailableCollections();

        btnBoulders.setOnClickListener(v -> loadFragment(TrainingBouldersFragment.newInstance(centerId, trainingId), "BOULDERS"));
        btnRoutes.setOnClickListener(v -> loadFragment(TrainingRoutesFragment.newInstance(centerId, trainingId), "ROUTES"));


        return view;

    }

    private void checkAvailableCollections() {
        db.collection("centers")
                .document(centerId)
                .collection("boulders")
                .get()
                .addOnSuccessListener(bouldersSnapshot -> {
                    final boolean bouldersExist = (bouldersSnapshot != null && !bouldersSnapshot.isEmpty());
                    db.collection("centers")
                            .document(centerId)
                            .collection("routes")
                            .get()
                            .addOnSuccessListener(routesSnapshot -> {
                                final boolean routesExist = (routesSnapshot != null && !routesSnapshot.isEmpty());
                                if (bouldersExist) {
                                    btnBoulders.setVisibility(View.VISIBLE);
                                }
                                if (routesExist) {
                                    btnRoutes.setVisibility(View.VISIBLE);
                                }
                                if (routesExist) {
                                    loadFragment(TrainingRoutesFragment.newInstance(centerId, trainingId), "ROUTES");
                                } else if (bouldersExist) {
                                    loadFragment(TrainingBouldersFragment.newInstance(centerId, trainingId), "BOULDERS");
                                }
                            })
                            .addOnFailureListener(e -> {
                                e.printStackTrace();
                            });
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    private void loadFragment(Fragment fragment, String fragmentType) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_containerTrainingDetailCenter, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        buttonColour(fragmentType);
    }

    private void buttonColour(String fragmentType) {
        if (fragmentType.equals("BOULDERS")) {
            btnBoulders.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            btnRoutes.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        } else if (fragmentType.equals("ROUTES")) {
            btnRoutes.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            btnBoulders.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }
}