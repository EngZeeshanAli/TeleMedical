package com.example.telemedical.Formaters;


public class MessageFormatter {
    String sender, reciver, message,timeStemp;

    public MessageFormatter() {
    }

    public MessageFormatter(String sender, String reciver, String message, String timeStemp) {
        this.sender = sender;
        this.reciver = reciver;
        this.message = message;
        this.timeStemp = timeStemp;
    }

    public String getSender() {
        return sender;
    }

    public String getReciver() {
        return reciver;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeStemp() {
        return timeStemp;
    }
}
