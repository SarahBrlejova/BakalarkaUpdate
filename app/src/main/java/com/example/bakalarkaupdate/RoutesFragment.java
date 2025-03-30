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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class RoutesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RoutesBouldersAdapter adapter;
    private List<RouteBoulder> routesList;
    private FirebaseFirestore db;
    private String centerId;
    private ListenerRegistration firestoreListener;

    public RoutesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        routesList = new ArrayList<>();
        if (getArguments() != null) {
            centerId = getArguments().getString("centerId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewRouters);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RoutesBouldersAdapter(getContext(), routesList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(route -> {
            DetailRouteBoulderFragment fragment = DetailRouteBoulderFragment.newInstance(
                    centerId,
                    "route",
                    route.getId()
            );
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        loadData();

        return view;
    }

    private void checkUserClimbedRoutes() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("climbedRoutes")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((userRoutesSnapshot, error) -> {
                    if (error != null) {
                        return;
                    }
                    if (userRoutesSnapshot != null) {
                        for (QueryDocumentSnapshot routeDoc : userRoutesSnapshot) {
                            String[] separe = routeDoc.getId().split("_");
                            if (separe.length == 2) {
                                String routeId = separe[1];
                                for (RouteBoulder route : routesList) {
                                    if (route.getId().equals(routeId)) {
                                        route.setClimbed(true);
                                    }
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }


    private void loadData() {
        if (centerId == null || centerId.isEmpty()) {
            return;
        }

        firestoreListener = db.collection("centers")
                .document(centerId)
                .collection("routes")
                .whereEqualTo("isActive", true)
                .orderBy("difficultyValue", Query.Direction.ASCENDING)
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
                        checkUserClimbedRoutes();
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