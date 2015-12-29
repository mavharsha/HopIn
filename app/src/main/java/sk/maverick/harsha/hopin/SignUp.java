/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    private EditText username, phone, password, repassword;
    private Button register;
    private TextView signup,oldUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

    private void init(){
        username    = (EditText) findViewById(R.id.sign_up_username_et);
        phone       = (EditText) findViewById(R.id.sign_up_phone_et);
        password    = (EditText) findViewById(R.id.sign_up_password_et);
        repassword  = (EditText) findViewById(R.id.sign_up_repassword_et);
        oldUser     = (TextView) findViewById(R.id.sign_up_old_user_tv);
        signup      = (TextView) findViewById(R.id.sign_up_text_view);
        register    = (Button) findViewById(R.id.sign_up_register_btn);

        oldUser.setPaintFlags(oldUser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(oldUser, Linkify.ALL);

        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        username.setTypeface(roboto_light);
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

    }

}
