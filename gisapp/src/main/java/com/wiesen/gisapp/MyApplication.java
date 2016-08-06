package com.wiesen.gisapp;

import android.app.Application;

import com.wiesen.provider.local.OfflineTilePro;

/**
 * Created by wiesen on 16-8-5.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OfflineTilePro.getInstance().InitMapIndex();
    }


}
