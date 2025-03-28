package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;


public class ProfilCollectionsFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private UsersBadgesAdapter adapter;

    private List<UserBadge> userBadgeList;
    private ListenerRegistration firestoreListener;

    public ProfilCollectionsFragment() {
        // Required empty public constructor
    }

    public static ProfilCollectionsFragment newInstance() {
        ProfilCollectionsFragment fragment = new ProfilCollectionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        userBadgeList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_collections, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProfilCollections);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter = new UsersBadgesAdapter(getContext(), userBadgeList);
        recyclerView.setAdapter(adapter);

        loadData();


        return view;
    }

    private void loadData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (userId == null) {
            return;
        }

        firestoreListener = db.collection("usersBadges")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (querySnapshot != null) {
                        userBadgeList.clear();
                        for (var doc : querySnapshot.getDocuments()) {
                            Log.d("Firestore Data", "Document: " + doc.getData());

                            UserBadge userBadge = doc.toObject(UserBadge.class);
                            userBadgeList.add(userBadge);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}