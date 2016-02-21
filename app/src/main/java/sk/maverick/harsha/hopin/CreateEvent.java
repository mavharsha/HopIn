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
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

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

    @Bind(R.id.createevent_ipl_eventdate) TextInputLayout layout_eventDate;
    @Bind(R.id.createevent_eventdate) EditText eventDate;

    @Bind(R.id.createevent_ipl_eventtime) TextInputLayout layout_eventTime;
    @Bind(R.id.createevent_eventtime) EditText eventTime;

    @Bind(R.id.createevent_ipl_pickuptime) TextInputLayout layout_pickupTime;
    @Bind(R.id.createevent_pickuptime) EditText pickupTime;


    private static final String TAG = "CREATEEVENT";
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @OnClick(R.id.createevent_datedialog)
    public void onClickDateDialog(View view) {

        Log.v(TAG, "Yes Butterknife works");
        Toast.makeText(CreateEvent.this,
                "Yes ButterKnife works", Toast.LENGTH_SHORT).show();
        calendar = Calendar.getInstance();

        new DatePickerDialog(CreateEvent.this, dateDialogListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();;
    }

    @OnClick(R.id.createevent_eventtimedialog)
    public void onClickEventTimeDialog(View view) {

        Log.v(TAG, "Yes Butterknife works");
        Toast.makeText(CreateEvent.this,
                "Yes ButterKnife works", Toast.LENGTH_SHORT).show();
        calendar = Calendar.getInstance();
        new TimePickerDialog(CreateEvent.this, eventTimeDialogListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }


    @OnClick(R.id.createevent_pickuptimedialog)
    public void onClickPickUpTimeDialog(View view) {

        Log.v(TAG, "Yes Butterknife works");
        Toast.makeText(CreateEvent.this,
                "Yes ButterKnife works", Toast.LENGTH_SHORT).show();

        calendar = Calendar.getInstance();
        new TimePickerDialog(CreateEvent.this, pickupTimeDialogListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();


    }

    @OnClick(R.id.createevent_eventlocation)
    public void onClickEventLocation(View view) {

        Log.v(TAG, "Yes Butterknife works");
        Toast.makeText(CreateEvent.this,
                "Yes ButterKnife works", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.createevent_pickuplocation)
    public void onClickPickUpLocation(View view) {

        Log.v(TAG, "Yes Butterknife works");
        Toast.makeText(CreateEvent.this,
                "Yes ButterKnife works", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.createevent_confirm)
    public void onClickCreate(View view) {

        Log.v(TAG, "Yes Butterknife works");
        Toast.makeText(CreateEvent.this,
                "Yes ButterKnife works", Toast.LENGTH_SHORT).show();
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
}
