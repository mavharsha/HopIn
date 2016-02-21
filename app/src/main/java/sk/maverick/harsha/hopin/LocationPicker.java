/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocationPicker extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener{

    GoogleMap mMap;
    EditText mLocation;
    Button mGeocode;
    Marker marker;
    Address final_address;
    int REQUEST_PLACE_PICKER = 1;


    private static final int DEFAULT_ZOOM_LEVEL = 17;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLocation = (EditText) findViewById(R.id.input_address);
        mGeocode = (Button) findViewById(R.id.button_geocode);

        mGeocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*hideKeyboard();

                String edit_text_add = mLocation.getText().toString();
                if(!TextUtils.isEmpty(edit_text_add)) {
                    Toast.makeText(getApplicationContext(), "Geo coding " + edit_text_add, Toast.LENGTH_SHORT).show();

                    final_address = geoCode(edit_text_add);

                    if(final_address != null)
                    {
                    String locality = final_address.getLocality();
                    double lat = final_address.getLatitude();
                    double lng = final_address.getLongitude();

                    goToLocation(lat, lng, locality);
                    }else{
                        Toast.makeText(getApplicationContext(), "Error! Re-Enter another well formed address.", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "Empty Address.", Toast.LENGTH_SHORT).show();
                }*/

                try {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    startActivityForResult(builder.build(LocationPicker.this), REQUEST_PLACE_PICKER);

                } catch (GooglePlayServicesRepairableException e) {
                    // ...
                } catch (GooglePlayServicesNotAvailableException e) {
                    // ...
                }

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public Address geoCode(String address) {

        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> location =  geocoder.getFromLocationName(address,1);
            Address currentAddress = location.get(0);

            return currentAddress;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void goToLocation(double lat, double lng, String locality) {

        LatLng newPointer = new LatLng(lat, lng);
        MarkerOptions options = new MarkerOptions().position(newPointer).title(locality).draggable(true);

        if(marker!=null){
            marker.remove();
        }
        marker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPointer, DEFAULT_ZOOM_LEVEL));
    }

    public void hideKeyboard() {

        View v = getCurrentFocus();
        if(getCurrentFocus() != null) {
            mGeocode.clearFocus();
            InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;

        Geocoder geocoder = new Geocoder(this);
        Address currentAddress = null;

        try {
            List<Address> location =  geocoder.getFromLocation(lat, lng, 1);
            currentAddress = location.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(currentAddress!=null)
        Toast.makeText(LocationPicker.this, "The locality is "+ currentAddress.getLocality(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(LocationPicker.this, "Couldn't find the locality", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
