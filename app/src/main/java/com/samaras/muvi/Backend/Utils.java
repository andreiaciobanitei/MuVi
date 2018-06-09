package com.samaras.muvi.Backend;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

public class Utils {
    public static boolean isEmpty(String string){
        return string.equals("");
    }

    public static void showDialog(ProgressBar mProgressBar){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public static void hideDialog(ProgressBar mProgressBar){
        if(mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
    public static boolean doStringsMatch(String s1, String s2){
        return s1.equals(s2);
    }
}
