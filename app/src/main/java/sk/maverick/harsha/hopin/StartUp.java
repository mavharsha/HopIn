package sk.maverick.harsha.hopin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class StartUp extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private static final String TAG = "CREATEEVENT";
    public static final String MyPREFERENCES = "MyPrefs" ;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Boolean isLoggedIn = prefs.getBoolean("isloggedin", false);

        Log.v(TAG, "Logged in? "+ isLoggedIn);

        if(isLoggedIn){
             intent = new Intent(StartUp.this, Home.class);
        }else
        {
             intent = new Intent(StartUp.this, Login.class);
        }


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
