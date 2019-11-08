package com.example.telemedical;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.telemedical.adapter.PateintTypeAdapter;

public class BookAppointment extends AppCompatActivity {
RecyclerView patients;
Button booingConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        Toolbar toolbar=findViewById(R.id.toolbar_bookingl);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        patients=findViewById(R.id.whoPateint);
        patients.setLayoutManager(new GridLayoutManager(this,3));
        patients.setAdapter(new PateintTypeAdapter(3));
        booingConfirm=findViewById(R.id.confirmation_button);
        booingConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    void openDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.confirm_bboking_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button ok=dialog.findViewById(R.id.okthanks);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
