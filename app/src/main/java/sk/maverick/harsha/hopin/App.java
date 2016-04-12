/*
 * Copyright (c)
 * Sree Harsha Mamilla
 * Pasyanthi
 * github/mavharsha
 *
 */

package sk.maverick.harsha.hopin;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import net.danlew.android.joda.JodaTimeAndroid;

import okhttp3.OkHttpClient;

/**
 * Created by Harsha on 2/14/2016.
 */
public class App extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();

        Stetho.initializeWithDefaults(this);
        JodaTimeAndroid.init(this);

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    public static String getIp(){
        return "http://localhost:3000/";
    }

    public static Context getAppContext() {
        return App.context;
    }
}
