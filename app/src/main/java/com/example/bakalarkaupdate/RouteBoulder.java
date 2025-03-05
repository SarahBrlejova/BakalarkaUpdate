package com.example.bakalarkaupdate;

import com.google.firebase.Timestamp;

public class RouteBoulder {
    private String id;
    private String colour;
    private Timestamp created_at;
    private String difficulty;
    private int height;
    private boolean is_active;
    private String name;
    private String notes;
    private String sektor;
    private String setter;

    public RouteBoulder() {
    }

    public RouteBoulder(String id, String colour, Timestamp created_at, String difficulty, int height, boolean is_active, String name, String notes, String sektor, String setter) {
        this.id = id;
        this.colour = colour;
        this.created_at = created_at;
        this.difficulty = difficulty;
        this.height = height;
        this.is_active = is_active;
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
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

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
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
}