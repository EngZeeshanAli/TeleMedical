package com.example.telemedical.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.R;

public class PrescriptionsAdapter extends RecyclerView.Adapter<PrescriptionsAdapter.item> {
    Context c;

    public PrescriptionsAdapter(Context c) {
        this.c = c;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.prescpitionview,parent,false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {
    holder.nested.setLayoutManager(new LinearLayoutManager(c));
    holder.nested.setAdapter(new NestedPrescription());


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class item extends RecyclerView.ViewHolder{
        RecyclerView nested;
        public item(@NonNull View itemView) {
            super(itemView);
            nested=itemView.findViewById(R.id.nested_prescrpitions);
        }
    }
}


