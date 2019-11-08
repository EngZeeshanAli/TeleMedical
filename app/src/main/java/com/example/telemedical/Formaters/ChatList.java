package com.example.telemedical.Formaters;

public class ChatList {
    String id;
    String msg;
    String timeStemp;

    public ChatList(String id, String msg, String timeStemp) {
        this.id = id;
        this.msg = msg;
        this.timeStemp = timeStemp;
    }

    public String getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public String getTimeStemp() {
        return timeStemp;
    }
}
