package com.example.mycareer.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SETTINGS = "settings";

    public static SharedPreferences getSharedPref(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        return pref;
    }

    public static void setPrefVal(Context mContext, String key, String value) {
        if(key!=null){
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putString(key, value);
            edit.apply();
        }
    }

    public static void setIntPrefVal(Context mContext, String key, int value) {
        if(key!=null){
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putInt(key, value);
            edit.apply();
        }
    }

    public static void setLongPrefVal(Context mContext, String key, Long value) {
        if(key!=null){
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putLong(key, value);
            edit.apply();
        }
    }

    public static void setBooleanPrefVal(Context mContext, String key, boolean value) {
        if(key!=null){
            SharedPreferences.Editor edit = getSharedPref(mContext).edit();
            edit.putBoolean(key, value);
            edit.apply();
        }
    }


    public static String getPrefVal(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        String val = "";
        try {
            if (pref.contains(key))
                val = pref.getString(key, "");
            else
                val = "";
        }catch (Exception e){
            e.printStackTrace();
        }
        return val;
    }

    public static int getIntPrefVal(Context mContext, String key, int defaultVal) {
        SharedPreferences pref = getSharedPref(mContext);
        int val = defaultVal;
        try {
            if(pref.contains(key)) val = pref.getInt(key, defaultVal);
        }catch (Exception e){
            e.printStackTrace();
        }
        return val;
    }

    public static Long getLongPrefVal(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        Long val = null;
        try{
            if(pref.contains(key)) val = pref.getLong(key, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return val;
    }

    public static boolean getBooleanPrefVal(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        boolean val = false;
        try{
            if(pref.contains(key)) val = pref.getBoolean(key, false);

        }catch (Exception e){
            e.printStackTrace();
        }
        return val;
    }


    public static boolean containkey(Context mContext,String key)
    {
        SharedPreferences pref = getSharedPref(mContext);
        return pref.contains(key);
    }

    public static void saveTestScreens(Context mContext, String key,
                                       String value) {
        SharedPreferences.Editor edit = getSharedPref(mContext).edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getTestScreens(Context mContext, String key) {
        SharedPreferences pref = getSharedPref(mContext);
        String val = "";
        if (pref.contains(key))
            val = pref.getString(key, "");
        else
            val = "";
        return val;
    }
}