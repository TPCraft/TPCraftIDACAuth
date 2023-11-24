package net.tpcraft.minecraft.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class HTTPRequest {
    public static String sendGET(String url, Map<String, String> headers) {
        OkHttpClient client = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        Request request = requestBuilder.url(url)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                return responseBody;
            } else {
                return "ERROR";
            }
        } catch (IOException e) {
            return "ERROR";
        }
    }

    public static String sendPOST(String url, Map<String, String> headers, Map<String, String> data) {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder formBuilder = new FormBody.Builder();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }

        RequestBody requestBody = formBuilder.build();

        Request.Builder requestBuilder = new Request.Builder();

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        Request request = requestBuilder.url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                return responseBody;
            } else {
                return "ERROR";
            }
        } catch (IOException e) {
            return "ERROR";
        }
    }
}
