package com.example.bakalarkaupdate;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public void addTestData() {


//        addCollection("mala_fatra", "Malá Fatra", "Highest peaks of Malá Fatra", "https://example.com/malafatra.jpg");
//        addBadge("mala_fatra", "velky_krivan", "Veľký Kriváň", 1709, "https://example.com/velkykrivan.jpg");
//        addBadge("mala_fatra", "stoh", "Stoh", 1608, "https://example.com/stoh.jpg");
//        addBadge("mala_fatra", "chleb", "Chleb", 1646, "https://example.com/chleb.jpg");
//        addBadge("mala_fatra", "rozsutec", "Veľký Rozsutec", 1610, "https://example.com/rozsutec.jpg");

//        addCollection("test", "test", "test", "https://example.com/alps.jpg");
//        addBadge("test", "test", "Mont test", 4807, "https://example.com/montblanc.jpg");
//        addBadge("test", "test", "test", 4478, "https://example.com/matterhorn.jpg");


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

    public void startTraining(String centerId, GetNewCreatedID getNewCreatedID) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> training = new HashMap<>();
        training.put("user_id", userId);
        training.put("center_id", centerId);
        training.put("date", FieldValue.serverTimestamp());
        training.put("time", 0);
        training.put("total_meters", 0);
        training.put("total_routes", 0);
        training.put("total_boulders", 0);
        training.put("completed_routes", new HashMap<>());
        training.put("completed_boulders", new HashMap<>());

        db.collection("trainings").add(training)
                .addOnSuccessListener(documentReference -> {
                    String trainingId = documentReference.getId();
                    Log.d("Firestore", "Training started with ID: " + trainingId);
                    getNewCreatedID.onSuccess(trainingId);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error starting training", e));
    }

    public void updateTrainingTime(String trainingId, long totalMinutes) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);

        trainingRef.update("time", totalMinutes)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Training time updated: " + totalMinutes + " min"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating training time", e));
    }


    public void updateTrainingRoutes(String trainingId, String routeId, int timesClimbed, String difficulty) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        if (timesClimbed > 0) {
            Map<String, Object> routeData = new HashMap<>();
            routeData.put("times_climbed", timesClimbed);
            routeData.put("difficulty", difficulty);

            trainingRef.update("completed_routes." + routeId, routeData)
                    .addOnSuccessListener(success -> Log.d("Firestore", "Updated climb count for route: " + routeId))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating climb count", e));
        } else {
            trainingRef.update("completed_routes." + routeId, FieldValue.delete())
                    .addOnSuccessListener(success -> Log.d("Firestore", "Route removed: " + routeId))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error removing route", e));
        }
    }

    public void updateTrainingMetersRoutes(String trainingId, boolean countUP, int height) {
        DocumentReference training = db.collection("trainings").document(trainingId);
        training.get().addOnSuccessListener(documentSnapshot -> {
            int meters = documentSnapshot.getLong("total_meters").intValue();
            int routes = documentSnapshot.getLong("total_routes").intValue();
            if (countUP) {
                meters += height;
                routes += 1;
            } else {
                meters -= height;
                if (routes > 0) {
                    routes -= 1;
                }
            }
            Map<String, Object> updates = new HashMap<>();
            updates.put("total_meters", meters);
            updates.put("total_routes", routes);
            training.update(updates)
                    .addOnSuccessListener(success -> Log.d("Firestore", "Updated total meters and routes"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating training data", e));
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching training data", e));
    }

    public void updateTrainingMetersBoulders(String trainingId, boolean countUP, int height) {
        DocumentReference training = db.collection("trainings").document(trainingId);
        training.get().addOnSuccessListener(documentSnapshot -> {
            int meters = documentSnapshot.getLong("total_meters").intValue();
            int boulders = documentSnapshot.getLong("total_boulders").intValue();
            if (countUP) {
                meters += height;
                boulders += 1;
            } else {
                meters -= height;
                if (boulders > 0) {
                    boulders -= 1;
                }
            }
            Map<String, Object> updates = new HashMap<>();
            updates.put("total_meters", meters);
            updates.put("total_boulders", boulders);
            training.update(updates)
                    .addOnSuccessListener(success -> Log.d("Firestore", "Updated total meters and boulders"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating training data", e));
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching training data", e));
    }

    public void updateTrainingBoulders(String trainingId, String boulderId, int timesClimbed, String difficulty) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        if (timesClimbed > 0) {
            Map<String, Object> boulderData = new HashMap<>();
            boulderData.put("times_climbed", timesClimbed);
            boulderData.put("difficulty", difficulty);

            trainingRef.update("completed_boulders." + boulderId, boulderData)
                    .addOnSuccessListener(success -> Log.d("Firestore", "Updated climb count for route: " + boulderId))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating climb count", e));
        } else {
            trainingRef.update("completed_boulders." + boulderId, FieldValue.delete())
                    .addOnSuccessListener(success -> Log.d("Firestore", "Route removed: " + boulderId))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error removing route", e));
        }
    }

    public void addTestTrainings() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String userId = "YX4GTKwnXLNd3DABYYDMG6KfcIC3";
        String centerId = "4mdlp9frrijYlQ88vYH3";


        Map<String, Object> training1 = new HashMap<>();
        training1.put("user_id", userId);
        training1.put("center_id", centerId);
        training1.put("date", "2024-03-09");
        training1.put("time", 90);
        training1.put("total_meters", 25);
        training1.put("total_routes", 3);
        training1.put("total_boulders", 4);

        List<Map<String, Object>> completedRoutes1 = new ArrayList<>();
        completedRoutes1.add(new HashMap<String, Object>() {{
            put("route_id", "Am1ahyfODZj2b5MoC3NG");
            put("times_climbed", 2);
            put("difficulty", "7B");
        }});
        completedRoutes1.add(new HashMap<String, Object>() {{
            put("route_id", "Q1uiC2tBPUUs3mZyUoT1p");
            put("times_climbed", 1);
            put("difficulty", "6A");
        }});
        training1.put("completed_routes", completedRoutes1);

        List<Map<String, Object>> completedBoulders1 = new ArrayList<>();
        completedBoulders1.add(new HashMap<String, Object>() {{
            put("boulder_id", "4PE78UyQxvVoQF9Gc2vs");
            put("times_climbed", 3);
            put("difficulty", "6C");
        }});
        completedBoulders1.add(new HashMap<String, Object>() {{
            put("boulder_id", "9z78KJV6pc1M6hgntHMP");
            put("times_climbed", 1);
            put("difficulty", "7A");
        }});
        training1.put("completed_boulders", completedBoulders1);


        Map<String, Object> training2 = new HashMap<>();
        training2.put("user_id", userId);
        training2.put("center_id", centerId);
        training2.put("date", "2024-03-10");
        training2.put("time", 120);
        training2.put("total_meters", 40);
        training2.put("total_routes", 4);
        training2.put("total_boulders", 5);

        List<Map<String, Object>> completedRoutes2 = new ArrayList<>();
        completedRoutes2.add(new HashMap<String, Object>() {{
            put("route_id", "qUgsUZVH3qJM0A35vnKb");
            put("times_climbed", 1);
            put("difficulty", "6C");
        }});
        completedRoutes2.add(new HashMap<String, Object>() {{
            put("route_id", "zPah4TEPFL4zDTHrJr3k");
            put("times_climbed", 3);
            put("difficulty", "7B");
        }});
        training2.put("completed_routes", completedRoutes2);

        List<Map<String, Object>> completedBoulders2 = new ArrayList<>();
        completedBoulders2.add(new HashMap<String, Object>() {{
            put("boulder_id", "SLdFMGboBJgo2ui3HrN2");
            put("times_climbed", 2);
            put("difficulty", "7A");
        }});
        completedBoulders2.add(new HashMap<String, Object>() {{
            put("boulder_id", "YHFKxt0KdIzWsNmeFaPs");
            put("times_climbed", 4);
            put("difficulty", "6B");
        }});
        training2.put("completed_boulders", completedBoulders2);


        db.collection("trainings").add(training1)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Prvý tréning bol pridaný!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Chyba pri pridávaní prvého tréningu", e));

        db.collection("trainings").add(training2)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Druhý tréning bol pridaný!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Chyba pri pridávaní druhého tréningu", e));
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

    public interface GetNewCreatedID {
        void onSuccess(String id);
    }


}