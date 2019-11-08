package com.example.telemedical.adapter;

import android.content.Context;
import android.net.sip.SipAudioCall;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.Formaters.MessageFormatter;
import com.example.telemedical.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.Item> {
    Context c;
    ArrayList<MessageFormatter> list;
    String doctorId;
    FirebaseUser user;

    public MessagesListAdapter(Context c, ArrayList<MessageFormatter> list) {
        this.c = c;
        this.list = list;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.message_list, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        MessageFormatter formatter = list.get(position);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("doctors");


        if (user.getUid().equals(formatter.getSender())) {
            doctorId = formatter.getReciver();
        } else {
            doctorId = formatter.getSender();
        }


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DoctorFormater formatter = postSnapshot.getValue(DoctorFormater.class);
                    holder.name.setText(formatter.getName());
                    Glide.with(c).load(formatter.getProfileImg()).into(holder.senderImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.msg.setText(formatter.getMessage());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView msg, name;
        CircleImageView senderImage;

        public Item(@NonNull View v) {
            super(v);
            msg = v.findViewById(R.id.msg);
            name = v.findViewById(R.id.sender_name);
            senderImage = v.findViewById(R.id.doc_img_message_view);

        }
    }


}
