package com.android.hmh.docpal;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

public class UserProfilePage extends AppCompatActivity {
    ImageView logoutBtn, profilePage;
    TextView userProfileNameHere, genderAndDob, userGMailHere;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userProfileNameHere = findViewById(R.id.userProfileNameHere);
        genderAndDob = findViewById(R.id.genderAndDob);
        userGMailHere = findViewById(R.id.userGMailHere);
        profilePage = findViewById(R.id.imageInDetails);
        Glide.with(getApplicationContext())
                .load(R.drawable.patientimagedefault)
                .into(profilePage);


        String userName = Registration.getUserName(getApplicationContext());
        String userGender = Registration.getUserGender(getApplicationContext());
        String userDob = Registration.getUserDob(getApplicationContext());
        String userGmail = Registration.getUserGmailID(getApplicationContext());

        userProfileNameHere.setText(userName);
        genderAndDob.setText("Gender: "+userGender+" | DOB: "+userDob);
        userGMailHere.setText(userGmail);


        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSharedPreferences();
                Intent i = new Intent(UserProfilePage.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void clearSharedPreferences() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}