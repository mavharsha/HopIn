package sk.maverick.harsha.hopin;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import java.io.IOException;

import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.RequestParams;

public class Login extends AppCompatActivity {

    protected final static String TAG = "LoginActivity";
    private EditText username, password;
    private TextView login_textView, signup_textView;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Initialize the views and
        init();

    }

    // On press for login button
    public void login(View view){

         if(username.getText().toString().isEmpty() ||  password.getText().toString().isEmpty()) {
            username.setError("Invalid username");
            password.setError("Invalid password");
            Log.v(TAG, "login clicked. username is ");
         }

       /* RequestParams request = new RequestParams();
        request.setUri("http://www.google.com");*/
    }


    public void signUp(View view){

        startActivity(new Intent(Login.this, SignUp.class));
    }

    private void init(){
        username = (EditText) findViewById(R.id.username_edit_text);
        password = (EditText) findViewById(R.id.password_edit_text);
        login_textView = (TextView) findViewById(R.id.login_text_view);
        signup_textView = (TextView) findViewById(R.id.signup_text_view);

        signup_textView.setPaintFlags(signup_textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(signup_textView, Linkify.ALL);


        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        username.setTypeface(roboto_light);
        password.setTypeface(roboto_light);

        Typeface roboto_thin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        login_textView.setTypeface(roboto_thin);
        signup_textView.setTypeface(roboto_thin);
        login  = (Button) findViewById(R.id.login_button);
    }


    private class LoginAsync extends AsyncTask<RequestParams, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(RequestParams... params) {

            String result;
            try {
                HttpManager.getData(params[0]);
                Log.i(TAG,"Async task fired");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG,"Async post execute. The result is "+ result);

        }
    }
}
