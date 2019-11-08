package com.example.telemedical.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.DoctorDetail;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveDocAdapter extends RecyclerView.Adapter<LiveDocAdapter.Item> {
    Context c;
    ArrayList<DoctorFormater> list;

    public LiveDocAdapter(Context c, ArrayList<DoctorFormater> gernallist) {
        this.c = c;
        this.list=gernallist;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.doc_v2,parent,false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        DoctorFormater formatter=list.get(position);
        Glide.with(c).load(formatter.getProfileImg()).into(holder.doc2);
        holder.ratings.setRating(Float.parseFloat(formatter.getRanking()));
        holder.practice.setText(formatter.getExpert());
        holder.name.setText(formatter.getName());
        holder.doctorHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), DoctorDetail.class);
                intent.putExtra("name",formatter.getName());
                intent.putExtra("constype",formatter.getConsulationType());
                intent.putExtra("expert",formatter.getExpert());
                intent.putExtra("fee",formatter.getFee());
                intent.putExtra("gender",formatter.getGender());
                intent.putExtra("language",formatter.getLanguage());
                intent.putExtra("phone",formatter.getPhone());
                intent.putExtra("address",formatter.getPrimaryAddress());
                intent.putExtra("img",formatter.getProfileImg());
                intent.putExtra("rank",formatter.getRanking());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder{
        CardView doctorHire;
        CircleImageView doc2;
        TextView name,practice;
        RatingBar ratings;
        public Item(@NonNull View itemView) {
            super(itemView);
            doctorHire=itemView.findViewById(R.id.doctorHire);
            doc2=itemView.findViewById(R.id.profile_image_doc);
            name=itemView.findViewById(R.id.doc2_name);
            practice=itemView.findViewById(R.id.doc2_practice);
            ratings=itemView.findViewById(R.id.ratingBar_doc2);
        }
    }
}
