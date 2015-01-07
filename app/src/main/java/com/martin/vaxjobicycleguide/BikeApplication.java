package com.martin.vaxjobicycleguide;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class BikeApplication extends Application {

    private Tracker mTracker;
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "5CRPyrJBVJACJSfINFZq741rNsCXnZbxXiVjT8EB", "sydCG04vI4TpGW2qvL7yeNUwJHLFlzfd6cpCVp5g");

        getTracker();
    }

    synchronized Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            mTracker = analytics.newTracker(R.xml.tracker);
            mTracker.enableAdvertisingIdCollection(true);
        }

        return mTracker;
    }
}
