package com.example.telemedical.frag;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.telemedical.R;
import com.example.telemedical.tabs.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class EhrFiles extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_ehr,container,false);
        TabLayout tabLayout=view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("SHARED"));
        tabLayout.addTab(tabLayout.newTab().setText("MY FILES"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager pager=view.findViewById(R.id.pager);
        PagerAdapter adapter=new TabPagerAdapter(getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
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
        return view;
    }
}
