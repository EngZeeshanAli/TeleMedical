
package com.example.telemedical.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.DoctorDetail;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.R;
import com.example.telemedical.adapter.MyFileAdapter;

public class DocProfileFrag extends Fragment {
    TextView consultaion, fee, gender, language, address, phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doc_profile, container, false);
        initGiu(view);

        return view;
    }

    void initGiu(View v) {
        consultaion = v.findViewById(R.id.consType);
        fee = v.findViewById(R.id.fee);
        gender = v.findViewById(R.id.gender_doc);
        language = v.findViewById(R.id.spoken);
        address = v.findViewById(R.id.addres);
        phone = v.findViewById(R.id.doc_phone);
        getSetValues();
    }
    void getSetValues() {
        Intent intent = getActivity().getIntent();
        consultaion.setText(intent.getStringExtra("constype"));
        fee.setText(intent.getStringExtra("fee"));
        gender.setText(intent.getStringExtra("gender"));
        language.setText(intent.getStringExtra("language"));
        phone.setText(intent.getStringExtra("phone"));
        address.setText(intent.getStringExtra("address"));
    }

}
