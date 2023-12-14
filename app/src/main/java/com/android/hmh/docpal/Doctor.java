package com.android.hmh.docpal;

public class Doctor {
    private String imageUrl;
    private String name;
    private String experience;
    private String ratings;

    public Doctor(String imageUrl, String name, String experience, String ratings) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.experience = experience;
        this.ratings = ratings;
    }

    // Add getters and setters

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }
}

