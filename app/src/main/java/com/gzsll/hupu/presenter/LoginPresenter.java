package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.login.LoginApi;
import com.gzsll.hupu.api.thread.ThreadApi;
import com.gzsll.hupu.otto.LoginSuccessEvent;
import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.support.db.UserDao;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.LoginData;
import com.gzsll.hupu.support.storage.bean.LoginResult;
import com.gzsll.hupu.support.utils.SecurityHelper;
import com.gzsll.hupu.view.LoginView;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sll on 2015/3/8.
 */
public class LoginPresenter extends Presenter<LoginView> {


    @Inject
    LoginApi loginAPi;
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


    public void login(String userName, String passWord) {
        view.showLoading();
        loginAPi.login(userName, mSecurityHelper.getMD5(passWord), new Callback<LoginResult>() {
            @Override
            public void success(LoginResult loginResult, Response response) {
                if (loginResult != null) {
                    if (loginResult.is_login == 1) {
                        LoginData data = loginResult.result;
                        User user = new User();
                        user.setUid(data.uid);
                        user.setToken(data.token);
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

            @Override
            public void failure(RetrofitError error) {
                view.hideLoading();
                view.showToast("登录失败，请检查您的网络");
            }
        });
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
