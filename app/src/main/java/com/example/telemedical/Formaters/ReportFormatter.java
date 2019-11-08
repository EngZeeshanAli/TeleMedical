package com.example.telemedical.Formaters;

public class ReportFormatter {
    String user;
    String name;
    String sharedWith;

    String downloadUrl;

    public ReportFormatter() {
    }

    public ReportFormatter(String uid, String name, String sharedWith, String downloadUrl) {
        this.user = uid;
        this.name = name;
        this.sharedWith = sharedWith;
        this.downloadUrl = downloadUrl;
    }


    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getUid() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getSharedWith() {
        return sharedWith;
    }
}
