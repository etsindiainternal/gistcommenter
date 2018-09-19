package util;


import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestHandler {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    //get data request
    public String getData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(Constants.DEV_AUTH_USERNAME, new Constants().decodePass(Constants.DEV_AUTH_PASS)))
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    //post data request with basic Auth
    public String doPostRequest(String url, String json, String uname, String upass) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", Credentials.basic(uname, upass))
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }



}
