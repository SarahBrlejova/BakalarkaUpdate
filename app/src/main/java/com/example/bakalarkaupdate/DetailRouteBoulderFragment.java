package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class DetailRouteBoulderFragment extends Fragment {

    private FirebaseFirestore db;
    private String centerId;
    private String type;
    private String routeId, boulderId;
    private TextView tvName, tvColour, tvDifficulty, tvHeight, tvSektor, tvSetter, tvNotes, tvCreatedAt, tvId, tvIsActive;

    public DetailRouteBoulderFragment() {
        // Required empty public constructor
    }

    public static DetailRouteBoulderFragment newInstance(String centerId, String type,  String itemId) {
        DetailRouteBoulderFragment fragment = new DetailRouteBoulderFragment();
        Bundle args = new Bundle();
        args.putString("centerId", centerId);
        args.putString("type", type);
        if (type.equals("boulder")) {
            args.putString("boulderId", itemId);
        } else {
            args.putString("routeId", itemId);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            centerId = getArguments().getString("centerId");
            type = getArguments().getString("type");
            if ("boulder".equals(type)) {
                boulderId = getArguments().getString("boulderId");
            } else {
                routeId = getArguments().getString("routeId");
            }
        }
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_route_boulder, container, false);

        tvName = view.findViewById(R.id.tvDetailRouteBoulderName);
        tvColour = view.findViewById(R.id.tvDetailRouteBoulderColour);
        tvDifficulty = view.findViewById(R.id.tvDetailRouteBoulderDifficulty);
        tvHeight = view.findViewById(R.id.tvDetailRouteBoulderHeight);
        tvSektor = view.findViewById(R.id.tvDetailRouteBoulderSektor);
        tvSetter = view.findViewById(R.id.tvDetailRouteBoulderSetter);
        tvNotes = view.findViewById(R.id.tvDetailRouteBoulderNotes);
        tvCreatedAt = view.findViewById(R.id.tvDetailRouteBoulderCreatedAt);
        tvId = view.findViewById(R.id.tvDetailRouteBoulderId);
        tvIsActive = view.findViewById(R.id.tvDetailRouteBoulderIsActive);

        Button btnBack = view.findViewById(R.id.btnDetailRouteBoulderFragmentBack);
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        loadDetails();
        return view;    }

    private void loadDetails() {
        if ("boulder".equals(type)) {
            db.collection("centers")
                    .document(centerId)
                    .collection("boulders")
                    .document(boulderId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            RouteBoulder boulder = documentSnapshot.toObject(RouteBoulder.class);
                            if (boulder != null) {
                                boulder.setId(documentSnapshot.getId());

                                tvName.setText(boulder.getName());
                                tvColour.setText(boulder.getColour());
                                tvDifficulty.setText(boulder.getDifficulty());
                                tvHeight.setText(String.valueOf(boulder.getHeight()));
                                tvSektor.setText(boulder.getSektor());
                                tvSetter.setText(boulder.getSetter());
                                tvNotes.setText(boulder.getNotes());
                                tvCreatedAt.setText(boulder.getCreatedAt().toDate().toString());
                                tvId.setText(boulder.getId());
                                if (tvIsActive != null) {
                                    tvIsActive.setText(String.valueOf(boulder.isActive()));
                                }
                            }
                        } else {
                            Toast.makeText(requireContext(), "Nenašli sa údaje o bouldri", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Chyba: " + e.getMessage(), Toast.LENGTH_LONG).show());

        } else if ("route".equals(type)) {
            db.collection("centers")
                    .document(centerId)
                    .collection("routes")
                    .document(routeId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            RouteBoulder route = documentSnapshot.toObject(RouteBoulder.class);
                            if (route != null) {
                                route.setId(documentSnapshot.getId());

                                tvName.setText(route.getName());
                                tvColour.setText(route.getColour());
                                tvDifficulty.setText(route.getDifficulty());
                                tvHeight.setText(String.valueOf(route.getHeight()));
                                tvSektor.setText(route.getSektor());
                                tvSetter.setText(route.getSetter());
                                tvNotes.setText(route.getNotes());
                                tvCreatedAt.setText(route.getCreatedAt().toDate().toString());
                                tvId.setText(route.getId());
                                if (tvIsActive != null) {
                                    tvIsActive.setText(String.valueOf(route.isActive()));
                                }
                            }
                        } else {
                            Toast.makeText(requireContext(), "Nenašli sa údaje o ceste", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Chyba: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

}