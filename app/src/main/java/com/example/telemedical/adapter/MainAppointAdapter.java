package com.example.telemedical.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.AppointmentDetail;
import com.example.telemedical.Booking;
import com.example.telemedical.R;
import com.example.telemedical.dialog.BookingDialog;
import com.example.telemedical.frag.HomeFrag;

public class MainAppointAdapter extends RecyclerView.Adapter<MainAppointAdapter.item> implements View.OnClickListener {
    Context c;

    public MainAppointAdapter(Context c) {
        this.c = c;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_appointement_view, parent, false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {
        holder.book.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.docdoc:
                Intent intent=new Intent(c, AppointmentDetail.class);
                c.startActivity(intent);
                break;

        }
    }


    public class item extends RecyclerView.ViewHolder {
        CardView book;
        public item(@NonNull View itemView) {
            super(itemView);
            book=itemView.findViewById(R.id.docdoc);
        }
    }




}
