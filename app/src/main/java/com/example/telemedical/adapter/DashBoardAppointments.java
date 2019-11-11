package com.example.telemedical.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.AppointmentDetail;
import com.example.telemedical.BookAppointment;
import com.example.telemedical.DoctorDetail;
import com.example.telemedical.Formaters.AppointmentFormatter;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoardAppointments extends RecyclerView.Adapter<DashBoardAppointments.Item> {
    ArrayList<AppointmentFormatter> list;
    DoctorFormater formatter;

    public DashBoardAppointments(ArrayList<AppointmentFormatter> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.appointment_slider_view, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        AppointmentFormatter appoint = list.get(position);
        getDocotr(appoint.getDocUid(), holder, position, appoint);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AppointmentDetail.class);
                intent.putExtra("type", appoint.getType());
                intent.putExtra("time", appoint.getTime());
                intent.putExtra("date", appoint.getDate());
                intent.putExtra("docId", appoint.getDocUid());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    void getDocotr(String docId, Item holder, int position, AppointmentFormatter appoint) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("doctors");
        mDatabase.child(docId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                formatter = snapshot.getValue(DoctorFormater.class);
                holder.name.setText(appoint.getDate() + "   " + appoint.getTime());
                holder.appoint.setText("Appointment With \n" + formatter.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class Item extends RecyclerView.ViewHolder {
        LinearLayout cardView;
        TextView name, appoint;

        public Item(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.home_frag_view_appointment);
            name = itemView.findViewById(R.id.docName_appoint);
            appoint = itemView.findViewById(R.id.appointNo);
        }
    }

}
