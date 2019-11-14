package com.example.telemedical.Formaters;

public class PatientBio {
    String weight, hieght, bloodGp;

    public PatientBio() {
    }

    public PatientBio(String weight, String hieght, String bloodGp) {
        this.weight = weight;
        this.hieght = hieght;
        this.bloodGp = bloodGp;
    }

    public String getWeight() {
        return weight;
    }

    public String getHieght() {
        return hieght;
    }

    public String getBloodGp() {
        return bloodGp;
    }
}
