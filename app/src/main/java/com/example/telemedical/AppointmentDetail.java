package com.example.telemedical;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AppointmentDetail extends AppCompatActivity implements View.OnClickListener {
Button cancel,reshedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        Toolbar toolbar=findViewById(R.id.toolbar_appointment_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cancel=findViewById(R.id.cancellAppoint);
        reshedule=findViewById(R.id.rescheduleappoint);
        cancel.setOnClickListener(this);
        reshedule.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancellAppoint:
                finish();
                break;
            case R.id.rescheduleappoint:
                Intent intent=new Intent(this,BookAppointment.class);
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
