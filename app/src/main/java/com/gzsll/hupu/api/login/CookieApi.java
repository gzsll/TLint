package com.gzsll.hupu.api.login;

import com.gzsll.hupu.bean.CookieData;
import com.gzsll.hupu.components.retrofit.FastJsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2015/3/8.
 */
public class CookieApi {
    private CookieService mCookieService;

    private static final String BASE_URL = "http://passport.hupu.com/pc/login/";

    public CookieApi(OkHttpClient mOkHttpClient) {
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(FastJsonConverterFactory.create())
                        .client(mOkHttpClient)
                        .baseUrl(BASE_URL)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
        mCookieService = retrofit.create(CookieService.class);
    }

    public Observable<CookieData> login(String userName, String passWord) {
        return mCookieService.login(userName, passWord).subscribeOn(Schedulers.io());
    }
}
