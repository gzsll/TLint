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

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.AppManager;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.UpdateAgent;
import com.gzsll.hupu.otto.ChangeThemeEvent;
import com.gzsll.hupu.otto.LoginSuccessEvent;
import com.gzsll.hupu.otto.NotificationEvent;
import com.gzsll.hupu.otto.ReceiveNoticeEvent;
import com.gzsll.hupu.presenter.NotificationPresenter;
import com.gzsll.hupu.support.db.User;
import com.gzsll.hupu.support.db.UserDao;
import com.gzsll.hupu.support.storage.UserStorage;
import com.gzsll.hupu.support.storage.bean.Notice;
import com.gzsll.hupu.support.utils.SettingPrefHelper;
import com.gzsll.hupu.ui.fragment.BoardListFragment_;
import com.gzsll.hupu.ui.fragment.ThreadRecommendFragment_;
import com.gzsll.hupu.ui.fragment.TopicFragment_;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {

    Logger logger = Logger.getLogger(MainActivity.class.getSimpleName());


    @ViewById(R.id.nav_view)
    NavigationView navigationView;
    @ViewById
    DrawerLayout drawerLayout;
    @ViewById
    Toolbar toolbar;
    SimpleDraweeView ivIcon;
    TextView tvName, tvNotification;


    @Inject
    SettingPrefHelper mSettingPrefHelper;
    @Inject
    UpdateAgent mUpdateAgent;
    @Inject
    NotificationPresenter mNotificationPresenter;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("帖子推荐");

        ivIcon = (SimpleDraweeView) navigationView.getHeaderView(0).findViewById(R.id.ivIcon);
        tvName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvName);
        tvNotification = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvNotification);
        navigationView.getHeaderView(0).findViewById(R.id.ivCover).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.llAccount).setOnClickListener(this);
        //创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerContent();
        Fragment fragment = ThreadRecommendFragment_.builder().build();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

        initUserInfo();
        if (mSettingPrefHelper.getAutoUpdate()) {
            mUpdateAgent.checkUpdate(this);
        }

    }

    private void initUserInfo() {
        ivIcon.setImageURI(Uri.parse("placeholder"));
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
                                groupId = 232;
                                break;
                            case R.id.nav_gambia:
                                groupId = 174;
                                break;
                            case R.id.nav_equipment:
                                groupId = 233;
                                break;
                            case R.id.nav_fitness:
                                groupId = 234;
                                break;
                            case R.id.nav_football:
                                groupId = 4596;
                                break;
                            case R.id.nav_intel_football:
                                groupId = 198;
                                break;
                            case R.id.nav_sport:
                                groupId = 41;
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
                            case R.id.nav_recommend:
                                groupId = Constants.NAV_THREAD_RECOMMEND;
                                break;
                        }
                        Fragment fragment;
                        if (groupId >= 10000) {
                            if (groupId == Constants.NAV_SETTING) {
                                SettingActivity_.intent(MainActivity.this).start();
                            } else if (groupId == Constants.NAV_FEEDBACK) {
                                PostActivity_.intent(MainActivity.this).type(Constants.TYPE_FEEDBACK).tid("2869008").start();

                            } else {
                                BrowserActivity_.intent(MainActivity.this).url("http://www.pursll.com/TLint").start();
                            }

                        } else {
                            if (groupId >= 0) {
                                fragment = BoardListFragment_.builder().id(groupId).build();
                            } else {
                                if (groupId != Constants.NAV_THREAD_RECOMMEND) {
                                    fragment = TopicFragment_.builder().type(groupId).uid(getUser().getUid()).build();
                                } else {
                                    fragment = ThreadRecommendFragment_.builder().build();
                                }
                            }
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                            menuItem.setChecked(true);
                            setTitle(menuItem.getTitle());
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

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

    private int toastCount = 1;

    @Subscribe
    public void onNotificationEvent(NotificationEvent event) {
        int count = event.getCount();
        if (count > 0) {
            tvNotification.setVisibility(View.VISIBLE);
            tvNotification.setText(String.valueOf(count));
            if (toastCount <= 3) {
                Toast.makeText(this, String.format("有%d条新消息", count), Toast.LENGTH_SHORT).show();
                toastCount++;
            }
        } else {
            tvNotification.setVisibility(View.GONE);
        }
    }

    @Inject
    UserDao mUserDao;
    @Inject
    UserStorage mUserStorage;


    private void showAccountMenu() {
        final List<User> userList = mUserDao.queryBuilder().list();
        for (User bean : userList) {
            if (bean.getUid().equals(mUserStorage.getUid())) {
                userList.remove(bean);
                break;
            }
        }

        final String[] items = new String[userList.size() + 1];
        for (int i = 0; i < userList.size(); i++)
            items[i] = userList.get(i).getUserName();
        items[items.length - 1] = "账号管理";
        new MaterialDialog.Builder(this).items(items).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                if (which == items.length - 1) {
                    // 账号管理
                    AccountActivity_.intent(MainActivity.this).start();
                } else {
                    mUserStorage.login(userList.get(which));
                    bus.post(new LoginSuccessEvent());
                }
            }
        }).show();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCover:
                NotificationActivity_.intent(this).start();
                drawerLayout.closeDrawers();
                break;
            case R.id.llAccount:
                showAccountMenu();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNotificationPresenter.loadNotification();
    }
}
