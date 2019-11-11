package com.example.telemedical.frag;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.Formaters.AppointmentFormatter;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.R;
import com.example.telemedical.adapter.DashBoardAppointments;
import com.example.telemedical.adapter.FindDocAdapter;
import com.example.telemedical.adapter.HomeDoc;
import com.example.telemedical.adapter.MainAppointAdapter;
import com.example.telemedical.controls.BottomNavigationController;
import com.example.telemedical.controls.UiControls;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFrag extends Fragment implements View.OnClickListener {
    CardView goEhr, findDoc, prescriptions, history, messages;
    RecyclerView appintments;
    ArrayList<AppointmentFormatter> list;
    FirebaseUser user;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        initGui(view);
        return view;
    }

    void initGui(View v) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        goEhr = v.findViewById(R.id.go_to_ehr);
        goEhr.setOnClickListener(this);

        findDoc = v.findViewById(R.id.find_doc_home);
        findDoc.setOnClickListener(this);

        prescriptions = v.findViewById(R.id.home_pres);
        prescriptions.setOnClickListener(this);

        history = v.findViewById(R.id.medical_home);
        history.setOnClickListener(this);

        messages = v.findViewById(R.id.message_home);
        messages.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("bookings");
        list = new ArrayList<>();
        getAppointments();
        appintments = v.findViewById(R.id.appointments_home);
        appintments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    void getAppointments() {
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    AppointmentFormatter formatter = postSnapshot.getValue(AppointmentFormatter.class);
                    if (formatter.getPatientUid().equals(user.getUid()) && formatter.getStatus().equals("live")) {
                        list.add(formatter);
                    }
                }
                appintments.setAdapter(new DashBoardAppointments(list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_to_ehr:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new EhrFiles()).commit();
                break;
            case R.id.appointments_home:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new AppointmentFrag()).commit();
                new BottomNavigationController(R.id.appointfrag, getActivity());
                break;
            case R.id.find_doc_home:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FindDoctor()).commit();
                new BottomNavigationController(R.id.docFrag, getActivity());
                break;
            case R.id.home_pres:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FragPrescripitions()).commit();
                break;
            case R.id.medical_home:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FragMedicalHistory()).commit();
                break;
            case R.id.message_home:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new MessagingFrag()).commit();
                new BottomNavigationController(R.id.messageFrag, getActivity());

                break;
        }
    }


}
