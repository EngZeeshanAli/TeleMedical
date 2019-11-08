package com.example.telemedical.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.telemedical.Formaters.DoctorFormater;
import com.example.telemedical.R;
import com.example.telemedical.adapter.FindDocAdapter;
import com.example.telemedical.adapter.LiveDocAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FindDoctor extends Fragment {
    RecyclerView docList, liveDoc, gernaldoc;
    private DatabaseReference mDatabase;
    ArrayList<DoctorFormater> list;
    ArrayList<DoctorFormater> gernallist;
    TextView liveCount, gernalPhysicians;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_doc, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<DoctorFormater>();
        gernallist = new ArrayList<DoctorFormater>();
        initUi(view);
        getDoctors();
        getGernalDoctors();

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        docList = view.findViewById(R.id.doc_list_find_frag);
        docList.setLayoutManager(horizontalLayoutManagaer);
        docList.setAdapter(new FindDocAdapter(list, getActivity()));

        liveDoc = view.findViewById(R.id.live_doc_find_frag);
        liveDoc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        liveDoc.setAdapter(new LiveDocAdapter(getActivity(), gernallist));


        gernaldoc = view.findViewById(R.id.gernal_Particains_View);
        gernaldoc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        gernaldoc.setAdapter(new LiveDocAdapter(getActivity(), gernallist));
        return view;
    }

    void initUi(View v) {
        liveCount = v.findViewById(R.id.live_doc_count);
        gernalPhysicians = v.findViewById(R.id.gernal_Particains_count);

    }

    void getDoctors() {
        list.clear();
        mDatabase.keepSynced(true);
        mDatabase.child("doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DoctorFormater formatter = postSnapshot.getValue(DoctorFormater.class);
                    list.add(formatter);
                    liveCount.setText("Live Doctors (" + list.size() + ")");
                }
                docList.setAdapter(new FindDocAdapter(list, getActivity()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    void getGernalDoctors() {
        mDatabase.keepSynced(true);
        mDatabase.child("doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    DoctorFormater formatter = postSnapshot.getValue(DoctorFormater.class);
                    gernallist.add(formatter);
                    gernalPhysicians.setText("General Practitioner (" + gernallist.size() + ")");

                }
                gernaldoc.setAdapter(new LiveDocAdapter(getActivity(), gernallist));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


}
