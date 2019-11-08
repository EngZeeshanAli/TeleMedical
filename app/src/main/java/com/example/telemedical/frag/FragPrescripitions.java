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
import com.example.telemedical.adapter.PrescriptionsAdapter;

public class FragPrescripitions extends Fragment {
    RecyclerView prescrpition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_prescription,container,false);
        prescrpition=view.findViewById(R.id.frag_presciption_view);
        prescrpition.setLayoutManager( new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        prescrpition.setAdapter(new PrescriptionsAdapter(getActivity()));

        return view;
    }
}
