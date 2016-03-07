/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;

public class Profile extends AppCompatActivity {

    @Bind(R.id.profile_firstname)EditText firstname;
    @Bind(R.id.profile_lasttname)EditText lastname;

    @Bind(R.id.profile_phonenumber) EditText phonenumber;
    @Bind(R.id.profile_emergencycnt1) EditText emergencycnt1;
    @Bind(R.id.profile_emergencycnt2) EditText emergencycnt2;

    @Bind(R.id.profile_emergencycnt1number) EditText emergencycntnum1;
    @Bind(R.id.profile_emergencycnt2number) EditText emergencycntnum2;

    @Bind(R.id.profile_image) CircleImageView profile_pic;
    @Bind(R.id.profile_username) TextView username;


    private static final String TAG = "Profile";
    public static final String MyPREFERENCES = "MyPrefs" ;
    String username_tx,firstname_tx,lastname_tx , phone_tx, avatar_tx, emergencycnt1_tx, emergencycnt2_tx, emergencycntnum1_tx, emergencycntnum2_tx = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String restoredusername = prefs.getString("username", null);
        RequestParams requestParams = new RequestParams();

        requestParams.setUri(App.getIp()+"profile/"+restoredusername);

        new GetProfileAsync(Profile.this).execute(requestParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String restoredusername = prefs.getString("username", null);
        RequestParams requestParams = new RequestParams();

        requestParams.setUri(App.getIp()+"profile/"+restoredusername);

        new GetProfileAsync(Profile.this).execute(requestParams);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String restoredusername = prefs.getString("username", null);
        RequestParams requestParams = new RequestParams();

        requestParams.setUri(App.getIp()+"profile/"+restoredusername);

        new GetProfileAsync(Profile.this).execute(requestParams);
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
            Intent intent = new Intent(Profile.this, UpdateProfile.class);
            intent.putExtra("username", username_tx);
            intent.putExtra("avatar", avatar_tx);
            intent.putExtra("firstname", firstname_tx);
            intent.putExtra("lastname", lastname_tx);
            intent.putExtra("phonenumber", phone_tx);

            intent.putExtra("emergencycnt1", emergencycnt1_tx);
            intent.putExtra("emergencycnt2", emergencycnt2_tx);
            intent.putExtra("emergencycntnum1", emergencycntnum1_tx);
            intent.putExtra("emergencycntnum2", emergencycntnum2_tx);
            startActivity(intent);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void changeProfilePic(String avatar) {
        switch (avatar){
            case "1" : profile_pic.setImageResource(R.drawable.avatar_1);
                avatar_tx = "1";
                break;
            case "2" : profile_pic.setImageResource(R.drawable.avatar_4);
                avatar_tx = "2";
                break;
            case "3" : profile_pic.setImageResource(R.drawable.avatar_8);
                avatar_tx = "3";
                break;
            case "4" : profile_pic.setImageResource(R.drawable.avatar_9);
                avatar_tx = "4";
                break;
            case "5" : profile_pic.setImageResource(R.drawable.avatar_11);
                avatar_tx = "5";
                break;
            default: break;

        }
    }

    private class GetProfileAsync extends AsyncTask<RequestParams, Void, HttpResponse>{

        private ProgressDialog dialog;

        public GetProfileAsync(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Retrieving your details, please wait.");
            dialog.show();
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

            dialog.dismiss();
            if(response == null){
                Snackbar.make(findViewById(R.id.getprofile_coordinator),
                        "Error! Please Check your Internet Connection", Snackbar.LENGTH_SHORT).show();
            }else if(response.getStatusCode() == 200){

                // Populate the edit text fields with the response
                Log.v(TAG, "The response is " + response.getBody());

                JSONObject jsonObject = null;
                JSONObject details;
                try {
                    jsonObject = new JSONObject(response.getBody());
                    username_tx = jsonObject.getString("username");

                    details = jsonObject.getJSONObject("details");
                     firstname_tx = details.getString("firstname");
                     lastname_tx = details.getString("lastname");
                     phone_tx = details.getString("phonenumber");
                     avatar_tx = details.getString("avatar");
                     emergencycnt1_tx = details.getString("emergencycntname1");
                     emergencycnt2_tx = details.getString("emergencycntname2");
                     emergencycntnum1_tx = details.getString("emergencycntnumber1");
                     emergencycntnum2_tx = details.getString("emergencycntnumber2");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                username.setText(username_tx);
                firstname.setText(firstname_tx);
                lastname.setText(lastname_tx);

                phonenumber.setText(phone_tx);

                emergencycnt1.setText(emergencycnt1_tx);
                emergencycnt2.setText(emergencycnt2_tx);
                emergencycntnum1.setText(emergencycntnum1_tx);
                emergencycntnum2.setText(emergencycntnum2_tx);
                changeProfilePic(avatar_tx);
            }
        }

    }

}
