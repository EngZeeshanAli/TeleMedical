package com.example.telemedical;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.telemedical.controls.UiControls;
import com.example.telemedical.tabs.TabPagerAdapter;
import com.example.telemedical.tabs.WelcomePagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class Welcome extends AppCompatActivity {
    ImageView getStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        new UiControls(this);
        setContentView(R.layout.activity_welcome);
        initGui();
    }

    void initGui() {
        TabLayout tabLayout=findViewById(R.id.tabs_welcome);
        tabLayout.addTab(tabLayout.newTab().setText("."));
        tabLayout.addTab(tabLayout.newTab().setText("."));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager pager=findViewById(R.id.pager_welcome);
        PagerAdapter adapter=new WelcomePagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
