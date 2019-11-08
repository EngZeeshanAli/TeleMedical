package com.example.telemedical.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.R;

import java.util.ArrayList;

public class FindDocAdapter extends RecyclerView.Adapter<FindDocAdapter.Item> {
    ArrayList<DoctorFormater> list;
    Context c;
    public FindDocAdapter(ArrayList<DoctorFormater> list, Context c) {
        this.list = list;
        this.c =c;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.doc_v1, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        DoctorFormater formatter=list.get(position);
        holder.doc1_name.setText(formatter.getName());
        Glide.with(c).load(formatter.getProfileImg()).into(holder.doc1_img);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        ImageView doc1_img;
        TextView doc1_name;

        public Item(@NonNull View itemView) {
            super(itemView);
            doc1_img = itemView.findViewById(R.id.doc1_img);
            doc1_name = itemView.findViewById(R.id.doc1_name);
        }
    }
}
