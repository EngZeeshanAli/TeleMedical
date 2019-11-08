package com.example.telemedical;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.telemedical.adapter.TimeAdapter;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

public class PickTime extends AppCompatActivity {
RecyclerView morningTime,afternonTime,evening;
Button gotoBookAppointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time);
        Toolbar toolbar=findViewById(R.id.toolbar_booking_time);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);
        picker
                .setDays(20)
                .setOffset(10)
                .setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(Color.DKGRAY)
                .setDayOfWeekTextColor(Color.DKGRAY)
                .showTodayButton(false)
                .init();

        // or on the View directly after init was completed
        picker.setBackgroundColor(Color.TRANSPARENT);
        picker.setDate(new DateTime().plusDays(4));

        //morning time
        morningTime=findViewById(R.id.morningtime);
        morningTime.setLayoutManager(new GridLayoutManager(this,4));
        morningTime.setAdapter(new TimeAdapter(3));

        afternonTime=findViewById(R.id.afternonTime);
        afternonTime.setLayoutManager(new GridLayoutManager(this,4));
        afternonTime.setAdapter(new TimeAdapter(7));

        evening=findViewById(R.id.eveingTime);
        evening.setLayoutManager(new GridLayoutManager(this,4));
        evening.setAdapter(new TimeAdapter(1));

        gotoBookAppointment=findViewById(R.id.gotoBookAppointment);
        gotoBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PickTime.this,BookAppointment.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
