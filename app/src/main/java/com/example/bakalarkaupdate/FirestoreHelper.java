package com.example.bakalarkaupdate;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    public void updateClimbedRoutes(String trainingId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference trainingRef = db.collection("trainings").document(trainingId);

        trainingRef.get().addOnSuccessListener(trainingSnapshot -> {

            Map<String, Object> training = trainingSnapshot.getData();

            String centerId = (String) training.get("centerId");
            Map<String, Object> completedRoutes = (Map<String, Object>) training.get("completedRoutes");

            if (centerId == null || completedRoutes == null) {
                return;
            }

            for (Map.Entry<String, Object> entry : completedRoutes.entrySet()) {
                String routeId = entry.getKey();
                Map<String, Object> routeData = (Map<String, Object>) entry.getValue();
                Number climbedRouteTraining = (Number) routeData.get("timesClimbed");

                long newClimbs = climbedRouteTraining.longValue();
                String docId = userId + "_" + routeId;
                DocumentReference climbedRouteRef = db.collection("climbedRoutes").document(docId);

                climbedRouteRef.get().addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Long climbsFromFirestore = doc.getLong("climbs");
                        if (climbsFromFirestore == null) {
                            climbsFromFirestore = 0L;
                        }

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("climbs", climbsFromFirestore + newClimbs);
                        updates.put("lastClimbed", FieldValue.serverTimestamp());

                        climbedRouteRef.update(updates);
                    } else {
                        Map<String, Object> newClimb = new HashMap<>();
                        newClimb.put("routeId", routeId);
                        newClimb.put("userId", userId);
                        newClimb.put("centerId", centerId);
                        newClimb.put("climbs", newClimbs);
                        newClimb.put("lastDateClimbed", FieldValue.serverTimestamp());

                        climbedRouteRef.set(newClimb);
                    }
                });
            }
        }).addOnFailureListener(e -> {
            Log.e("e", "Error", e);
        });
    }

    public void updateClimbedBoulders(String trainingId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        trainingRef.get().addOnSuccessListener(trainingSnapshot -> {
            Map<String, Object> training = trainingSnapshot.getData();
            String centerId = (String) training.get("centerId");
            Map<String, Object> completedBoulders = (Map<String, Object>) training.get("completedBoulders");

            if (centerId == null || completedBoulders == null) {
                return;
            }

            for (Map.Entry<String, Object> entry : completedBoulders.entrySet()) {
                String boulderID = entry.getKey();
                Map<String, Object> routeData = (Map<String, Object>) entry.getValue();
                Number climbedBoulderTraining = (Number) routeData.get("timesClimbed");

                long newClimbs = climbedBoulderTraining.longValue();
                String docId = userId + "_" + boulderID;
                DocumentReference climbedBoulderRef = db.collection("climbedBoulders").document(docId);

                climbedBoulderRef.get().addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Long currentClimbs = doc.getLong("climbs");
                        if (currentClimbs == null) {
                            currentClimbs = 0L;
                        }

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("climbs", currentClimbs + newClimbs);
                        updates.put("lastClimbed", FieldValue.serverTimestamp());

                        climbedBoulderRef.update(updates);
                    } else {
                        Map<String, Object> newClimb = new HashMap<>();
                        newClimb.put("boulderId", boulderID);
                        newClimb.put("userId", userId);
                        newClimb.put("centerId", centerId);
                        newClimb.put("climbs", newClimbs);
                        newClimb.put("lastDateClimbed", FieldValue.serverTimestamp());

                        climbedBoulderRef.set(newClimb);
                    }
                });
            }
        }).addOnFailureListener(e -> {
            Log.e("e", "Error", e);
        });
    }


    public void startTraining(String centerId, GetNewCreatedID getNewCreatedID) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> training = new HashMap<>();
        training.put("userId", userId);
        training.put("centerId", centerId);
        training.put("startTraining", FieldValue.serverTimestamp());
        training.put("endTraining", FieldValue.serverTimestamp());
        training.put("totalMeters", 0);
        training.put("totalDifficultyPoints", 0);
        training.put("totalRoutes", 0);
        training.put("totalBoulders", 0);
        training.put("completedRoutes", new HashMap<>());
        training.put("completedBoulders", new HashMap<>());

        db.collection("trainings").add(training).addOnSuccessListener(documentReference -> {
            String trainingId = documentReference.getId();
            getNewCreatedID.onSuccess(trainingId);
        }).addOnFailureListener(fail -> Log.e("F", "Error starting training"));
    }

    public void unlockBadge(String badgeId, String collectionId, String imgUrl) {
        if (badgeId == null || collectionId == null) {
            return;
        }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference user = db.collection("users").document(userId);
        DocumentReference badgeRef = db.collection("collections").document(collectionId).collection("badges").document(badgeId);

        badgeRef.get().addOnCompleteListener(badgeTask -> {
            if (badgeTask.isSuccessful()) {
                DocumentSnapshot badgeSnapshot = badgeTask.getResult();
                if (badgeSnapshot.exists()) {
                    int height = badgeSnapshot.getLong("height").intValue();
                    user.get().addOnCompleteListener(userTask -> {
                        if (userTask.isSuccessful()) {
                            DocumentSnapshot userSnapshot = userTask.getResult();
                            if (userSnapshot.exists()) {
                                int availableMeters = userSnapshot.getLong("availableMeters").intValue();
                                if (availableMeters >= height) {
                                    int meterAfterHike = availableMeters - height;
                                    user.update("availableMeters", meterAfterHike).addOnSuccessListener(aVoid -> {
                                        Map<String, Object> badgeData = new HashMap<>();
                                        badgeData.put("userId", userId);
                                        badgeData.put("badgeId", badgeId);
                                        badgeData.put("collectionId", collectionId);
                                        badgeData.put("earnedAt", Timestamp.now());
                                        badgeData.put("imageUrl", imgUrl);
                                        db.collection("usersBadges").add(badgeData)
                                                .addOnSuccessListener(success -> Log.d("F", "Badge added"))
                                                .addOnFailureListener(fail -> Log.e("F", "Error with badge"));
                                    }).addOnFailureListener(fail -> Log.e("F", "Error with user meters"));
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    public void endTraining(String trainingId) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);

        trainingRef.update("endTraining", FieldValue.serverTimestamp())
                .addOnSuccessListener(success -> Log.d("F", "Training time saved"))
                .addOnFailureListener(fail -> Log.e("F", "Error with training time"));
    }

    public void getCenterName(String centerId, TextView textView) {
        db.collection("centers").document(centerId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("name")) {
                String centerName = documentSnapshot.getString("name");
                textView.setText(centerName);
            }
        }).addOnFailureListener(fail -> Log.e("F", "Error with center name"));
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
                        long trainingTotalDifficultyPoints = trainingSnapshot.getLong("totalDifficultyPoints");
                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> userTask) {
                                if (userTask.isSuccessful()) {
                                    DocumentSnapshot userDocumnent = userTask.getResult();
                                    if (userDocumnent.exists()) {
                                        int userTotalMeters = userDocumnent.getLong("totalMeters").intValue();
                                        int userAvailableMeters = userDocumnent.getLong("availableMeters").intValue();
                                        long userTotalDifficultyPoints = userDocumnent.getLong("totalDifficultyPoints");
                                        int totalMeters = userTotalMeters + trainingMeters;
                                        int availableMeters = userAvailableMeters + trainingMeters;
                                        long totalDifficultyPoints = userTotalDifficultyPoints + trainingTotalDifficultyPoints;

                                        userRef.update("totalMeters", totalMeters, "availableMeters", availableMeters, "totalDifficultyPoints", totalDifficultyPoints)
                                                .addOnSuccessListener(success -> Log.d("F", "User meters updated"))
                                                .addOnFailureListener(fail -> Log.e("F", "Error with user meters"));
                                    }
                                }
                            }
                        });
                    } else {
                        Log.d("F", "No training document");
                    }
                } else {
                    Log.e("F", "Error with training document", trainingTask.getException());
                }
            }
        });
    }


    public void updateTrainingMapRoutes(String trainingId, String routeId, int timesClimbed, String difficulty, long difficultyValue) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        if (timesClimbed > 0) {
            Map<String, Object> routeData = new HashMap<>();
            routeData.put("timesClimbed", timesClimbed);
            routeData.put("difficulty", difficulty);
            routeData.put("difficultyValue", difficultyValue);

            trainingRef.update("completedRoutes." + routeId, routeData)
                    .addOnSuccessListener(success -> Log.d("F", "Updated climb count"))
                    .addOnFailureListener(fail -> Log.e("F", "Error with climb count"));
        } else {
            trainingRef.update("completedRoutes." + routeId, FieldValue.delete())
                    .addOnSuccessListener(success -> Log.d("F", "Route removed"))
                    .addOnFailureListener(fail -> Log.e("F", "Error with removing route"));
        }
    }

    public void updateTrainingAllDataForRoutes(String trainingId, boolean countUP, int height, long difficultyValue) {
        DocumentReference training = db.collection("trainings").document(trainingId);
        training.get().addOnSuccessListener(documentSnapshot -> {
            int meters = documentSnapshot.getLong("totalMeters").intValue();
            int routes = documentSnapshot.getLong("totalRoutes").intValue();
            long totalDifficultyPoints = documentSnapshot.getLong("totalDifficultyPoints");
            if (countUP) {
                meters += height;
                routes += 1;
                totalDifficultyPoints += difficultyValue;
            } else {
                meters -= height;
                totalDifficultyPoints -= difficultyValue;
                if (routes > 0) {
                    routes -= 1;
                }
            }
            Map<String, Object> updates = new HashMap<>();
            updates.put("totalMeters", meters);
            updates.put("totalRoutes", routes);
            updates.put("totalDifficultyPoints", totalDifficultyPoints);
            training.update(updates)
                    .addOnSuccessListener(success -> Log.d("F", "Updated training routes data"))
                    .addOnFailureListener(fail -> Log.e("F", "Error training routes data"));
        }).addOnFailureListener(fail -> Log.e("F", "Error with training data"));
    }

    public void updateTrainingAllDataForBoulders(String trainingId, boolean countUP, int height, long difficultyValue) {
        DocumentReference training = db.collection("trainings").document(trainingId);
        training.get().addOnSuccessListener(documentSnapshot -> {
            int meters = documentSnapshot.getLong("totalMeters").intValue();
            int boulders = documentSnapshot.getLong("totalBoulders").intValue();
            long totalDifficultyPoints = documentSnapshot.getLong("totalDifficultyPoints");
            if (countUP) {
                meters += height;
                boulders += 1;
                totalDifficultyPoints += difficultyValue;
            } else {
                totalDifficultyPoints -= difficultyValue;
                meters -= height;
                if (boulders > 0) {
                    boulders -= 1;
                }
            }
            Map<String, Object> updates = new HashMap<>();
            updates.put("totalMeters", meters);
            updates.put("totalBoulders", boulders);
            updates.put("totalDifficultyPoints", totalDifficultyPoints);
            training.update(updates).addOnSuccessListener(success -> Log.d("F", "Updated training boulders data"))
                    .addOnFailureListener(fail -> Log.e("F", "Error training boulders data"));
        }).addOnFailureListener(fail -> Log.e("F", "Error with training data"));
    }

    public void updateTrainingMapBoulders(String trainingId, String boulderId, int timesClimbed, String difficulty, long difficultyValue) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        if (timesClimbed > 0) {
            Map<String, Object> boulderData = new HashMap<>();
            boulderData.put("timesClimbed", timesClimbed);
            boulderData.put("difficulty", difficulty);
            boulderData.put("difficultyValue", difficultyValue);
            trainingRef.update("completedBoulders." + boulderId, boulderData)
                    .addOnSuccessListener(success -> Log.d("F", "Updated boulder climb count"))
                    .addOnFailureListener(fail -> Log.e("F", "Error updating climb count"));
        } else {
            trainingRef.update("completedBoulders." + boulderId, FieldValue.delete())
                    .addOnSuccessListener(success -> Log.d("F", "Boulder removed"))
                    .addOnFailureListener(fail -> Log.e("F", "Error removing boulder"));
        }
    }


    public interface GetNewCreatedID {
        void onSuccess(String id);
    }


}