package com.gzsll.hupu.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/10.
 */
public class LoginActivity extends BaseSwipeBackActivity implements LoginContract.View {

    public static void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

    @Inject
    LoginPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.textInputUserName)
    TextInputLayout textInputUserName;
    @BindView(R.id.etPassWord)
    EditText etPassWord;
    @BindView(R.id.textInputPassword)
    TextInputLayout textInputPassword;

    private MaterialDialog dialog;

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initInjector() {
        DaggerLoginComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        initToolBar(toolbar);
        setTitle("登录");
        dialog = new MaterialDialog.Builder(this).title("提示").content("登录中").progress(true, 0).build();
        etUserName.addTextChangedListener(new MTextWatcher(textInputUserName));
        etPassWord.addTextChangedListener(new MTextWatcher(textInputPassword));
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
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
    public void showUserNameError(String error) {
        textInputUserName.setError(error);
        textInputUserName.setErrorEnabled(true);
    }

    @Override
    public void showPassWordError(String error) {
        textInputPassword.setError(error);
        textInputPassword.setErrorEnabled(true);
    }

    @Override
    public void loginSuccess() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
        mPresenter.login(mUserName, mPassword);
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
