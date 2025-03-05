package com.example.bakalarkaupdate;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.FirebaseFirestore;

public class DetailCenterActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    String centerId,centerName;
    private Button btnBoulders, btnRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_center);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        centerId = getIntent().getStringExtra("centerId");

        db = FirebaseFirestore.getInstance();
        btnBoulders = findViewById(R.id.BtnActivityDetailBoulders);
        btnRoutes = findViewById(R.id.BtnActivityDetailRoutes);

        btnBoulders.setVisibility(View.GONE);
        btnRoutes.setVisibility(View.GONE);

        checkAvailableCollections();

        btnBoulders.setOnClickListener(v -> changeFragment(new BouldersFragment(), "BOULDERS"));
        btnRoutes.setOnClickListener(v -> changeFragment(new RoutesFragment(),"ROUTES"));
    }

    private void changeFragment(Fragment fragment, String fragmentType) {
        Bundle bundle = new Bundle();
        bundle.putString("centerId", centerId);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
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
                                    changeFragment(new RoutesFragment(),"ROUTES");
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

    private void buttonColour(String fragmentType){
        if (fragmentType.equals("BOULDERS")) {
            btnBoulders.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            btnRoutes.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        } else if (fragmentType.equals("ROUTES")) {
            btnRoutes.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            btnBoulders.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        }
    }
}