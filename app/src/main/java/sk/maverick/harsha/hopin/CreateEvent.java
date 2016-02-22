/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Models.Event;
import sk.maverick.harsha.hopin.Util.ConnectionManager;
import sk.maverick.harsha.hopin.Util.RegexValidator;

public class CreateEvent extends AppCompatActivity {

    @Bind(R.id.createevent_progressbar) ProgressBar progressBar;

    @Bind(R.id.createevent_ipl_eventname) TextInputLayout layout_eventName;
    @Bind(R.id.createevent_eventname) EditText eventName;

    @Bind(R.id.createevent_ipl_eventtype) TextInputLayout layout_eventType;
    @Bind(R.id.createevent_eventtype) EditText eventType;

    @Bind(R.id.createevent_ipl_seatsavailable) TextInputLayout layout_seatsavailable;
    @Bind(R.id.createevent_seatsavailable) EditText seatsavailable;

    @Bind(R.id.createevent_ipl_eventdate) TextInputLayout layout_eventDate;
    @Bind(R.id.createevent_eventdate) EditText eventDate;

    @Bind(R.id.createevent_ipl_eventtime) TextInputLayout layout_eventTime;
    @Bind(R.id.createevent_eventtime) EditText eventTime;

    @Bind(R.id.createevent_ipl_pickuptime) TextInputLayout layout_pickupTime;
    @Bind(R.id.createevent_pickuptime) EditText pickupTime;

    @Bind(R.id.createevent_ipl_eventlocation) TextInputLayout layout_eventLocation;
    @Bind(R.id.createevent_eventlocation) EditText eventLocation;

    @Bind(R.id.createevent_ipl_pickuplocation) TextInputLayout layout_pickupLocation;
    @Bind(R.id.createevent_pickuplocation) EditText pickupLocation;


    private static final String TAG = "CREATEEVENT";
    private Calendar calendar;
    private int REQUEST_EVENT_PLACE_PICKER = 1;
    private int REQUEST_PICKUP_PLACE_PICKER = 2;
    private int day, month, year, eventTimeHour, eventTimeMinute,pickUpTimeHour,pickUpTimeMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {

        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        eventName.setTypeface(roboto_light);
        eventType.setTypeface(roboto_light);
        seatsavailable.setTypeface(roboto_light);
        eventDate.setTypeface(roboto_light);
        eventTime.setTypeface(roboto_light);
        pickupTime.setTypeface(roboto_light);
        eventLocation.setTypeface(roboto_light);
        pickupLocation.setTypeface(roboto_light);

        layout_eventName.setTypeface(roboto_light);
        layout_eventType.setTypeface(roboto_light);
        layout_seatsavailable.setTypeface(roboto_light);
        layout_eventDate.setTypeface(roboto_light);
        layout_eventTime.setTypeface(roboto_light);
        layout_pickupTime.setTypeface(roboto_light);
        layout_eventLocation.setTypeface(roboto_light);
        layout_pickupLocation.setTypeface(roboto_light);


        /*Typeface roboto_thin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        eventName.setTypeface(roboto_thin);
        eventType.setTypeface(roboto_thin);
        seatsavailable.setTypeface(roboto_thin);
        eventDate.setTypeface(roboto_thin);
        eventTime.setTypeface(roboto_thin);
        pickupTime.setTypeface(roboto_thin);
        eventLocation.setTypeface(roboto_thin);
        pickupLocation.setTypeface(roboto_thin);

        layout_eventName.setTypeface(roboto_thin);
        layout_eventType.setTypeface(roboto_thin);
        layout_seatsavailable.setTypeface(roboto_thin);
        layout_eventDate.setTypeface(roboto_thin);
        layout_eventTime.setTypeface(roboto_thin);
        layout_pickupTime.setTypeface(roboto_thin);
        layout_eventLocation.setTypeface(roboto_thin);
        layout_pickupLocation.setTypeface(roboto_thin);*/
    }

    @OnClick(R.id.createevent_datedialog)
    public void onClickDateDialog(View view) {

        Log.v(TAG, "Yes Butterknife works");
        hideSoftKeyboard(view);

        calendar = Calendar.getInstance();
        new DatePickerDialog(CreateEvent.this, dateDialogListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.createevent_eventtimedialog)
    public void onClickEventTimeDialog(View view) {

        Log.v(TAG, "Yes Butterknife works");
        hideSoftKeyboard(view);

        calendar = Calendar.getInstance();
        new TimePickerDialog(CreateEvent.this, eventTimeDialogListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }


    @OnClick(R.id.createevent_pickuptimedialog)
    public void onClickPickUpTimeDialog(View view) {

        Log.v(TAG, "Yes Butterknife works");
        hideSoftKeyboard(view);

        calendar = Calendar.getInstance();
        new TimePickerDialog(CreateEvent.this, pickupTimeDialogListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();


    }

    @OnClick(R.id.createevent_eventlocationpicker)
    public void onClickEventLocation(View view) {

        Log.v(TAG, "Yes Butter knife works");
        hideSoftKeyboard(view);


        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(CreateEvent.this), REQUEST_EVENT_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @OnClick(R.id.createevent_pickuplocationpicker)
    public void onClickPickUpLocation(View view) {

        Log.v(TAG, "Yes Butterknife works");
        hideSoftKeyboard(view);


        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(CreateEvent.this), REQUEST_PICKUP_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }

    @OnClick(R.id.createevent_confirm)
    public void onClickCreate(View view) {

        Log.v(TAG, "Yes Butterknife works");
        Toast.makeText(CreateEvent.this,
                "Yes ButterKnife works", Toast.LENGTH_SHORT).show();
        hideSoftKeyboard(view);

        boolean result = validateInputs();

        if(result){
            // Call async task to send data to the server

            if(ConnectionManager.isConnected(CreateEvent.this)){

                RequestParams request = new RequestParams();
                request.setUri("http://localhost:3000/createevent");
                request.setParam("eventName", eventName.getText().toString());
                request.setParam("eventType", eventType.getText().toString());

                request.setParam("seatsAvailable", seatsavailable.getText().toString());

                request.setParam("dateDay", ""+day);
                request.setParam("dateMonth", ""+month);
                request.setParam("dateYear", ""+year);


                request.setParam("eventTimeHour", ""+ eventTimeHour);
                request.setParam("eventTimeMinute", ""+ eventTimeMinute);

                request.setParam("pickUpTimeHour", ""+ pickUpTimeHour);
                request.setParam("pickUpTimeMinute", ""+ pickUpTimeMinute);

                request.setParam("eventLocation", eventLocation.getText().toString());
                request.setParam("pickUpLocation", pickupLocation.getText().toString());

                Log.v(TAG, new JSONObject(request.getParams()).toString());
                Log.v(TAG, "CreateEvent sending a request to the server");

                new CreateEventAsync().execute(request);
            }else
            {
                Snackbar.make(findViewById(R.id.createevent_confirm), "No Internet", Snackbar.LENGTH_LONG).show();
            }



        }else{
            Snackbar.make(findViewById(R.id.createevent_coordinator),"Enter Valid Inputs!", Snackbar.LENGTH_LONG).show();
        }


    }

    DatePickerDialog.OnDateSetListener dateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {

            month = monthOfYear;
            day = dayOfMonth;
            year = Year;
            eventDate.setText(monthOfYear + "/"+ dayOfMonth +"/"+ Year);
        }
    };

    TimePickerDialog.OnTimeSetListener eventTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            eventTimeHour = hourOfDay;
            eventTimeMinute = minute;

            eventTime.setText(hourOfDay + ":" + minute);
        }
    };

    TimePickerDialog.OnTimeSetListener pickupTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            pickUpTimeHour = hourOfDay;
            pickUpTimeMinute = minute;

            pickupTime.setText(hourOfDay + ":" + minute);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EVENT_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                if (place.getAddress()!= null){
                    String address = place.getAddress().toString();
/*
                    address = address.replace(',',' ');
*/

                    eventLocation.setText(address);
                }
                else{
                    Toast.makeText(this, "Couldn't find the address, Please choose another location", Toast.LENGTH_LONG).show();
                }
            }
        }

        if (requestCode == REQUEST_PICKUP_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                if (place.getAddress()!= null){
                    String address = place.getAddress().toString();
/*
                    address = address.replace(',',' ');
*/

                    pickupLocation.setText(address);
                }
                else{
                    Toast.makeText(this, "Couldn't find the address, Please choose another location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void hideSoftKeyboard(View v){

        InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private boolean validateInputs() {
/*
    validate name
    validate type with name
    validate seats with number
    validate date
    validate time
*/
        if(!RegexValidator.validateName(eventName.getText().toString()) || TextUtils.isEmpty(eventName.getText().toString())){
            layout_eventName.setError("Invalid name");
            return false;
        }

        if(!RegexValidator.validateName(eventType.getText().toString()) || TextUtils.isEmpty(eventType.getText().toString())){
            layout_eventType.setError("Invalid input");
            return false;
        }
        if(!RegexValidator.validateNumber(seatsavailable.getText().toString()) || TextUtils.isEmpty(seatsavailable.getText().toString())){
            layout_seatsavailable.setError("Invalid input");
            return false;
        }

        // To-Do validate eventDate, eventTime and pickUpTime

        return true;
    }


    private class CreateEventAsync extends AsyncTask<RequestParams, Void, HttpResponse>{


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected HttpResponse doInBackground(RequestParams... params) {
            HttpResponse httpResponse = null;

            try {
                httpResponse = HttpManager.postData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return httpResponse;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {

            if(result == null){
                Snackbar.make(findViewById(R.id.createevent_coordinator), "Error! Please try later", Snackbar.LENGTH_LONG).show();
            }

        }

    }
}


/*
Sample Event JSON POST
{
"eventName":"event",
"eventType":"type",
"seatsAvailable":"4",
"pickUpTimeHour":"15",
"eventTimeHour":"15",
"pickUpTimeMinute":"30",
"eventTimeMinute":"15",
"dateYear":"2016",
"dateDay":"24",
"dateMonth":"1",
"eventLocation":"6000 J Street, Sacramento, CA 95819, United States",
"pickUpLocation":"Student Union, 6000 J Street, Sacramento, CA 95819, United States"
}
*/