package com.android.hmh.docpal;

public class Patient {
    String patientName;
    String imageUrl;
    String sessionDetails;
    String gender;
    int age;
    String symptoms;

    public Patient(String patientName, String imageUrl, String sessionDetails, String gender, int age, String symptoms) {
        this.patientName = patientName;
        this.imageUrl = imageUrl;
        this.sessionDetails = sessionDetails;
        this.gender = gender;
        this.age = age;
        this.symptoms = symptoms;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSessionDetails() {
        return sessionDetails;
    }

    public void setSessionDetails(String sessionDetails) {
        this.sessionDetails = sessionDetails;
    }
}
