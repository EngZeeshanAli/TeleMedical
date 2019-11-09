package com.example.telemedical.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.ChatBoard;
import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.ChatList;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.Formaters.MessageFormatter;
import com.example.telemedical.R;
import com.example.telemedical.frag.DocFeedBack;
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
    ArrayList list;
    FirebaseUser user;
    ArrayList<ChatList> listChat;
    String theLastMessage;

    public MessagesListAdapter(Context c, ArrayList list, ArrayList<ChatList> listChat) {
        this.c = c;
        this.list = list;
        this.listChat = listChat;
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
        DoctorFormater doc = (DoctorFormater) list.get(position);
        ChatList chatMsg = listChat.get(position);
        Glide.with(c).load(doc.getProfileImg()).into(holder.senderImage);
        holder.name.setText(doc.getName());
        gettingLastMessage(doc.getUid(), holder.msg);
        //holder.msg.setText(chatMsg.getMsg());
        holder.timeStemp.setText(chatMsg.getTimeStemp());
        holder.gotoMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, ChatBoard.class);
                intent.putExtra("reciever", doc.getUid());
                intent.putExtra("img", doc.getProfileImg());
                c.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView msg, name, timeStemp;
        CircleImageView senderImage;
        LinearLayout gotoMessage;

        public Item(@NonNull View v) {
            super(v);
            msg = v.findViewById(R.id.msg);
            name = v.findViewById(R.id.sender_name);
            senderImage = v.findViewById(R.id.doc_img_message_view);
            timeStemp = v.findViewById(R.id.timeStemp);
            gotoMessage = v.findViewById(R.id.go_to_message);
        }
    }


    void gettingLastMessage(String userId, TextView lastMsg) {
        theLastMessage = "default";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chatboard");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MessageFormatter chat = ds.getValue(MessageFormatter.class);
                    if (chat.getReciver().equals(user.getUid()) && chat.getSender().equals(userId) ||
                            chat.getReciver().equals(userId) && chat.getSender().equals(user.getUid())) {
                        theLastMessage = chat.getMessage();
                    }
                }

                switch (theLastMessage) {
                    case "default":
                        lastMsg.setText("no message");
                        break;
                    default:
                        lastMsg.setText(theLastMessage);
                        break;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
