package ru.mrcolt.anidubmobile.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    public interface OKHttpNetwork {
        void onSuccess(String body);

        void onFailure(IOException e);
    }

    public void getAPIRequest(String url, final OKHttpNetwork okHttpCallBack) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0")
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okHttpCallBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    okHttpCallBack.onSuccess(response.body().string());
                }
            }
        });
    }

    public void getAnidubRequest(String url, final OKHttpNetwork okHttpCallBack) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0")
                .addHeader("Referer", "https://anime.anidub.com/")
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                okHttpCallBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    okHttpCallBack.onSuccess(response.body().string());
                }
            }
        });
    }
}
