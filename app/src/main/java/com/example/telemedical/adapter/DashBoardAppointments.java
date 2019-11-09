package com.example.telemedical.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.R;

public class DashBoardAppointments extends RecyclerView.Adapter<DashBoardAppointments.Item> {
    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.appointment_slider_view, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class Item extends RecyclerView.ViewHolder {
        public Item(@NonNull View itemView) {
            super(itemView);
        }
    }

}
