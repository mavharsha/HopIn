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
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        Typeface roboto_thin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        eventName.setTypeface(roboto_thin);
        eventType.setTypeface(roboto_thin);
        seatsavailable.setTypeface(roboto_thin);
        eventDate.setTypeface(roboto_thin);
        eventTime.setTypeface(roboto_thin);
        pickupTime.setTypeface(roboto_thin);
        eventLocation.setTypeface(roboto_thin);
        pickupLocation.setTypeface(roboto_thin);
    }

    @OnClick(R.id.createevent_datedialog)
    public void onClickDateDialog(View view) {

        Log.v(TAG, "Yes Butterknife works");
        hideSoftKeyboard(view);

        calendar = Calendar.getInstance();
        new DatePickerDialog(CreateEvent.this, dateDialogListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();;
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

        validateInputs();
    }

    DatePickerDialog.OnDateSetListener dateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            eventDate.setText(monthOfYear + "/"+ dayOfMonth +"/"+ year);
        }
    };

    TimePickerDialog.OnTimeSetListener eventTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            eventTime.setText(hourOfDay + ":" + minute);
        }
    };

    TimePickerDialog.OnTimeSetListener pickupTimeDialogListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            pickupTime.setText(hourOfDay + ":" + minute);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EVENT_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                if (place.getAddress()!= null){

                    eventLocation.setText(place.getAddress());
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

                    pickupLocation.setText(place.getAddress());
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

    private void validateInputs() {


    }


}
