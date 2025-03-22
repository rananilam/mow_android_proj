package com.application;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;


import com.mow.cash.android.R;


public class BaseApplication extends MultiDexApplication  {
    public static Context appContext;

    public static Typeface tfRegular;
    public static Typeface tfBold;

    public static Typeface tfBRegular;
    public static Typeface tfBBold;
    public static Typeface tfEBBold;

    @Override
    public void onCreate() {
        super.onCreate();
        //StorageUtility.getInstance();
        appContext = getApplicationContext();
        //SettingController.getInstance().changeLanguage();

        tfRegular = ResourcesCompat.getFont(appContext, R.font.roboto_regular);
        tfBold = ResourcesCompat.getFont(appContext, R.font.roboto_bold);

        tfBRegular = ResourcesCompat.getFont(appContext, R.font.barlow_medium);
        tfEBBold = ResourcesCompat.getFont(appContext, R.font.barlow_extrabold);
        tfBBold = ResourcesCompat.getFont(appContext, R.font.barlow_bold);
        Log.e("hello","main page called");

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }



}