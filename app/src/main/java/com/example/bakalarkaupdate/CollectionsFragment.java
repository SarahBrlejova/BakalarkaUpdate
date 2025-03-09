package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class CollectionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CollectionAdapter adapter;

    private List<Collection> collectionList;
    private FirebaseFirestore db;
    private ListenerRegistration firestoreListener;


    public static CollectionsFragment newInstance() {
        CollectionsFragment fragment = new CollectionsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        collectionList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCollections);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CollectionAdapter(getContext(), collectionList);
        recyclerView.setAdapter(adapter);
        loadData();

        return view;
    }

    private void loadData() {
        firestoreListener = db.collection("collections")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Chyba s datami", Toast.LENGTH_LONG).show();
                        return;
                    }
                    collectionList.clear();
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            Collection collection = dc.getDocument().toObject(Collection.class);
                            collection.setId(dc.getDocument().getId());
                            collectionList.add(collection);
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