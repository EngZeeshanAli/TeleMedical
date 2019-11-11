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
import android.widget.TextView;
import android.widget.Toast;

import com.example.telemedical.adapter.TimeAdapter;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PickTime extends AppCompatActivity {
RecyclerView morningTime,afternonTime,evening;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time);
        Toolbar toolbar=findViewById(R.id.toolbar_booking_time);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //morning time
        morningTime=findViewById(R.id.morningtime);
        morningTime.setLayoutManager(new GridLayoutManager(this,4));
        morningTime.setAdapter(new TimeAdapter(3, this));

        afternonTime=findViewById(R.id.afternonTime);
        afternonTime.setLayoutManager(new GridLayoutManager(this,4));
        afternonTime.setAdapter(new TimeAdapter(7, this));

        evening=findViewById(R.id.eveingTime);
        evening.setLayoutManager(new GridLayoutManager(this,4));
        evening.setAdapter(new TimeAdapter(1, this));


    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


