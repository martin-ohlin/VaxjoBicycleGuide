package com.martin.vaxjobicycleguide;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class BikeApplication extends Application {

    public void onCreate() {
        super.onCreate();

        /*
        Thread.currentThread().setContextClassLoader(new ClassLoader() {
            @Override
            public Enumeration<URL> getResources(String resName) throws IOException {
                Log.i("Debug", "Stack trace of who uses " +
                        "Thread.currentThread().getContextClassLoader()." +
                        "getResources(String resName):", new Exception());
                return super.getResources(resName);
            }
        });
        */

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "5CRPyrJBVJACJSfINFZq741rNsCXnZbxXiVjT8EB", "sydCG04vI4TpGW2qvL7yeNUwJHLFlzfd6cpCVp5g");
    }
}
