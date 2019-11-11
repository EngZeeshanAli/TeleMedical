package com.example.telemedical.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.BookAppointment;
import com.example.telemedical.PickTime;
import com.example.telemedical.R;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.item> {
    int timer;
    String dater, type, docId, time;
    Context c;
    ArrayList<TextView> timesList = new ArrayList();

    public TimeAdapter(int timer, Context c) {
        this.timer = timer;
        this.c = c;
        getIntentData(c);
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.time_view,parent,false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {
        timesList.add(holder.timePicked);
        holder.timePicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView timerView : timesList) {
                    timerView.setBackgroundColor(Color.parseColor("#456C8192"));
                    timerView.setTextColor(Color.parseColor("#7C838D"));
                }
                time = holder.timePicked.getText().toString();
                holder.timePicked.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));
                holder.timePicked.setTextColor(Color.WHITE);
                Intent intent = new Intent(c, BookAppointment.class);
                intent.putExtra("date", dater);
                intent.putExtra("action", type);
                intent.putExtra("docId", docId);
                intent.putExtra("time", time);
                ((Activity) c).finish();
                c.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return timer;
    }

    void getIntentData(Context c) {
        Intent i = ((Activity) c).getIntent();
        dater = i.getStringExtra("date");
        type = i.getStringExtra("action");
        docId = i.getStringExtra("docId");
    }

    public class item extends RecyclerView.ViewHolder{
        TextView timePicked;
        public item(@NonNull View itemView) {
            super(itemView);
            timePicked = itemView.findViewById(R.id.timePicked);

        }
    }
}
