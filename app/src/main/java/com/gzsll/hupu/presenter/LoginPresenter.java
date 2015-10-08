package com.gzsll.hupu.presenter;

import com.gzsll.hupu.api.hupu.HuPuApi;
import com.gzsll.hupu.api.login.LoginAPi;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.otto.LoginSuccessEvent;
import com.gzsll.hupu.storage.UserStorage;
import com.gzsll.hupu.storage.bean.LoginResult;
import com.gzsll.hupu.storage.bean.UserInfo;
import com.gzsll.hupu.storage.bean.UserResult;
import com.gzsll.hupu.utils.SecurityHelper;
import com.gzsll.hupu.view.LoginView;
import com.squareup.otto.Bus;

import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * Created by sll on 2015/3/8.
 */
public class LoginPresenter extends Presenter<LoginView> {


    @Inject
    LoginAPi loginAPi;
    @Inject
    HuPuApi huPuApi;
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
        loginAPi.login(userName, mSecurityHelper.getMD5(passWord), new LoginAPi.LoginCallBack() {
            @Override
            public void onFinish(LoginResult result) {
                if (result != null && result.result != null) {
                    User user = result.result.user;
                    user.setIsLogin(true);
                    getUserInfo(user);
                    return;
                }
                view.hideLoading();
                if (result != null && result.error != null) {
                    view.showToast(result.error.text);
                } else {
                    view.showToast("登录失败，请检查您的网络");
                }
            }

            @Override
            public void onError(String errorMessage) {
                view.hideLoading();
                view.showToast("登录失败，请检查您的网络");
            }
        });
    }

    private void getUserInfo(final User user) {
        huPuApi.getUserInfo(user.getUid(), new retrofit.Callback<UserResult>() {
            @Override
            public void success(UserResult userResult, retrofit.client.Response response) {
                view.hideLoading();
                if (userResult != null && userResult.getStatus() == 200) {
                    UserInfo userInfo = userResult.getData();
                    user.setIcon(userInfo.getIcon());
                    user.setSex(userInfo.getSex());
                    user.setSyncTime(userInfo.getSynctime());
                    user.setLevel(userInfo.getLevel());
                    mUserStorage.setUser(user);
                    insertOrUpdateUser(user);
                    bus.post(new LoginSuccessEvent());
                    view.showToast("登录成功");
                    view.loginSuccess();
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
