package sk.maverick.harsha.hopin.Http;


import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sk.maverick.harsha.hopin.App;
import sk.maverick.harsha.hopin.Util.SharedPrefs;

public class HttpManager {

    private final static String TAG = "HTTPManager";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static HttpResponse getData(RequestParams requestParams) throws IOException {

        HttpResponse httpResponse = new HttpResponse();

        String uri = requestParams.getUri();
        Log.v(TAG, "The URL is " + uri);

        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Token", (String) SharedPrefs.getStringValue(App.getAppContext(),"token"))
                .build();
        Response response = okHttpClient.newCall(request).execute();

        httpResponse.setStatusCode(response.code());
        httpResponse.setBody(response.body().string());
        return httpResponse;
    }


    public static HttpResponse putData(RequestParams requestParams) throws IOException {

        HttpResponse httpResponse = new HttpResponse();

        String uri = requestParams.getUri();
        Log.v(TAG, "The URL is " + uri);
        HashMap map = (HashMap) requestParams.getParams();
        Log.v(TAG, "MAP to string is  "+ map.toString());

        JSONObject data = null;
        data = new JSONObject(map);

        RequestBody requestBody = RequestBody.create(JSON, data.toString());

        Request request = new Request.Builder()
                .url(uri)
                .put(requestBody)
                .addHeader("Token", (String) SharedPrefs.getStringValue(App.getAppContext(),"token"))
                .build();
        Response response = okHttpClient.newCall(request).execute();

        httpResponse.setStatusCode(response.code());
        httpResponse.setBody(response.body().string());
        return httpResponse;
    }


    public static HttpResponse postData(RequestParams requestParams) throws IOException{

        HttpResponse httpResponse = new HttpResponse();
        String uri = requestParams.getUri();
/*
        String params = requestParams.getParams().toString();
*/
        HashMap map = (HashMap) requestParams.getParams();
        Log.v(TAG, "MAP to string is  "+ map.toString());


        JSONObject data = null;
        data = new JSONObject(map);

        RequestBody requestBody = RequestBody.create(JSON, data.toString());

        Log.v(TAG, "Json data is "+ data.toString());
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Token", (String) SharedPrefs.getStringValue(App.getAppContext(),"token"))
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();

        httpResponse.setStatusCode(response.code());
        httpResponse.setBody(response.body().string());

        return httpResponse;
    }
}
