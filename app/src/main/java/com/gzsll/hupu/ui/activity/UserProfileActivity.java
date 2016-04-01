package com.gzsll.hupu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.UserResult;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.presenter.UserProfilePresenter;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.fragment.BrowserFragment;
import com.gzsll.hupu.ui.view.UserProfileView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 * 个人中心
 */
public class UserProfileActivity extends BaseSwipeBackActivity implements UserProfileView, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.ivCover)
    ImageView ivCover;
    @Bind(R.id.ivPhoto)
    SimpleDraweeView ivPhoto;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.toolbarLayout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.maincontent)
    CoordinatorLayout maincontent;
    @Bind(R.id.ivGender)
    ImageView ivGender;
    @Bind(R.id.tvRegisterTime)
    TextView tvRegisterTime;

    public static void startActivity(Context mContext, String uid) {
        Intent intent = new Intent(mContext, UserProfileActivity.class);
        intent.putExtra("uid", uid);
        mContext.startActivity(intent);
    }


    @Inject
    UserProfilePresenter mPresenter;
    @Inject
    SettingPrefHelper mSettingPrefHelper;

    private MaterialDialog mDialog;
    private String uid;


    @Override
    public int initContentView() {
        return R.layout.activity_profile;
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
        toolbarLayout.setTitleEnabled(false);
        mDialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("正在加载").cancelable(false)
                .progress(true, 0).build();
        uid = getIntent().getStringExtra("uid");
        mPresenter.receiveUserInfo(uid);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    @Override
    public void renderUserData(UserResult userResult) {
        if (userResult != null) {
            setupViewPager(userResult);
            if (!TextUtils.isEmpty(userResult.header)) {
                ivPhoto.setImageURI(Uri.parse(userResult.header));
            }
            ivGender.setImageResource(userResult.gender == 0 ? R.drawable.list_male : R.drawable.list_female);
            tvRegisterTime.setText(userResult.reg_time_str);
            setTitle(userResult.nickname);
        }
    }


    private void setupViewPager(UserResult userResult) {
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        adapter.addFragment(BrowserFragment.newInstance(userResult.bbs_msg_url, ""), String.format("发帖(%s)", userResult.bbs_msg_count));
        adapter.addFragment(BrowserFragment.newInstance(userResult.bbs_post_url, ""), String.format("回帖(%s)", userResult.bbs_post_count));
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void showLoading() {
        if (!mDialog.isShowing() && !isFinishing()) {
            mDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mDialog.isShowing() && !isFinishing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showError() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onRefresh() {

    }

}
