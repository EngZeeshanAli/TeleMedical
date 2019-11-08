package com.example.telemedical.Formaters;

import android.os.Parcelable;

public class DoctorFormater {
    String name;
    String profileImg;
    String ranking;
    String phone;
    String language;
    String primaryAddress;
    String consulationType;
    String expert;
    String fee;
    String gender;
    String uid;

    public DoctorFormater() {
    }

    public DoctorFormater(String name, String profileImg, String ranking, String phone, String language, String primaryAddress, String consulationType, String expert, String fee, String gender, String uid) {
        this.name = name;
        this.profileImg = profileImg;
        this.ranking = ranking;
        this.phone = phone;
        this.language = language;
        this.primaryAddress = primaryAddress;
        this.consulationType = consulationType;
        this.expert = expert;
        this.fee = fee;
        this.gender = gender;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getRanking() {
        return ranking;
    }

    public String getPhone() {
        return phone;
    }

    public String getLanguage() {
        return language;
    }

    public String getPrimaryAddress() {
        return primaryAddress;
    }

    public String getConsulationType() {
        return consulationType;
    }

    public String getExpert() {
        return expert;
    }

    public String getFee() {
        return fee;
    }

    public String getGender() {
        return gender;
    }

    public String getUid() {
        return uid;
    }
}
