package com.bcs.andy.strayanimalsshelter;

import android.app.Application;

import com.firebase.client.Firebase;



public class MyFirebaseApp extends Application {

    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);


    }

}
