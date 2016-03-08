/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootUpReceiver extends BroadcastReceiver {

    final static String TAG = "BOOTBROADCAST";
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Broadcast works", Toast.LENGTH_LONG).show();
        Log.v(TAG, "bootloaded");

        Intent intent1 = new Intent(context, Security.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        //PendingIntent pintent = PendingIntent.getService(context, 0, intent1, 0);
    }
}
