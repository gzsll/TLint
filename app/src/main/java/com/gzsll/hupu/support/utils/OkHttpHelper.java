package com.gzsll.hupu.support.utils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;

/**
 * Created by sll on 2015/3/10.
 */
public class OkHttpHelper {
    private OkHttpClient mOkHttpClient;

    public OkHttpHelper(OkHttpClient mOkHttpClient) {
        this.mOkHttpClient = mOkHttpClient;
    }

    /**
     * 该不会开启异步线程。
     *
     * @param request
     * @return
     * @throws IOException
     */
    public Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public void enqueue(Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    public String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

<<<<<<< HEAD:app/src/main/java/com/gzsll/hupu/utils/OkHttpHelper.java
    private static final String CHARSET_NAME = "UTF-8";

=======
>>>>>>> origin/develop:app/src/main/java/com/gzsll/hupu/support/utils/OkHttpHelper.java


    public void httpDownload(String url, File target) throws Exception {
        Request request = new Request.Builder().url(url).build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            BufferedSink sink = Okio.buffer(Okio.sink(target));
            sink.writeAll(response.body().source());
            sink.close();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

}
