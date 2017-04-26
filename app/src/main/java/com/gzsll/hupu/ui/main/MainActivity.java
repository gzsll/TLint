package com.gzsll.hupu.ui.main;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.injector.HasComponent;
import com.gzsll.hupu.ui.BaseActivity;
import com.gzsll.hupu.ui.account.AccountActivity;
import com.gzsll.hupu.ui.browser.BrowserActivity;
import com.gzsll.hupu.ui.login.LoginActivity;
import com.gzsll.hupu.ui.messagelist.MessageActivity;
import com.gzsll.hupu.ui.post.PostActivity;
import com.gzsll.hupu.ui.setting.SettingActivity;
import com.gzsll.hupu.ui.thread.recommend.RecommendThreadListFragment;
import com.gzsll.hupu.ui.userprofile.UserProfileActivity;
import com.gzsll.hupu.util.ResourceUtil;
import com.gzsll.hupu.util.SettingPrefUtil;
import com.gzsll.hupu.util.StatusBarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/9.
 */
public class MainActivity extends BaseActivity
        implements View.OnClickListener, MainContract.View, HasComponent<MainComponent> {

    public static void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
    }

    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ImageView ivTheme;
    SimpleDraweeView ivIcon;
    TextView tvName;

    @Inject
    MainPresenter mPresenter;

    private MainComponent mMainComponent;

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule()).mainModule(new MainModule(this))
                .build();
        mMainComponent.inject(this);
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
        navigationView.getHeaderView(0).findViewById(R.id.ivCover).setOnClickListener(this);
        navigationView.getHeaderView(0).findViewById(R.id.llAccount).setOnClickListener(this);
        ivTheme = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.ivTheme);
        ivTheme.setOnClickListener(this);
        ivTheme.setImageResource(SettingPrefUtil.getNightModel(this) ? R.drawable.ic_wb_sunny_white_24dp
                : R.drawable.ic_brightness_3_white_24dp);
        //创建返回键，并实现打开关/闭监听
        ActionBarDrawerToggle mDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
        StatusBarUtil.setColorForDrawerLayout(this, drawerLayout, ResourceUtil.getThemeColor(this), 0);
        setupDrawerContent();
        getFragmentManager().beginTransaction()
                .replace(R.id.content,
                        new RecommendThreadListFragment())
                .commit();
        mPresenter.attachView(this);
    }

    private void setupDrawerContent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mPresenter.onNavigationClick(menuItem);
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
            tvCount.setText(String.valueOf(count));
        }

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
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
                mPresenter.onNotificationClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    @Override
    public void onBackPressed() {
        mPresenter.exist();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivCover:
                mPresenter.onCoverClick();
                break;
            case R.id.llAccount:
                mPresenter.showAccountMenu();
                break;
            case R.id.ivTheme:
                mPresenter.onNightModelClick();
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
            ivIcon.setImageURI("");
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

    @Override
    public void closeDrawers() {
        drawerLayout.closeDrawers();
    }

    @Override
    public void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    public void showMessageUi() {
        MessageActivity.startActivity(this);
    }

    @Override
    public void showUserProfileUi(String uid) {
        UserProfileActivity.startActivity(this, uid);
    }

    @Override
    public void showLoginUi() {
        LoginActivity.startActivity(this);
    }

    @Override
    public void showAccountUi() {
        AccountActivity.startActivity(this);
    }

    @Override
    public void showSettingUi() {
        SettingActivity.startActivity(this);
    }

    @Override
    public void showFeedBackUi() {
        PostActivity.startActivity(MainActivity.this, Constants.TYPE_FEEDBACK, "", "2869008", "",
                "TLint For Android");
    }

    @Override
    public void showAboutUi() {
        BrowserActivity.startActivity(this, "http://www.pursll.com/TLint");
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }
}
