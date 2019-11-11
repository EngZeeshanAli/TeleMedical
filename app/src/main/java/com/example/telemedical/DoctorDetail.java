package com.example.telemedical;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.telemedical.dialog.BookingDialog;
import com.example.telemedical.main.ConnectActivity;
import com.example.telemedical.tabs.DocProfileTabPagerAdapter;
import com.example.telemedical.tabs.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorDetail extends AppCompatActivity implements View.OnClickListener {
    Button fram;
    CircleImageView doc;
    TextView name, expert;
    RatingBar ratings;
    Button booking;
    String docId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        Toolbar toolbar = findViewById(R.id.toolbar_doc_detail);
        toolbar.setTitle("Doctor Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Experience"));
        tabLayout.addTab(tabLayout.newTab().setText("Feedback"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager pager = findViewById(R.id.docter_pager);
        PagerAdapter adapter = new DocProfileTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        pager.setAdapter(adapter);
        fram = findViewById(R.id.make_call_detail);
        fram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorDetail.this, ConnectActivity.class));
            }
        });
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
        initGui();
    }

    void initGui() {
        doc = findViewById(R.id.profile_image_doc_detail);
        name = findViewById(R.id.name_for_doc);
        expert = findViewById(R.id.experties);
        ratings = findViewById(R.id.ratingIn_detail);
        booking = findViewById(R.id.boooking_doctor);
        booking.setOnClickListener(this);
        getSetData();
    }

    void getSetData() {
        Intent intent = getIntent();
        String img = intent.getStringExtra("img");
        String name = intent.getStringExtra("name");
        String expert = intent.getStringExtra("expert");
        docId = intent.getStringExtra("docId");
        Float rank = Float.parseFloat(intent.getStringExtra("rank"));
        Glide.with(this)
                .load(img)
                .into(doc);
        this.name.setText(name);
        this.ratings.setRating(rank);
        this.expert.setText(expert);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boooking_doctor:
                BookingDialog dialog = new BookingDialog();
                dialog.show(getSupportFragmentManager(), "Booking Dialog");
                dialog.setCancelable(false);
                break;
        }
    }
}
