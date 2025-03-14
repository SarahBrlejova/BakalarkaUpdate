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

        db.collection("trainings").add(training).addOnSuccessListener(documentReference -> {
            String trainingId = documentReference.getId();
            getNewCreatedID.onSuccess(trainingId);
        }).addOnFailureListener(fail -> Log.e("F", "Error starting training"));
    }

    public void unlockBadge(String badgeId, String collectionId) {
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


    public void updateTrainingRoutes(String trainingId, String routeId, int timesClimbed, String difficulty) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        if (timesClimbed > 0) {
            Map<String, Object> routeData = new HashMap<>();
            routeData.put("timesClimbed", timesClimbed);
            routeData.put("difficulty", difficulty);

            trainingRef.update("completedRoutes." + routeId, routeData)
                    .addOnSuccessListener(success -> Log.d("F", "Updated climb count"))
                    .addOnFailureListener(fail -> Log.e("F", "Error with climb count"));
        } else {
            trainingRef.update("completedRoutes." + routeId, FieldValue.delete())
                    .addOnSuccessListener(success -> Log.d("F", "Route removed"))
                    .addOnFailureListener(fail -> Log.e("F", "Error with removing route"));
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
                    .addOnSuccessListener(success -> Log.d("F", "Updated training routes data"))
                    .addOnFailureListener(fail -> Log.e("F", "Error training routes data"));
        }).addOnFailureListener(fail -> Log.e("F", "Error with training data"));
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
            training.update(updates).addOnSuccessListener(success -> Log.d("F", "Updated training boulders data"))
                    .addOnFailureListener(fail -> Log.e("F", "Error training boulders data"));
        }).addOnFailureListener(fail -> Log.e("F", "Error with training data"));
    }

    public void updateTrainingBoulders(String trainingId, String boulderId, int timesClimbed, String difficulty) {
        DocumentReference trainingRef = db.collection("trainings").document(trainingId);
        if (timesClimbed > 0) {
            Map<String, Object> boulderData = new HashMap<>();
            boulderData.put("timesClimbed", timesClimbed);
            boulderData.put("difficulty", difficulty);

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