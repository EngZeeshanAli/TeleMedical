package com.example.telemedical.Formaters;

public class UserFormater {
    public UserFormater() {
    }

    String uid,name,email,mobile,gender,password,insurance,fileNo;

    public UserFormater(String uid, String name, String email, String mobile, String gender, String password, String insurance, String fileNo) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.password = password;
        this.insurance = insurance;
        this.fileNo = fileNo;
        this.uid=uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getInsurance() {
        return insurance;
    }

    public String getFileNo() {
        return fileNo;
    }

    public String getUid() {
        return uid;
    }
}
