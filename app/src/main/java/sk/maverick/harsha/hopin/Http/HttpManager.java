package sk.maverick.harsha.hopin.Http;


import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class HttpManager {

    protected final static String TAG = "HTTPManager";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static String getData(RequestParams requestParams) throws IOException {

        String uri = requestParams.getUri();

        Request request = new Request.Builder()
                .url(uri)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        return response.body().toString();
    }


    public static String postData(RequestParams requestParams) throws IOException{

        String uri = requestParams.getUri();
        String data = requestParams.getParams().toString();
        RequestBody requestBody = RequestBody.create(JSON, data);

        Request request = new Request.Builder()
                .url(uri)
                .post(requestBody)
                .build();
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
    }
}
