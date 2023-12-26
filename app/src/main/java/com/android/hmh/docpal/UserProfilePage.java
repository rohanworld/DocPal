package com.android.hmh.docpal;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        userGMailHere.setText(userGmail);
//        genderAndDob.setText("Gender: "+userGender+" | DoB: "+userDob);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date dobDate = null;
        try {
            dobDate = sdf.parse(userDob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDob = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(dobDate);
        genderAndDob.setText("Gender: " + userGender + " | DoB: " + formattedDob);

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showConfirmationAlert();
            }
        });
    }

    private void showConfirmationAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfilePage.this);
        builder.setTitle("Log Out Confirmation");
        builder.setMessage("Are you sure want to Log Out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearSharedPreferences();
                Intent iy = new Intent(UserProfilePage.this, MainActivity.class);
                startActivity(iy);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    private void clearSharedPreferences() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}