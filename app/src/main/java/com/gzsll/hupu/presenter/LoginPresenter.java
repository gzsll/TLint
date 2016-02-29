package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.cookie.CookieApi;
import com.gzsll.hupu.api.login.LoginApi;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.otto.LoginSuccessEvent;
import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.support.db.UserDao;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.CookieResult;
import com.gzsll.hupu.support.storage.bean.LoginData;
import com.gzsll.hupu.support.storage.bean.LoginResult;
import com.gzsll.hupu.support.utils.SecurityHelper;
import com.gzsll.hupu.view.LoginView;
import com.squareup.otto.Bus;

import org.apache.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by sll on 2015/3/8.
 */
public class LoginPresenter extends Presenter<LoginView> {


    Logger logger = Logger.getLogger(LoginPresenter.class.getSimpleName());

    @Inject
    LoginApi mLoginApi;
    @Inject
    CookieApi mCookieApi;

    @Inject
    ThreadApi threadApi;
    @Inject
    Bus bus;
    @Inject
    UserDao mUserDao;
    @Inject
    SecurityHelper mSecurityHelper;
    @Inject
    UserStorage mUserStorage;


    public void login(final String userName, final String passWord) {
        view.showLoading();
        mCookieApi.login(userName, passWord).flatMap(new Func1<CookieResult, Observable<LoginResult>>() {
            @Override
            public Observable<LoginResult> call(CookieResult result) {
                if (result.code == 1000) {
                    return mLoginApi.login(userName, mSecurityHelper.getMD5(passWord));
                } else {
                    return null;
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<LoginResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LoginResult loginResult) {
                if (loginResult != null) {
                    if (loginResult.is_login == 1) {
                        LoginData data = loginResult.result;
                        User user = new User();
                        user.setUid(data.uid);
                        user.setToken(data.token);
                        user.setCookie(mUserStorage.getCookie());
                        user.setUserName(data.username);
                        user.setIsLogin(true);
                        mUserStorage.setUser(user);
                        insertOrUpdateUser(user);
                        bus.post(new LoginSuccessEvent());
                        view.showToast("登录成功");
                        view.loginSuccess();
                    } else {
                        view.hideLoading();
                        view.showToast("登录失败，请检查您的网络");
                    }
                }
            }
        });

//        loginAPi.login(userName, mSecurityHelper.getMD5(passWord), new Callback<LoginResult>() {
//            @Override
//            public void success(LoginResult loginResult, Response response) {
//                if (loginResult != null) {
//                    if (loginResult.is_login == 1) {
//                        LoginData data = loginResult.result;
//                        User user = new User();
//                        user.setUid(data.uid);
//                        user.setToken(data.token);
//                        user.setUserName(data.username);
//                        user.setIsLogin(true);
//                        mUserStorage.setUser(user);
//                        insertOrUpdateUser(user);
//                        bus.post(new LoginSuccessEvent());
//                        view.showToast("登录成功");
//                        view.loginSuccess();
//                    } else {
//                        view.hideLoading();
//                        view.showToast("登录失败，请检查您的网络");
//                    }
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                view.hideLoading();
//                view.showToast("登录失败，请检查您的网络");
//            }
//        });
    }

    private void insertOrUpdateUser(User user) {
        List<User> users = mUserDao.queryBuilder().where(UserDao.Properties.Uid.eq(user.getUid())).list();
        if (!users.isEmpty()) {
            user.setId(users.get(0).getId());
        }
        mUserDao.insertOrReplace(user);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
