package com.example.telemedical;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.telemedical.Formaters.AppointmentFormatter;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.adapter.FindDocAdapter;
import com.example.telemedical.adapter.PateintTypeAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookAppointment extends AppCompatActivity {
    RecyclerView patients;
    Button booingConfirm;
    String docId, date, action, time;
    TextView name, type, daytime,
    //dialog views
    reference, dateDialog, timeDialog, toName, fee, contYpe;
    CircleImageView img;
    LinearLayout physical1, physical2;
    FirebaseUser user;
    DoctorFormater formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        Toolbar toolbar = findViewById(R.id.toolbar_bookingl);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initGui();
        patients = findViewById(R.id.whoPateint);
        patients.setLayoutManager(new GridLayoutManager(this, 3));
        patients.setAdapter(new PateintTypeAdapter(3));
    }

    void initGui() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        name = findViewById(R.id.doc_name_confirm);
        type = findViewById(R.id.appint_type_confirm);
        daytime = findViewById(R.id.dater_confirm);
        img = findViewById(R.id.confirm_doc_img);
        physical1 = findViewById(R.id.physical_address);
        physical2 = findViewById(R.id.physical_address2);
        booingConfirm = findViewById(R.id.confirmation_button);
        booingConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBooingConfirm(formatter);

            }
        });
        getItentData();
        getDoctors(docId);
    }


    void openDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirm_bboking_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        reference = dialog.findViewById(R.id.reference);
        dateDialog = dialog.findViewById(R.id.dialog_date);
        timeDialog = dialog.findViewById(R.id.dialog_time);
        toName = dialog.findViewById(R.id.toName);
        fee = dialog.findViewById(R.id.feeDialog);
        contYpe = dialog.findViewById(R.id.typeDialog);


        Button ok = dialog.findViewById(R.id.okthanks);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void getItentData() {
        Intent i = getIntent();
        time = i.getStringExtra("time");
        date = i.getStringExtra("date");
        action = i.getStringExtra("action");
        docId = i.getStringExtra("docId");
        if (!action.equals("Physical Appointment")) {
            physical1.setVisibility(View.GONE);
            physical2.setVisibility(View.GONE);
        }

    }

    void setBooingConfirm(DoctorFormater formatter) {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
        dialog.setMessage("Booking ...");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("bookings");
        AppointmentFormatter appointment = new AppointmentFormatter(docId, user.getUid(), date, time, formatter.getFee(), action, "live");
        mDatabase.push().setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                openDialog();
                reference.setText("Reference ID: \n" + mDatabase.push().getKey());
                dateDialog.setText(date);
                timeDialog.setText(time);
                toName.setText(user.getUid());
                fee.setText(formatter.getFee());
                contYpe.setText(action);

            }
        });
    }

    void getDoctors(String docId) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("doctors");
        mDatabase.child(docId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                formatter = snapshot.getValue(DoctorFormater.class);
                Glide.with(BookAppointment.this).load(formatter.getProfileImg()).into(img);
                name.setText(formatter.getName());
                type.setText(action);
                daytime.setText(date + "     " + time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
