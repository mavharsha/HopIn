/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class ConnectionManager {

    private boolean isConnected = false;

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnectedOrConnecting();
    }
}
