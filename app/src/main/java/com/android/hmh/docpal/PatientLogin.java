package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class PatientLogin extends AppCompatActivity {
    EditText enterUserMailID, enterUserPatient;
    Button submitButton_UserLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
        submitButton_UserLogin = findViewById(R.id.submitButton_UserLogin);
        enterUserPatient = findViewById(R.id.enterUserPatient);
        enterUserMailID = findViewById(R.id.enterUserMailID);
    }
}