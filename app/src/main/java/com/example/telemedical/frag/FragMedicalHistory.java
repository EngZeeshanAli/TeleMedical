package com.example.telemedical.frag;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.telemedical.Formaters.QuestionFormater;
import com.example.telemedical.R;
import com.example.telemedical.adapter.HomeDoc;
import com.example.telemedical.adapter.UpcomingsAppointmentsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragMedicalHistory extends Fragment implements View.OnClickListener {
    Button save;
    EditText problemDuration, cause, symtoms, weakness, numbness;
    FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_medical,container,false);
        intiGui(view);
        return view;
    }

    void intiGui(View v) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        problemDuration = v.findViewById(R.id.problemLongLast);
        cause = v.findViewById(R.id.cause);
        symtoms = v.findViewById(R.id.symtoms);
        weakness = v.findViewById(R.id.weakness);
        numbness = v.findViewById(R.id.numbness);
        save = v.findViewById(R.id.save_medicalQuestion);
        save.setOnClickListener(this);
        getData();
    }

    void getData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Medical Quest").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                QuestionFormater formater = dataSnapshot.getValue(QuestionFormater.class);
                if (formater != null) {
                    if (formater.getProblemDuration() != null) {
                        problemDuration.setText(formater.getProblemDuration());
                    }

                    if (formater.getCause() != null) {
                        cause.setText(formater.getCause());
                    }

                    if (formater.getSymtoms() != null) {
                        symtoms.setText(formater.getSymtoms());
                    }

                    if (formater.getWeakness() != null) {
                        weakness.setText(formater.getWeakness());
                    }

                    if (formater.getNumbness() != null) {
                        numbness.setText(formater.getNumbness());
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getSave() {
        String prob = problemDuration.getText().toString();
        String cau = cause.getText().toString();
        String sym = symtoms.getText().toString();
        String weak = weakness.getText().toString();
        String num = numbness.getText().toString();
        QuestionFormater formater = new QuestionFormater(prob, cau, sym, weak, num);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Medical Quest").child(user.getUid());
        reference.setValue(formater, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                //Find the currently focused view, so we can grab the correct window token from it.
                View view = getActivity().getCurrentFocus();
                //If no view currently has focus, create a new one, just so we can grab a window token from it
                if (view == null) {
                    view = new View(getActivity());
                }
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_medicalQuestion:
                getSave();
                break;
        }
    }


}
