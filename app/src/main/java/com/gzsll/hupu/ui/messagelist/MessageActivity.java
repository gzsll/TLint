package com.gzsll.hupu.ui.messagelist;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.gzsll.hupu.R;
import com.gzsll.hupu.injector.HasComponent;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.pmlist.PmListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sll on 2016/3/13.
 */
public class MessageActivity extends BaseSwipeBackActivity
        implements HasComponent<MessageComponent> {

    public static void startActivity(Context mContext) {
        Intent intent = new Intent(mContext, MessageActivity.class);
        mContext.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private MessageComponent mMessageComponent;

    @Override
    public int initContentView() {
        return R.layout.activity_message;
    }

    @Override
    public void initInjector() {
        mMessageComponent = DaggerMessageComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        initToolBar(toolbar);
        setTitle("消息中心");
        MyAdapter adapter = new MyAdapter(getFragmentManager());
        adapter.addFragment(new MessageListFragment(), "回复");
        adapter.addFragment(new PmListFragment(), "PM");
        viewpager.setAdapter(adapter);
        tabs.setupWithViewPager(viewpager);
    }

    @Override
    public MessageComponent getComponent() {
        return mMessageComponent;
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
    protected boolean isApplyStatusBarColor() {
        return true;
    }
}
