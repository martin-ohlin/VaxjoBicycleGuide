package com.martin.vaxjobicycleguide;

import android.app.Application;

import com.parse.Parse;

public class BikeApplication extends Application {

    public void onCreate() {
        super.onCreate();
        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "5CRPyrJBVJACJSfINFZq741rNsCXnZbxXiVjT8EB", "sydCG04vI4TpGW2qvL7yeNUwJHLFlzfd6cpCVp5g");
    }
}
