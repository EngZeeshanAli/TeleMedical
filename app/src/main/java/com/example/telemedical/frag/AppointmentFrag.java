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

import com.example.telemedical.Formaters.AppointmentFormatter;
import com.example.telemedical.R;
import com.example.telemedical.adapter.DashBoardAppointments;
import com.example.telemedical.adapter.HomeDoc;
import com.example.telemedical.adapter.UpcomingsAppointmentsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppointmentFrag extends Fragment {
    RecyclerView upcoming, previous;
    ArrayList<AppointmentFormatter> list;
    DatabaseReference mDatabase;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_appointment, container, false);
        init();
        upcoming = view.findViewById(R.id.upcomings);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        upcoming.setLayoutManager(horizontalLayoutManagaer);
        previous = view.findViewById(R.id.previous);
        previous.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        previous.setAdapter(new UpcomingsAppointmentsAdapter(getContext(), list));
        return view;
    }

    void init() {
        list = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("bookings");
        user = FirebaseAuth.getInstance().getCurrentUser();
        getAppointments();
    }


    void getAppointments() {
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (isAdded())
                    list.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    AppointmentFormatter formatter = postSnapshot.getValue(AppointmentFormatter.class);
                    if (formatter.getPatientUid().equals(user.getUid()) && formatter.getStatus().equals("live")) {
                        list.add(formatter);
                    }
                }
                upcoming.setAdapter(new UpcomingsAppointmentsAdapter(getContext(), list));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
