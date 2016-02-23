package sk.maverick.harsha.hopin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Util.ConnectionManager;

public class Login extends AppCompatActivity {

    protected final static String TAG = "LoginActivity";
    public static final String MyPREFERENCES = "MyPrefs" ;

    private EditText username, password;
    private TextView login_textView, signup_textView;
    private Button login;
    private ProgressBar pb;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Initialize the views and typeface
        init();

    }

    // On press for login button
    public void login(View view){

         if(username.getText().toString().isEmpty() ||  password.getText().toString().isEmpty()) {
            username.setError("Invalid username");
            password.setError("Invalid password");
            Log.v(TAG, "login clicked. username is ");
         }else
         {
             if(ConnectionManager.isConnected(Login.this)){

                 RequestParams request = new RequestParams();
                 request.setUri("http://localhost:3000/login");
                 request.setParam("username", username.getText().toString());
                 request.setParam("password", password.getText().toString());

                 new LoginAsync().execute(request);
             }else
             {
                 Snackbar.make(findViewById(R.id.login_coordinator), "No Internet", Snackbar.LENGTH_LONG).show();
             }
         }
    }


    public void signUp(View view){

        startActivity(new Intent(Login.this, SignUp.class));
    }

    private void init(){
        username = (EditText) findViewById(R.id.username_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        login_textView = (TextView) findViewById(R.id.login_text_view);
        signup_textView = (TextView) findViewById(R.id.signup_text_view);
        pb = (ProgressBar) findViewById(R.id.login_progressBar);
        login  = (Button) findViewById(R.id.login_button);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        pb.setVisibility(View.INVISIBLE);
        signup_textView.setPaintFlags(signup_textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(signup_textView, Linkify.ALL);


        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        username.setTypeface(roboto_light);
        password.setTypeface(roboto_light);

        Typeface roboto_thin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        login_textView.setTypeface(roboto_thin);
        signup_textView.setTypeface(roboto_thin);
    }


    private class LoginAsync extends AsyncTask<RequestParams, Void, HttpResponse> {

        protected void onPreExecute() {

            pb.setVisibility(View.VISIBLE);
        }

        protected HttpResponse doInBackground(RequestParams... params) {

            HttpResponse httpResponse = null;
            try {
                 httpResponse =  HttpManager.postData(params[0]);
                Log.i(TAG,"Async task fired");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return httpResponse;
        }

        protected void onPostExecute(HttpResponse result) {

            if(result == null){
                Snackbar.make(findViewById(R.id.login_coordinator), "Error! Please try later", Snackbar.LENGTH_LONG).show();
            }else if (result.getStatusCode() == 200){

                SharedPreferences.Editor editor = sharedpreferences.edit();

                String response = result.getBody();
                JSONObject resultJson = null;
                Log.v(TAG, "The response is "+ response);
                try {
                     resultJson = new JSONObject(response);

                    editor.putString("username", resultJson.getString("username"));
                    editor.putString("token", resultJson.getString("token"));
                    Log.v(TAG, "The username is "+ resultJson.getString("username"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                editor.commit();


                // get the token an save it in shared preferences
                startActivity(new Intent(Login.this, Home.class));

            }
            pb.setVisibility(View.INVISIBLE);
        }
    }
}