package com.example.telemedical.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.telemedical.Booking;
import com.example.telemedical.PickTime;
import com.example.telemedical.R;

public class BookingDialog extends AppCompatDialogFragment implements View.OnClickListener {
    Button cancell;
    LinearLayout physical, video, voice;
    String docId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.booking_dialog_view, null);
        getDoctor();
        iniGui(view);
        builder.setView(view);
        return builder.create();
    }

    void iniGui(View v) {
        cancell = v.findViewById(R.id.close_dialog_booking);
        cancell.setOnClickListener(this);
        physical = v.findViewById(R.id.physical);
        physical.setOnClickListener(this);
        video = v.findViewById(R.id.video_booking);
        video.setOnClickListener(this);
        voice = v.findViewById(R.id.voice_booking);
        voice.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_dialog_booking:
                dismiss();
                break;
            case R.id.physical:
                Intent physical = new Intent(getActivity(), Booking.class);
                physical.putExtra("action", "Physical Appointment");
                physical.putExtra("docId", docId);
                startActivity(physical);
                getActivity().finish();
                break;
            case R.id.voice_booking:
                Intent voice_booking = new Intent(getActivity(), Booking.class);
                voice_booking.putExtra("action", "Voice Booking");
                voice_booking.putExtra("docId", docId);
                startActivity(voice_booking);
                getActivity().finish();
                break;
            case R.id.video_booking:
                Intent video_booking = new Intent(getActivity(), Booking.class);
                video_booking.putExtra("action", "Video Booking");
                video_booking.putExtra("docId", docId);
                startActivity(video_booking);
                getActivity().finish();
                break;
        }
    }

    void getDoctor() {
        Intent intent = getActivity().getIntent();
        docId = intent.getStringExtra("docId");
    }
}
