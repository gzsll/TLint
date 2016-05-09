package com.gzsll.hupu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gzsll.hupu.R;
import com.gzsll.hupu.helper.StringHelper;
import com.gzsll.hupu.helper.ToastHelper;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.fragment.BrowserFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/11.
 */
public class BrowserActivity extends BaseSwipeBackActivity {


    public static void startActivity(Context mContext, String url) {
        Intent intent = new Intent(mContext, BrowserActivity.class);
        intent.putExtra("url", url);
        mContext.startActivity(intent);
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Inject
    ToastHelper mToastHelper;
    @Inject
    StringHelper mStringHelper;
    private String url;
    private BrowserFragment mFragment;

    @Override
    public int initContentView() {
        return R.layout.base_content_toolbar_layout;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        initToolBar(toolbar);
        url = getIntent().getStringExtra("url");
        mFragment = BrowserFragment.newInstance(url, "", true);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mFragment).commit();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.removeGroup(R.id.browser);
        getMenuInflater().inflate(R.menu.menu_browser, menu);

        String shareContent = String.format("%s %s ", getTitle() + "", url + "");
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);


        MenuItem shareItem = menu.findItem(R.id.share);
        ShareActionProvider shareProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        shareProvider.setShareHistoryFileName("channe_share.xml");
        shareProvider.setShareIntent(shareIntent);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            if (mFragment != null) {
                mFragment.reload();
            }
        } else if (item.getItemId() == R.id.copy) {
            mStringHelper.copy(url);
        } else if (item.getItemId() == R.id.to_browser) {
            try {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
            } catch (Exception e) {
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
