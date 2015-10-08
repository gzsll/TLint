package com.gzsll.hupu.ui.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.fragment.MessageAtFragment_;
import com.gzsll.hupu.ui.fragment.MessageReplyFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sll on 2015/9/8 0008.
 */
@EActivity(R.layout.activity_notice)
public class NoticeActivity extends BaseSwipeBackActivity {


    @ViewById
    Toolbar toolbar;
    @ViewById
    TabLayout tabs;
    @ViewById
    AppBarLayout appbar;
    @ViewById
    ViewPager viewpager;

    @AfterViews
    void init() {
        initToolBar(toolbar);
        setTitle("消息中心");
        setupViewPager();
    }

    private void setupViewPager() {
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        adapter.addFragment(MessageReplyFragment_.builder().build(), "回复");
        adapter.addFragment(MessageAtFragment_.builder().build(), "@我");
        viewpager.setAdapter(adapter);
        tabs.setupWithViewPager(viewpager);
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
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


}
