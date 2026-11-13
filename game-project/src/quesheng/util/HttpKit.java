/*
 * Decompiled with CFR 0.152.
 */
package quesheng.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import quesheng.util.param;

public final class HttpKit {
    private static final HashMap<String, List<Cookie>> cookieStore = new HashMap();

    public static void Post(String url, Map<String, String> map, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (null != map) {
            map.entrySet().stream().forEach(entry -> builder.add((String)entry.getKey(), (String)entry.getValue()));
        }
        FormBody body = builder.build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar(){

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                cookieStore.put(httpUrl.host(), list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = (List)cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList();
            }
        }).build();
        Request request = new Request.Builder().url(url).post(body).header("Authorization", "Bearer " + param.jwt).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void get(String url, Map<String, String> map, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        if (null != map) {
            map.entrySet().stream().forEach(entry -> builder.add((String)entry.getKey(), (String)entry.getValue()));
        }
        FormBody body = builder.build();
        Request request = new Request.Builder().url(url).put(body).build();
        client.newCall(request).enqueue(callback);
    }
}

