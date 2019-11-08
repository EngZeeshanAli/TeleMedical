package com.example.telemedical.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.R;
import com.example.telemedical.adapter.CertificatesAdapter;
import com.example.telemedical.adapter.EducationAdapter;
import com.example.telemedical.adapter.ExperienceAdapter;

public class FragDocExperience extends Fragment {
    RecyclerView experience, education, certificates;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.doc_expereince_frag,container,false);
        experience=view.findViewById(R.id.experience_recyclerView);
        education=view.findViewById(R.id.education_recycler);
        certificates=view.findViewById(R.id.certificate_recyclerview);

        experience.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        education.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        certificates.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


        experience.setAdapter(new ExperienceAdapter());
        education.setAdapter(new EducationAdapter());
        certificates.setAdapter(new CertificatesAdapter());



        return view;

    }
}
