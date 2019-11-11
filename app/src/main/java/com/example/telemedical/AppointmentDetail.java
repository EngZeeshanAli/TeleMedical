package com.example.telemedical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.telemedical.Formaters.AppointmentFormatter;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.adapter.DashBoardAppointments;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class AppointmentDetail extends AppCompatActivity implements View.OnClickListener {
    Button cancel, reshedule;
    TextView type, time, duration, date, name, typeOnTop;
    DoctorFormater formatter;
    String types, times, docId, dates;
    CircleImageView docImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_appointment_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initGui();
    }

    void initGui() {
        cancel = findViewById(R.id.cancellAppoint);
        reshedule = findViewById(R.id.rescheduleappoint);
        cancel.setOnClickListener(this);
        reshedule.setOnClickListener(this);
        type = findViewById(R.id.type_appoint);
        time = findViewById(R.id.time_appoint);
        duration = findViewById(R.id.duration_appoint);
        date = findViewById(R.id.date_appoint);
        docImg = findViewById(R.id.deatail_pic_doc);
        name = findViewById(R.id.name_doc_detail);
        typeOnTop = findViewById(R.id.type_appoint_detail);
        getIntentData();

    }

    void getIntentData() {
        Intent intent = getIntent();
        types = intent.getStringExtra("type");
        docId = intent.getStringExtra("docId");
        times = intent.getStringExtra("time");
        dates = intent.getStringExtra("date");
        getDocotr(docId);
        type.setText(types);
        time.setText(times);
        date.setText(dates);
    }


    void getDocotr(String docId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("doctors");
        mDatabase.child(docId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                formatter = snapshot.getValue(DoctorFormater.class);
                Glide.with(AppointmentDetail.this).load(formatter.getProfileImg()).into(docImg);
                name.setText(formatter.getName());
                typeOnTop.setText(types);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancellAppoint:
                finish();
                break;
            case R.id.rescheduleappoint:
                Intent intent = new Intent(this, BookAppointment.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
