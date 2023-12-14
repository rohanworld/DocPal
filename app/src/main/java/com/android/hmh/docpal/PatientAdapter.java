package com.android.hmh.docpal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

public class PatientAdapter extends ArrayAdapter<Patient> {
    private Context context;
    private List<Patient> patientsList;

    public static String[] AIResponses = {
            "According to the above patient symptoms, the possible causes are varied and may include viral infections, stress, or nutritional deficiencies.\nPossible prescription: Recommend rest, hydration, and a balanced diet. Further diagnostic tests may be needed for a precise diagnosis.",
            "The patient's symptoms may be indicative of both physiological and psychological factors. Potential causes might include hormonal imbalances, lifestyle factors, or mild infections.\nPossible prescription: Suggest lifestyle modifications, stress management techniques, and a thorough examination to rule out specific medical conditions.",
            "Considering the reported symptoms, potential causes could be environmental factors, allergies, or a mild reaction to certain foods.\nPossible prescription: Advise identifying and avoiding potential triggers, antihistamines for relief, and monitoring for any worsening of symptoms.",
            "The patient's symptoms may be linked to common ailments such as cold or flu, allergic reactions, or minor injuries.\nPossible prescription: Recommend over-the-counter medications for symptom relief, rest, and self-monitoring. Further evaluation may be necessary if symptoms persist.",
            "Based on the described symptoms, the causes could range from lifestyle-related issues to mild infections or inflammatory responses.\nPossible prescription: Encourage lifestyle changes, anti-inflammatory medications for symptom management, and follow-up consultations for a more in-depth analysis.",
            "The reported symptoms might be indicative of a broad spectrum of causes, including but not limited to, dietary factors, lack of sleep, or minor infections.\nPossible prescription: Propose dietary adjustments, improved sleep hygiene, and general supportive measures. Consider further investigations if symptoms persist.",
            "Considering the patient's symptoms, potential causes may involve stress, fatigue, or common viral infections.\nPossible prescription: Recommend stress reduction techniques, adequate rest, and over-the-counter medications for symptom relief. Reassess if symptoms do not improve.",
            "The reported symptoms could be attributed to common conditions like allergies, mild infections, or exposure to environmental irritants.\nPossible prescription: Suggest antihistamines, supportive measures, and avoiding potential triggers. Monitoring for any worsening of symptoms is advised.",
            "Based on the patient's symptoms, the causes might be related to both physical and mental well-being, such as fatigue, mild infections, or psychological stress.\nPossible prescription: Advocate for a balanced lifestyle, adequate rest, and considering mental health support. Further investigation may be necessary for a comprehensive understanding.",
            "The symptoms reported by the patient may be associated with common ailments like seasonal illnesses, minor injuries, or stress-related factors.\nPossible prescription: Recommend symptomatic relief measures, rest, and stress management techniques. Follow-up consultations for persistent symptoms are advisable."
    };


    public static String getRandomResponse() {
        Random random = new Random();
        int index = random.nextInt(AIResponses.length);
        return AIResponses[index];
    }

    public PatientAdapter(Context context, List<Patient> patientsList) {
        super(context, 0, patientsList);
        this.context = context;
        this.patientsList = patientsList;
    }


    @Override
    public int getCount() {
        return patientsList.size();
    }
    @Nullable
    @Override
    public Patient getItem(int position) {
        return patientsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_appointment, parent, false);
        }

        Patient patient = getItem(position);

        ImageView patientImage = convertView.findViewById(R.id.patientImage);
        TextView patientName = convertView.findViewById(R.id.patientName);
        TextView sessionDetails = convertView.findViewById(R.id.sessionDetails);
        Button viewNowButton = convertView.findViewById(R.id.viewNowButton);

        // Use Glide library to load the image
        Glide.with(context)
                .load(R.drawable.patientimagedefault)
                .into(patientImage);

        patientName.setText(patient.getPatientName());
        sessionDetails.setText(patient.getSessionDetails());

        viewNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "View Now" button click
                // You can use the patient object to get the details of the selected patient
                String name = patient.getPatientName();
                String imageUrl = patient.getImageUrl();
                String sessionDetails = patient.getSessionDetails();
                String gender = patient.getGender();
                int age = patient.getAge();
                String symptoms = patient.getSymptoms();

                //bundling to pass
                Bundle patientDetails = new Bundle();
                patientDetails.putString("patientName", name);
//                patientDetails.putString("patientImageUrl", imageUrl);
                patientDetails.putString("patientSessionDetails", sessionDetails);
                patientDetails.putString("patientGender", gender);
                patientDetails.putInt("patientAge", age);
                patientDetails.putString("patientSymptoms", symptoms);

                String randomResponse = getRandomResponse();
                patientDetails.putString("aiResponse", randomResponse);


                // Example: Start a new activity with the bundled data
                 Intent i = new Intent(getContext(), SeePatientDetails.class);
                 i.putExtras(patientDetails);
                 getContext().startActivity(i);
            }
        });

        return convertView;
    }



}

