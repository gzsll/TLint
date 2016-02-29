package com.gzsll.hupu.api.cookie;

import com.gzsll.hupu.support.storage.bean.CookieResult;

import org.apache.log4j.Logger;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * Created by sll on 2015/3/8.
 */
public class CookieApi {
    Logger logger = Logger.getLogger("RetrofitLoginApi");
    private CookieService mCookieService;


    private static final String BASE_URL = "http://passport.hupu.com/pc/login/";

    public CookieApi(OkHttpClient mOkHttpClient) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(mOkHttpClient).baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mCookieService = retrofit.create(CookieService.class);
    }


    public Observable<CookieResult> login(String userName, String passWord) {
        return mCookieService.login(userName, passWord).subscribeOn(Schedulers.io());
    }
}
