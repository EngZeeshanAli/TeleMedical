package com.example.telemedical.controls;

import android.app.Activity;
import android.view.View;

import com.example.telemedical.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationController {
    int id;
    Activity activity;

    public BottomNavigationController(int id, Activity activity) {
        this.id = id;
        this.activity = activity;
        itemChecked(id, activity);
    }

    void itemChecked(int id, Activity activity) {
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) activity.findViewById(R.id.bottomNavigationView);
        View view = bottomNavigationView.findViewById(id);
        view.performClick();
    }
}
