package com.android.hmh.docpal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

// DoctorAdapterForChat.java
public class DoctorAdapterForChat extends ArrayAdapter<SimpleDoctors> {
    private Context context;
    private List<SimpleDoctors> doctorsList;

    public DoctorAdapterForChat(Context context, List<SimpleDoctors> doctorsList) {
        super(context, 0);
        this.context = context;
        this.doctorsList = doctorsList;
    }

    @Override
    public int getCount() {
        return doctorsList.size();
    }

    @Nullable
    @Override
    public SimpleDoctors getItem(int position) {
        return doctorsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_doctors_for_chat, parent, false);
        }

        SimpleDoctors doctor = getItem(position);

        ImageView doctorImage = convertView.findViewById(R.id.doctorImageForChat);
        TextView doctorName = convertView.findViewById(R.id.doctorNameForChat);

        Glide.with(context)
                .load(doctor.getImageUrl())
                .into(doctorImage);
        doctorName.setText(doctor.getName());

        return convertView;
    }
}
