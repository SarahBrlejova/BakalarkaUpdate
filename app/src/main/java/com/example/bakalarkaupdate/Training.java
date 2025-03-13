package com.example.bakalarkaupdate;

import com.google.firebase.Timestamp;
import java.util.Map;

public class Training {
    private String center_id;
    private Map<String, Object> completed_boulders;
    private Map<String, Object> completed_routes;
    private Timestamp date;
    private int time;
    private int total_boulders;
    private int total_meters;
    private int total_routes;
    private String user_id;

    public Training() {
    }
    public Training(String center_id, Map<String, Object> completed_boulders, Map<String, Object> completed_routes,
                    Timestamp date, int time, int total_boulders, int total_meters, int total_routes, String user_id) {
        this.center_id = center_id;
        this.completed_boulders = completed_boulders;
        this.completed_routes = completed_routes;
        this.date = date;
        this.time = time;
        this.total_boulders = total_boulders;
        this.total_meters = total_meters;
        this.total_routes = total_routes;
        this.user_id = user_id;
    }

    public String getCenterId() {
        return center_id;
    }

    public void setCenterId(String center_id) {
        this.center_id = center_id;
    }

    public Map<String, Object> getCompletedBoulders() {
        return completed_boulders;
    }

    public void setCompletedBoulders(Map<String, Object> completed_boulders) {
        this.completed_boulders = completed_boulders;
    }

    public Map<String, Object> getCompletedRoutes() {
        return completed_routes;
    }

    public void setCompletedRoutes(Map<String, Object> completed_routes) {
        this.completed_routes = completed_routes;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTotalBoulders() {
        return total_boulders;
    }

    public void setTotalBoulders(int total_boulders) {
        this.total_boulders = total_boulders;
    }

    public int getTotalMeters() {
        return total_meters;
    }

    public void setTotalMeters(int total_meters) {
        this.total_meters = total_meters;
    }

    public int getTotalRoutes() {
        return total_routes;
    }

    public void setTotalRoutes(int total_routes) {
        this.total_routes = total_routes;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }
}
