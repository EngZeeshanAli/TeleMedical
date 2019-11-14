package com.example.telemedical.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.AppointmentDetail;
import com.example.telemedical.Formaters.AppointmentFormatter;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpcomingsAppointmentsAdapter extends RecyclerView.Adapter<UpcomingsAppointmentsAdapter.Item> {
    Context context;
    ArrayList<AppointmentFormatter> list;

    public UpcomingsAppointmentsAdapter(Context context, ArrayList<AppointmentFormatter> list) {
        this.context = context;
        this.list = list;
    }

    public static String getMonthShortName(int monthNumber) {
        String monthName = "";
        monthNumber = monthNumber - 1;
        if (monthNumber >= 0 && monthNumber < 12)
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                //simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            } catch (Exception e) {
                if (e != null)
                    e.printStackTrace();
            }
        return monthName;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.appointmentview, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        AppointmentFormatter formatter = list.get(position);
        holder.type.setText(formatter.getType());
        String dater = formatter.getDate();
        String[] date = dater.split("-");
        holder.date.setText(getMonthShortName(Integer.parseInt(date[1])) + "\n" + date[0]);
        holder.time.setText(formatter.getTime());
        getGernalDoctors(holder.docImg, holder.name, formatter.getDocUid());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AppointmentDetail.class);
                intent.putExtra("type", formatter.getType());
                intent.putExtra("time", formatter.getTime());
                intent.putExtra("date", formatter.getDate());
                intent.putExtra("docId", formatter.getDocUid());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void getGernalDoctors(CircleImageView v, TextView n, String id) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DoctorFormater formatter = postSnapshot.getValue(DoctorFormater.class);
                    if (id.equals(formatter.getUid())) {
                        Glide.with(context).load(formatter.getProfileImg()).into(v);
                        n.setText(formatter.getName());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public class Item extends RecyclerView.ViewHolder {
        CardView cardView;
        CircleImageView docImg;
        TextView name, type, date, time;

        public Item(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.appointview);
            docImg = itemView.findViewById(R.id.docImg_appoint);
            date = itemView.findViewById(R.id.date_appoint_view);
            time = itemView.findViewById(R.id.time_appoint_view);
            type = itemView.findViewById(R.id.type_appoint_view);
            name = itemView.findViewById(R.id.name_doc_apoint);
        }
    }
}
