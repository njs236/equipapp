package com.nathan.equipapp;

import android.app.Activity;
import android.app.Application;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify
                .with(new FontAwesomeModule());
    }

    public static Activity currentActivity;
}
