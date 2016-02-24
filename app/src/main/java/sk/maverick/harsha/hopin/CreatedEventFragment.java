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
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Models.Event;
import sk.maverick.harsha.hopin.Util.DividerItemDecorator;


public class CreatedEventFragment extends Fragment {

    private final static String TAG  = "CREATEDEVENTFRAG";
    public static final String MyPREFERENCES = "MyPrefs" ;
    ContentAdapter contentAdapter;

    private List <Event> mdataset = new ArrayList<>();

    public CreatedEventFragment() {

    }

    @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            // call an async call
            SharedPreferences prefs = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String restoredusername = prefs.getString("username", null);

            RequestParams requestParams = new RequestParams();

            requestParams.setUri("http://localhost:3000/events/"+restoredusername);
            new CreateEventAsyncTask(getActivity()).execute(requestParams);
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Created Events");

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(
                R.layout.fragment_created_event, container, false);

        RecyclerView recyclerView = (RecyclerView) linearLayout.findViewById(R.id.createdevent_recycler);


        contentAdapter = new ContentAdapter(mdataset);
        recyclerView.setAdapter(contentAdapter);
        RecyclerView.ItemDecoration decoration = new DividerItemDecorator(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // Inflate the layout for this fragment
        return linearLayout;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, subhead;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.recyclerview_eventname);
            subhead = (TextView) itemView.findViewById(R.id.recyclerview_eventtype);
        }

    }


    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{
        private List<Event> dataset;

        public ContentAdapter(List<Event> dataset) {
            this.dataset = dataset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_row_layout, parent, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.title.setText(dataset.get(position).getEventname());
            holder.subhead.setText(dataset.get(position).getEventtype());

            holder.itemView.setLongClickable(true);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getActivity(), dataset.get(position).getEventname() + " shortPressed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        @Override
        public int getItemCount() {
            return dataset.size();
        }

    }


    private class CreateEventAsyncTask extends AsyncTask<RequestParams, Void, HttpResponse>{

        ProgressDialog progressDialog;
        public CreateEventAsyncTask(Activity activity) {
            this.progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            Log.v(TAG, "Started the request");
            progressDialog.setMessage("Fetching your events. Please wait.");
            progressDialog.show();
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
            Log.v(TAG, "Post execute");

            if(response.getStatusCode()!= 200){
                Toast.makeText(getActivity(), "Error! Please try later", Toast.LENGTH_LONG).show();
            }
            else if (response.getStatusCode() == 200){
                parseResopnse(response.getBody());
            }
            contentAdapter.notifyDataSetChanged();
            progressDialog.dismiss();

        }
    }

    private void parseResopnse(String body) {

        Log.v(TAG, "parseRespone" + body);

        JSONObject newJson;
        JSONArray details = null;
        Event myevent = null;

        try {
            newJson = new JSONObject(body);
            details = newJson.getJSONArray("details");
            if(details== null){
                Log.v(TAG, "Null hai sir");
            }
            else
            {
                Log.v(TAG, "Null nahi hai sir");
            }
            Log.v(TAG, "Array is "+ details);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Event> jsonAdapter = moshi.adapter(Event.class);

        for (int i=0; i< details.length(); i++){

            try {
                myevent = jsonAdapter.fromJson(details.getJSONObject(i).toString());
                mdataset.add(myevent);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.v(TAG, "Event size is " + mdataset.size());
        Log.v(TAG, "Event name is " + mdataset.get(0).getEventname());
    }
}
