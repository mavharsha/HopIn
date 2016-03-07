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
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;

public class UpdateProfile extends AppCompatActivity {

    @Bind(R.id.updateprofile_image) CircleImageView profile_pic;
    @Bind(R.id.updateprofile_username) TextView username;
    @Bind(R.id.updateprofile_firstname) EditText firstname;
    @Bind(R.id.updateprofile_lasttname) EditText lastname;
    @Bind(R.id.updateprofile_phonenumber) EditText phonenumber;


    @Bind(R.id.updateprofile_emergencycnt1) EditText emergencycnt1;
    @Bind(R.id.updateprofile_emergencycnt2) EditText emergencycnt2;
    @Bind(R.id.updateprofile_emergencycnt1number) EditText emergencycnt1number;
    @Bind(R.id.updateprofile_emergencycnt2number) EditText emergencycnt2number;

    private String username_tx,firstname_tx,lastname_tx , phone_tx, avatar_tx, emergencycnt1_tx, emergencycnt2_tx, emergencycntnum1_tx, emergencycntnum2_tx = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    private void init() {

        username_tx = getIntent().getStringExtra("username");
        avatar_tx = getIntent().getStringExtra("avatar");
        firstname_tx = getIntent().getStringExtra("firstname");
        lastname_tx = getIntent().getStringExtra("lastname");
        phone_tx = getIntent().getStringExtra("phonenumber");


        emergencycnt1_tx = getIntent().getStringExtra("emergencycnt1");
        emergencycnt2_tx = getIntent().getStringExtra("emergencycnt2");
        emergencycntnum1_tx = getIntent().getStringExtra("emergencycntnum1");
        emergencycntnum2_tx = getIntent().getStringExtra("emergencycntnum2");

        username.setText(username_tx);
        firstname.setText(firstname_tx);
        lastname.setText(lastname_tx);
        phonenumber.setText(phone_tx);

        emergencycnt1.setText(emergencycnt1_tx);
        emergencycnt2.setText(emergencycnt2_tx);

        emergencycnt1number.setText(emergencycntnum1_tx);
        emergencycnt2number.setText(emergencycntnum2_tx);

        changeProfilePic(avatar_tx);
    }


    public void changeAvatar(View view){
        String  avatar = (String) view.getTag();

        Toast.makeText(getApplicationContext(), "Clicked on avatar " + avatar, Toast.LENGTH_SHORT).show();
        changeProfilePic(avatar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_updateprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.update_profile) {

            RequestParams requestParams = new RequestParams();
            requestParams.setUri(App.getIp()+"profile/"+username_tx);
            requestParams.setParam("firstname", firstname.getText().toString());
            requestParams.setParam("lastname", lastname.getText().toString());
            requestParams.setParam("phonenumber", phonenumber.getText().toString());

            requestParams.setParam("avatar", avatar_tx);

            requestParams.setParam("emergencycntname1", emergencycnt1.getText().toString());
            requestParams.setParam("emergencycntname2", emergencycnt2.getText().toString());
            requestParams.setParam("emergencycntnumber1", emergencycnt1number.getText().toString());
            requestParams.setParam("emergencycntnumber2", emergencycnt2number.getText().toString());

            new UpdateProfileAsync(UpdateProfile.this).execute(requestParams);
            return true;
        }
        if (id == R.id.change_password) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class UpdateProfileAsync extends AsyncTask<RequestParams, Void, HttpResponse>{

        ProgressDialog progressDialog;
        public UpdateProfileAsync(Activity activity) {
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Trying to update you profile, please wait.");
            progressDialog.show();
        }

        @Override
        protected HttpResponse doInBackground(RequestParams... params) {

            HttpResponse httpResponse =null;
            try {
                 httpResponse = HttpManager.putData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return httpResponse;
        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            progressDialog.dismiss();

            if(response.getStatusCode() == 200)
            {
                new AlertDialog.Builder(UpdateProfile.this)
                        .setMessage("Successfully updated profile")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                finish();
                            }
                        }).show();
            }else
            {
                Snackbar.make(findViewById(R.id.updateprofile_coordinator),
                        "Error! Could not update. Please try later",Snackbar.LENGTH_LONG).show();
            }
        }
    }

}
