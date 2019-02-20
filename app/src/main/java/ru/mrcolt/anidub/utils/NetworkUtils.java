package ru.mrcolt.anidub.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    private RequestQueue queue;

    public void sendGETRequest(Context context, String url, Map<String, String> headers,
                               final httpNetwork okHttpCallBack) {
        queue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                response -> okHttpCallBack.onSuccess(response),
                error -> okHttpCallBack.onFailure(error.toString())
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
                params.putAll(headers);
                return params;
            }
        };
        queue.add(getRequest);
    }

    public interface httpNetwork {
        void onSuccess(String body);

        void onFailure(String e);
    }
}
