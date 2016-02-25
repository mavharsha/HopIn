/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import butterknife.Bind;
import sk.maverick.harsha.hopin.Http.HttpManager;
import sk.maverick.harsha.hopin.Http.HttpResponse;
import sk.maverick.harsha.hopin.Http.RequestParams;
import sk.maverick.harsha.hopin.Util.ConnectionManager;
import sk.maverick.harsha.hopin.Util.RegexValidator;

public class SignUp extends AppCompatActivity {

    protected final static String TAG = "LoginActivity";
    private EditText username, phone, password, repassword, firstname, lastname;
    private Button register;
    private TextView signup,oldUser;

    @Bind(R.id.signup_ipl_fristname) EditText ipl_firstname;
    @Bind(R.id.signup_ipl_lastname) EditText ipl_lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    private void init(){
        username    = (EditText) findViewById(R.id.sign_up_username_et);

        phone       = (EditText) findViewById(R.id.sign_up_phone_et);
        firstname   = (EditText)findViewById(R.id.sign_up_firstname);
        lastname    = (EditText) findViewById(R.id.sign_up_lastname);
        password    = (EditText) findViewById(R.id.sign_up_password_et);
        repassword  = (EditText) findViewById(R.id.sign_up_repassword_et);
        oldUser     = (TextView) findViewById(R.id.sign_up_old_user_tv);
        signup      = (TextView) findViewById(R.id.sign_up_text_view);
        register    = (Button) findViewById(R.id.sign_up_register_btn);

        oldUser.setPaintFlags(oldUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(oldUser, Linkify.ALL);

        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        username.setTypeface(roboto_light);
        firstname.setTypeface(roboto_light);
        lastname.setTypeface(roboto_light);
        phone.setTypeface(roboto_light);
        password.setTypeface(roboto_light);
        repassword.setTypeface(roboto_light);

        Typeface roboto_thin = Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf");
        signup.setTypeface(roboto_thin);
        oldUser.setTypeface(roboto_thin);

    }

    public void backToLogin(View view){
        finish();
    }

    public void register(View view){

        String username_text, phone_text, pass_text, repass_text, firstname_text, lastname_text;

        username_text   = username.getText().toString();
        firstname_text  = firstname.getText().toString();
        lastname_text   = lastname.getText().toString();
        phone_text      = phone.getText().toString();
        pass_text       = password.getText().toString();
        repass_text     = repassword.getText().toString();

        boolean validUsername   = RegexValidator.validateName(username_text);
        boolean validFirstName  = RegexValidator.validateName(firstname_text);
        boolean validLastName   = RegexValidator.validateName(lastname_text);
        boolean validPhone      = RegexValidator.validatePhoneNumber(phone_text);
        boolean validPassword   = RegexValidator.validatePassword(pass_text);
        boolean passwordsMatch  = pass_text.equals(repass_text);


        if(!validUsername){
            username.setError("Invalid UserName");
        }

        if(!validPhone){
            phone.setError("Invalid Phone number");
        }

        if(!validPassword){
            password.setError("Invalid");
        }
        if(!passwordsMatch){
            repassword.setError("Doesn't Match");
        }

        if(!validFirstName){
            ipl_firstname.setError("Invalid Name");
        }

        if(!validFirstName){
            ipl_lastname.setError("Invalid Name");
        }


        if(validUsername && validPassword && validPhone && passwordsMatch && validFirstName && validLastName){
            // TODO
            //call async task to register the user
            RequestParams request = new RequestParams();
            request.setUri("http://localhost:3000/register");
            request.setParam("username", username_text);
            request.setParam("password", pass_text);
            request.setParam("phonenumber", phone_text);
            request.setParam("firstname", firstname_text);
            request.setParam("lastname", lastname_text);

            Log.v(TAG, "Request being sent with params " + request.getUri() + request.getParams().toString());


            if(ConnectionManager.isConnected(SignUp.this)){
                new AsyncSignUp().execute(request);
            }else{
                Snackbar.make(findViewById(R.id.sign_up_coordinator), "No Internet connection", Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    private class AsyncSignUp extends AsyncTask<RequestParams, Void, HttpResponse>{
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(SignUp.this );
            progressDialog.setTitle("Registering");
            progressDialog.show();
        }

        protected HttpResponse doInBackground(RequestParams... params) {

            HttpResponse response = null;
            try {
                 response = HttpManager.postData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.v(TAG, "Do background, async call goes to the server");
            return response;
        }

        protected void onPostExecute(HttpResponse result) {

            progressDialog.dismiss();
            if(result.getStatusCode() == 200){
                Snackbar.make(findViewById(R.id.sign_up_coordinator), "Successfully registered", Snackbar.LENGTH_LONG).show();
                Log.v(TAG, "Result is "+ result.getBody());
                finish();
            }else
            {
                Snackbar.make(findViewById(R.id.sign_up_coordinator), "Error! Couldn't register", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
