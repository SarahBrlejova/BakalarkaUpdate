package com.example.bakalarkaupdate;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class CentersFragment extends Fragment {

    private RecyclerView recyclerView;
    private CentersAdapter adapter;
    private List<Center> centersList;
    private FirebaseFirestore db;
    private ListenerRegistration firestoreListener;

    public CentersFragment() {
        // Required empty public constructor
    }

    public static CentersFragment newInstance(String param1, String param2) {
        CentersFragment fragment = new CentersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        centersList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_centers, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCenters);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CentersAdapter(getContext(), centersList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(center -> {
            DetailCenterFragment fragment = DetailCenterFragment.newInstance(center.getId(), center.getName());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        loadData();
        return view;
    }

    private void loadData() {
        firestoreListener = db.collection("centers")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Chyba s datami", Toast.LENGTH_LONG).show();
                        return;
                    }
                    centersList.clear();
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            Center center = dc.getDocument().toObject(Center.class);
                            center.setId(dc.getDocument().getId());
                            centersList.add(center);
                        }
                    }
                    adapter.notifyDataSetChanged();
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