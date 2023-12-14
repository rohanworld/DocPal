package com.android.hmh.docpal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.window.OnBackInvokedDispatcher;

public class MainActivity extends AppCompatActivity {


    Button doctorLogin, patientLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doctorLogin = findViewById(R.id.loginasdoctor);
        patientLogin = findViewById(R.id.loginaspatient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        patientLogin.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, Registration.class);
            startActivity(i);
        });

        doctorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DoctorLogin.class);
                startActivity(i);
            }
        });

        patientLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(MainActivity.this, HomePagePatient.class);
                startActivity(i);
                return false;
            }
        });

        doctorLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(MainActivity.this, HomePageDoctor.class);
                startActivity(i);
                return false;
            }
        });
    }

//    @NonNull
//    @Override
//    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
//        finish();
//        return super.getOnBackInvokedDispatcher();
//    }
}