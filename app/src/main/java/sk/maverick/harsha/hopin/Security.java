/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Security extends AppCompatActivity {

    @Bind(R.id.sos_emergencycnt1)
    EditText econtact1;
    @Bind(R.id.sos_emergencycnt2)
    EditText econtact2;
    @Bind(R.id.sos_emergencycnt1number)
    EditText econtactnum1;
    @Bind(R.id.sos_emergencycnt2number)
    EditText econtactnum2;
    @Bind(R.id.sos_button)
    Button sosbutton;

    public static final String MyPREFERENCES = "MyPrefs";
    final int REQUESTCODE_FINE_LOCATION = 1;
    SharedPreferences prefs;
    int count = 5;
    String restoredcontact1, restoredcontact2, restoredcontactnum1, restoredcontactnum2;
    double latitude, longitude;
    LocationManager locationManager;
    protected final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        sosbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count == 0) {
                    broadcastMessage();
                } else {
                    count = count - 1;
                    sosbutton.setText("" + count);
                }
            }
        });
    }

    private void broadcastMessage() {
        // getLocation();

       if(latitude!=0 & longitude!=0){
           Geocoder geocoder = new Geocoder(this, Locale.getDefault());
           List<Address> addresses = new ArrayList<>();
           try {
               addresses = geocoder.getFromLocation(latitude, longitude, 1);
           } catch (IOException e) {
               e.printStackTrace();
           }

           SmsManager smsManager = SmsManager.getDefault();
           smsManager.sendTextMessage(restoredcontactnum1, null, restoredcontact1 + ", Im in trouble. " +
                   "My last known location is "+addresses.get(0).getAddressLine(0) +" "+addresses.get(0).getLocality(), null, null);
           smsManager.sendTextMessage(restoredcontactnum2, null, restoredcontact2 + ", Im in trouble. " +
                   "My last known location is "+addresses.get(0).getAddressLine(0) +" "+addresses.get(0).getLocality(), null, null);
           Toast.makeText(getApplicationContext(), "Sending, Im in trouble. " +
                   "My last know location is "+addresses.get(0).getAddressLine(0) +" "+addresses.get(0).getLocality(), Toast.LENGTH_SHORT).show();

       }else
       {
           Toast.makeText(getApplicationContext(), "No Location found yet.", Toast.LENGTH_SHORT).show();
       }
    }

    private void getLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(Security.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(Security.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUESTCODE_FINE_LOCATION);
                return;
        }
            enableLocationUpdates();
    }

    private void enableLocationUpdates() {

        final int minTime = 10;
        final int minDistance = 1;

        final Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String provider = locationManager.getBestProvider(criteria,true);
        locationManager.requestLocationUpdates(provider, 1000, 0, locationListener);
        Toast.makeText(Security.this, "Enabling location", Toast.LENGTH_SHORT).show();
    }

    private void init() {

        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         restoredcontact1 = prefs.getString("contact1", null);
         restoredcontact2 = prefs.getString("contact2", null);
         restoredcontactnum1 = prefs.getString("contactnum1", null);
         restoredcontactnum2 = prefs.getString("contactnum2", null);

        econtact1.setText(restoredcontact1);
        econtact2.setText(restoredcontact2);
        econtactnum1.setText(restoredcontactnum1);
        econtactnum2.setText(restoredcontactnum2);
        getLocation();

    }

    private void updateLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        if ((requestCode == REQUESTCODE_FINE_LOCATION) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            enableLocationUpdates();
        }
    }

}
