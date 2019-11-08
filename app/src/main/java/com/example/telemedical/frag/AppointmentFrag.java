package com.example.telemedical.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.R;
import com.example.telemedical.adapter.HomeDoc;
import com.example.telemedical.adapter.UpcomingsAppointmentsAdapter;

public class AppointmentFrag extends Fragment {
RecyclerView upcoming,previous;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_appointment,container,false);


        upcoming=view.findViewById(R.id.upcomings);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        upcoming.setLayoutManager(horizontalLayoutManagaer);
        upcoming.setAdapter(new UpcomingsAppointmentsAdapter(getContext()));

        previous=view.findViewById(R.id.previous);
        previous.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        previous.setAdapter(new UpcomingsAppointmentsAdapter(getContext()));

        return view;
    }
}
