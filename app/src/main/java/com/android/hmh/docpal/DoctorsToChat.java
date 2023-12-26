package com.android.hmh.docpal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;

import java.util.ArrayList;
import java.util.List;

public class DoctorsToChat extends AppCompatActivity {
    DoctorAdapterForChat doctorAdapterForChat;
    ListView listOfDoctorsForChat;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_to_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listOfDoctorsForChat = findViewById(R.id.listOfDoctorsForChat);
        db = FirebaseFirestore.getInstance();
        fetchDataForChat();
    }

    private void fetchDataForChat() {
        db.collection("doctors")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<SimpleDoctors> doctorList = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get data from Firestore document
                            String imageUrl = document.getString("imageUrl");
                            String name = document.getString("name");

                            // Create a Doctor object and add it to the list
                            SimpleDoctors doctor = new SimpleDoctors(imageUrl, name);
                            doctorList.add(doctor);
                        }

                        // Initialize the adapter and set it to the ListView
                        doctorAdapterForChat = new DoctorAdapterForChat(DoctorsToChat.this, doctorList);
                        listOfDoctorsForChat.setAdapter(doctorAdapterForChat);

                        // Set item click listener to open the chat activity
                        listOfDoctorsForChat.setOnItemClickListener((parent, view, position, id) -> {
                            SimpleDoctors selectedDoctor = doctorList.get(position);
                            openChatActivity(selectedDoctor);
                        });

                    } else {
                        Toast.makeText(DoctorsToChat.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openChatActivity(SimpleDoctors doctor) {
//        String userID = Registration.getUserName(DoctorsToChat.this);
//        startService(userID);

        Intent chatIntent = new Intent(DoctorsToChat.this, ChaaatPage.class);
        chatIntent.putExtra("doctorName", doctor.getName());
        startActivity(chatIntent);
    }

}
