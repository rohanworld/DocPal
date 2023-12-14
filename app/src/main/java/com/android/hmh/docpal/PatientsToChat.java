package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientsToChat extends AppCompatActivity {
    PatientAdapterForChat patientAdapterForChat;
    ListView listOfPatientsForChat;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_to_chat);
        listOfPatientsForChat = findViewById(R.id.listOfPatientsForChat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db = FirebaseFirestore.getInstance();
        fetchDataForChat();
    }
    private void fetchDataForChat() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SimplePatients> patientsList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get data from Firestore document
                            String imageUrl = document.getString("imageUrl");
                            String name = document.getString("name");

                            // Create a Doctor object and add it to the list
                            SimplePatients patient = new SimplePatients(imageUrl, name);
                            patientsList.add(patient);
                        }

                        // Initialize the adapter and set it to the ListView
                        patientAdapterForChat = new PatientAdapterForChat(PatientsToChat.this, patientsList);
                        listOfPatientsForChat.setAdapter(patientAdapterForChat);

                        // Set item click listener to open the chat activity
                        listOfPatientsForChat.setOnItemClickListener((parent, view, position, id) -> {
                            SimplePatients selectedPatient = patientsList.get(position);
                            openChatActivity(selectedPatient);
                        });

                    } else {
                        Toast.makeText(PatientsToChat.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openChatActivity(SimplePatients patients) {
        Intent chatIntent = new Intent(PatientsToChat.this, ChaaatPage.class);
        chatIntent.putExtra("doctorName", patients.getName());
        startActivity(chatIntent);
    }
}