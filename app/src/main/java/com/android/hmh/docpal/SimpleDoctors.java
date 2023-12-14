package com.android.hmh.docpal;
// SimpleDoctor.java
public class SimpleDoctors {
    private String imageUrl;
    private String name;

    public SimpleDoctors(String imageUrl, String name) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
