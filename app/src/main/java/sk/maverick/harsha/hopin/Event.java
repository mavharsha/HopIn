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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;

public class Event extends AppCompatActivity implements OnMapReadyCallback{

    String eventId = "";
    private final static String TAG  = "EVENT ACTIVITY";

    @Bind(R.id.event_eventname)
    TextView eventname;
    @Bind(R.id.event_eventtype) TextView eventtype;
    @Bind(R.id.event_eventdate) TextView eventdate;
    @Bind(R.id.event_eventtime) TextView eventtime;
    @Bind(R.id.event_seatsavailable) TextView seats;
    @Bind(R.id.event_pickuptime) TextView pickuptime;
    @Bind(R.id.event_location) TextView location;
    @Bind(R.id.event_Pickuplocation) TextView pickuplocation;

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

    public void init(){

        RequestParams requestParams = new RequestParams();

        requestParams.setUri("http://localhost:3000/event/"+eventId);
        new EventAsyncTask(Event.this).execute(requestParams);
    }

    private void parseResopnse(String body) {

        Log.v(TAG, "parseRespone" + body);

        JSONObject newJson;
        JSONArray details = null;
        sk.maverick.harsha.hopin.Models.Event myevent = null;

        try {
            newJson = new JSONObject(body);
            details = newJson.getJSONArray("details");
            if(details== null){
                Log.v(TAG, "Null hai sir");
            }
            else
            {
                Log.v(TAG, "Null nahi hai sir");
            }
            Log.v(TAG, "Array is "+ details);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<sk.maverick.harsha.hopin.Models.Event> jsonAdapter = moshi.adapter(sk.maverick.harsha.hopin.Models.Event.class);

        for (int i=0; i< details.length(); i++){

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
        eventtype.setText(myevent.getEventtype());
        eventdate.setText("Event on "+myevent.getDatemonth() +"/" + myevent.getDateday()+"/" +myevent.getDateyear());
        eventtime.setText("At time" + myevent.getEventtimehour()+"/" +myevent.getEventtimeminute());
        seats.setText("Seats : " +myevent.getSeatsavailable());
        pickuptime.setText("Pick at"+ myevent.getPickuptimehour()+"/" +myevent.getPickuptimeminute());

        location.setText("Event Location at " +myevent.getEventlocation());
        pickuplocation.setText("Pickup Location at "+ myevent.getPickuplocation());

        goToLocation(myevent.getEventlocationlat(), myevent.getEventlocationlng(), myevent.getEventname());

    }


    public void goToLocation(double lat, double lng, String locality) {

        LatLng newPointer = new LatLng(lat, lng);
        MarkerOptions options = new MarkerOptions().position(newPointer).title(locality).draggable(true);

        if(marker!=null){
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

            if(response == null){
                Snackbar.make(findViewById(R.id.event_coordinator), "Error! Please check your internet connection", Snackbar.LENGTH_LONG).show();
            }
            else if(response.getStatusCode() !=200){
                Snackbar.make(findViewById(R.id.event_coordinator), "Error! Please retry later", Snackbar.LENGTH_LONG).show();
            }
            else if(response.getStatusCode() ==200)
            {
                parseResopnse(response.getBody());
            }
        }
    }

}
