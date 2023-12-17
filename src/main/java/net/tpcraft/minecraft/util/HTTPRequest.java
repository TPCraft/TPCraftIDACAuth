package net.tpcraft.minecraft.util;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class HTTPRequest {
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM_MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private static final OkHttpClient client = new OkHttpClient();

    public static String sendDeleteRequest(String url, Map<String, String> headers, String data, Map<String, String> params) {
        Request.Builder requestBuilder = new Request.Builder().url(buildUrlWithParams(url, params)).delete();

        addHeaders(requestBuilder, headers);

        if (data != null) {
            requestBuilder = requestBuilder.method("DELETE", RequestBody.create(JSON_MEDIA_TYPE, data));
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public static String sendGetRequest(String url, Map<String, String> headers, Map<String, String> params) {
        Request.Builder requestBuilder = new Request.Builder().url(buildUrlWithParams(url, params)).get();

        addHeaders(requestBuilder, headers);

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public static String sendPostRequest(String url, Map<String, String> headers, String data, Map<String, String> params, boolean isJson) {
        Request.Builder requestBuilder = new Request.Builder().url(buildUrlWithParams(url, params));

        addHeaders(requestBuilder, headers);

        if (isJson) {
            requestBuilder = requestBuilder.post(RequestBody.create(JSON_MEDIA_TYPE, data));
        } else {
            requestBuilder = requestBuilder.post(RequestBody.create(FORM_MEDIA_TYPE, data));
        }

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public static String sendPutRequest(String url, Map<String, String> headers, String data, Map<String, String> params) {
        Request.Builder requestBuilder = new Request.Builder().url(buildUrlWithParams(url, params)).put(RequestBody.create(JSON_MEDIA_TYPE, data));

        addHeaders(requestBuilder, headers);

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            return "ERROR";
        }
    }

    private static void addHeaders(Request.Builder requestBuilder, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static String buildUrlWithParams(String url, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
            return urlBuilder.build().toString();
        } else {
            return url;
        }
    }
}