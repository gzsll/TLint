package com.gzsll.hupu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.LoginPresenter;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.view.LoginView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/10.
 */
public class LoginActivity extends BaseSwipeBackActivity implements LoginView {


    public static void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }


    @Inject
    LoginPresenter mPresenter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.etUserName)
    EditText etUserName;
    @Bind(R.id.textInputUserName)
    TextInputLayout textInputUserName;
    @Bind(R.id.etPassWord)
    EditText etPassWord;
    @Bind(R.id.textInputPassword)
    TextInputLayout textInputPassword;


    private MaterialDialog dialog;

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        initToolBar(toolbar);
        setTitle("登录");
        dialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("登录中")
                .progress(true, 0).build();
        etUserName.addTextChangedListener(new MTextWatcher(textInputUserName));
        etPassWord.addTextChangedListener(new MTextWatcher(textInputPassword));
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    public void showLoading() {
        if (!isFinishing() && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (!isFinishing() && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void loginSuccess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.startActivity(LoginActivity.this);
                finish();
            }
        }, 1500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.login) {
            doLogin();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void doLogin() {
        String mUserName = etUserName.getText().toString().trim();
        String mPassword = etPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(mUserName)) {
            textInputUserName.setError("请输入用户名");
            textInputUserName.setErrorEnabled(true);
        } else if (TextUtils.isEmpty(mPassword)) {
            textInputPassword.setError("请输入密码");
            textInputPassword.setErrorEnabled(true);
        } else {
            mPresenter.login(mUserName, mPassword);
        }
    }


    class MTextWatcher implements TextWatcher {

        TextInputLayout textInputLayout;

        public MTextWatcher(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void afterTextChanged(Editable arg0) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            textInputLayout.setErrorEnabled(false);
        }

    }
}
