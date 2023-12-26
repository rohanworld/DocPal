package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListOfAppointments extends AppCompatActivity {
    ListView listOfAppointmentsDoctorCanSee;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_appointments);
        listOfAppointmentsDoctorCanSee = findViewById(R.id.listOfAppointmentsDoctorCanSee);
        db = FirebaseFirestore.getInstance();
        fetchMyPatientsFromFirestore();
    }

    private void fetchMyPatientsFromFirestore() {
        String doctorName = DoctorLogin.getDoctorName(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("doctors")
                .whereEqualTo("name", doctorName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doctorSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        CollectionReference appointmentsRef = doctorSnapshot.getReference().collection("appointments");

                        appointmentsRef.get().addOnSuccessListener(querySnapshot -> {
                                    List<Patient> appointmentList = new ArrayList<>();
                                    for (QueryDocumentSnapshot appointmentSnapshot : querySnapshot) {
                                        String patientName = appointmentSnapshot.getString("userName");
//                                        String patientName = appointmentSnapshot.getString("name");
                                        String imageURL = appointmentSnapshot.getString("patientImageURL"); // Add field for patient image
//                                        String timeSlot = appointmentSnapshot.getString("sessionDetails");
                                        String dateSlot = appointmentSnapshot.getString("date");
                                        String timingSlot = appointmentSnapshot.getString("timeSlot");
                                        String timeSlot = dateSlot+" | "+timingSlot;
                                        String gender = appointmentSnapshot.getString("userGender");
                                        int age = appointmentSnapshot.getLong("userAge").intValue();
                                        String symptoms = appointmentSnapshot.getString("symptoms");
                                        Patient patient = new Patient( patientName,imageURL, timeSlot, gender, age, symptoms);
                                        appointmentList.add(patient);
                                    }

                                    // Create and set adapter for the ListView
                                    ListView appointmentsListView = findViewById(R.id.listOfAppointmentsDoctorCanSee);
                                    PatientAdapter adapter = new PatientAdapter(this, appointmentList);
                                    appointmentsListView.setAdapter(adapter);
                                })
                                .addOnFailureListener(e -> Log.e("Appointment", "Error getting appointments", e));
                    } else {
                        Log.w("Doctor", "No doctor found with name: " + doctorName);
                    }
                })
                .addOnFailureListener(e -> Log.e("Doctor", "Error finding doctor", e));
    }
}