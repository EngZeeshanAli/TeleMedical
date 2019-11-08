package com.example.telemedical.main;


import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.telemedical.R;

public class PickCall extends AppCompatActivity {
    Button answer, reject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_call);
        answer=findViewById(R.id.ans);
        reject=findViewById(R.id.reject);
        ConnectActivity callPick=new ConnectActivity();
        callPick.connectToRoom("alialialialialialiali",false,false,false,0);

    }






}
