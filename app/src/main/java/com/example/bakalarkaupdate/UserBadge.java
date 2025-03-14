package com.example.bakalarkaupdate;

import com.google.firebase.Timestamp;

public class UserBadge {
    private String id;
    private String userId;
    private String badgeId;
    private String collectionId;
    private Timestamp earnedAt;
    private String imageUrl;

    public UserBadge() {
    }

    public UserBadge(String id, String userId, String badgeId, String collectionId, Timestamp earnedAt, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.badgeId = badgeId;
        this.collectionId = collectionId;
        this.earnedAt = earnedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public Timestamp getEarnedAt() {
        return earnedAt;
    }

    public void setEarnedAt(Timestamp earnedAt) {
        this.earnedAt = earnedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
