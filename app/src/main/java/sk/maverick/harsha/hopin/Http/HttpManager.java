package sk.maverick.harsha.hopin.Http;


import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HttpManager {

    protected final static String TAG = "HTTPManager";
    static OkHttpClient okHttpClient = new OkHttpClient();

    public static String getData(String uri) throws IOException {
        Request request = new Request.Builder()
                .url(uri)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().toString();
    }


    public static String postData(String uri, String postdata) throws IOException{

        RequestBody formbody =  new FormEncodingBuilder()
                .add("username", "")
                .add("password", "")
                .build();

        Request request = new Request.Builder()
                .url(uri)
                .post(formbody)
                .build();
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
    }
}
