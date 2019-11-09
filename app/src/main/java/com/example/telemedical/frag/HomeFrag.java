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

import com.example.telemedical.R;
import com.example.telemedical.adapter.DashBoardAppointments;
import com.example.telemedical.adapter.HomeDoc;
import com.example.telemedical.adapter.MainAppointAdapter;
import com.example.telemedical.controls.BottomNavigationController;
import com.example.telemedical.controls.UiControls;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFrag extends Fragment implements View.OnClickListener {
    CardView goEhr, findDoc, prescriptions, history, messages;
    RecyclerView appintments;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        initGui(view);
        return view;
    }

    void initGui(View v){
        goEhr=v.findViewById(R.id.go_to_ehr);
        goEhr.setOnClickListener(this);

        findDoc=v.findViewById(R.id.find_doc_home);
        findDoc.setOnClickListener(this);

        prescriptions=v.findViewById(R.id.home_pres);
        prescriptions.setOnClickListener(this);

        history=v.findViewById(R.id.medical_home);
        history.setOnClickListener(this);

        messages=v.findViewById(R.id.message_home);
        messages.setOnClickListener(this);

        appintments=v.findViewById(R.id.appointments_home);
        appintments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        appintments.setAdapter(new DashBoardAppointments());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.go_to_ehr:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new EhrFiles()).commit();
                break;
            case R.id.appointments_home:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new AppointmentFrag()).commit();
                new BottomNavigationController(R.id.appointfrag, getActivity());
                break;
            case R.id.find_doc_home:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new FindDoctor()).commit();
                new BottomNavigationController(R.id.docFrag, getActivity());
                break;
            case R.id.home_pres:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new FragPrescripitions()).commit();
                break;
            case R.id.medical_home:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new FragMedicalHistory()).commit();
                break;
            case R.id.message_home:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new MessagingFrag()).commit();
                new BottomNavigationController(R.id.messageFrag, getActivity());

                break;
        }
    }


}
