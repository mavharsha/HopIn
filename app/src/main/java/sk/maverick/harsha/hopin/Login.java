package sk.maverick.harsha.hopin;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private EditText username, password;
    private TextView login_textView, signup_textView;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

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
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setError("Invalid username");
                password.setError("Invalid password");
            }
        } );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
