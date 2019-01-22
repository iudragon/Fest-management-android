package com.naveen.itfest;
import com.firebase.client.Firebase;
import android.app.Application;

public class ITFest extends Application {
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
