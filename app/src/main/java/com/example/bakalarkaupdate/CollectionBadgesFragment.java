package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class CollectionBadgesFragment extends Fragment {


    private RecyclerView recyclerView;
    private BadgeAdapter adapter;
    private List<Badge> badgeList;
    private FirebaseFirestore db;
    private String collectionId;

    public CollectionBadgesFragment() {
        // Required empty public constructor
    }


    public static CollectionBadgesFragment newInstance(String collectionId) {
        CollectionBadgesFragment fragment = new CollectionBadgesFragment();
        Bundle args = new Bundle();
        args.putString("collectionId", collectionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        badgeList = new ArrayList<>();
        if (getArguments() != null) {
            collectionId = getArguments().getString("collectionId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collection_badges, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCollectionBadges);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BadgeAdapter(getContext(), badgeList);
        recyclerView.setAdapter(adapter);

        loadData();

        Button btnBack = view.findViewById(R.id.btnCollectionBadgesFragmentBack);
        btnBack.setOnClickListener(v -> closeFragment());

        return view;    }

    private void loadData() {
        db.collection("collections").document(collectionId).collection("badges")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    badgeList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Badge badge = doc.toObject(Badge.class);
                        badgeList.add(badge);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error loading badges", Toast.LENGTH_SHORT).show());
    }

    private void closeFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();

        if (getActivity() != null) {
            getActivity().findViewById(R.id.recyclerViewCollections).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.fragment_containerCollections).setVisibility(View.GONE);
        }
    }
}