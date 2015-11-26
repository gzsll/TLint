package com.gzsll.hupu.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.gzsll.hupu.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sll on 2015/10/10.
 * 新闻
 */
@EFragment
public class NewsFragment extends BaseFragment {

    @ViewById
    TabLayout tabs;
    @ViewById
    ViewPager viewPager;

    @Override
    public View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_news, null);
    }

    @AfterViews
    void init() {
        showContent(true);
        MyAdapter adapter = new MyAdapter(getChildFragmentManager());
        adapter.addFragment(NewsListFragment_.builder().build(), "nba");
        adapter.addFragment(NewsListFragment_.builder().build(), "nba");
        adapter.addFragment(NewsListFragment_.builder().build(), "nba");
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    private class MyAdapter extends FragmentPagerAdapter {
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
