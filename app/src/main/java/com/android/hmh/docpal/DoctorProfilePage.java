package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class DoctorProfilePage extends AppCompatActivity {
    Button doctorLogOutButton;
    TextView docotNameInProfilePage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        doctorLogOutButton = findViewById(R.id.doctorLogOutButton);
        docotNameInProfilePage = findViewById(R.id.docotNameInProfilePage);


        String userName = DoctorLogin.getDoctorName(getApplicationContext());
        docotNameInProfilePage.setText(userName);
        doctorLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSharedPreferences();
                Intent i = new Intent(DoctorProfilePage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void clearSharedPreferences() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("doc_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}