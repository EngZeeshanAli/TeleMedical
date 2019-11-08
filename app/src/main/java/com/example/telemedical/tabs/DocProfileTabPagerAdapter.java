package com.example.telemedical.tabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.telemedical.frag.DocFeedBack;
import com.example.telemedical.frag.FragDocExperience;


public class DocProfileTabPagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;

    public DocProfileTabPagerAdapter(@NonNull FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DocProfileFrag frag = new DocProfileFrag();
                return frag;

            case 1:
                FragDocExperience experience = new FragDocExperience();
                return experience;

            case 2:
                DocFeedBack docFeedBack = new DocFeedBack();
                return docFeedBack;

            default:
                DocProfileFrag rag = new DocProfileFrag();
                return rag;

        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
