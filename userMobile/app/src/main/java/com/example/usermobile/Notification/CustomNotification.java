package com.example.usermobile.Notification;


public class CustomNotification {
    private static int CONTOR_ID = 0;
    private static int getNewId()
    {
        CONTOR_ID ++;
        return CONTOR_ID;
    }

    int id;
    String title;
    String text;
    long timeToRing;// in milisec

    public CustomNotification(String title, String text, long timeToRing) {
        this.id = getNewId();
        this.title = title;
        this.text = text;
        this.timeToRing = timeToRing;
    }
}
