package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class DoctorLogin extends AppCompatActivity {
    EditText enterdocusername, enterdocpass;
    Button submitButtonDoctor;
    private static final String DOC_PREF_NAME = "doc_prefs";
    private static final String DOC_KEY_NAME = "doc_name";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        enterdocpass = findViewById(R.id.enterdocpass);
        enterdocusername = findViewById(R.id.enterdocusername);
        submitButtonDoctor = findViewById(R.id.submitButtonDoctor);

        submitButtonDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docUsername = enterdocusername.getText().toString();
                String docPass = enterdocpass.getText().toString();

                saveUserInfo(docUsername);
                if(!docUsername.isEmpty() && !docPass.isEmpty()){
                    Intent i = new Intent(DoctorLogin.this, HomePageDoctor.class);
                    startActivity(i);

                }
            }
        });
    }

    private void saveUserInfo(String docUsername) {
        SharedPreferences preferences = getSharedPreferences(DOC_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DOC_KEY_NAME, docUsername);
        editor.apply();
    }


    public static String getDoctorName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(DOC_PREF_NAME, MODE_PRIVATE);
        return preferences.getString(DOC_KEY_NAME, "");
    }
}