package com.example.telemedical.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class WelcomePagerAdapter extends FragmentStatePagerAdapter {
    int noOfTabs;

    public WelcomePagerAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs=noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Welcome1 welcome1=new Welcome1();
                return welcome1;

            case 1:
                Welcome2 welcome2=new Welcome2();
                return welcome2;

            default:
                Welcome1 def=new Welcome1();
                return def;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
