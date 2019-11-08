package com.example.telemedical.adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.AppointmentDetail;
import com.example.telemedical.R;

public class UpcomingsAppointmentsAdapter extends RecyclerView.Adapter<UpcomingsAppointmentsAdapter.Item> {
    Context context;

    public UpcomingsAppointmentsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.appointmentview,parent,false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AppointmentDetail.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class Item extends RecyclerView.ViewHolder{
        CardView cardView;
        public Item(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.appointview);
        }
    }
}
