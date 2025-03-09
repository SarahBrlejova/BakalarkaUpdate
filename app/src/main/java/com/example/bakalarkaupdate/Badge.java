package com.example.bakalarkaupdate;

public class Badge {
    private String id;
    private String name;
    private int height;
    private String imageUrl;

    public Badge() {}

    public Badge(String id, String name, int height, String imageUrl) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getHeight() { return height; }
    public String getImageUrl() { return imageUrl; }
}
