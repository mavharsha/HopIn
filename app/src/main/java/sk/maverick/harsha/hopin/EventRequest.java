/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import sk.maverick.harsha.hopin.Util.ProfilePic;

public class EventRequest extends AppCompatActivity {

    String eventid;
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

        String requester, seatsrequested, eventname, avatar;

        Intent intent = getIntent();
        requester = intent.getStringExtra("requester");
        seatsrequested = intent.getStringExtra("seatsrequested");
        eventname = intent.getStringExtra("eventname");
        avatar = intent.getStringExtra("requesteravatar");
        eventid = intent.getStringExtra("id");

        StringBuilder sb = new StringBuilder();
        sb.append(requester);
        sb.append(" has requested for ");
        sb.append(seatsrequested);
        sb.append(" seats to ");
        sb.append(eventname);
        sb.append(" event.");
        eventrequesttext.setText(sb);

        profilepic.setImageResource(ProfilePic.getAvatar(avatar));
    }

}
