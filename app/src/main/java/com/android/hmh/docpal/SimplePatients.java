package com.android.hmh.docpal;

import android.widget.ListAdapter;

// SimpleDoctor.java
public class SimplePatients {
    private String imageUrl;
    private String name;

    public  SimplePatients(String imageUrl, String name) {
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

