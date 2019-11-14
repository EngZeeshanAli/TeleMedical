package com.example.telemedical.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.ChatBoard;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnlineDoctorsMessageAdapter extends RecyclerView.Adapter<OnlineDoctorsMessageAdapter.Item> {
    ArrayList<DoctorFormater> list;
    Context c;

    public OnlineDoctorsMessageAdapter(ArrayList<DoctorFormater> list, Context c) {
        this.list = list;
        this.c = c;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.online_doc_view,parent,false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
    DoctorFormater formater=list.get(position);
    holder.docName.setText(formater.getName());
    Glide.with(c).load(formater.getProfileImg()).into(holder.img);
    holder.img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(c, ChatBoard.class);
            intent.putExtra("reciever",formater.getUid());
            intent.putExtra("img", formater.getProfileImg());
            intent.putExtra("name", formater.getName());
            c.startActivity(intent);
        }
    });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView docName;
        public Item(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.message_img_view);
            docName=itemView.findViewById(R.id.doc_name_message);


        }
    }
}
