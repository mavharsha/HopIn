/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;

public class Profile extends AppCompatActivity {

    @Bind(R.id.profile_firstname)EditText firstname;
    @Bind(R.id.profile_lasttname)EditText lastname;

    @Bind(R.id.profile_phonenumber) EditText phonenumber;
    @Bind(R.id.profile_emergencycnt1) EditText emergencycnt1;
    @Bind(R.id.profile_emergencycnt2) EditText emergencycnt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        RequestParams requestParams = new RequestParams();

        requestParams.setUri("http://localhost:3000/profile");

        new GetProfileAsync().execute(requestParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_profile) {
            Toast.makeText(getApplicationContext(),
                    "Here I'll intent to another activity, where you can edit", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetProfileAsync extends AsyncTask<RequestParams, Void, HttpResponse>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected HttpResponse doInBackground(RequestParams... params) {

            HttpResponse httpResponse = null;
            try {
               httpResponse =  HttpManager.getData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return httpResponse;
        }
        @Override
        protected void onPostExecute(HttpResponse response) {

            if(response == null){
                Snackbar.make(findViewById(R.id.getprofile_coordinator),
                        "Error! Please Check your Internet Connection", Snackbar.LENGTH_SHORT).show();
            }else if(response.getStatusCode() == 200){

                // Populate the edit text fields with the response

            }

            firstname.setText("Sree Harsha");
            lastname.setText("M");

            phonenumber.setText("1234567890");

            emergencycnt1.setText("Harsha");
            emergencycnt2.setText("DumbKing");

        }

    }

}
