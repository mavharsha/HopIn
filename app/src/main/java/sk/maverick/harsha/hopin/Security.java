/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    @Bind(R.id.sos_button) Button sosbutton;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences prefs;
    int count =5;
    String restoredcontact1,restoredcontact2, restoredcontactnum1, restoredcontactnum2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.security);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SmsManager smsManager = SmsManager.getDefault();
        init();


        sosbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count == 0){

                broadcastMessage();
                }else{
                    count = count -1;
                    sosbutton.setText(""+count);
                }
            }
        });
    }

    private void broadcastMessage() {
       // getLocation();

        SmsManager smsManager = SmsManager.getDefault();
/*
        smsManager.sendTextMessage("9259674678", null, restoredcontact1 +", Im in trouble ",null,null);
*/
        Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT);
    }

   /* private void getLocation() {

        LocationManager  locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isGPSEnabled){
            Snackbar.make(findViewById(R.id.sos_coordinator), "GPS not enabled", Snackbar.LENGTH_LONG).show();
        }
        else if(!isNetworkEnabled){
            Snackbar.make(findViewById(R.id.sos_coordinator), "Network not enabled", Snackbar.LENGTH_LONG).show();
        }else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
        }


    }*/

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
    }


}
