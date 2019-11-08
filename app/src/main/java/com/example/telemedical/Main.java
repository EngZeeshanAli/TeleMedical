package com.example.telemedical;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.controls.UiControls;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

public class Main extends AppCompatActivity {
    private static final int REQUEST_TIME = 2000;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        OneSignal.startInit(this).init();

        new UiControls(this);
        setContentView(R.layout.activity_main);
        initGui();
        checkFirstTime();
    }

    void initGui() {
        sharedpreferences = getSharedPreferences(Constants.SPLASH_CHECK, Context.MODE_PRIVATE);
    }

    void setSharedpreferencesDataSet() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.SPLASH_CHECK, Constants.SPLASH_CHECK);
        editor.commit();
    }

    void checkFirstTime() {
        String checking = sharedpreferences.getString(Constants.SPLASH_CHECK, "");
        if (!checking.equals("")) {
            Intent intent = new Intent(Main.this, SignIn.class);
            runSplash(intent);
        } else {
            setSharedpreferencesDataSet();
            Intent intent = new Intent(Main.this, Welcome.class);
            runSplash(intent);
        }
    }




    void runSplash(Intent intent) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(intent);
            }
        }, REQUEST_TIME);
    }
}

