package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProfilTrainingDetailFragment extends Fragment {

    String userId;
    String trainingId;
    private FirebaseFirestore db;
    private TextView tvProfilTrainingDetailTime, tvProfilTrainingDetailDate, tvProfilTrainingDetailTotalBoulders, tvProfilTrainingDetailTotalRoutes, tvProfilTrainingDetailTotalMeters, tvProfilTrainingDetailCenter;

    private RecyclerView bouldersRecyclerView, routesRecyclerView;

    public ProfilTrainingDetailFragment() {
        // Required empty public constructor
    }

    public static ProfilTrainingDetailFragment newInstance(String trainingId) {
        ProfilTrainingDetailFragment fragment = new ProfilTrainingDetailFragment();
        Bundle args = new Bundle();
        args.putString("trainingId", trainingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trainingId = getArguments().getString("trainingId");
        }
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_training_detail, container, false);

        tvProfilTrainingDetailTime = view.findViewById(R.id.tvProfilTrainingDetailTime);
        tvProfilTrainingDetailDate = view.findViewById(R.id.tvProfilTrainingDetailDate);
        tvProfilTrainingDetailTotalBoulders = view.findViewById(R.id.tvProfilTrainingDetailTotalBoulders);
        tvProfilTrainingDetailTotalRoutes = view.findViewById(R.id.tvProfilTrainingDetailTotalRoutes);
        tvProfilTrainingDetailTotalMeters = view.findViewById(R.id.tvProfilTrainingDetailTotalMeters);
        tvProfilTrainingDetailCenter = view.findViewById(R.id.tvProfilTrainingDetailCenter);

        bouldersRecyclerView = view.findViewById(R.id.bouldersRecyclerViewTrainingDetail);
        routesRecyclerView = view.findViewById(R.id.routesRecyclerViewTrainingDetail);

        bouldersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        routesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView btnBack = view.findViewById(R.id.btnProfilTrainingDetailFragmentBack);
        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        loadDetails();
        return view;
    }

    private void loadDetails() {
        db.collection("trainings").document(trainingId).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Toast.makeText(requireContext(), "Tréning sa nenašiel.", Toast.LENGTH_LONG).show();
                return;
            }

            Training training = documentSnapshot.toObject(Training.class);
            if (training == null) return;

            String centerId = training.getCenterId();

            db.collection("centers").document(centerId).get().addOnSuccessListener(centerSnapshot -> {
                String centerName = centerSnapshot.getString("name");
                tvProfilTrainingDetailCenter.setText(centerName != null ? centerName : "Neznáme centrum");
            });

            tvProfilTrainingDetailTotalBoulders.setText(String.valueOf(training.getTotalBoulders()));
            tvProfilTrainingDetailTotalRoutes.setText(String.valueOf(training.getTotalRoutes()));
            tvProfilTrainingDetailTotalMeters.setText(String.valueOf(training.getTotalMeters()));

            if (training.getStartTraining() != null) {
                tvProfilTrainingDetailDate.setText(android.text.format.DateFormat.format("dd.MM.yyyy", training.getStartTraining().toDate()));
            }

            if (training.getStartTraining() != null && training.getEndTraining() != null) {
                String timeFormatted = TrainingDetailAdapter.trainingTimeRecalculation(training.getStartTraining(), training.getEndTraining());
                tvProfilTrainingDetailTime.setText(timeFormatted);
            } else {
                tvProfilTrainingDetailTime.setText("Neukončený tréning");
            }


            if (training.getCompletedBoulders() != null && training.getCompletedBoulders().size() > 0) {
                loadCompletedBoulders(training);
            }

            if (training.getCompletedRoutes() != null && training.getCompletedRoutes().size() > 0) {
                loadCompletedRoutes(training);
            }
        }).addOnFailureListener(e -> Toast.makeText(requireContext(), "Chyba: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void loadCompletedBoulders(Training training) {
        Map<String, Object> map = training.getCompletedBoulders();
        List<ClimbedRoutesBoulders> list = new ArrayList<>();
        String centerId = training.getCenterId();

        for (String id : map.keySet()) {
            Map<String, Object> values = (Map<String, Object>) map.get(id);
            String difficulty = (String) values.get("difficulty");
            int difficultyValue = ((Number) values.get("difficultyValue")).intValue();
            int timesClimbed = ((Number) values.get("timesClimbed")).intValue();

            db.collection("centers").document(centerId).collection("boulders").document(id).get().addOnSuccessListener(doc -> {
                String name = doc.getString("name");
                list.add(new ClimbedRoutesBoulders(id, name, difficulty, difficultyValue, timesClimbed));

                if (list.size() == map.size()) {
                    bouldersRecyclerView.setAdapter(new TrainingDetailAdapter(requireContext(), list));
                    requireView().findViewById(R.id.bouldersContainer).setVisibility(View.VISIBLE);

                }
            });
        }
    }

    private void loadCompletedRoutes(Training training) {
        Map<String, Object> map = training.getCompletedRoutes();
        List<ClimbedRoutesBoulders> list = new ArrayList<>();
        String centerId = training.getCenterId();

        for (String id : map.keySet()) {
            Map<String, Object> values = (Map<String, Object>) map.get(id);
            String difficulty = (String) values.get("difficulty");
            int difficultyValue = ((Number) values.get("difficultyValue")).intValue();
            int timesClimbed = ((Number) values.get("timesClimbed")).intValue();

            db.collection("centers").document(centerId).collection("routes").document(id).get().addOnSuccessListener(doc -> {
                String name = doc.getString("name");
                list.add(new ClimbedRoutesBoulders(id, name, difficulty, difficultyValue, timesClimbed));

                if (list.size() == map.size()) {
                    routesRecyclerView.setAdapter(new TrainingDetailAdapter(requireContext(), list));
                    requireView().findViewById(R.id.routesContainer).setVisibility(View.VISIBLE);
                }
            });
        }
    }


}