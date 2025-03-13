package com.example.bakalarkaupdate;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public void startTraining(String centerId, GetNewCreatedID getNewCreatedID) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> training = new HashMap<>();
        training.put("userId", userId);
        training.put("centerId", centerId);
        training.put("startTraining", FieldValue.serverTimestamp());
        training.put("endTraining", null);
        training.put("totalMeters", 0);
        training.put("totalRoutes", 0);
        training.put("totalBoulders", 0);
        training.put("completedRoutes", new HashMap<>());
        training.put("completedBoulders", new HashMap<>());

        db.collection("trainings").add(training)
                .addOnSuccessListener(documentReference -> {
                    String trainingId = documentReference.getId();
                    getNewCreatedID.onSuccess(trainingId);
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error starting training", e));
    }


    public void endTraining(String trainingId) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);

        trainingRef.update("endTraining", FieldValue.serverTimestamp())
                .addOnSuccessListener(aVoid -> Log.d("F", "Training time saved."))
                .addOnFailureListener(e -> Log.e("F", "Error training time", e));
    }


    public void updateUserClimbedMeters(String trainingId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        DocumentReference userRef = db.collection("users").document(userId);
        trainingRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> trainingTask) {
                if (trainingTask.isSuccessful()) {
                    DocumentSnapshot trainingSnapshot = trainingTask.getResult();
                    if (trainingSnapshot.exists()) {
                        int trainingMeters = trainingSnapshot.getLong("totalMeters").intValue();
                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                if (userTask.isSuccessful()) {
                                    DocumentSnapshot userDocumnent = userTask.getResult();
                                    if (userDocumnent.exists()) {
                                        int userTotalMeters = userDocumnent.getLong("totalMeters").intValue();
                                        int userAvailableMeters = userDocumnent.getLong("availableMeters").intValue();
                                        int totalMeters = userTotalMeters + trainingMeters;
                                        int availableMeters = userAvailableMeters + trainingMeters;

                                        userRef.update("totalMeters", totalMeters, "availableMeters", availableMeters)
                                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User meters updated successfully!"))
                                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating user meters", e));
                                    } else {
                                        Log.d("Firestore", "No such user document");
                                    }
                                } else {
                                    Log.e("Firestore", "User document fetch failed", userTask.getException());
                                }
                            }
                        });
                    } else {
                        Log.d("Firestore", "No such training document");
                    }
                } else {
                    Log.e("Firestore", "Training document fetch failed", trainingTask.getException());
                }
            }
        });
    }


    public void updateTrainingRoutes(String trainingId, String routeId, int timesClimbed, String difficulty) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        if (timesClimbed > 0) {
            Map<String, Object> routeData = new HashMap<>();
            routeData.put("timesClimbed", timesClimbed);
            routeData.put("difficulty", difficulty);

            trainingRef.update("completedRoutes." + routeId, routeData)
                    .addOnSuccessListener(success -> Log.d("Firestore", "Updated climb count for route: " + routeId))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating climb count", e));
        } else {
            trainingRef.update("completedRoutes." + routeId, FieldValue.delete())
                    .addOnSuccessListener(success -> Log.d("Firestore", "Route removed: " + routeId))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error removing route", e));
        }
    }

    public void updateTrainingMetersRoutes(String trainingId, boolean countUP, int height) {
        DocumentReference training = db.collection("trainings").document(trainingId);
        training.get().addOnSuccessListener(documentSnapshot -> {
            int meters = documentSnapshot.getLong("totalMeters").intValue();
            int routes = documentSnapshot.getLong("totalRoutes").intValue();
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
            updates.put("totalMeters", meters);
            updates.put("totalRoutes", routes);
            training.update(updates)
                    .addOnSuccessListener(success -> Log.d("Firestore", "Updated total meters and routes"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating training data", e));
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching training data", e));
    }

    public void updateTrainingMetersBoulders(String trainingId, boolean countUP, int height) {
        DocumentReference training = db.collection("trainings").document(trainingId);
        training.get().addOnSuccessListener(documentSnapshot -> {
            int meters = documentSnapshot.getLong("totalMeters").intValue();
            int boulders = documentSnapshot.getLong("totalBoulders").intValue();
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
            updates.put("totalMeters", meters);
            updates.put("totalBoulders", boulders);
            training.update(updates)
                    .addOnSuccessListener(success -> Log.d("Firestore", "Updated total meters and boulders"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating training data", e));
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching training data", e));
    }

    public void updateTrainingBoulders(String trainingId, String boulderId, int timesClimbed, String difficulty) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        if (timesClimbed > 0) {
            Map<String, Object> boulderData = new HashMap<>();
            boulderData.put("timesClimbed", timesClimbed);
            boulderData.put("difficulty", difficulty);

            trainingRef.update("completedBoulders." + boulderId, boulderData)
                    .addOnSuccessListener(success -> Log.d("Firestore", "Updated climb count for route: " + boulderId))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating climb count", e));
        } else {
            trainingRef.update("completedBoulders." + boulderId, FieldValue.delete())
                    .addOnSuccessListener(success -> Log.d("Firestore", "Route removed: " + boulderId))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error removing route", e));
        }
    }


    public interface GetNewCreatedID {
        void onSuccess(String id);
    }


}