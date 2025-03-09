package com.example.bakalarkaupdate;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper {
    private final FirebaseFirestore db;
    private final CollectionReference centers;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        centers = db.collection("centers");
    }

    public void getAllCenters() {

    }

//    testovacie data>

    public void addTestData() {

//        addCollection("vysoke_tatry", "Vysoké Tatry", "Summits in the High Tatras", "https://example.com/tatry.jpg");
//        addBadge("vysoke_tatry", "lomnicky_stit", "Lomnický štít", 2634, "https://example.com/lomnicky.jpg");
//        addBadge("vysoke_tatry", "gerlachovsky_stit", "Gerlachovský štít", 2655, "https://example.com/gerlach.jpg");
//        addBadge("vysoke_tatry", "krivan", "Kriváň", 2494, "https://example.com/krivan.jpg");
//
//        addCollection("alps", "Alps", "Famous peaks of the Alps", "https://example.com/alps.jpg");
//        addBadge("alps", "mont_blanc", "Mont Blanc", 4807, "https://example.com/montblanc.jpg");
//        addBadge("alps", "matterhorn", "Matterhorn", 4478, "https://example.com/matterhorn.jpg");


//        addCenter("La Skala", "Slovakia", "Žilina", "Kamenná ulica 123, Žilina, 010 01");
//        addCenter("K2 Bratislava", "Slovakia", "Bratislava", "Elektrárenská 1, Bratislava, 831 04");

//        String centerId = "4mdlp9frrijYlQ88vYH3";
//
//        addBoulder(centerId, "Druhý boulder", "strop", 5, "6b", "Peter", "modrá", "vynikajúce chyty");
//        addBoulder(centerId, "Tretí boulder", "sektor 2", 4, "5a", "Katarína", "zelená", "nová vybudovaná cesta");
//        addBoulder(centerId, "Štvrtý boulder", "prevís", 3, "4b", "Alex", "žltá", "ľahký pre začiatočníkov");
//
//        addRoute(centerId, "Druhá cesta", "B sektor", 12, "7a", "Eva", "oranžová", "vyskúšaná a obľúbená");
//        addRoute(centerId, "Tretia cesta", "C sektor", 20, "8b", "Juraj", "hnedá", "ťažká, ale populárna");
//        addRoute(centerId, "Štvrtá cesta", "D sektor", 18, "7c", "Lucia", "červená", "nové a moderné");
    }

    public void addCenter(String name, String country, String city, String address) {
        Map<String, Object> center = new HashMap<>();
        center.put("name", name);
        center.put("country", country);
        center.put("city", city);
        center.put("address", address);
        center.put("created_at", FieldValue.serverTimestamp());

        db.collection("centers")
                .add(center)  // 🔹 Použijeme add() pre automatické generovanie ID
                .addOnSuccessListener(documentReference -> {
                    String generatedId = documentReference.getId(); // Získanie ID centra
                    Log.d("Firestore", "Centrum úspešne pridané s ID: " + generatedId);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Chyba pri pridávaní centra", e));
    }



    public void addBoulder(String centerId, String name, String sektor, int height, String difficulty, String setter, String colour, String notes) {
        Map<String, Object> boulder = new HashMap<>();
        boulder.put("name", name);
        boulder.put("sektor", sektor);
        boulder.put("height", height);
        boulder.put("difficulty", difficulty);
        boulder.put("setter", setter);
        boulder.put("colour", colour);
        boulder.put("notes", notes);
        boulder.put("created_at", FieldValue.serverTimestamp());
        boulder.put("is_active", true);

        db.collection("centers").document(centerId).collection("boulders")
                .add(boulder)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Boulder pridaný!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Chyba pri pridávaní bouldra", e));
    }

    public void addRoute(String centerId, String name, String sektor, int height, String difficulty, String setter, String colour, String notes) {
        Map<String, Object> route = new HashMap<>();
        route.put("name", name);
        route.put("sektor", sektor);
        route.put("height", height);
        route.put("difficulty", difficulty);
        route.put("setter", setter);
        route.put("colour", colour);
        route.put("notes", notes);
        route.put("created_at", FieldValue.serverTimestamp());
        route.put("is_active", true);

        db.collection("centers").document(centerId).collection("routes")
                .add(route)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Route pridaná!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Chyba pri pridávaní route", e));
    }

    public void addCollection(String collectionId, String name, String description, String imageUrl) {
        Map<String, Object> collection = new HashMap<>();
        collection.put("name", name);
        collection.put("description", description);
        collection.put("imageUrl", imageUrl);
        collection.put("created_at", FieldValue.serverTimestamp());

        db.collection("collections").document(collectionId)
                .set(collection)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Collection added: " + name))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding collection", e));
    }

    public void addBadge(String collectionId, String badgeId, String name, int height, String imageUrl) {
        Map<String, Object> badge = new HashMap<>();
        badge.put("name", name);
        badge.put("height", height);
        badge.put("imageUrl", imageUrl);
        badge.put("created_at", FieldValue.serverTimestamp());

        db.collection("collections").document(collectionId)
                .collection("badges").document(badgeId)
                .set(badge)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Badge added: " + name))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding badge", e));
    }



}