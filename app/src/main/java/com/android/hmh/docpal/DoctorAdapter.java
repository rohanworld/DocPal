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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class DoctorAdapter extends ArrayAdapter<Doctor> {
    private Context context;
    private List<Doctor> doctorList;


    String[] s1  = new String[]{"4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM"};
    String[] s2  = new String[]{"1:00 PM", "4:00 PM", "7:00 PM", "8:00 PM"};
    String[] s3  = new String[]{"12:00 PM", "1:00 PM", "3:00 PM", "4:00 PM"};
    String[] s4  = new String[]{"10:00 AM", "1:00 PM", "5:00 PM"};
    String[] s5  = new String[]{"9:00 AM", "12:00 PM", "4:00 PM"};
    String[] s6  = new String[]{"10:00 AM", "1:00 PM", "3:00 PM"};




    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        super(context, 0, doctorList);
        this.context = context;
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_doctor, parent, false);
        }

        Doctor currentDoctor = doctorList.get(position);

        ImageView doctorImage = convertView.findViewById(R.id.doctorImage);
        TextView doctorName = convertView.findViewById(R.id.doctorName);
        TextView doctorExperience;
        doctorExperience = convertView.findViewById(R.id.doctorExperience);
        TextView doctorRatings = convertView.findViewById(R.id.doctorRatings);
        Button bookNowButton = convertView.findViewById(R.id.bookNowButton);
        bookNowButton.setTag(currentDoctor);

        // Use Glide library to load the image
        Glide.with(context)
                .load(currentDoctor.getImageUrl())
                .placeholder(R.drawable.baseline_local_hospital_24)
                .into(doctorImage);

        doctorName.setText(currentDoctor.getName());
        doctorExperience.setText(currentDoctor.getExperience()+" Years of Experience");
        doctorRatings.setText(currentDoctor.getRatings()+" ratings");
        String hopeRatingsWorks = currentDoctor.getRatings();

        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Doctor selectedDoctor = (Doctor) v.getTag();
                // Handle book now button click, open a new activity, etc.
                // You can use currentDoctor to get the details of the selected doctor
                String name = selectedDoctor.getName();
                String imageUrl = selectedDoctor.getImageUrl();
                String experience = selectedDoctor.getExperience();
                String ratings = hopeRatingsWorks;
//                String ratings = selectedDoctor.getRatings();
//                String ratings = currentDoctor.getRatings();

                //bundling to pass
                Bundle doctorDetails = new Bundle();
                doctorDetails.putString("name", name);
                doctorDetails.putString("imageUrl", imageUrl);
                doctorDetails.putString("experience", experience);
                doctorDetails.putString("ratings", ratings);

                String[] selectedArray = getRandomArray(s1, s2, s3, s4, s5, s6);
                if (selectedArray != null) {
                    doctorDetails.putStringArray("timeSlots", selectedArray);
                } else {
                    // Handle the case where the selectedArray is null
                    Log.e("DetailedBookAppointment", "Selected array is null");
                }



                Intent i = new Intent(getContext(), DetailedBookAppointment.class);
                i.putExtras(doctorDetails);
                getContext().startActivity(i);

            }
        });

        return convertView;
    }

    private String[] getRandomArray(String[]... arrays) {
        Random random = new Random();
        return arrays[random.nextInt(arrays.length)];
    }
}
