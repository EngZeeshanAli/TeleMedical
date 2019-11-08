package com.example.telemedical.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.R;

public class PaymenyHistoryAdapter extends RecyclerView.Adapter<PaymenyHistoryAdapter.Item> {


    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.transactions_view,parent,false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class Item extends RecyclerView.ViewHolder{

        public Item(@NonNull View itemView) {
            super(itemView);
        }
    }
}
