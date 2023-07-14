package com.google.mlkit.vision.demo.frontend;

import android.util.EventLogTags;

import com.google.mlkit.vision.demo.R;

public class User {

    String Asanas, TimeRequired, Description;
    int imageId;

    public User(int imageId, String Asanas, String TimeRequired, String Description) {
        this.imageId = imageId;
        this.Asanas = Asanas;
        this.TimeRequired = TimeRequired;
        this.Description = Description;
    }
}
