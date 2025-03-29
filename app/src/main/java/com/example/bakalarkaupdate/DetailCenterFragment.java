package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;


public class DetailCenterFragment extends Fragment {

    String centerId, centerNameString;
    private FirebaseFirestore db;
    private TextView tvDetailCenterName;
    private Button btnBoulders, btnRoutes, btnBack;

    public DetailCenterFragment() {
        // Required empty public constructor
    }


    public static DetailCenterFragment newInstance(String centerId, String centerName) {
        DetailCenterFragment fragment = new DetailCenterFragment();
        Bundle args = new Bundle();
        args.putString("centerId", centerId);
        args.putString("centerName", centerName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            centerId = getArguments().getString("centerId");
            centerNameString = getArguments().getString("centerName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_center, container, false);
        tvDetailCenterName = view.findViewById(R.id.tvDetailCenterName);
        btnBack = view.findViewById(R.id.btnDetailCenterBack);
        btnBoulders = view.findViewById(R.id.BtnActivityDetailBoulders);
        btnRoutes = view.findViewById(R.id.BtnActivityDetailRoutes);

        tvDetailCenterName.setText(centerNameString);

        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnBoulders.setVisibility(View.GONE);
        btnRoutes.setVisibility(View.GONE);

        btnBoulders.setOnClickListener(v -> changeFragment(new BouldersFragment(), "BOULDERS"));
        btnRoutes.setOnClickListener(v -> changeFragment(new RoutesFragment(), "ROUTES"));

        checkAvailableCollections();

        return view;
    }

    private void changeFragment(Fragment fragment, String fragmentType) {
        if (!isAdded()) return;
        Bundle bundle = new Bundle();
        bundle.putString("centerId", centerId);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.replace(R.id.fragment_containerDetailCenter, fragment);
        fragmentTransaction.commit();
        buttonColour(fragmentType);
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
                                    changeFragment(new RoutesFragment(), "ROUTES");
                                } else if (bouldersExist) {
                                    changeFragment(new BouldersFragment(), "BOULDERS");
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

    private void buttonColour(String fragmentType) {
        if (fragmentType.equals("BOULDERS")) {
            btnBoulders.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            btnRoutes.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        } else if (fragmentType.equals("ROUTES")) {
            btnRoutes.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            btnBoulders.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}