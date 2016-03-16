package com.gzsll.hupu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.MessageData;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.db.UserDao;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.otto.AccountChangeEvent;
import com.gzsll.hupu.otto.LoginSuccessEvent;
import com.gzsll.hupu.otto.MessageReadEvent;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.ui.fragment.BrowserFragment;
import com.gzsll.hupu.ui.fragment.ForumListFragment;
import com.gzsll.hupu.ui.fragment.ThreadCollectFragment;
import com.gzsll.hupu.ui.fragment.ThreadRecommendFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/9.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }


    @Bind(R.id.navigationView)
    NavigationView navigationView;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    SimpleDraweeView ivIcon;
    TextView tvName, tvNotification;

    @Inject
    SettingPrefHelper mSettingPrefHelper;
    @Inject
    UpdateAgent mUpdateAgent;
    @Inject
    UserStorage mUserStorage;
    @Inject
    UserDao mUserDao;
    @Inject
    Bus mBus;
    @Inject
    ForumApi mForumApi;


    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
        mBus.register(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
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
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ThreadRecommendFragment.newInstance()).commit();
        initUserInfo();
        initNotification();
        if (mSettingPrefHelper.getAutoUpdate()) {
            mUpdateAgent.checkUpdate(this);
        }
    }

    private void initUserInfo() {
        if (mUserStorage.isLogin()) {
            User user = mUserStorage.getUser();
            if (!TextUtils.isEmpty(user.getIcon())) {
                ivIcon.setImageURI(Uri.parse(user.getIcon()));
            }
            tvName.setText(user.getUserName());
        } else {
            ivIcon.setImageURI(null);
            tvName.setText("");
        }
    }

    private void initNotification() {
        mForumApi.getMessageList("", 1).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<MessageData>() {
            @Override
            public void call(MessageData result) {
                if (result != null && result.status == 200) {
                    count = result.result.list.size();
                    invalidateOptionsMenu();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }


    private void setupDrawerContent() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_collect:
                    case R.id.nav_topic:
                    case R.id.nav_recommend:
                    case R.id.nav_nba:
                    case R.id.nav_my:
                    case R.id.nav_cba:
                    case R.id.nav_gambia:
                    case R.id.nav_equipment:
                    case R.id.nav_fitness:
                    case R.id.nav_football:
                    case R.id.nav_intel_football:
                    case R.id.nav_sport:
                        Fragment mFragment;
                        if (menuItem.getItemId() == R.id.nav_collect) {
                            mFragment = ThreadCollectFragment.newInstance();
                        } else if (menuItem.getItemId() == R.id.nav_topic) {
                            mFragment = BrowserFragment.newInstance(mUserStorage.getUser().getThreadUrl(), "我的帖子");
                        } else if (menuItem.getItemId() == R.id.nav_recommend) {
                            mFragment = ThreadRecommendFragment.newInstance();
                        } else {
                            mFragment = ForumListFragment.newInstance(Constants.mNavMap.get(menuItem.getItemId()));
                        }
                        menuItem.setChecked(true);
                        setTitle(menuItem.getTitle());
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, mFragment).commit();
                        break;
                    case R.id.nav_setting:
                        SettingActivity.startActivity(MainActivity.this);
                        break;
                    case R.id.nav_feedback:
                        PostActivity.startActivity(MainActivity.this, Constants.TYPE_FEEDBACK, "", "2869008", "", "TLint For Android");
                        break;
                    case R.id.nav_about:
                        BrowserActivity.startActivity(MainActivity.this, "http://www.pursll.com/TLint");
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });


    }

    private int count = 0;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_menu_notification));
        return true;
    }


    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.notification_count_layout, null);
        view.setBackgroundResource(backgroundImageId);
        TextView tvCount = (TextView) view.findViewById(R.id.tvCount);
        if (count == 0) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_notification:
                MessageListActivity.startActivity(MainActivity.this);
                break;

        }
        return super.onOptionsItemSelected(item);
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
                if (mUserStorage.isLogin()) {
                    UserProfileActivity.startActivity(this, mUserStorage.getUid());
                } else {
                    AccountActivity.startActivity(this);
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.llAccount:
                showAccountMenu();
                break;
        }
    }


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
                    AccountActivity.startActivity(MainActivity.this);
                } else {
                    mUserStorage.login(userList.get(which));
                    initUserInfo();
                }
            }
        }).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBus.unregister(this);
    }

    @Subscribe
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        initUserInfo();
    }

    @Subscribe
    public void onAccountChangeEvent(AccountChangeEvent event) {
        initUserInfo();
    }

    @Subscribe
    public void onMessageReadEvent(MessageReadEvent event) {
        if (count >= 1) {
            count--;
        }
        invalidateOptionsMenu();
    }

}
