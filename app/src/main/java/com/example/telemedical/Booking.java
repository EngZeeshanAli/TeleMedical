package com.example.telemedical;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Booking extends AppCompatActivity implements View.OnClickListener {
    Button datePicked;
    CalendarView calendarView;
    String dater, type, docId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        Toolbar toolbar = findViewById(R.id.toolbar_booking);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getIntentData();
        initGui();
    }

    void initGui() {
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {
                dater = date + "-" + month + "-" + year;
                Toast.makeText(getApplicationContext(), date + "-" + month + "-" + year, Toast.LENGTH_SHORT).show();
            }
        });
        datePicked = findViewById(R.id.datePicked);
        datePicked.setOnClickListener(this);


    }


    void getIntentData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("action");
        docId = intent.getStringExtra("docId");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.datePicked:
                Intent pickedTime = new Intent(Booking.this, PickTime.class);
                if (dater == null) {
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    dater = df.format(c);
                }
                pickedTime.putExtra("date", dater);
                pickedTime.putExtra("action", type);
                pickedTime.putExtra("docId", docId);
                startActivity(pickedTime);
                finish();
                break;
        }
    }
}
