/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG  = "MainActivity";
    public static final String MyPREFERENCES = "MyPrefs" ;

    CircleImageView profile;
    TextView username;
    String username_tx, avatar_tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(Home.this, CreateEvent.class));
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout =
                navigationView.getHeaderView(0);

        profile = (CircleImageView) headerLayout.findViewById(R.id.nav_header_profile_image);
        username = (TextView) headerLayout.findViewById(R.id.nav_header_username);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Home.this, Profile.class));
            }
        });
        init();

        if(findViewById(R.id.fragment_container)!= null) {

            //to make sure if the a fragment is added only when onCreate is called for the first time
            if (savedInstanceState != null) {
                return;
            }
            CreatedEventFragment fragmentOne = new CreatedEventFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragmentOne).commit();

        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    private void init() {

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String restoredusername = prefs.getString("username", null);
        RequestParams requestParams = new RequestParams();

        requestParams.setUri("http://localhost:3000/profile/"+restoredusername);
        new HomeProfileAsyc().execute(requestParams);
    }

    private void updateNavUi(){
        username.setText(username_tx);
        changeProfilePic(avatar_tx);
    }

    private void changeProfilePic(String avatar) {
        switch (avatar){
            case "1" : profile.setImageResource(R.drawable.avatar_1);
                avatar_tx = "1";
                break;
            case "2" : profile.setImageResource(R.drawable.avatar_4);
                avatar_tx = "2";
                break;
            case "3" : profile.setImageResource(R.drawable.avatar_8);
                avatar_tx = "3";
                break;
            case "4" : profile.setImageResource(R.drawable.avatar_9);
                avatar_tx = "4";
                break;
            case "5" : profile.setImageResource(R.drawable.avatar_11);
                avatar_tx = "5";
                break;
            default: break;

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_createdevents) {

        } else if (id == R.id.nav_searchevent) {

        } else if (id == R.id.nav_myevents) {

        } else if (id == R.id.nav_savedevents) {

        } else if (id == R.id.nav_share) {
            startActivity(new Intent(Home.this, LocationPicker.class));


        } else if (id == R.id.nav_sos) {
            startActivity(new Intent(Home.this, Security.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class HomeProfileAsyc extends AsyncTask<RequestParams, Void, HttpResponse>{

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

            return httpResponse;        }

        @Override
        protected void onPostExecute(HttpResponse response) {

            if(response == null){
                Snackbar.make(findViewById(R.id.appbar_main_coordinator), "Error! Please check you internet connection", Snackbar.LENGTH_SHORT).show();
            }
            else if(response.getStatusCode() != 200){
                Snackbar.make(findViewById(R.id.appbar_main_coordinator), "Error! Please try later", Snackbar.LENGTH_SHORT).show();
            }else if(response.getStatusCode() == 200){

                // Populate the edit text fields with the response
                Log.v(TAG, "The response is " + response.getBody());

                JSONObject jsonObject = null;
                JSONObject details;
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<sk.maverick.harsha.hopin.Models.Profile> jsonAdapter = moshi.adapter(sk.maverick.harsha.hopin.Models.Profile.class);


                try {
                    jsonObject = new JSONObject(response.getBody());
                    username_tx = jsonObject.getString("username");

                    details = jsonObject.getJSONObject("details");
                    sk.maverick.harsha.hopin.Models.Profile profile = jsonAdapter.fromJson(details.toString());
                    avatar_tx = profile.getAvatar();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateNavUi();
            }

        }
    }
}
