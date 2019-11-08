package com.example.telemedical.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.telemedical.R;
import com.example.telemedical.SignIn;
import com.google.android.material.tabs.TabLayout;

public class Welcome1 extends Fragment {
    Button button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.welocome1,container,false);
        button=view.findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabs = (TabLayout)getActivity().findViewById(R.id.tabs_welcome);
                tabs.getTabAt(1).select();
            }
        });
        return view;
    }
}
