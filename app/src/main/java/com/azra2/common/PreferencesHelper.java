package com.azra2.common;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesHelper {

    public static void saveValue(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(YMConst.LOG_STATE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static String loadValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(YMConst.LOG_STATE, MODE_PRIVATE);
        String result = sharedPreferences.getString(key, "");
        return result;
    }

}
