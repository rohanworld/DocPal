package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class SeePatientDetails extends AppCompatActivity {
    ImageView imageInDetails;
    TextView myPatientName, sessionDetails, AISymptoms, userGender, userAge, userSymptoms;
    String gotPatientName, gotPatientGender, gotPatientSessionDetails, gotPatientSymptoms, gotAIResponse;
    int gotPatientAge;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_patient_details);
        myPatientName = findViewById(R.id.myPatientName);
        imageInDetails = findViewById(R.id.imageInDetails);
        Glide.with(SeePatientDetails.this).load(R.drawable.patient_icon).into(imageInDetails);
        sessionDetails = findViewById(R.id.sessionDetails);
        userGender = findViewById(R.id.userGender);
        userAge = findViewById(R.id.userAge);
        userSymptoms = findViewById(R.id.userSymptoms);
        AISymptoms = findViewById(R.id.AISymptoms);

        Bundle doctorDetails = getIntent().getExtras();
        if (doctorDetails != null) {
            gotPatientName = doctorDetails.getString("patientName");
            gotPatientSessionDetails = doctorDetails.getString("patientSessionDetails");
            gotPatientGender = doctorDetails.getString("patientGender");
            gotPatientAge = doctorDetails.getInt("patientAge", 0);
            gotPatientSymptoms = doctorDetails.getString("patientSymptoms");
            gotAIResponse = doctorDetails.getString("aiResponse");
         }
        myPatientName.setText(gotPatientName);
        sessionDetails.setText(gotPatientSessionDetails);
        userAge.setText("Age: "+gotPatientAge+" years");
        userGender.setText("Gender: "+gotPatientGender);
        userSymptoms.setText(gotPatientSymptoms);
        AISymptoms.setText(gotAIResponse);

        //Need to Implement AI Generated Content

    }
}