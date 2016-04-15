package sk.maverick.harsha.hopin;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Models.Request;
import sk.maverick.harsha.hopin.Util.ConnectionManager;
import sk.maverick.harsha.hopin.Util.SharedPrefs;


public class CheckForEventService extends Service {

    private final static String TAG = "CHECKFOREVENT";
    final int REQUESTCODE_FINE_LOCATION = 1;
    static int var = 1;
    int Unique = 87981;
    public static final String MyPREFERENCES = "MyPrefs";
    sk.maverick.harsha.hopin.Models.Event event = null;
    static double latitude, longitude;
    LocationManager locationManager;
    protected final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        latitude = sk.maverick.harsha.hopin.Models.Location.getLatitude();
        longitude = sk.maverick.harsha.hopin.Models.Location.getLatitude();

        enableLocationUpdates();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.v(TAG, "Polling " + var);
        var++;
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Boolean isLogged = prefs.getBoolean("isloggedin", false);

        if (ConnectionManager.isConnected(this) && isLogged) {
            RequestParams requestParams = new RequestParams();
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            requestParams.setUri(App.getIp() + "event/today/"+
                            SharedPrefs.getStringValue(getApplicationContext(), "username")+"/"+day+"/"+hour);

            new NotifyUserAsync().execute(requestParams);
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Destroyed myservice");

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
                event = parseRespone(response.getBody());
                Log.v(TAG, "the latitude and longitude is" + latitude +" "+ longitude);
                if(latitude!= 0 && longitude!=0){

                String origin = ""+ latitude+","+longitude;
                String destination = event.getEventlocation();
                int arrivaltime  = event.getEventtimehour() * 60 * 60;

                String uri = "https://maps.googleapis.com/maps/api/directions/json?origin="
                                    +origin+"&destination="+destination+"&arrival_time="+arrivaltime+"&mode=driving";
                RequestParams requestParams = new RequestParams();
                requestParams.setUri(uri);

                new AsyncEventTask().execute(requestParams);
                }
            }
        }
    }

    private sk.maverick.harsha.hopin.Models.Event parseRespone(String result) {

        Log.v(TAG, result);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<sk.maverick.harsha.hopin.Models.Event> jsonAdapter = moshi.adapter(sk.maverick.harsha.hopin.Models.Event.class);
        sk.maverick.harsha.hopin.Models.Event event = null;
        try {
            event = jsonAdapter.fromJson(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }

    private class AsyncEventTask extends AsyncTask<RequestParams, Void, HttpResponse>{

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

                Log.v(TAG, "The response is"+ response.getBody());

                parseGoogleResponse(response.getBody());
            }
        }
    }

    private void parseGoogleResponse(String body) {

        JSONObject route,output, leg, duration=null;
        JSONArray  routes, legs;
        try {
            output = new JSONObject(body);
            routes = output.getJSONArray("routes");
            route = routes.getJSONObject(0);
            legs = route.getJSONArray("legs");
            leg = legs.getJSONObject(0);

            duration = leg.getJSONObject("duration");

            Log.v(TAG, "The GOOGLE response is"+duration.getString("text"));
            NotifyUser(duration.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void NotifyUser(String string) {
        NotificationManager notificationManager;

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Unique);

        Intent intent = new Intent(getApplicationContext(), Event.class);

        intent.putExtra("eventid", event.get_id());
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notificationBuilder = new Notification.Builder(getApplicationContext())
                .setContentTitle("Hop In Notification")
                .setContentText("Usual time to commute to the event is "+ string)
                .setSmallIcon(R.drawable.idea)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        notificationManager.notify(Unique, notificationBuilder);
    }

    private void enableLocationUpdates() {

        final int minTime = 10;
        final int minDistance = 1;

        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(criteria,true);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        Toast.makeText(CheckForEventService.this, "Enabling location", Toast.LENGTH_SHORT).show();
    }

    private void updateLocation(Location location) {
        latitude = location.getLatitude();
        -longitude = location.getLongitude();
    }


}
