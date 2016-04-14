package sk.maverick.harsha.hopin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.maverick.harsha.hopin.Models.Event;
import sk.maverick.harsha.hopin.Models.Pickup;

public class EditEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Event myevent = null;
    final static String TAG = "EditEvent";
    private Calendar calendar;
    private static final String[] preferences = new String[]{
            "Both",
            "Male",
            "Female"
    };
    private int REQUEST_EVENT_PLACE_PICKER = 98;
    private int REQUEST_PICKUP_PLACE_PICKER = 99;
    private List<HashMap<String, Object>> pickupid = new ArrayList<>();
    private int day, month, year, eventTimeHour, eventTimeMinute, pickUpTimeHour, pickUpTimeMinute;
    LatLng locationltlng, pickupltlng;


    @Bind(R.id.editevent_ipl_eventname)
    TextInputLayout layout_eventName;
    @Bind(R.id.editevent_eventname)
    EditText eventName;

    @Bind(R.id.editevent_ipl_eventtype)
    TextInputLayout layout_eventType;
    @Bind(R.id.editevent_eventtype)
    EditText eventType;

    @Bind(R.id.editevent_ipl_seatsavailable)
    TextInputLayout layout_seatsavailable;
    @Bind(R.id.editevent_seatsavailable)
    EditText seatsavailable;

    @Bind(R.id.editevent_tv_passpreference)
    TextView tv_passpreference;
    @Bind(R.id.editevent_passpreference)
    Spinner passpreference;

    @Bind(R.id.editevent_ipl_eventdate)
    TextInputLayout layout_eventDate;
    @Bind(R.id.editevent_eventdate)
    EditText eventDate;

    @Bind(R.id.editevent_ipl_eventtime)
    TextInputLayout layout_eventTime;
    @Bind(R.id.editevent_eventtime)
    EditText eventTime;

    @Bind(R.id.editevent_ipl_pickuptime)
    TextInputLayout layout_pickupTime;
    @Bind(R.id.editevent_pickuptime)
    EditText pickupTime;

    @Bind(R.id.editevent_ipl_eventlocation)
    TextInputLayout layout_eventLocation;
    @Bind(R.id.editevent_eventlocation)
    EditText eventLocation;

    @Bind(R.id.editevent_ipl_pickuplocation)
    TextInputLayout layout_pickupLocation;
    @Bind(R.id.editevent_pickuplocation)
    EditText pickupLocation;

    @Bind(R.id.editevent_radioGroup)
    RadioGroup privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();

        Intent intent = getIntent();
        String jsonSent = intent.getStringExtra("EventObject");

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Event> jsonAdapter = moshi.adapter(Event.class);

        try {
             myevent = jsonAdapter.fromJson(jsonSent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(myevent != null) {

            eventName.setText(myevent.getEventname());
            eventType.setText(myevent.getEventtype());

            if(myevent.getPrivacytype() == 0){
                findViewById(R.id.editevent_radioButton1).setSelected(true);
            }else{
                findViewById(R.id.editevent_radioButton2).setSelected(true);
            }

            seatsavailable.setText(myevent.getSeatsavailable());
            eventDate.setText(myevent.getDatemonth()+"/"+myevent.getDateday()+"/"+myevent.getDateyear());
            eventTime.setText(myevent.getEventtimehour()+":"+myevent.getEventtimeminute());
            eventLocation.setText(myevent.getEventlocation());

            Pickup pickuppoint = myevent.getPickup().get(0);
            pickupTime.setText(pickuppoint.getPickuptime());
            pickupLocation.setText(pickuppoint.getPickuplocation());

            for(int i =1; i<myevent.getPickup().size(); i++){
                 pickuppoint = myevent.getPickup().get(i);
                addPickUp();

                HashMap<String, Object> ids = pickupid.get(i-1);

                EditText timeet = (EditText) ids.get("Time");
                EditText locationet = (EditText) ids.get("Location");

                timeet.setText(pickuppoint.getPickuptime());
                locationet.setText(pickuppoint.getPickuplocation());
             }

        }else{
            Snackbar.make(findViewById(R.id.editevent_coordinator), "Error! Please try later",Snackbar.LENGTH_SHORT).show();
        }


    }

    private void init() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                preferences);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        passpreference.setOnItemSelectedListener(this);
        passpreference.setAdapter(adapter);

    }

    @OnClick(R.id.editevent_addview)
    public void addPickUpLocation(View v) {
        addPickUp();
    }


    @OnClick(R.id.editevent_datedialog)
    public void onClickDateDialog(View view) {

        hideSoftKeyboard(view);
        calendar = Calendar.getInstance();
        new DatePickerDialog(EditEvent.this, dateDialogListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.editevent_eventtimedialog)
    public void onClickEventTimeDialog(View view) {

        hideSoftKeyboard(view);
        calendar = Calendar.getInstance();
        new TimePickerDialog(EditEvent.this, eventTimeDialogListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }


    @OnClick(R.id.editevent_pickuptimedialog)
    public void onClickPickUpTimeDialog(View view) {

        hideSoftKeyboard(view);
        calendar = Calendar.getInstance();
        new TimePickerDialog(EditEvent.this, pickupTimeDialogListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();


    }

    @OnClick(R.id.editevent_eventlocationpicker)
    public void onClickEventLocation(View view) {

        hideSoftKeyboard(view);

        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(EditEvent.this), REQUEST_EVENT_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.editevent_pickuplocationpicker)
    public void onClickPickUpLocation(View view) {

        hideSoftKeyboard(view);

        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(EditEvent.this), REQUEST_PICKUP_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    DatePickerDialog.OnDateSetListener dateDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {

            month = monthOfYear;
            day = dayOfMonth;
            year = Year;
            eventDate.setText(monthOfYear + "/" + dayOfMonth + "/" + Year);
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


    public void addPickUp() {

        Snackbar.make(findViewById(R.id.editevent_coordinator), "Adding a view", Snackbar.LENGTH_LONG).show();
        LayoutInflater layoutInflater = getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.add_pickup, null);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.editevent_placeholder);
        viewGroup.addView(view, viewGroup.getChildCount() - 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        Log.v(TAG, "The number of children are " + viewGroup.getChildCount());

        afterAddingView(viewGroup, view);
    }

    private void afterAddingView(final ViewGroup viewGroup, final View view) {

        final EditText et = (EditText) view.findViewWithTag("time");
        final EditText et1 = (EditText) view.findViewWithTag("location");

        Button btn = (Button) view.findViewWithTag("timedialog");
        final Button btn1 = (Button) view.findViewWithTag("locationbutton");

        HashMap<String, Object> map = new HashMap<>(1);

        int id = et.generateViewId();
        et.setId(id);
        map.put("Time", et);

        id = et1.generateViewId();
        et1.setId(id);
        map.put("Location", et1);

        id = btn.generateViewId();
        btn.setId(id);

        id = btn1.generateViewId();
        btn1.setId(id);

        pickupid.add(map);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                new TimePickerDialog(EditEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        et.setText(hourOfDay + ":" + minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Log.v(TAG, "The et id is " + et1.getId());
                    startActivityForResult(builder.build(EditEvent.this), et1.getId());

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.v(TAG, "The request code in onActivityResut is " + requestCode);
        if (requestCode == REQUEST_EVENT_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                if (place.getAddress() != null) {
                    String address = place.getAddress().toString();
                    locationltlng = place.getLatLng();

                    if (locationltlng == null)
                        Toast.makeText(this, "latlong is null", Toast.LENGTH_LONG).show();
                    eventLocation.setText(address);
                } else {
                    Toast.makeText(this, "Couldn't find the address, Please choose another location", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == REQUEST_PICKUP_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                if (place.getAddress() != null) {
                    String address = place.getAddress().toString();
                    pickupltlng = place.getLatLng();
                    pickupLocation.setText(address);
                } else {
                    Toast.makeText(this, "Couldn't find the address, Please choose another location", Toast.LENGTH_LONG).show();
                }
            }
        } else {

            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                if (place.getAddress() != null) {
                    String address = place.getAddress().toString();
                    EditText et = (EditText) findViewById(requestCode);
                    et.setText(address);
                } else {
                    Toast.makeText(this, "Couldn't find the address, Please choose another location", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void hideSoftKeyboard(View v) {

        InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
