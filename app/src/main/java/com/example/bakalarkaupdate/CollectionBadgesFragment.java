package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class CollectionBadgesFragment extends Fragment {


    int availableMeters = 0;
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
        loadUserMetersData();
        adapter = new BadgeAdapter(getContext(), badgeList, 0, collectionId);
        recyclerView.setAdapter(adapter);

        loadBadgeData();

        Button btnBack = view.findViewById(R.id.btnCollectionBadgesFragmentBack);
        btnBack.setOnClickListener(v -> closeFragment());

        return view;
    }

    private void loadBadgeData() {
        db.collection("collections").document(collectionId).collection("badges")
                .addSnapshotListener((document, error) -> {
                    if (error != null) {
                        return;
                    }
                    badgeList.clear();
                    if (document != null) {
                        for (QueryDocumentSnapshot doc : document) {
                            Badge badge = doc.toObject(Badge.class);
                            badge.setId(doc.getId());
                            badgeList.add(badge);
                        }
                    }
                    checkUserBadges();
                });
    }


    private void checkUserBadges() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("usersBadges")
                .whereEqualTo("userId", userId)
                .whereEqualTo("collectionId", collectionId)
                .addSnapshotListener((userBadgesObjects, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (userBadgesObjects != null) {
                        for (QueryDocumentSnapshot userBadgeObject : userBadgesObjects) {
                            String unlockedBadgeId = userBadgeObject.getString("badgeId");
                            for (Badge badge : badgeList) {
                                if (badge.getId().equals(unlockedBadgeId)) {
                                    badge.setUnlocked(true);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }



    private void loadUserMetersData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(userId)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Long meters = documentSnapshot.getLong("availableMeters");
                        if (meters != null) {
                            availableMeters = meters.intValue();
                            adapter.setUserMeters(availableMeters);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void closeFragment() {
        requireActivity().getSupportFragmentManager().popBackStack();

        if (getActivity() != null) {
            getActivity().findViewById(R.id.recyclerViewCollections).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.fragment_containerCollections).setVisibility(View.GONE);
        }
    }
}