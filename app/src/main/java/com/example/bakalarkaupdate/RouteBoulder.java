package com.example.bakalarkaupdate;

import com.google.firebase.Timestamp;

public class RouteBoulder {
    private String id;
    private String colour;
    private Timestamp createdAt;
    private String difficulty;
    private int height;
    private boolean isActive;
    private String name;
    private String notes;
    private String sektor;
    private String setter;
    private int timesClimbed = 0;

    public RouteBoulder() {
    }

    public RouteBoulder(String id, String colour, Timestamp createdAt, String difficulty, int height, boolean isActive, String name, String notes, String sektor, String setter) {
        this.id = id;
        this.colour = colour;
        this.createdAt = createdAt;
        this.difficulty = difficulty;
        this.height = height;
        this.isActive = isActive;
        this.name = name;
        this.notes = notes;
        this.sektor = sektor;
        this.setter = setter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSektor() {
        return sektor;
    }

    public void setSektor(String sektor) {
        this.sektor = sektor;
    }

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public int getClimbs() {
        return timesClimbed;
    }
    public void countUpClimbs() {
        timesClimbed++;
    }

    public void countDownClimbs() {
        if (timesClimbed > 0) {
            timesClimbed--;
        }
    }
}