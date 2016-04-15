package sk.maverick.harsha.hopin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Util.ConnectionManager;
import sk.maverick.harsha.hopin.Util.SharedPrefs;

/**
 * Created by Harsha on 4/12/2016.
 */
public class NotifyRequesterService extends Service{
    private final static String TAG = "NOTIFYREQUESTERSERVICE";
    public static final String MyPREFERENCES = "MyPrefs" ;
    static int var = 1;
    int Unique = 78798;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v(TAG, "Polling " + var);
        var++;
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean isLogged = prefs.getBoolean("isloggedin", false);

        if (ConnectionManager.isConnected(this) && isLogged) {
            RequestParams requestParams = new RequestParams();
            requestParams.setUri(App.getIp() + "notifyrequester/"+ SharedPrefs.getStringValue(getApplicationContext(), "username"));
            new NotifyUserAsync().execute(requestParams);
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Destroyed myservice");

    }

    private void NotifyUser() {
        NotificationManager notificationManager;

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Unique);

        Intent intent = new Intent(getApplicationContext(), Home.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        Notification notificationBuiler = new Notification.Builder(getApplicationContext())
                .setContentTitle("HOP IN Notification")
                .setContentText("Your ride request has been responded")
                .setSmallIcon(R.drawable.idea)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        notificationManager.notify(Unique, notificationBuiler);
    }

    private class NotifyUserAsync extends AsyncTask<RequestParams, Void, HttpResponse> {

        @Override
        protected void onPreExecute() {
            Log.v(TAG, "Pre Poll " + var);
        }

        @Override
        protected HttpResponse doInBackground(RequestParams... params) {

            HttpResponse httpResponse = null;
            try {
                httpResponse = HttpManager.getData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResponse;
        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            Log.v(TAG, "Post Poll " + var);

            if (response == null) {
            } else if (response.getStatusCode() != 200) {
            } else if (response.getStatusCode() == 200) {

                // get the fragment_requests and create a notification
                String result = response.getBody();

                NotifyUser();
            }
        }
    }
}
