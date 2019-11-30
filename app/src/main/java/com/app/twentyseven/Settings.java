package com.app.twentyseven;

import android.content.Context;

public class Settings {
    public static String ServerUrl="http://192.168.8.253/";
    public static int numberOfCols=1;
    public static String getAndroidId(Context context) {
        return(android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
    }


}


