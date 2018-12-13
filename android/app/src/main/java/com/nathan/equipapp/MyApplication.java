package com.nathan.equipapp;

import android.app.Activity;
import android.app.Application;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }

    public static Activity currentActivity;
}
