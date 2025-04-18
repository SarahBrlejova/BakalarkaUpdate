package com.example.bakalarkaupdate;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Training {

    private String id;
    private String centerId;
    private Map<String, Object> completedBoulders;
    private Map<String, Object> completedRoutes;
    private Timestamp startTraining;
    private Timestamp endTraining;
    private int totalBoulders;
    private int totalMeters;
    private int totalRoutes;
    private String userId;

    public Training() {
    }

    public Training(String id, String centerId, Map<String, Object> completedBoulders, Map<String, Object> completedRoutes, Timestamp startTraining, Timestamp endTraining, int totalBoulders, int totalMeters, int totalRoutes, String userId) {
        this.id = id;
        this.centerId = centerId;
        this.completedBoulders = completedBoulders;
        this.completedRoutes = completedRoutes;
        this.startTraining = startTraining;
        this.endTraining = endTraining;
        this.totalBoulders = totalBoulders;
        this.totalMeters = totalMeters;
        this.totalRoutes = totalRoutes;
        this.userId = userId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public Map<String, Object> getCompletedBoulders() {
        return completedBoulders;
    }

    public void setCompletedBoulders(Map<String, Object> completedBoulders) {
        this.completedBoulders = completedBoulders;
    }

    public Map<String, Object> getCompletedRoutes() {
        return completedRoutes;
    }

    public void setCompletedRoutes(Map<String, Object> completedRoutes) {
        this.completedRoutes = completedRoutes;
    }

    public Timestamp getStartTraining() {
        return startTraining;
    }

    public void setStartTraining(Timestamp startTraining) {
        this.startTraining = startTraining;
    }

    public Timestamp getEndTraining() {
        return endTraining;
    }

    public void setEndTraining(Timestamp endTraining) {
        this.endTraining = endTraining;
    }

    public int getTotalBoulders() {
        return totalBoulders;
    }

    public void setTotalBoulders(int totalBoulders) {
        this.totalBoulders = totalBoulders;
    }

    public int getTotalMeters() {
        return totalMeters;
    }

    public void setTotalMeters(int totalMeters) {
        this.totalMeters = totalMeters;
    }

    public int getTotalRoutes() {
        return totalRoutes;
    }

    public void setTotalRoutes(int totalRoutes) {
        this.totalRoutes = totalRoutes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
