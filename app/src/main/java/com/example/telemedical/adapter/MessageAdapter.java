package com.example.telemedical.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.MessageFormatter;
import com.example.telemedical.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageItem> {
    Context c;
    FirebaseUser user;
    ArrayList<MessageFormatter> list;
    static final int MESSAGE_LEFT = 0;
    static final int MESSAGE_RIGHT = 1;

    public MessageAdapter(Context c, ArrayList<MessageFormatter> list) {
        this.c = c;
        this.list = list;
    }

    @NonNull
    @Override
    public MessageItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MESSAGE_RIGHT) {
            View view = inflater.inflate(R.layout.right_message, parent, false);
            return new MessageItem(view);
        } else {
            View view = inflater.inflate(R.layout.left_message, parent, false);
            return new MessageItem(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MessageItem holder, int position) {
        MessageFormatter formatter = list.get(position);
        holder.message.setText(formatter.getMessage());
        holder.timerDate.setText(formatter.getTimeStemp());
        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.timerDate.getVisibility() == View.GONE) {
                    holder.timerDate.setVisibility(View.VISIBLE);
                } else {
                    holder.timerDate.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (list.get(position).getSender().equals(user.getUid())) {
            return MESSAGE_RIGHT;
        } else {
            return MESSAGE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MessageItem extends RecyclerView.ViewHolder {
        TextView message, timerDate;

        public MessageItem(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.show_message);
            timerDate = itemView.findViewById(R.id.datetime_message);
        }
    }
}
