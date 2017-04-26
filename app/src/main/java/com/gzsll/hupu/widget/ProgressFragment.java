package com.gzsll.hupu.widget;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gzsll.hupu.R;
import com.gzsll.hupu.widget.state.ContentState;
import com.gzsll.hupu.widget.state.EmptyState;
import com.gzsll.hupu.widget.state.ErrorState;
import com.gzsll.hupu.widget.state.NonState;
import com.gzsll.hupu.widget.state.ProgressState;
import com.gzsll.hupu.widget.state.ShowState;

/**
 * Created by sll on 2015/3/13.
 */
public class ProgressFragment extends Fragment {

    public boolean isPrepare = false;

    //Override this method to change content view
    public View onCreateContentView(LayoutInflater inflater) {
        return null;
    }

    //Override this method to change error view
    public View onCreateContentErrorView(LayoutInflater inflater) {
        return null;
    }

    public View onCreateContentEmptyView(LayoutInflater inflater) {
        return null;
    }

    public View onCreateProgressView(LayoutInflater inflater) {
        return null;
    }

    private View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup main = (ViewGroup) inflater.inflate(R.layout.epf_layout, container, false);

        View content = onCreateContentView(inflater);
        View error = onCreateContentErrorView(inflater);
        View empty = onCreateContentEmptyView(inflater);
        View progress = onCreateProgressView(inflater);

        replaceViewById(main, R.id.epf_content, content);
        replaceViewById(main, R.id.epf_error, error);
        replaceViewById(main, R.id.epf_empty, empty);
        replaceViewById(main, R.id.epf_progress, progress);

        mContentView = main;

        mAnimIn = onCreateAnimationIn();
        mAnimOut = onCreateAnimationOut();

        initStates();
        isPrepare = true;
        return main;
    }

    public Animation onCreateAnimationIn() {
        return AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
    }

    public Animation onCreateAnimationOut() {
        return AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
    }

    private void replaceViewById(ViewGroup container, int viewId, View newView) {
        if (newView == null) {
            return;
        }
        newView.setId(viewId);
        View oldView = container.findViewById(viewId);
        int index = container.indexOfChild(oldView);
        container.removeView(oldView);
        container.addView(newView, index);

        newView.setVisibility(View.GONE);
    }

    private ShowState mEmptyState, mProgressState, mErrorState, mContentState, mLoginState,
            mCollectState;
    private Animation mAnimIn, mAnimOut;

    private void initStates() {

        mEmptyState = new EmptyState();
        mProgressState = new ProgressState();
        mErrorState = new ErrorState();
        mContentState = new ContentState();

        initState(mEmptyState);
        initState(mProgressState);
        initState(mErrorState);
        initState(mContentState);
    }

    private void initState(ShowState state) {
        state.setAnimIn(mAnimIn);
        state.setAnimOut(mAnimOut);
        state.setFragmentView(mContentView);
    }

    private ShowState mLastState = new NonState();

    public void showContent(boolean animate) {
        if (mLastState == mContentState) {
            return;
        }
        mContentState.show(animate);
        mLastState.dismiss(animate);
        mLastState = mContentState;
    }

    public void showEmpty(boolean animate) {
        if (mLastState == mEmptyState) {
            return;
        }
        mEmptyState.show(animate);
        mLastState.dismiss(animate);
        mLastState = mEmptyState;
    }

    public void showError(boolean animate) {
        if (mLastState == mErrorState) {
            return;
        }
        mErrorState.show(animate);
        mLastState.dismiss(animate);
        mLastState = mErrorState;
    }

    public void showProgress(boolean animate) {
        if (mLastState == mProgressState) {
            return;
        }
        mProgressState.show(animate);
        mLastState.dismiss(animate);
        mLastState = mProgressState;
    }
}
