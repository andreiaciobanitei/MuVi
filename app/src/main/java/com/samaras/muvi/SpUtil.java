package com.samaras.muvi;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Cosmin Robert on 28.05.2018.
 */

public class SpUtil {
    private SpUtil() {}
    private static final String PREF_NAME = "AuthCredentials";


    public static SharedPreferences getPrefs(Context context) {
        return  context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getPreferenceString(Context context, String key) {
        return getPrefs(context).getString(key,"");
    }

    public static void setPreferenceString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(key, value);
        editor.apply();
    }
}


