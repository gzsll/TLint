package com.gzsll.hupu.ui;

import android.os.Bundle;
import android.view.View;

import com.gzsll.hupu.MyApplication;
import com.gzsll.hupu.injector.component.DaggerFragmentComponent;
import com.gzsll.hupu.injector.module.FragmentModule;

/**
 * Created by sll on 2016/1/14.
 */
public abstract class BaseLazyLoadFragment extends BaseFragment {

    private boolean isFirstLoad = true;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(((MyApplication) getActivity().getApplication()).getApplicationComponent())
                .build();
        initInjector();
        getBundle(getArguments());
        initUI(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        }
    }

    private void onVisible() {
        if (isFirstLoad && isPrepare) {
            initData();
            isFirstLoad = false;
        }
    }


}
