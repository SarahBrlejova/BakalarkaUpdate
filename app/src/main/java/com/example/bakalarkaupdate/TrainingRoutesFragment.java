package com.example.bakalarkaupdate;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;


public class TrainingRoutesFragment extends Fragment {
    private FirebaseFirestore db;
    String centerId;
    String trainingId;
    private RecyclerView recyclerView;
    private RoutesBouldersTrainingAdapter adapter;
    private List<RouteBoulder> routesList;
    private ListenerRegistration firestoreListener;

    FirestoreHelper firestoreHelper;

    public TrainingRoutesFragment() {
        // Required empty public constructor
    }


    public static TrainingRoutesFragment newInstance(String centerId, String trainingId) {
        TrainingRoutesFragment fragment = new TrainingRoutesFragment();
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
        routesList = new ArrayList<>();
        if (getArguments() != null) {
            centerId = getArguments().getString("centerId");
            trainingId = getArguments().getString("trainingId");
        }
        firestoreHelper = new FirestoreHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_routes, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingRoutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutesBouldersTrainingAdapter(getContext(),routesList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(route -> {
            route.addClimbs();
            int position = routesList.indexOf(route);
            if (position != -1) {
                adapter.notifyItemChanged(position);
            }
            firestoreHelper.updateTrainingRoutes(trainingId, route.getId(), route.getClimbs(), route.getDifficulty());
        });
        adapter.setOnItemLongClickListener(route -> {
            route.deleteClimbs();
            int position = routesList.indexOf(route);
            if (position != -1) {
                adapter.notifyItemChanged(position);
            }
            firestoreHelper.updateTrainingRoutes(trainingId, route.getId(), route.getClimbs(), route.getDifficulty());
        });


        loadData();

        return view;
    }

    private void loadData() {
        if (centerId == null || centerId.isEmpty()) {
            Toast.makeText(getContext(), "Chyba: centerId nie je dostupné", Toast.LENGTH_LONG).show();
            return;
        }
        firestoreListener = db.collection("centers")
                .document(centerId)
                .collection("routes")
                .whereEqualTo("is_active", true)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (querySnapshot != null) {
                        routesList.clear();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            RouteBoulder route = doc.toObject(RouteBoulder.class);
                            route.setId(doc.getId());
                            routesList.add(route);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (firestoreListener != null) {
            firestoreListener.remove();
        }
    }
}