package com.example.telemedical.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;
    public TabPagerAdapter(@NonNull FragmentManager fm, int noOfTabs) {
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
