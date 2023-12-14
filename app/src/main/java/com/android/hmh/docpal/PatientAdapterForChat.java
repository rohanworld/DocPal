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

public class PatientAdapterForChat extends ArrayAdapter<SimplePatients> {
    private Context context;
    private List<SimplePatients> doctorsList;

    public PatientAdapterForChat(Context context, List<SimplePatients> doctorsList) {
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
    public SimplePatients getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_patients_to_chat, parent, false);
        }

        SimplePatients doctor = getItem(position);

        ImageView doctorImage = convertView.findViewById(R.id.patientImageForChat);
        TextView doctorName = convertView.findViewById(R.id.patientNameForChat);

        Glide.with(context)
                .load(R.drawable.patientimagedefault)
                .into(doctorImage);
        doctorName.setText(doctor.getName());

        return convertView;
    }
}
