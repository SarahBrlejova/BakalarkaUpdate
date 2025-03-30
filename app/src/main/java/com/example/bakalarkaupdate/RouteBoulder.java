package com.example.bakalarkaupdate;

import com.google.firebase.Timestamp;

public class RouteBoulder {
    private String id;
    private String colour;
    private Timestamp createdAt;
    private String difficulty;
    private int height;
    private long difficultyValue;
    private boolean isActive;
    private String name;
    private String notes;
    private String sektor;
    private String setter;
    private int timesClimbed = 0;
    private long totalDifficultyPoints = 0;

    private boolean climbed = false;

    public RouteBoulder() {
    }

    public RouteBoulder(String id, String colour, Timestamp createdAt, String difficulty, long difficultyValue, int height, boolean isActive, String name, String notes, String sektor, String setter) {
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
        this.difficultyValue = difficultyValue;
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

    public long getDifficultyValue() {
        return difficultyValue;
    }

    public void setDifficultyValue(long difficultyValue) {
        this.difficultyValue = difficultyValue;
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

    public void countUpTotalDifficultyPoints() {
        totalDifficultyPoints = totalDifficultyPoints + difficultyValue;
    }

    public void countDownTotalDifficultyPoints() {
        if (timesClimbed > 0 && totalDifficultyPoints > 0) {
            totalDifficultyPoints = totalDifficultyPoints - difficultyValue;
        }
    }

    public void countUpClimbs() {
        timesClimbed++;
    }

    public void countDownClimbs() {
        if (timesClimbed > 0) {
            timesClimbed--;
        }
    }

    public boolean isClimbed() {
        return climbed;
    }

    public void setClimbed(boolean climbed) {
        this.climbed = climbed;
    }

}