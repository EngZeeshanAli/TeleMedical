package com.example.telemedical.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.telemedical.tabs.MyFiles;
import com.example.telemedical.tabs.Shared;

public class WelcomeAdapter extends FragmentStatePagerAdapter {
    int noOfTabs;

    public WelcomeAdapter(@NonNull FragmentManager fm,int noOfTabs) {
        super(fm);
        this.noOfTabs=noOfTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Shared shared=new Shared();
                return shared;

            case 1:
                MyFiles myFiles=new MyFiles();
                return myFiles;

            default:

                Shared def=new Shared();
                return def;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
