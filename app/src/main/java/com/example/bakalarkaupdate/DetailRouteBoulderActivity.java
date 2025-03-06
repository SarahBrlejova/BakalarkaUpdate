package com.example.bakalarkaupdate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class DetailRouteBoulderActivity extends AppCompatActivity {

    private TextView tvName, tvColour, tvDifficulty, tvHeight, tvSektor, tvSetter, tvNotes, tvCreatedAt, tvId, tvIsActive;
    private FirebaseFirestore db;
    private String routeId, boulderId;
    private String centerId;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_route_boulder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();

        // Get the views from layout
        tvName = findViewById(R.id.tvDetailRouteBoulderName);
        tvColour = findViewById(R.id.tvDetailRouteBoulderColour);
        tvDifficulty = findViewById(R.id.tvDetailRouteBoulderDifficulty);
        tvHeight = findViewById(R.id.tvDetailRouteBoulderHeight);
        tvSektor = findViewById(R.id.tvDetailRouteBoulderSektor);
        tvSetter = findViewById(R.id.tvDetailRouteBoulderSetter);
        tvNotes = findViewById(R.id.tvDetailRouteBoulderNotes);
        tvCreatedAt = findViewById(R.id.tvDetailRouteBoulderCreatedAt);
        tvIsActive = findViewById(R.id.tvDetailRouteBoulderIsActive);
        tvId = findViewById(R.id.tvDetailRouteBoulderId);


        centerId = getIntent().getStringExtra("centerId");
        type = getIntent().getStringExtra("type");

        loadDetails();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadDetails() {
        if ("boulder".equals(type)) {
            boulderId = getIntent().getStringExtra("boulderId");
            db.collection("centers")
                    .document(centerId)
                    .collection("boulders")
                    .document(boulderId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            RouteBoulder routeBoulder = documentSnapshot.toObject(RouteBoulder.class);
                            if (routeBoulder != null) {
                                routeBoulder.setId(documentSnapshot.getId());

                                tvName.setText(routeBoulder.getName());
                                tvColour.setText(routeBoulder.getColour());
                                tvDifficulty.setText(routeBoulder.getDifficulty());
                                tvHeight.setText(String.valueOf(routeBoulder.getHeight()));
                                tvSektor.setText(routeBoulder.getSektor());
                                tvSetter.setText(routeBoulder.getSetter());
                                tvNotes.setText(routeBoulder.getNotes());
                                tvCreatedAt.setText(routeBoulder.getCreated_at().toDate().toString());
                                tvId.setText(routeBoulder.getId());
                            }
                        } else {
                            Toast.makeText(this, "No details found for this route", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error loading details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else if ("route".equals(type)) {
            routeId = getIntent().getStringExtra("routeId");
            db.collection("centers")
                    .document(centerId)
                    .collection("routes")
                    .document(routeId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            RouteBoulder routeBoulder = documentSnapshot.toObject(RouteBoulder.class);
                            if (routeBoulder != null) {
                                routeBoulder.setId(documentSnapshot.getId());
                                tvName.setText(routeBoulder.getName());
                                tvColour.setText(routeBoulder.getColour());
                                tvDifficulty.setText(routeBoulder.getDifficulty());
                                tvHeight.setText(String.valueOf(routeBoulder.getHeight()));
                                tvSektor.setText(routeBoulder.getSektor());
                                tvSetter.setText(routeBoulder.getSetter());
                                tvNotes.setText(routeBoulder.getNotes());
                                tvCreatedAt.setText(routeBoulder.getCreated_at().toDate().toString());
                                tvId.setText(routeBoulder.getId());
                            }
                        } else {
                            Toast.makeText(this, "No details found for this route", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error loading details: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }

    }

}