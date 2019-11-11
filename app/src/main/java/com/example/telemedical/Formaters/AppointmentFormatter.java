package com.example.telemedical.Formaters;

import android.os.Parcelable;

import java.util.ArrayList;

public class AppointmentFormatter {
    String docUid, patientUid, date, time, fee, type, status;

    public AppointmentFormatter() {
    }

    public AppointmentFormatter(String docUid, String patientUid, String date, String time, String fee, String type, String status) {
        this.docUid = docUid;
        this.patientUid = patientUid;
        this.date = date;
        this.time = time;
        this.fee = fee;
        this.type = type;
        this.status = status;
    }

    public String getDocUid() {
        return docUid;
    }

    public String getPatientUid() {
        return patientUid;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getFee() {
        return fee;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }
}
