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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Util.ProfilePic;

public class EventRequest extends AppCompatActivity {

    String eventid, requester, seatsrequested, pickuplocation;
    @Bind(R.id.eventrequest_text)
    TextView eventrequesttext;
    @Bind(R.id.eventrequest_profile_image)
    CircleImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        init();

    }

    private void init() {

        String  eventname, avatar;

        Intent intent = getIntent();
        requester = intent.getStringExtra("requester");
        seatsrequested = intent.getStringExtra("seatsrequested");
        eventname = intent.getStringExtra("eventname");
        avatar = intent.getStringExtra("requesteravatar");
        eventid = intent.getStringExtra("id");
        pickuplocation = intent.getStringExtra("pickuplocation");

        StringBuilder sb = new StringBuilder();
        sb.append(requester);
        sb.append(" has requested for ");
        sb.append(seatsrequested);
        sb.append(" seats to ");
        sb.append(eventname);
        sb.append(" event ");
        sb.append("at ");
        sb.append(pickuplocation);

        eventrequesttext.setText(sb);

        profilepic.setImageResource(ProfilePic.getAvatar(avatar));
    }


    @OnClick(R.id.eventrequest_accept)
    public void acceptRide(View view){

        Snackbar.make(findViewById(R.id.eventrequest_coordinator), "You accepted the request!",  Snackbar.LENGTH_LONG).show();

        RequestParams requestParams = new RequestParams();

        requestParams.setUri(App.getIp()+"request");
        requestParams.setParam("eventid", eventid);
        requestParams.setParam("requesteduser", requester);
        requestParams.setParam("seatsrequested", seatsrequested);
        requestParams.setParam("status", "Accepted");

        new EventRequestAsync(EventRequest.this).execute(requestParams);
    }


    @OnClick(R.id.eventrequest_reject)
    public void rejectRide(View view){

        Snackbar.make(findViewById(R.id.eventrequest_coordinator), "You rejected the request!",  Snackbar.LENGTH_LONG).show();

        RequestParams requestParams = new RequestParams();

        requestParams.setUri(App.getIp()+"request");
        requestParams.setParam("eventid", eventid);
        requestParams.setParam("seatsrequested", seatsrequested);
        requestParams.setParam("status", "Rejected");

        new EventRequestAsync(EventRequest.this).execute(requestParams);

    }


    private class EventRequestAsync extends AsyncTask<RequestParams, Void, HttpResponse>{

        ProgressDialog progressDialog;

        public EventRequestAsync(Activity activity) {
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Talking to the server");
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
        protected void onPostExecute(HttpResponse response) {
            super.onPostExecute(response);
            progressDialog.dismiss();
            if (response == null) {
                Snackbar.make(findViewById(R.id.eventrequest_coordinator), "Error! Please check you internet",  Snackbar.LENGTH_LONG).show();
            } else if (response.getStatusCode() != 200) {
                Snackbar.make(findViewById(R.id.eventrequest_coordinator), "Error! Please try later",  Snackbar.LENGTH_LONG).show();
            } else if (response.getStatusCode() == 200) {
                Snackbar.make(findViewById(R.id.eventrequest_coordinator), "Done!",  Snackbar.LENGTH_LONG).show();
            }

        }
    }

}
