package com.example.telemedical;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.ChatList;
import com.example.telemedical.Formaters.MessageFormatter;
import com.example.telemedical.adapter.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatBoard extends AppCompatActivity implements View.OnClickListener {
    RecyclerView messsagesChat, chatR;
    EditText messageBody;
    Button sendButton;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;
    LinearLayout back;
    ArrayList<MessageFormatter> list;
    CircleImageView reciverView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_board);
        toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        initGui();
    }

    void initGui() {
        messsagesChat = findViewById(R.id.message);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);
        back = findViewById(R.id.go_to_messageFrag);
        back.setOnClickListener(this);
        messageBody = findViewById(R.id.messageBody);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(Constants.chats);
        chatR = findViewById(R.id.message_inoming_outgoing);
        chatR.setLayoutManager(new LinearLayoutManager(this));
        getMessage(user.getUid(), reciverId());
        reciverView = toolbar.findViewById(R.id.reciver_img);
        Glide.with(this).load(reciverImg()).into(reciverView);
    }

    void sendMessage() {
        if (!TextUtils.isEmpty(messageBody.getText().toString())) {
            Date date = new Date();
            SimpleDateFormat formater = new SimpleDateFormat("EEE, MMM dd hh:mm a");
            String dateAndTime = formater.format(date);
            String msg = messageBody.getText().toString();
            MessageFormatter formatter = new MessageFormatter(user.getUid(), reciverId(), messageBody.getText().toString(), dateAndTime);
            myRef.push().setValue(formatter, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    messageBody.getText().clear();
                }
            });
            DatabaseReference mmyRef = FirebaseDatabase.getInstance().getReference("ChatList").child(user.getUid()).child(reciverId());
            mmyRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ChatList chat = new ChatList(reciverId(), msg, dateAndTime);
                    mmyRef.setValue(chat);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            messageBody.setError("At Least One Charater !");
        }
    }

    void getMessage(String myId, String Sender) {
        list = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot contact : dataSnapshot.getChildren()) {
                    String sendr = (String) contact.child("sender").getValue();
                    String reciver = (String) contact.child("reciver").getValue();
                    String message = (String) contact.child("message").getValue();
                    String timeStemp = (String) contact.child("timeStemp").getValue();
                    MessageFormatter messageFormatter = new MessageFormatter(sendr, reciver, message, timeStemp);
                    if (messageFormatter.getReciver().equals(myId) && messageFormatter.getSender().equals(Sender)
                            || messageFormatter.getSender().equals(myId) && messageFormatter.getReciver().equals(Sender)) {
                        list.add(messageFormatter);
                    }
                    chatR.scrollToPosition(list.size() - 1);
                    chatR.setAdapter(new MessageAdapter(getApplicationContext(), list));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    String reciverId() {
        Intent i = getIntent();
        String returner = i.getStringExtra("reciever");
        return returner;
    }

    String reciverImg() {
        Intent i = getIntent();
        String imgReturn = i.getStringExtra("img");
        return imgReturn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_board_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton:
                sendMessage();
                break;
            case R.id.go_to_messageFrag:
                finish();
                break;
        }
    }
}
