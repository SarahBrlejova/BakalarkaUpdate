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

    private FirebaseFirestore db;
    String centerId;
    private RecyclerView recyclerView;
    private RoutesBouldersAdapter adapter;
    private List<RouteBoulder> boulderList;
    private ListenerRegistration firestoreListener;


    public TrainingBouldersFragment() {
        // Required empty public constructor
    }


    public static TrainingBouldersFragment newInstance(String centerId) {
        TrainingBouldersFragment fragment = new TrainingBouldersFragment();
        Bundle args = new Bundle();
        args.putString("centerId", centerId);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training_boulders, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewTrainingBoulders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutesBouldersAdapter(getContext(),boulderList);
        recyclerView.setAdapter(adapter);

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
                .whereEqualTo("is_active", true)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        // handle error
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