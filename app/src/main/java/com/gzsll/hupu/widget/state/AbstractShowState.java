package com.gzsll.hupu.widget.state;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by sll on 2015/3/13.
 */
public abstract class AbstractShowState implements ShowState {
    protected View mFragmentView;
    protected Animation mAnimationIn;
    protected Animation mAnimationOut;

    protected void showViewById(int viewId, boolean animate) {
        View content = mFragmentView.findViewById(viewId);
        if (animate) {
            mAnimationIn.reset();
            content.startAnimation(mAnimationIn);
        } else {
            content.clearAnimation();
        }
        content.setVisibility(View.VISIBLE);
    }

    protected void dismissViewById(int viewId, boolean animate) {
        View content = mFragmentView.findViewById(viewId);
        if (animate) {
            mAnimationOut.reset();
            content.startAnimation(mAnimationOut);
        } else {
            content.clearAnimation();
        }
        content.setVisibility(View.GONE);
    }

    @Override
    public void setFragmentView(View fragmentView) {
        mFragmentView = fragmentView;
    }

    @Override
    public void setAnimIn(Animation in) {
        mAnimationIn = in;
    }

    @Override
    public void setAnimOut(Animation out) {
        mAnimationOut = out;
    }
}
