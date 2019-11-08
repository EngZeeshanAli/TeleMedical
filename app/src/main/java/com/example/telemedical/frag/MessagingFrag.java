package com.example.telemedical.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.ChatList;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.Formaters.MessageFormatter;
import com.example.telemedical.R;
import com.example.telemedical.adapter.FindDocAdapter;
import com.example.telemedical.adapter.MessageAdapter;
import com.example.telemedical.adapter.MessagesListAdapter;
import com.example.telemedical.adapter.OnlineDoctorsMessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class MessagingFrag extends Fragment {
    RecyclerView onlineDoctors, mesages;
    private DatabaseReference mDatabase;
    ArrayList<DoctorFormater> list;
    ArrayList<MessageFormatter> messageList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    ArrayList<DoctorFormater> userList;
    ArrayList<ChatList> chatLists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_messaging, container, false);
        list = new ArrayList<>();
        init();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getDoctors();
        onlineDoctors = view.findViewById(R.id.onlineMessagesDoctors);
        mesages = view.findViewById(R.id.message_view_point);
        onlineDoctors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mesages.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        onlineDoctors.setAdapter(new OnlineDoctorsMessageAdapter(list, getContext()));


        return view;
    }

    void init() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(Constants.chats);
        userList = new ArrayList<>();
        chatLists = new ArrayList<>();
        // getMessage(user.getUid());
        getMessageList();
    }

    private void getMessageList() {
        DatabaseReference mrf = FirebaseDatabase.getInstance().getReference("ChatList");
        mrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatLists.clear();
                for (DataSnapshot dk : dataSnapshot.getChildren()) {

                    for (DataSnapshot ds : dk.getChildren()) {
                        String id = (String) ds.child("id").getValue();
                        String msg = (String) ds.child("msg").getValue();
                        String timeStemp = (String) ds.child("timeStemp").getValue();
                        ChatList chat = new ChatList(id, msg, timeStemp);
                        chatLists.add(chat);
                    }

                }

                getChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getChats() {
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("doctors");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot dv : dataSnapshot.getChildren()) {
                    DoctorFormater docs = dv.getValue(DoctorFormater.class);
                    for (ChatList list : chatLists) {

                        if (docs.getUid().equals(list.getId()) && !user.getUid().equals(list.getId())) {
                            userList.add(docs);
                        }
                    }
                }
                getMessage();
                mesages.setAdapter(new MessagesListAdapter(getContext(), userList, chatLists));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    void getMessage() {
        messageList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot chat : dataSnapshot.getChildren()) {
                    String sendr = (String) chat.child("sender").getValue();
                    String reciver = (String) chat.child("reciver").getValue();
                    String message = (String) chat.child("message").getValue();
                    String timeStemp = (String) chat.child("timeStemp").getValue();
                    MessageFormatter messageFormatter = new MessageFormatter(sendr, reciver, message, timeStemp);

                    messageList.add(messageFormatter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void getDoctors() {
        list.clear();
        mDatabase.keepSynced(true);
        mDatabase.child("doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DoctorFormater formatter = postSnapshot.getValue(DoctorFormater.class);
                    list.add(formatter);
                }
                onlineDoctors.setAdapter(new OnlineDoctorsMessageAdapter(list, getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void dataFire(String doctorId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("doctors");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DoctorFormater formatter = postSnapshot.getValue(DoctorFormater.class);
                    if (formatter.getUid().equals(doctorId)) {


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
