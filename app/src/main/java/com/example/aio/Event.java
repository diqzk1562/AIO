package com.example.aio;

import android.graphics.Bitmap;

public class Event {
    private Bitmap event_image;

    public Event(Bitmap event_image){
        this.event_image = event_image;
    }
    public Bitmap getEvent_image(){
        return this.event_image;
    }
}
