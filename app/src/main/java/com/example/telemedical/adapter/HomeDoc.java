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

import com.example.telemedical.Booking;
import com.example.telemedical.R;
import com.example.telemedical.dialog.BookingDialog;
import com.example.telemedical.frag.HomeFrag;

public class HomeDoc extends RecyclerView.Adapter<HomeDoc.item> implements View.OnClickListener {
    Context c;

    public HomeDoc(Context c) {
        this.c = c;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recomendedview, parent, false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {
holder.book.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.book_Doc_home:
            openDialog();
                break;

        }
    }


    public class item extends RecyclerView.ViewHolder {
        CardView book;
        public item(@NonNull View itemView) {
            super(itemView);
            book=itemView.findViewById(R.id.book_Doc_home);
        }
    }

    public void openDialog(){
    final Dialog dialog=new Dialog(c);
    dialog.setContentView(R.layout.booking_dialog_view);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    Button close=dialog.findViewById(R.id.close_dialog_booking);
        LinearLayout voice=dialog.findViewById(R.id.voice_booking);
        LinearLayout video=dialog.findViewById(R.id.video_booking);
        LinearLayout physicall=dialog.findViewById(R.id.physical);


        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(c, Booking.class);
                c.startActivity(intent);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(c, Booking.class);
                c.startActivity(intent);
            }
        });

        physicall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(c, Booking.class);
                c.startActivity(intent);
            }
        });


    close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });





    dialog.show();


    }



}
