package com.gzsll.hupu.ui.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.UserProfilePresenter;
import com.gzsll.hupu.support.storage.bean.UserInfo;
import com.gzsll.hupu.support.utils.SystemBarTintManager;
import com.gzsll.hupu.ui.fragment.TopicFragment_;
import com.gzsll.hupu.view.UserProfileView;
import com.gzsll.hupu.widget.ProfileScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/9/14.
 * 个人中心
 */
@EActivity(R.layout.activity_profile)
public class UserProfileActivity extends BaseSwipeBackActivity implements UserProfileView, SwipeRefreshLayout.OnRefreshListener {

    @Extra
    String uid;
    @ViewById
    ImageView ivCover;
    @ViewById
    View viewBgDes;
    @ViewById
    SimpleDraweeView ivPhoto, ivPost, ivReply, ivLight;
    @ViewById
    TextView tvUserName;
    @ViewById
    ImageView ivGender;
    @ViewById
    RelativeLayout layDesc;
    @ViewById
    RelativeLayout layTop;
    @ViewById
    TabLayout tabs;
    @ViewById
    ViewPager pager;
    @ViewById
    ProfileScrollView scrollView;
    @ViewById
    SwipeRefreshLayout refreshLayout;
    @ViewById
    View viewToolbar;
    @ViewById
    Toolbar toolbar;
    @ViewById
    RelativeLayout layToolbar;
    private MaterialDialog mDialog;


    @Inject
    UserProfilePresenter mUserProfilePresenter;

    @AfterViews
    void init() {
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        initToolBar(toolbar);
        setTitle("");
        SystemBarTintManager manager = new SystemBarTintManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            layToolbar.setPadding(layToolbar.getPaddingLeft(),
                    layToolbar.getPaddingTop() + manager.getConfig().getStatusBarHeight(),
                    layToolbar.getPaddingRight(),
                    layToolbar.getPaddingBottom());

            viewToolbar.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) + manager.getConfig().getStatusBarHeight()));
        }


        mUserProfilePresenter.setView(this);
        mUserProfilePresenter.initialize();
        mDialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("正在加载...").cancelable(false)
                .progress(true, 0).build();
        refreshLayout.setOnRefreshListener(this);
        mUserProfilePresenter.receiveUserInfo(uid);


    }

    private void setupViewPager(UserInfo userInfo) {
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        adapter.addFragment(TopicFragment_.builder().uid(uid).type(Constants.NAV_TOPIC_LIST).build(), String.format("发帖(%d)", userInfo.getPostNum()));
        adapter.addFragment(TopicFragment_.builder().uid(uid).type(Constants.NAV_TOPIC_FAV).build(), String.format("收藏(%d)", userInfo.getFavoriteNum()));
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public void onRefresh() {
        mUserProfilePresenter.receiveUserInfo(uid);
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
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return false;
    }

    @Override
    public void renderUserInfo(UserInfo userInfo) {
        refreshLayout.setRefreshing(false);
        setupViewPager(userInfo);
        scrollView.setUser(userInfo);
        ivPhoto.setImageURI(Uri.parse(userInfo.getIcon()));
        tvUserName.setText(userInfo.getUsername());
        ivPost.setImageURI(Uri.parse(userInfo.getBadge().getSmall().get(0)));
        ivReply.setImageURI(Uri.parse(userInfo.getBadge().getSmall().get(1)));
        ivLight.setImageURI(Uri.parse(userInfo.getBadge().getSmall().get(2)));
        ivGender.setImageResource(userInfo.getSex() == 0 ? R.drawable.list_male : R.drawable.list_female);
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
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
