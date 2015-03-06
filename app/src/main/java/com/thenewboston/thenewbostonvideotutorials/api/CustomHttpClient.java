package com.thenewboston.thenewbostonvideotutorials.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class CustomHttpClient {

    public static final String THENEWBOSTON_PUBLIC_API_KEY = "";
    public static final String API_BASE_URL = "https://www.thenewboston.com";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static RequestHandle get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (params == null)
            params = new RequestParams();
        return client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String url) {
        return API_BASE_URL + url + "&TOKEN=" + THENEWBOSTON_PUBLIC_API_KEY;
    }


}
