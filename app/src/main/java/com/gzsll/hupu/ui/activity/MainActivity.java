package com.gzsll.hupu.ui.activity;

import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.AppManager;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.UpdateAgent;
import com.gzsll.hupu.otto.ChangeThemeEvent;
import com.gzsll.hupu.otto.LoginSuccessEvent;
import com.gzsll.hupu.otto.ReceiveNoticeEvent;
import com.gzsll.hupu.support.storage.bean.Notice;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.ui.fragment.BoardListFragment_;
import com.gzsll.hupu.ui.fragment.TopicFragment_;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import javax.inject.Inject;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    Logger logger = Logger.getLogger(MainActivity.class.getSimpleName());


    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    @ViewById
    DrawerLayout drawerLayout;
    @ViewById
    Toolbar toolbar;
    @ViewById
    SimpleDraweeView ivIcon;
    @ViewById
    TextView tvName, tvNotification;


    @Inject
    SettingPrefHelper mSettingPrefHelper;
    @Inject
    UpdateAgent mUpdateAgent;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.nav_my));
        //创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerContent();
        Fragment fragment = BoardListFragment_.builder().id(0).build();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

        initUserInfo();
        if (mSettingPrefHelper.getAutoUpdate()) {
            mUpdateAgent.checkUpdate(this);
        }
    }

    private void initUserInfo() {
        ivIcon.setImageURI(Uri.parse(getUser().getIcon()));
        tvName.setText(getUser().getUserName());
    }


    private void setupDrawerContent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int groupId = 0;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_nba:
                                groupId = 1;
                                break;
                            case R.id.nav_my:
                                groupId = 0;
                                break;
                            case R.id.nav_topic:
                                groupId = Constants.NAV_TOPIC_LIST;
                                break;
                            case R.id.nav_fav:
                                groupId = Constants.NAV_TOPIC_FAV;
                                break;
                            case R.id.nav_cba:
                                groupId = 2;
                                break;
                            case R.id.nav_gambia:
                                groupId = 7;
                                break;
                            case R.id.nav_equipment:
                                groupId = 3;
                                break;
                            case R.id.nav_fitness:
                                groupId = 8;
                                break;
                            case R.id.nav_football:
                                groupId = 4;
                                break;
                            case R.id.nav_sport:
                                groupId = 6;
                                break;
                            case R.id.nav_race:
                                groupId = 5;
                                break;
                            case R.id.nav_setting:
                                groupId = Constants.NAV_SETTING;
                                break;
                            case R.id.nav_feedback:
                                groupId = Constants.NAV_FEEDBACK;
                                break;
                            case R.id.nav_about:
                                groupId = Constants.NAV_ABOUT;
                                break;
                        }
                        Fragment fragment;
                        if (groupId >= 1000) {
                            if (groupId == Constants.NAV_SETTING) {
                                SettingActivity_.intent(MainActivity.this).start();
                            } else if (groupId == Constants.NAV_FEEDBACK) {
                                PostActivity_.intent(MainActivity.this).type(Constants.TYPE_FEEDBACK).groupThreadId("2869008").start();

                            } else {
                                AboutActivity_.intent(MainActivity.this).start();
                                // getSupportFragmentManager().beginTransaction().replace(R.id.content, NewsFragment_.builder().build()).commit();
                            }

                        } else {
                            if (groupId >= 0) {
                                fragment = BoardListFragment_.builder().id(groupId).build();
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                            } else {
                                if (isLogin()) {
                                    fragment = TopicFragment_.builder().type(groupId).uid(getUser().getUid()).build();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                                } else {
                                    LoginActivity_.intent(MainActivity.this).start();
                                }
                            }
                            menuItem.setChecked(true);
                            setTitle(menuItem.getTitle());
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }


    @Subscribe
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        reload();
    }

    @Subscribe
    public void onChangeThemeEvent(ChangeThemeEvent event) {
        reload();
    }

    @Subscribe
    public void onReceiveNoticeEvent(ReceiveNoticeEvent event) {
        Notice notice = event.getNotice();
        tvNotification.setVisibility(View.VISIBLE);
        tvNotification.setText(String.valueOf(notice.getNewNum()));
        Toast.makeText(this, notice.getNewMsg(), Toast.LENGTH_SHORT).show();

    }


    @Click
    void llAccount() {

    }


    @Click
    void ivCover() {
        NoticeActivity_.intent(this).start();
        drawerLayout.closeDrawers();
    }


    @Override
    public void onBackPressed() {
        if (isCanExit()) {
            AppManager.getAppManager().AppExit(this);
        }
    }

    private long mExitTime = 0;

    public boolean isCanExit() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }


}
