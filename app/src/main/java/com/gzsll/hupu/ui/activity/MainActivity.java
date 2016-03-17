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

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.UpdateAgent;
import com.gzsll.hupu.components.storage.UserStorage;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.presenter.MainPresenter;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.ui.fragment.BrowserFragment;
import com.gzsll.hupu.ui.fragment.ForumListFragment;
import com.gzsll.hupu.ui.fragment.ThreadCollectFragment;
import com.gzsll.hupu.ui.fragment.ThreadRecommendFragment;
import com.gzsll.hupu.ui.view.MainView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/9.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, MainView {

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
    MainPresenter mPresenter;


    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
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
        mPresenter.attachView(this);
        if (mSettingPrefHelper.getAutoUpdate()) {
            mUpdateAgent.checkUpdate(this);
        }
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
                mPresenter.clickNotification();
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
        mPresenter.exist();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCover:
                mPresenter.clickCover();
                drawerLayout.closeDrawers();
                break;
            case R.id.llAccount:
                mPresenter.showAccountMenu();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }


    @Override
    public void renderUserInfo(User user) {
        if (user != null) {
            if (!TextUtils.isEmpty(user.getIcon())) {
                ivIcon.setImageURI(Uri.parse(user.getIcon()));
            }
            tvName.setText(user.getUserName());
        } else {
            ivIcon.setImageURI(null);
            tvName.setText(null);
        }
    }

    @Override
    public void renderAccountList(final List<User> users, final String[] items) {
        new MaterialDialog.Builder(this).items(items).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                mPresenter.onAccountItemClick(which, users, items);
            }
        }).show();
    }

    @Override
    public void renderNotification(int count) {
        this.count = count;
        invalidateOptionsMenu();
    }
}
