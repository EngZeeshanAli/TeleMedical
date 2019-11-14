package com.example.telemedical.frag;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.telemedical.Formaters.PatientBio;
import com.example.telemedical.Formaters.UserFormater;
import com.example.telemedical.R;
import com.example.telemedical.adapter.HomeDoc;
import com.example.telemedical.adapter.UpcomingsAppointmentsAdapter;
import com.example.telemedical.controls.BottomNavigationController;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static org.webrtc.ContextUtils.getApplicationContext;

public class ProfileFrag extends Fragment implements View.OnClickListener {
    TextView name, profile, blood_group, height, weight;
    FirebaseUser user;
    CircleImageView circleImageView;
    LinearLayout bioentery, myDoctors, appointments, ehrFiles, payment;
    EditText getValue;
    Button setValue;
    ImageButton close;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);
        initGui(view);
        return view;
    }

    void initGui(View v) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        name = v.findViewById(R.id.current_user_name_profile);
        profile = v.findViewById(R.id.sample_profile);
        blood_group = v.findViewById(R.id.bloog_gp);
        blood_group.setOnClickListener(this);
        height = v.findViewById(R.id.height_profile);
        height.setOnClickListener(this);
        weight = v.findViewById(R.id.weight_profiel);
        weight.setOnClickListener(this);
        bioentery = v.findViewById(R.id.bioentery);
        circleImageView = v.findViewById(R.id.profile_image_fragProfile);
        getValue = v.findViewById(R.id.value_bio);
        setValue = v.findViewById(R.id.setValue);
        setValue.setOnClickListener(this);
        close = v.findViewById(R.id.close_bioTab);
        close.setOnClickListener(this);
        myDoctors = v.findViewById(R.id.go_to_doc_profile);
        myDoctors.setOnClickListener(this);
        appointments = v.findViewById(R.id.go_to_appoint_profile);
        appointments.setOnClickListener(this);
        ehrFiles = v.findViewById(R.id.go_to_ehr_profile);
        ehrFiles.setOnClickListener(this);
        payment = v.findViewById(R.id.go_to_payment_profile);
        payment.setOnClickListener(this);
        getUserInfo(user.getUid());
        getMedicalProfile();
    }

    void getMedicalProfile() {
        DatabaseReference bioReff = FirebaseDatabase.getInstance().getReference().child("medialBio").child(user.getUid());
        bioReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PatientBio bio = dataSnapshot.getValue(PatientBio.class);
                if (bio != null) {
                    if (bio.getBloodGp() != null) {
                        blood_group.setText(bio.getBloodGp());
                    }
                    if (bio.getHieght() != null) {
                        height.setText(bio.getHieght());
                    }
                    if (bio.getWeight() != null) {
                        weight.setText(bio.getWeight());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    void getUserInfo(String uid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("patients").child(uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (isAdded()) {
                    UserFormater formater = dataSnapshot.getValue(UserFormater.class);
                    String value = formater.getProfileImgUser();
                    if (value != null && !value.equals("")) {
                        Glide.with(getContext()).load(value).into(circleImageView);
                    }
                    name.setText(formater.getName());
                    profile.setText("Email: " + formater.getEmail() + "\n" + "Mobile: " + formater.getMobile() + "\n" + "Gender: " + formater.getGender());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.weight_profiel:
                bioentery.setVisibility(View.VISIBLE);
                getValue.setHint("Enter Weight");
                break;
            case R.id.height_profile:
                bioentery.setVisibility(View.VISIBLE);
                getValue.setHint("Enter Height");
                break;
            case R.id.bloog_gp:
                bioentery.setVisibility(View.VISIBLE);
                getValue.setHint("Enter Blood Group");
                break;
            case R.id.setValue:
                String hint = getValue.getHint().toString();
                if (hint.equals("Enter Blood Group")) {
                    addMedicalBio(0);
                }
                if (hint.equals("Enter Height")) {
                    addMedicalBio(1);
                }
                if (hint.equals("Enter Weight")) {
                    addMedicalBio(2);
                }
                break;
            case R.id.close_bioTab:
                bioentery.setVisibility(View.GONE);
                break;
            case R.id.go_to_doc_profile:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FindDoctor()).commit();
                BottomNavigationController doc = new BottomNavigationController(R.id.docFrag, getActivity());
                break;

            case R.id.go_to_appoint_profile:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new AppointmentFrag()).commit();
                BottomNavigationController appoint = new BottomNavigationController(R.id.appointfrag, getActivity());
                break;

            case R.id.go_to_ehr_profile:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new EhrFiles()).commit();
                break;

            case R.id.go_to_payment_profile:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new FragPaymentHistory()).commit();
                break;
        }
    }

    void addMedicalBio(int bioType) {
        DatabaseReference bioReff = FirebaseDatabase.getInstance().getReference().child("medialBio").child(user.getUid());
        String bio = getValue.getText().toString();
        if (bio.equals("") || bio == null) {
            getValue.setError("Invalid");
            return;
        }
        getValue.getText().clear();
        switch (bioType) {
            case 0:
                bioReff.child("bloodGp").setValue(bio);
                break;
            case 1:
                bioReff.child("hieght").setValue(bio);
                break;
            case 2:
                bioReff.child("weight").setValue(bio);
                break;
        }
    }

}
