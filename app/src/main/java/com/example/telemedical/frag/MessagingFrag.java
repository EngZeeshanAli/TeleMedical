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
import java.util.Map;

public class MessagingFrag extends Fragment {
    RecyclerView onlineDoctors, mesages;
    private DatabaseReference mDatabase;
    ArrayList<DoctorFormater> list;
    ArrayList<MessageFormatter> messageList;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
    String doctorId, name,img;

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
        getMessage(user.getUid());
    }


    void getMessage(String myId) {
        messageList = new ArrayList<>();
        ArrayList id = new ArrayList();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot contact : dataSnapshot.getChildren()) {
                    String sendr = (String) contact.child("sender").getValue();
                    String reciver = (String) contact.child("reciver").getValue();
                    String message = (String) contact.child("message").getValue();
                    String timeStemp = (String) contact.child("timeStemp").getValue();
                    MessageFormatter messageFormatter = new MessageFormatter(sendr, reciver, message, timeStemp);
                    if (messageFormatter.getSender().equals(myId)
                            || messageFormatter.getReciver().equals(myId)) {
                        messageList.add(messageFormatter);
                    }
                    mesages.setAdapter(new MessagesListAdapter(getContext(), messageList));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    void getDoctors() {
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
