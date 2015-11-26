package com.gzsll.hupu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gzsll.hupu.AppApplication;
import com.gzsll.hupu.R;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.widget.ProgressBarCircularIndeterminate;
import com.gzsll.hupu.widget.ProgressFragment;

import javax.inject.Inject;

/**
 * Created by sll on 2015/3/7.
 */
public class BaseFragment extends ProgressFragment {

    private TextView tvError, tvEmpty, tvLoading;
    private Button btnReload;

    @Inject
    ResourceHelper mResourceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    private void injectDependencies() {
        ((AppApplication) getActivity().getApplicationContext()).inject(this);
    }

    @Override
    public View onCreateContentErrorView(LayoutInflater inflater) {
        View error = inflater.inflate(R.layout.error_view_layout, null);
        tvError = (TextView) error.findViewById(R.id.tvError);
        error.findViewById(R.id.btnReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReloadClicked();
            }
        });
        return error;
    }

    @Override
    public View onCreateContentEmptyView(LayoutInflater inflater) {
        View empty = inflater.inflate(R.layout.empty_view_layout, null);
        tvEmpty = (TextView) empty.findViewById(R.id.tvEmpty);
        btnReload = (Button) empty.findViewById(R.id.btnReload);
        empty.findViewById(R.id.btnReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReloadClicked();
            }
        });
        return empty;
    }

    @Override
    public View onCreateProgressView(LayoutInflater inflater) {
        View loading = inflater.inflate(R.layout.loading_view_layout, null);
        tvLoading = (TextView) loading.findViewById(R.id.tvLoading);
        ProgressBarCircularIndeterminate progressBar = (ProgressBarCircularIndeterminate) loading.findViewById(R.id.progress_view);
        progressBar.setBackgroundColor(mResourceHelper.getThemeColor(getActivity()));
        return loading;
    }


    public void setErrorText(String text) {
        tvError.setText(text);
    }

    public void setErrorText(int textResId) {
        setErrorText(getString(textResId));
    }

    public void setEmptyText(String text) {
        tvEmpty.setText(text);
    }

    public void setEmptyButtonVisible(int visible) {
        btnReload.setVisibility(visible);
    }

    public void setEmptyText(int textResId) {
        setEmptyText(getString(textResId));
    }

    public void setLoadingText(String text) {
        tvLoading.setText(text);
    }

    public void setLoadingText(int textResId) {
        setLoadingText(getString(textResId));
    }

    //Override this to reload
    public void onReloadClicked() {

    }

}
