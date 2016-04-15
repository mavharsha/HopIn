package sk.maverick.harsha.hopin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.RadioButton;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Models.Event;
import sk.maverick.harsha.hopin.Models.Pickup;
import sk.maverick.harsha.hopin.Util.ConnectionManager;

public class EditEvent extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    Event myevent = null;
    final static String TAG = "EditEvent";
    public static final String MyPREFERENCES = "MyPrefs";

    private Calendar calendar;
    private static final String[] preferences = new String[]{
            "Both",
            "Male",
            "Female"
    };
    private int REQUEST_EVENT_PLACE_PICKER = 98;
    private int REQUEST_PICKUP_PLACE_PICKER = 99;
    private List<HashMap<String, Object>> pickupid = new ArrayList<>();
    private int day, month, year, eventTimeHour, eventTimeMinute, privacytype;
    String passengerpref, eventid;
    double locationlat, locationlng;
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        init();
    }

    private void init() {

        Intent intent = getIntent();
        String jsonSent = intent.getStringExtra("EventObject");

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Event> jsonAdapter = moshi.adapter(Event.class);

        try {
            myevent = jsonAdapter.fromJson(jsonSent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (myevent != null) {
            eventid = myevent.get_id();
            day = myevent.getDateday();
            month = myevent.getDatemonth();
            year = myevent.getDateyear();

            eventTimeHour = myevent.getEventtimehour();
            eventTimeMinute = myevent.getEventtimeminute();

            locationlat = myevent.getEventlocationlat();
            locationlng = myevent.getEventlocationlng();
            passengerpref = myevent.getPreferences();

            eventName.setText(myevent.getEventname());
            eventType.setText(myevent.getEventtype());

            if (myevent.getPrivacytype() == 0) {
                findViewById(R.id.editevent_radioButton1).setSelected(true);
                privacytype = 0;
            } else {
                findViewById(R.id.editevent_radioButton2).setSelected(true);
                privacytype = 1;
            }

            seatsavailable.setText(myevent.getSeatsavailable());
            eventDate.setText(myevent.getDatemonth() + "/" + myevent.getDateday() + "/" + myevent.getDateyear());
            eventTime.setText(myevent.getEventtimehour() + ":" + myevent.getEventtimeminute());
            eventLocation.setText(myevent.getEventlocation());

            Pickup pickuppoint = myevent.getPickup().get(0);

            pickupTime.setText(pickuppoint.getPickuptime());
            pickupLocation.setText(pickuppoint.getPickuplocation());

            for (int i = 1; i < myevent.getPickup().size(); i++) {
                pickuppoint = myevent.getPickup().get(i);
                addPickUp();

                HashMap<String, Object> ids = pickupid.get(i - 1);

                EditText timeet = (EditText) ids.get("Time");
                EditText locationet = (EditText) ids.get("Location");

                timeet.setText(pickuppoint.getPickuptime());
                locationet.setText(pickuppoint.getPickuplocation());
            }

        } else {
            Snackbar.make(findViewById(R.id.editevent_coordinator), "Error! Please try later", Snackbar.LENGTH_SHORT).show();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                preferences);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        passpreference.setOnItemSelectedListener(this);
        passpreference.setAdapter(adapter);
        privacy.setOnCheckedChangeListener(this);

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


    @OnClick(R.id.editevent_confirm)
    public void onClickSaveEvent(View view) {

        hideSoftKeyboard(view);

        // Call async task to send data to the server
        if (ConnectionManager.isConnected(EditEvent.this)) {


            RequestParams request = new RequestParams();
            request.setUri(App.getIp()+"event");

            request.setParam("eventid", eventid);
            request.setParam("eventName", eventName.getText().toString());
            request.setParam("eventType", eventType.getText().toString());
            request.setParam("privacyType", "" + privacytype);


            request.setParam("seatsAvailable", seatsavailable.getText().toString());
            request.setParam("preferences", passengerpref);

            request.setParam("dateDay", "" + day);
            request.setParam("dateMonth", "" + month);
            request.setParam("dateYear", "" + year);


            request.setParam("eventTimeHour", "" + eventTimeHour);
            request.setParam("eventTimeMinute", "" + eventTimeMinute);
            request.setParam("eventLocation", eventLocation.getText().toString());

            request.setParam("eventLocationLat", "" + locationlat);
            request.setParam("eventLocationLng", "" + locationlng);


            List<HashMap<String, String>> listofmap = new ArrayList<>();

            HashMap<String, String> pick_up = new HashMap<>();
            pick_up.put("pickuptime", pickupTime.getText().toString());
            pick_up.put("pickuplocation", pickupLocation.getText().toString());

            listofmap.add(pick_up);


            for (int i = 0; i < pickupid.size(); i++) {

                String timestr, locationstr;
                // Get the edit texts from the array list of hash map
                Map<String, Object> idsmap = pickupid.get(i);
                Log.v(TAG, "The ids of row " + i + " is " + idsmap.keySet());

                EditText time = (EditText) idsmap.get("Time");
                EditText location = (EditText) idsmap.get("Location");

                Log.v(TAG, "Time is " + time.getText().toString());
                Log.v(TAG, "Location is " + location.getText().toString());


                timestr = time.getText().toString();
                locationstr = location.getText().toString();

                //Create a hashmap of pickup location addresses
                HashMap<String, String> pickup = new HashMap<>();

                pickup.put("pickuptime", timestr);
                pickup.put("pickuplocation", locationstr);
                listofmap.add(pickup);
            }

            request.setParam("pickup", listofmap);

            Log.v(TAG, new JSONObject(request.getParams()).toString());
            Log.v(TAG, "EditEvent sending a request to the server");
            new EditEventAsync(EditEvent.this).execute(request);
        } else {
            Snackbar.make(findViewById(R.id.createevent_confirm), "No Internet", Snackbar.LENGTH_LONG).show();
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
                    locationlat = locationltlng.latitude;
                    locationlng = locationltlng.longitude;

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

        int index = passpreference.getSelectedItemPosition();
        passengerpref = preferences[index];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        RadioButton radioButton = (RadioButton) findViewById(checkedId);
        if (null != radioButton && checkedId > -1) {

            String privacytype_str = (String) radioButton.getText();

            if (privacytype_str.equalsIgnoreCase("Public")) {
                privacytype = 0;
            } else {
                privacytype = 1;
            }
            Toast.makeText(EditEvent.this, "Privacy type is " + privacytype, Toast.LENGTH_SHORT).show();
        }
    }

    private class EditEventAsync extends AsyncTask<RequestParams, Void, HttpResponse> {

        ProgressDialog progressDialog;

        public EditEventAsync(Activity activity) {
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Trying to create your event");
            progressDialog.show();
        }

        @Override
        protected HttpResponse doInBackground(RequestParams... params) {
            HttpResponse httpResponse = null;

            try {
                httpResponse = HttpManager.putData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResponse;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {

            progressDialog.dismiss();
            if (result == null) {
                Snackbar.make(findViewById(R.id.editevent_coordinator), "Error! Please try later", Snackbar.LENGTH_LONG).show();
            } else if (result.getStatusCode() == 200) {
                new AlertDialog.Builder(EditEvent.this)
                        .setMessage("Successfully edited your event")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                   finish();
                            }
                        }).show();
            } else if (result.getStatusCode() != 200) {

                Snackbar.make(findViewById(R.id.editevent_coordinator), "Error! Please try later", Snackbar.LENGTH_LONG).show();
            }

        }

    }
}
