package com.example.bakalarkaupdate;

public class Badge {
    private String id;
    private String name;
    private int height;
    private String imageUrl;
    private boolean isUnlocked = false;

    public Badge() {
    }

    public Badge(String id, String name, int height, String imageUrl) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.imageUrl = imageUrl;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
