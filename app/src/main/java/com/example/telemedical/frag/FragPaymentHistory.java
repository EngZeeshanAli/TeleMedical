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
import com.example.telemedical.adapter.PaymenyHistoryAdapter;

public class FragPaymentHistory extends Fragment {
    RecyclerView paymentHistory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_payment_history,container,false);
        paymentHistory=view.findViewById(R.id.paymentsHistory);
        paymentHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        paymentHistory.setAdapter(new PaymenyHistoryAdapter());
        return view;
    }
}
