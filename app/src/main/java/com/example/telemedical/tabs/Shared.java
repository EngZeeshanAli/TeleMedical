package com.example.telemedical.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.ConstantsUsage.Constants;
import com.example.telemedical.Formaters.ReportFormatter;
import com.example.telemedical.R;
import com.example.telemedical.adapter.MyFileAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Shared extends Fragment {
    RecyclerView shared;
    FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    ArrayList list=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_shared,container,false);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        getStarted();
        shared=view.findViewById(R.id.shared);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        shared.setLayoutManager(horizontalLayoutManagaer);
        shared.setAdapter(new MyFileAdapter(list,getActivity(),currentUser.getUid()));
        return view;
    }


    void getStarted(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Constants.REPORTS);
        myRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datas: dataSnapshot.getChildren()){
                    ReportFormatter formatter=datas.getValue(ReportFormatter.class);
                    list.add(formatter);
                }
                shared.setAdapter(new MyFileAdapter(list,getActivity(),currentUser.getUid()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
