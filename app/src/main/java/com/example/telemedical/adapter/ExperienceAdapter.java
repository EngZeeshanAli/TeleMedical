package com.example.telemedical.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.R;
public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.item> {
    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.experience_view,parent,false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class item extends RecyclerView.ViewHolder{

        public item(@NonNull View itemView) {
            super(itemView);
        }
    }
}