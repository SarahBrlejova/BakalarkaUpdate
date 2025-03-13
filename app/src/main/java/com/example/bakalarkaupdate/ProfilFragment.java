package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;


public class ProfilFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private FirestoreHelper firestoreHelper;
    private User user;
    private FirebaseFirestore db;
    private ListenerRegistration firestoreListener;
    private Button btnTrainings, btnCollections;


    public ProfilFragment() {
        // Required empty public constructor
    }


    public static ProfilFragment newInstance() {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        firestoreHelper = new FirestoreHelper();
        user = new User();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        btnTrainings = view.findViewById(R.id.btnProfilTrainigsFragment);
        btnCollections = view.findViewById(R.id.btnProfilCollectionsFragment);

        recyclerView = view.findViewById(R.id.recyclerViewUserProfilFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsersAdapter(getContext(), user);
        recyclerView.setAdapter(adapter);

        loadFragment(ProfilTrainigFragment.newInstance());
        btnTrainings.setOnClickListener(v -> loadFragment(ProfilTrainigFragment.newInstance()));
        btnCollections.setOnClickListener(v -> loadFragment(ProfilCollectionsFragment.newInstance()));


        loadData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (firestoreListener != null) {
            firestoreListener.remove();
        }
    }

    private void loadData() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestoreListener = db.collection("users").document(currentUserId).addSnapshotListener((documentSnapshot, error) -> {
            if (error != null) {
                Log.e("ProfilFragment", "Error loading user data", error);
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                // Convert Firestore document to User object
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    this.user = user;
                    adapter.setUser(user);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("ProfilFragment", "User object is null");
                }
            } else {
                Log.d("ProfilFragment", "User data not found!");
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_containerTrainingsOrCollections, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}