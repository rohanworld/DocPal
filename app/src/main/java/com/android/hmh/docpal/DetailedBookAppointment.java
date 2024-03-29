package com.android.hmh.docpal;

import static java.util.Calendar.*;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.Manifest;

public class DetailedBookAppointment extends AppCompatActivity {

    ImageView doctorImage;
    EditText symptoms;
    Button confirmApptBtn;
    TextView doctorName, doctorExperience, doctorRatings;
    String docName, docImg, docExp, docRatings;
    String[] timeSlots;
    RadioGroup timeSlotsRadioGroup, datesRadioGroup;
    String doctorNameForDocument, patientSymptoms;

    NotificationCompat.Builder builder;
    NotificationManager notificationManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_book_appointment);

        doctorImage = findViewById(R.id.imageInDetails);
        doctorName = findViewById(R.id.doctorName);
        doctorExperience = findViewById(R.id.doctorExperience);
        doctorRatings = findViewById(R.id.doctorRatingsForDetailedBooking);
        confirmApptBtn = findViewById(R.id.confirmApptBtn);
        symptoms = findViewById(R.id.describeSymptons);
        timeSlotsRadioGroup = findViewById(R.id.timeSlotsRadioGroup);
        datesRadioGroup = findViewById(R.id.datesRadioGroup);

        askNotificationsPermission();
        Bundle doctorDetails = getIntent().getExtras();
        if (doctorDetails != null) {
            docName = doctorDetails.getString("name");
            docImg = doctorDetails.getString("imageUrl");
            docExp = doctorDetails.getString("experience");
            docRatings = doctorDetails.getString("ratings");
            timeSlots = doctorDetails.getStringArray("timeSlots");

            doctorNameForDocument = doctorDetails.getString("name");
        }

        doctorName.setText(docName);
        doctorExperience.setText(docExp + " years of Experience");
        doctorRatings.setText(docRatings + " ratings");
//        Toast.makeText(this, doctorRatings, Toast.LENGTH_SHORT).show();
        Glide.with(DetailedBookAppointment.this)
                .load(docImg)
                .into(doctorImage);
        for (String slot : timeSlots) {
            RadioButton radioButton = new RadioButton(DetailedBookAppointment.this);
            radioButton.setText(slot);
            radioButton.setBackground(getResources().getDrawable(R.drawable.custom_timeslot_radio_button));
            radioButton.setButtonDrawable(null);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                radioButton.setTextAppearance(R.style.RadioButtonStyle);
            }else{
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                );
//                params.setMargins(5,1,5, 1 );
                params.setMargins(dpToPx(5), dpToPx(1), dpToPx(5), dpToPx(1));

                radioButton.setLayoutParams(params);
            }

            timeSlotsRadioGroup.addView(radioButton);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE \ndd MMM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        for (int i =0; i<3; i++){
            RadioButton radioButton = new RadioButton(DetailedBookAppointment.this);
//            radioButton.setText((sdf.format(calendar.getTime())).toUpperCase() +"\n"+timeSlots.length+" Slots");
            radioButton.setText((sdf.format(calendar.getTime())).toUpperCase());
            radioButton.setBackground(getResources().getDrawable(R.drawable.custom_timeslot_radio_button));
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setButtonDrawable(null);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                radioButton.setTextAppearance(R.style.RadioButtonStyle);
            }else{
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(5,1,5, 1 );
                params.gravity = Gravity.CENTER;
                radioButton.setLayoutParams(params);
            }
            datesRadioGroup.addView(radioButton);
            calendar.add(DAY_OF_MONTH, 1);
        }

        confirmApptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting Symptoms
                patientSymptoms = symptoms.getText().toString().trim();

                //addBookingDetails to a new document in a subcollection of the clicked doctor whose names saved in doctorNameForDocument;
                addBookingDetails();

                //send booking to doctor and firebase
            }
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    private void aaaaddBookingDetails() {
        // Get the selected time slot
        int selectedRadioButtonId = timeSlotsRadioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedTimeSlot = selectedRadioButton.getText().toString();

            // Get the current date
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());

            // Get user details (replace these with your actual user data retrieval logic)
            String userName = Registration.getUserName(getApplicationContext());
            int userAge = Registration.getUserAge(getApplicationContext());
            String userGender = Registration.getUserGender(getApplicationContext());

            // Access the Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a reference to the doctor's document
            DocumentReference doctorRef = db.collection("doctors").document(doctorNameForDocument);

            // Create a reference to the 'appointments' subcollection within the doctor's document
            CollectionReference appointmentsRef = doctorRef.collection("appointments");

            // Create a new appointment document with auto-generated ID
            appointmentsRef.add(new HashMap<String, Object>() {
                {
                    put("date", currentDate);
                    put("timeSlot", selectedTimeSlot);
                    put("userAge", userAge);
                    put("userGender", userGender);
                    put("userName", userName);
                    put("userSymptoms", patientSymptoms);
                    // Add more fields as needed
                }
            }).addOnSuccessListener(documentReference -> {
                // Handle success
                new AlertDialog.Builder(DetailedBookAppointment.this)
                        .setMessage("Booking added to doctors subcollection")
                        .setPositiveButton("Okay", null)
                        .show();
                Intent i = new Intent(DetailedBookAppointment.this, BookingConfirmedPage.class);
                startActivity(i);
                finish();
                // Appointment data added successfully
            }).addOnFailureListener(e -> {
                // Handle failure
                new AlertDialog.Builder(DetailedBookAppointment.this)
                        .setMessage("Some Error idk what is that")
                        .setPositiveButton("Okay", null)
                        .show();
                // Failed to add appointment data
            });
        } else {
            // Handle the case where no time slot is selected
            new AlertDialog.Builder(DetailedBookAppointment.this)
                    .setMessage("Please select a time slot")
                    .setPositiveButton("Okay", null)
                    .show();
        }
    }


    private void addBookingDetails() {
        // Get the selected time slot
        int selectedRadioButtonId = timeSlotsRadioGroup.getCheckedRadioButtonId();

        if (selectedRadioButtonId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String selectedTimeSlot = selectedRadioButton.getText().toString();

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());

            String userName = Registration.getUserName(getApplicationContext());
            int userAge = Registration.getUserAge(getApplicationContext());
            String userGender = Registration.getUserGender(getApplicationContext());

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a reference to the doctor's document
            db.collection("doctors").whereEqualTo("name", docName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentReference doctorRef = document.getReference();
                        CollectionReference appointmentRef = doctorRef.collection("appointments");
                        Map<String, Object> appointmentData = new HashMap<>();
                        appointmentData.put("userName", userName);
                        appointmentData.put("userAge", userAge);
                        appointmentData.put("userGender", userGender);
                        appointmentData.put("date", currentDate);
                        appointmentData.put("timeSlot", selectedTimeSlot);
                        appointmentData.put("symptoms", patientSymptoms);
//                        appointmentData.put("patientImageUrl", "https://i.pngimg.me/thumb/f/720/m2i8m2m2N4H7m2K9.jpg");

                        appointmentRef.add(appointmentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //Add booking Details to my document also
                                addBookingDetailsToMyDocumentAlso(docName, docExp, docRatings, docImg, patientSymptoms, selectedTimeSlot, currentDate);


                                Toast.makeText(DetailedBookAppointment.this, "Booked Confirmed", Toast.LENGTH_SHORT).show();
                                //start new intetn for bokingconfirmed actiit
                                Intent i = new Intent(DetailedBookAppointment.this, BookingConfirmedPage.class);

                                showNotifications();

                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("booking failed", "onFailure: " + e);
                            }
                        });
                        break;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailedBookAppointment.this, "Error finding doctor", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if(o){
                Toast.makeText(DetailedBookAppointment.this, "Notifications Permission Received", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(DetailedBookAppointment.this, "Notifications Permission NOT Received", Toast.LENGTH_SHORT).show();
            }
        }
    });
    private void askNotificationsPermission() {
        builder = new NotificationCompat.Builder(DetailedBookAppointment.this, "notify_appt")
                .setSmallIcon(R.drawable.baseline_calendar_today_24)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Booking Successfull")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager = getSystemService(NotificationManager.class);
    }

    private void showNotifications(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(DetailedBookAppointment.this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }else{
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                NotificationChannel notificationChannel = new NotificationChannel("testHere", getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Appointment Booking Successful");
                notificationManager.createNotificationChannel(notificationChannel);
                notificationManager.notify(10, builder.build());
            }
        }
    }
    private void addBookingDetailsToMyDocumentAlso(String docName, String docExp, String docRatings, String docImg, String patientSymptoms, String selectedTimeSlot, String currentDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("name", Registration.getUserName(DetailedBookAppointment.this)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    DocumentReference doctorRef = document.getReference();
//                    CollectionReference appointmentRef = doctorRef.collection("appointments");
                    CollectionReference appointmentRef = doctorRef.collection("myAppointments");
                    Map<String, Object> appointmentData = new HashMap<>();
                    appointmentData.put("myDoctorName", docName);
                    appointmentData.put("myDoctorExperience", docExp);
                    appointmentData.put("myDoctorRatings", docRatings);
                    appointmentData.put("myDoctorImg", docImg);
                    appointmentData.put("mySymptoms", patientSymptoms);
                    appointmentData.put("myTimeSlot", selectedTimeSlot);
                    appointmentData.put("myDate", currentDate);

                    appointmentRef.add(appointmentData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(DetailedBookAppointment.this, "Booking Added to my Document also", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("booking failed in my document also", "onFailure: " + e);
                        }
                    });
                    break;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailedBookAppointment.this, "Error saving my doctor also", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
