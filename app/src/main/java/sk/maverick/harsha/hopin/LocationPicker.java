/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocationPicker extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mMap;
    EditText mLocation;
    Button mGeocode;
    Marker marker;
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
                hideKeyboard();

                String add = mLocation.getText().toString();
                if(!TextUtils.isEmpty(add)) {
                    Toast.makeText(getApplicationContext(), "Geo coding " + add, Toast.LENGTH_SHORT).show();
                    geoCode(add);
                }else
                {
                    Toast.makeText(getApplicationContext(), "Error! Re-Enter another well formed address.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void geoCode(String address) {

        Geocoder geocoder = new Geocoder(this);

        try {
            List<Address> location =  geocoder.getFromLocationName(address,1);
            Address currentAddress = location.get(0);
            String locality = currentAddress.getLocality();

            double lat = currentAddress.getLatitude();
            double lng = currentAddress.getLongitude();

            goToLocation(lat, lng, locality);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void goToLocation(double lat, double lng, String locality) {


        LatLng newPointer = new LatLng(lat, lng);
        MarkerOptions options = new MarkerOptions().position(newPointer).title(locality);

        if(marker!=null){
            marker.remove();
        }
        marker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPointer, 10));
    }

    private void hideKeyboard() {

        if(getCurrentFocus() != null) {
            InputMethodManager inm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
