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
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import sk.maverick.harsha.hopin.Util.SharedPrefs;

public class Event extends AppCompatActivity implements OnMapReadyCallback, RadioGroup.OnCheckedChangeListener {

    String eventId = "";
    private final static String TAG = "EVENT ACTIVITY";
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String BASE_URI = "http://www.mavharsha.github.io/";

    sk.maverick.harsha.hopin.Models.Event myevent = null;
    private String pickup;

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
    @Bind(R.id.event_radiogroup)
    RadioGroup radioGroup;

    GoogleMap mMap;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        toolbar.setTitle("Event");

        Intent intent = getIntent();
        String action = intent.getAction();
        Log.v(TAG, "Action intent is " + action);

        if (intent != null && intent.getAction() != null && intent.getAction().equals("android.intent.action.VIEW")) {

            Uri uri = intent.getData();
            Log.v(TAG, "URI data" + uri.toString());
            String sharedText = uri.getQueryParameter("eventid");
            Log.v(TAG, "Eventid is " + sharedText);
            if (sharedText != null) {
                eventId = sharedText;
            }
        } else {
            eventId = intent.getStringExtra("eventid");
        }

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.event_map);
        mapFragment.getMapAsync(this);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            String eventid = "http://www.mavharsha.github.io/" + myevent.get_id();
            Uri uri = Uri.parse(BASE_URI)
                    .buildUpon()
                    .appendQueryParameter("eventid", myevent.get_id())
                    .build();

            String eventname = myevent.getEventname();
            String sender = SharedPrefs.getStringValue(Event.this, "username");
            share(uri.toString(), eventname, sender);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void share(String eventId, String eventName, String sender) {
        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        String message = sender + " requested you to checkout " + eventName + " at " + eventId;
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        sharingIntent.setType("text/plain");
        startActivity(Intent.createChooser(sharingIntent, "Sharing " + eventName));
    }


    @OnClick(R.id.event_requestride)
    public void RideRequest(View view) {

        if (myevent != null && !TextUtils.isEmpty(seatsrequested.getText().toString())) {

            int seatsreq = Integer.parseInt(seatsrequested.getText().toString());
            int seatsavailable = Integer.parseInt(myevent.getSeatsavailable());

            if (seatsavailable >= seatsreq) {

                RequestParams requestParams = new RequestParams();
                requestParams.setUri(App.getIp() + "request");
                requestParams.setParam("eventId", myevent.get_id());
                requestParams.setParam("eventName", myevent.getEventname());
                requestParams.setParam("createdUser", myevent.getUsername());
                requestParams.setParam("seatsRequested", seatsreq);
                requestParams.setParam("pickupLocation", pickup);
                requestParams.setParam("requestedUser", SharedPrefs.getStringValue(getApplicationContext(), "username"));
                requestParams.setParam("requestedUserAvatar", SharedPrefs.getStringValue(getApplicationContext(), "avatar"));

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

        radioGroup.setOnCheckedChangeListener(this);

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

        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

        eventname.setText(myevent.getEventname());
        eventtype.setText("Event Type: " + myevent.getEventtype());
        eventdate.setText("Event on " + myevent.getDatemonth() + "/" + myevent.getDateday() + "/" + myevent.getDateyear());
        eventtime.setText("At time " + myevent.getEventtimehour() + ":" + myevent.getEventtimeminute());
        seats.setText("Seats Available : " + myevent.getSeatsavailable());

        RadioGroup.LayoutParams radioGLayoutParm;

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < myevent.getPickup().size(); i++) {
            Pickup pickup = myevent.getPickup().get(i);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            radioButton.setText(pickup.getPickuplocation() + " at " + pickup.getPickuptime());
            radioButton.setTypeface(roboto_light);
            radioButton.setTextColor(Color.GRAY);
            radioGLayoutParm = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioGroup.addView(radioButton, radioGLayoutParm);

            stringBuilder.append("Pickup Location at " + pickup.getPickuplocation() + "\n");
            stringBuilder.append("Pickup Time at " + pickup.getPickuptime() + "\n \n");
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
            radioGroup.setVisibility(View.GONE);
        } else {
            pickuplocation.setVisibility(View.GONE);
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        RadioButton _PickRadio = (RadioButton) group.findViewById(checkedId);
        if (null != _PickRadio && checkedId > -1) {
            pickup = _PickRadio.getText().toString();
        }
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