/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Models.Pickup;

public class Event extends AppCompatActivity implements OnMapReadyCallback {

    String eventId = "";
    private final static String TAG = "EVENT ACTIVITY";
    public static final String MyPREFERENCES = "MyPrefs";

    sk.maverick.harsha.hopin.Models.Event myevent = null;

    @Bind(R.id.event_eventname)
    TextView eventname;
    @Bind(R.id.event_eventtype)
    TextView eventtype;
    @Bind(R.id.event_eventdate)
    TextView eventdate;
    @Bind(R.id.event_eventtime)
    TextView eventtime;
    @Bind(R.id.event_seatsavailable)
    TextView seats;
    @Bind(R.id.event_location)
    TextView location;
    @Bind(R.id.event_Pickuplocation)
    TextView pickuplocation;
    @Bind(R.id.event_seatsrequested)
    EditText seatsrequested;
    @Bind(R.id.event_requestride)
    Button requestride;

    GoogleMap mMap;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        eventId = intent.getStringExtra("eventid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.event_map);
        mapFragment.getMapAsync(this);

        init();
    }


    @OnClick(R.id.event_requestride)
    public void RideRequest(View view) {

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String restoredusername = prefs.getString("username", null);


        if (myevent != null && !TextUtils.isEmpty(seatsrequested.getText().toString())) {

            int seatsreq = Integer.parseInt(seatsrequested.getText().toString());
            int seatsavailable = Integer.parseInt(myevent.getSeatsavailable());

            if (seatsavailable >= seatsreq) {

                RequestParams requestParams = new RequestParams();
                requestParams.setUri(App.getIp() + "request");
                requestParams.setParam("eventId", eventId);
                requestParams.setParam("eventName", myevent.getEventname());
                requestParams.setParam("createdUser", myevent.getUsername());
                requestParams.setParam("seatsRequested", seatsreq);
                requestParams.setParam("requestedUser", restoredusername);

                Log.v(TAG, "The request params before async task are " + requestParams.getParams());

                new RequestRideAsync(this).execute(requestParams);

            } else {
                Snackbar.make(findViewById(R.id.event_coordinator),
                        "Requested # of seats more than available", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(findViewById(R.id.event_coordinator),
                    "Error!", Snackbar.LENGTH_LONG).show();
        }


    }

    public void init() {

        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        eventname.setTypeface(roboto_light);
        eventtype.setTypeface(roboto_light);
        eventdate.setTypeface(roboto_light);
        seats.setTypeface(roboto_light);
        location.setTypeface(roboto_light);
        eventtime.setTypeface(roboto_light);
        pickuplocation.setTypeface(roboto_light);

        /*Typeface roboto_regular = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        eventname.setTypeface(roboto_regular);
*/
        RequestParams requestParams = new RequestParams();

        requestParams.setUri(App.getIp() + "event/" + eventId);
        new EventAsyncTask(Event.this).execute(requestParams);
    }

    private void parseResopnse(String body) {

        Log.v(TAG, "parseRespone" + body);

        JSONObject newJson;
        JSONArray details = null;

        try {
            newJson = new JSONObject(body);
            details = newJson.getJSONArray("details");
            if (details == null) {
                Log.v(TAG, "Null hai sir");
            } else {
                Log.v(TAG, "Null nahi hai sir");
            }
            Log.v(TAG, "Array is " + details);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<sk.maverick.harsha.hopin.Models.Event> jsonAdapter = moshi.adapter(sk.maverick.harsha.hopin.Models.Event.class);

        for (int i = 0; i < details.length(); i++) {

            try {
                myevent = jsonAdapter.fromJson(details.getJSONObject(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.v(TAG, "Event id is " + myevent.get_id());

        updateUI(myevent);
    }

    private void updateUI(sk.maverick.harsha.hopin.Models.Event myevent) {

        eventname.setText(myevent.getEventname());
        eventtype.setText("Event Type: " + myevent.getEventtype());
        eventdate.setText("Event on " + myevent.getDatemonth() + "/" + myevent.getDateday() + "/" + myevent.getDateyear());
        eventtime.setText("At time " + myevent.getEventtimehour() + ":" + myevent.getEventtimeminute());
        seats.setText("Seats Available : " + myevent.getSeatsavailable());

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < myevent.getPickup().size(); i++) {
            Pickup pickup = myevent.getPickup().get(i);
            stringBuilder.append("Pickup Location at " + pickup.getPickuplocation() + "\n");
            stringBuilder.append("Pickup Time at " + pickup.getPickuptime()+"\n \n");
        }

        pickuplocation.setText(stringBuilder);
        location.setText("Event Location at " + myevent.getEventlocation());

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String restoredusername = prefs.getString("username", null);

        Log.v(TAG, "Pref name is " + restoredusername);
        Log.v(TAG, "Create event username is " + myevent.getUsername());
        if (restoredusername.equals(myevent.getUsername())) {
            seatsrequested.setVisibility(View.GONE);
            requestride.setVisibility(View.GONE);
        }
        goToLocation(myevent.getEventlocationlat(), myevent.getEventlocationlng(), myevent.getEventname());
    }


    public void goToLocation(double lat, double lng, String locality) {

        LatLng newPointer = new LatLng(lat, lng);
        MarkerOptions options = new MarkerOptions().position(newPointer).title(locality).draggable(true);

        if (marker != null) {
            marker.remove();
        }
        marker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPointer, 8));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private class EventAsyncTask extends AsyncTask<RequestParams, Void, HttpResponse> {

        ProgressDialog progressDialog;

        public EventAsyncTask(Activity activity) {

            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Retrieving event details");
            progressDialog.show();
        }

        @Override
        protected HttpResponse doInBackground(RequestParams... params) {

            HttpResponse response = null;

            try {
                response = HttpManager.getData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(HttpResponse response) {

            if (response == null) {
                Snackbar.make(findViewById(R.id.event_coordinator), "Error! Please check your internet connection", Snackbar.LENGTH_LONG).show();
            } else if (response.getStatusCode() != 200) {
                Snackbar.make(findViewById(R.id.event_coordinator), "Error! Please retry later", Snackbar.LENGTH_LONG).show();
            } else if (response.getStatusCode() == 200) {
                parseResopnse(response.getBody());
                progressDialog.dismiss();
            }
        }
    }


    private class RequestRideAsync extends AsyncTask<RequestParams, Void, HttpResponse> {


        private ProgressDialog progressDialog;

        public RequestRideAsync(Activity activity) {
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Requesting ride");
            progressDialog.show();
        }

        @Override
        protected HttpResponse doInBackground(RequestParams... params) {

            HttpResponse response = null;

            try {
                response = HttpManager.postData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            super.onPostExecute(response);

            if (response == null) {
                Snackbar.make(findViewById(R.id.event_coordinator), "Error! Please check your internet connection", Snackbar.LENGTH_LONG).show();
            } else if (response.getStatusCode() != 200) {
                Snackbar.make(findViewById(R.id.event_coordinator), "Error! Please retry later", Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (response.getStatusCode() == 200) {
                progressDialog.dismiss();
                new AlertDialog.Builder(Event.this)
                        .setMessage("Requested your ride")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        }).show();
            }
        }
    }
}