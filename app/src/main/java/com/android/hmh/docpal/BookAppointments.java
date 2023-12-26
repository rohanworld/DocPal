package com.android.hmh.docpal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookAppointments extends AppCompatActivity {
    ListView listOfDoctors;
    DoctorAdapter doctorAdapter;
    FirebaseFirestore db;
    ArrayList<String> filterchips = new ArrayList<>();
    ChipGroup chipGroup;
    //added for chip sorrting
    private String selectedCategory = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointments);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        listOfDoctors = findViewById(R.id.listOfDoctors);
        chipGroup = findViewById(R.id.chipGroup);
        db = FirebaseFirestore.getInstance();
        fetchDataFromFirestore();

        filterchips.add("Regular Health");
        filterchips.add("ENT");
        filterchips.add("Ortho");
        filterchips.add("Cardio");
        int chipCounter = 1;
        for (String s: filterchips) {
            Chip chip = (Chip) LayoutInflater.from(BookAppointments.this).inflate(R.layout.chip_layout, null);
            chip.setText(s);
            chip.setId(chipCounter++);
            chipGroup.addView(chip);
        }
        chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {

                //added for sorting
                selectedCategory = null;


                if(checkedIds.isEmpty()){
                    fetchDataFromFirestore();
                }else{
                    for (int i: checkedIds) {
                        Chip chip = findViewById(i);
                        Toast.makeText(BookAppointments.this, chip.getText(), Toast.LENGTH_SHORT).show();
                        //added for sorting
                        selectedCategory = chip.getText().toString();
                    }
                }
            }
        });
    }

    //original
    private void fetchDaataFromFirestore() {
        db.collection("doctors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override

                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Doctor> doctorList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get data from Firestore document
                                String imageUrl = document.getString("imageUrl");
                                String name = document.getString("name");
                                String experience = document.getString("experience");
                                String ratings = document.getString("ratings");

                                // Create a Doctor object and add it to the list
                                Doctor doctor = new Doctor(imageUrl, name, experience, ratings);
                                doctorList.add(doctor);
                            }

                            // Initialize the adapter and set it to the ListView
                            doctorAdapter = new DoctorAdapter(BookAppointments.this, doctorList);
                            listOfDoctors.setAdapter(doctorAdapter);
                        } else {
                            Toast.makeText(BookAppointments.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    //added for sorting
    private void fetchDataFromFirestore() {
        Query query;

        // Check if a category is selected
        if (selectedCategory != null) {
            query = db.collection("doctors").whereEqualTo("category", selectedCategory);
        } else {
            query = db.collection("doctors");
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Doctor> doctorList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Get data from Firestore document
                        String imageUrl = document.getString("imageUrl");
                        String name = document.getString("name");
                        String experience = document.getString("experience");
                        String ratings = document.getString("ratings");

                        // Create a Doctor object and add it to the list
                        Doctor doctor = new Doctor(imageUrl, name, experience, ratings);
                        doctorList.add(doctor);
                    }

                    // Initialize the adapter and set it to the ListView
                    doctorAdapter = new DoctorAdapter(BookAppointments.this, doctorList);
                    listOfDoctors.setAdapter(doctorAdapter);
                } else {
                    Toast.makeText(BookAppointments.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}