package com.gzsll.hupu.ui.fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.ThreadRecommendPresenter;
import com.gzsll.hupu.support.storage.bean.Thread;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.adapter.BaseListAdapter;
import com.gzsll.hupu.ui.adapter.ThreadRecommendAdapter;
import com.gzsll.hupu.ui.view.ThreadListItem;
import com.gzsll.hupu.view.ThreadRecommendView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import javax.inject.Inject;

/**
 * Created by sll on 2015/12/12.
 */
@EFragment
public class ThreadRecommendFragment extends BaseListFragment<Thread, ThreadListItem> implements ThreadRecommendView {

    @Inject
    ThreadRecommendPresenter mPresenter;
    @Inject
    ThreadRecommendAdapter adapter;

    private AdView mAdView;


    @Override
    protected int inflateContentView() {
        return R.layout.base_list_layout;
    }

    @AfterViews
    void init() {
        mPresenter.setView(this);
        mPresenter.initialize();
        mPresenter.onRecommendListReceive();
        mAdView = new AdView(getActivity());
        mAdView.setAdUnitId("ca-app-pub-8075807288160204/8098671171");
        mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        listView.addFooterView(mAdView);
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onLoadMore() {
        mPresenter.onLoadMore();
    }

    @Override
    protected void onRefresh() {
        mPresenter.onRefresh();
    }

    @Override
    public void onReloadClicked() {
        mPresenter.onReload();
    }

    @Override
    protected BaseListAdapter<Thread, ThreadListItem> getAdapter() {
        adapter.setActivity((BaseActivity) getActivity());
        return adapter;
    }

    @Override
    public void onRefreshing(boolean refresh) {
        refreshLayout.setRefreshing(refresh);
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
