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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;


public class ProfilTrainigFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private TrainingsAdapter adapter;
    private List<Training> trainingList;
    private ListenerRegistration firestoreListener;
    private FirestoreHelper firestoreHelper;



    public ProfilTrainigFragment() {
        // Required empty public constructor
    }


    public static ProfilTrainigFragment newInstance() {
        ProfilTrainigFragment fragment = new ProfilTrainigFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        trainingList = new ArrayList<>();
        firestoreHelper = new FirestoreHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_trainig, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewProfilTrainings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TrainingsAdapter(getContext(), trainingList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(training -> {

            ProfilTrainingDetailFragment fragment = ProfilTrainingDetailFragment.newInstance(training.getId());
            recyclerView.setVisibility(View.GONE);
            getView().findViewById(R.id.fragment_containerTrainingDetailCenterData).setVisibility(View.VISIBLE);

            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        loadData();
        return view;
    }

    private void loadData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestoreListener = db.collection("trainings")
                .whereEqualTo("userId", userId)
                .orderBy("startTraining", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (querySnapshot != null) {
                        trainingList.clear();
                        for (var doc : querySnapshot.getDocuments()) {
                            Training training = doc.toObject(Training.class);
                            training.setId(doc.getId());
                            trainingList.add(training);
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