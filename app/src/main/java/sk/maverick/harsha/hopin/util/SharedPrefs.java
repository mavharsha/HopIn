/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Harsha on 3/9/2016.
 */
public class SharedPrefs {

    public static final String MyPREFERENCES = "MyPrefs" ;

    public static String getStringValue(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String restoredvalue = prefs.getString(key, "");
        return restoredvalue;
    }
}
