package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Registration extends AppCompatActivity {
    EditText uname, ugmailid, udob, upassword, uconfirmpassword;
    Spinner ugender;
    String[] genders = {"Male", "Female", "Other"};
    Button continueToPhoneNumber;

    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_GENDER = "user_gender";
    private static final String KEY_AGE = "user_age";

    private static final String KEY_DOB = "user_dob";
    private static final String KEY_GMAIL = "user_gmail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        uname = findViewById(R.id.username);
        ugmailid = findViewById(R.id.usergmail);
        ugender = findViewById(R.id.usergender);
        udob = findViewById(R.id.userdob);
        upassword = findViewById(R.id.userpassword);
        uconfirmpassword = findViewById(R.id.usercnfpassword);
        continueToPhoneNumber = findViewById(R.id.continueToPhoneNumberPage);

        //Setting Genders to Dropdown
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(Registration.this, android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ugender.setAdapter(genderAdapter);

        //Date
        udob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Registration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        udob.setText(i2+"-"+(i1+1)+"-"+i);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        continueToPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEnteredName = uname.getText().toString().trim();
                String userEnteredGmail = ugmailid.getText().toString().trim();
                String userEnteredGender = ugender.getSelectedItem().toString();
                String userEnteredDOB = udob.getText().toString();
                String userEnteredPassword = upassword.getText().toString();
                String userEnteredConfirmPassword = uconfirmpassword.getText().toString();

                int age = calculateAge(userEnteredDOB);

                boolean allFieldsFilled = true;
                // Check if all fields are filled
                if (userEnteredName.isEmpty()) {
                    uname.setError("Name cannot be empty");
                    allFieldsFilled = false;
                }
                if (userEnteredGmail.isEmpty()) {
                    ugmailid.setError("Email cannot be empty");
                    allFieldsFilled = false;
                }
                if (userEnteredGender.isEmpty()) {
                    ((TextView) ugender.getSelectedView()).setError("Please Select a Gender");
                    allFieldsFilled = false;
                }
                if (userEnteredDOB.isEmpty()) {
                    udob.setError("Date of birth cannot be empty");
                    allFieldsFilled = false;
                }
                if (userEnteredPassword.isEmpty()) {
                    upassword.setError("Password cannot be empty");
                    allFieldsFilled = false;
                }
                if (userEnteredConfirmPassword.isEmpty()) {
                    uconfirmpassword.setError("Confirm password cannot be empty");
                    allFieldsFilled = false;
                }
                if (!userEnteredPassword.equals(userEnteredConfirmPassword)) {
                    uconfirmpassword.setError("Passwords do not match");
                    allFieldsFilled = false;
                }

                saveUserInfo(userEnteredName, userEnteredGender, age, userEnteredDOB, userEnteredGmail);



                // Start next activity if all fields are filled and passwords match
                if (allFieldsFilled) {
 //                    String allString = userEnteredName + userEnteredGmail + userEnteredGender + userEnteredDOB +age+userEnteredPassword;
                    Intent enterNumber = new Intent(Registration.this, PhoneNumber.class);
                    enterNumber.putExtra("name", userEnteredName);
                    enterNumber.putExtra("mail", userEnteredGmail);
                    enterNumber.putExtra("gender", userEnteredGender);
                    enterNumber.putExtra("dob", userEnteredDOB);
                    enterNumber.putExtra("age", age);
                    enterNumber.putExtra("password", userEnteredPassword);
                    startActivity(enterNumber);
                }
            }
        });
    }

    private void saveUserInfo(String name, String gender, int age, String dob, String gmailId) {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_GENDER, gender);
        editor.putInt(KEY_AGE, age);
        editor.putString(KEY_DOB, dob);  // Make sure dob is in the format "dd-MM-yyyy"
        editor.putString(KEY_GMAIL, gmailId);
        editor.apply();
    }


    private int calculateAge(String userEnteredDOB) {
        String[] dates = userEnteredDOB.split("-");
        int day = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int year = Integer.parseInt(dates[2]);
        Calendar currCal = Calendar.getInstance();
        int curYear = currCal.get(Calendar.YEAR);
        int curMonth = currCal.get(Calendar.MONTH)+1;
        int curDate = currCal.get(Calendar.DAY_OF_MONTH);
        int age=curYear-year;
        if(curMonth<month || (curMonth==month && curDate<day)){
            age--;
        }
        return age;
    }



    //Used to call from anywhere
    public static String getUserName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getString(KEY_NAME, "");
    }

    public static String getUserGender(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getString(KEY_GENDER, "");
    }

    public static int getUserAge(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getInt(KEY_AGE, 0);
    }

    public static String  getUserDob(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getString(KEY_DOB, "");
    }
    public static String  getUserGmailID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return preferences.getString(KEY_GMAIL, "");
    }


    //paste it from which page u need to call this
//    String userName = Registration.getUserName(getApplicationContext());
//    String userGender = Registration.getUserGender(getApplicationContext());
//    int userAge = Registration.getUserAge(getApplicationContext());

}