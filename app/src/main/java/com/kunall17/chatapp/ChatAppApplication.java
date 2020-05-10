package com.kunall17.chatapp;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class ChatAppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
