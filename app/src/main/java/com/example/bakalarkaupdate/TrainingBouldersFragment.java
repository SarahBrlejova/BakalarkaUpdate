package com.example.bakalarkaupdate;

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


public class TrainingBouldersFragment extends Fragment {

    String centerId;
    boolean countUp;
    String trainingId;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RoutesBouldersTrainingAdapter adapter;
    private List<RouteBoulder> boulderList;
    private ListenerRegistration firestoreListener;
    private FirestoreHelper firestoreHelper;

    public TrainingBouldersFragment() {
        // Required empty public constructor
    }


    public static TrainingBouldersFragment newInstance(String centerId, String trainingId) {
        TrainingBouldersFragment fragment = new TrainingBouldersFragment();
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
        boulderList = new ArrayList<>();
        if (getArguments() != null) {
            centerId = getArguments().getString("centerId");
            trainingId = getArguments().getString("trainingId");
        }
        firestoreHelper = new FirestoreHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_boulders, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingBoulders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutesBouldersTrainingAdapter(getContext(), boulderList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(boulder -> {
            boulder.countUpClimbs();
            int position = boulderList.indexOf(boulder);
            if (position != -1) {
                adapter.notifyItemChanged(position);
            }
            countUp = true;
            firestoreHelper.updateTrainingAllDataForBoulders(trainingId, countUp, boulder.getHeight(), boulder.getDifficultyValue());
            firestoreHelper.updateTrainingMapBoulders(trainingId, boulder.getId(), boulder.getClimbs(), boulder.getDifficulty(), boulder.getDifficultyValue());
        });
        adapter.setOnItemLongClickListener(boulder -> {
            boulder.countDownClimbs();
            int position = boulderList.indexOf(boulder);
            if (position != -1) {
                adapter.notifyItemChanged(position);
            }
            countUp = false;
            firestoreHelper.updateTrainingAllDataForBoulders(trainingId, countUp, boulder.getHeight(), boulder.getDifficultyValue());
            firestoreHelper.updateTrainingMapBoulders(trainingId, boulder.getId(), boulder.getClimbs(), boulder.getDifficulty(), boulder.getDifficultyValue());
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
                .collection("boulders")
                .whereEqualTo("isActive", true)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (querySnapshot != null) {
                        boulderList.clear();
                        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                            RouteBoulder boulder = doc.toObject(RouteBoulder.class);
                            boulder.setId(doc.getId());
                            boulderList.add(boulder);
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