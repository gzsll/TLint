package com.gzsll.hupu.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.ReportPresenter;
import com.gzsll.hupu.ui.view.ReportListItem;
import com.gzsll.hupu.ui.view.ReportListItem_;
import com.gzsll.hupu.view.ReportView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/12/12.
 */
@EActivity(R.layout.activity_report)
public class ReportActivity extends BaseSwipeBackActivity implements ReportView {

    private MaterialDialog mDialog;


    @Extra
    String tid;
    @Extra
    String pid;


    @Inject
    ReportPresenter mReportPresenter;

    @ViewById
    ListView lvTypes;
    @ViewById
    EditText etContent;
    @ViewById
    Toolbar toolbar;

    private List<String> list = new ArrayList<>();

    private ReportAdapter adapter;
    private int type = 1;

    @AfterViews
    void init() {
        mReportPresenter.setView(this);
        mReportPresenter.initialize();
        initToolBar(toolbar);
        setTitle("举报");
        mDialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("正在举报.....")
                .progress(true, 0).build();
        initData();
        adapter = new ReportAdapter();
        lvTypes.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lvTypes);

        lvTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
                type = position + 1;
            }
        });
    }

    @Click
    void btCommit() {
        mReportPresenter.submitReports(tid, pid, String.valueOf(type), etContent.getText().toString());
    }

    private void initData() {
        list.add("\u5e7f\u544a\u6216\u5783\u573e\u5185\u5bb9");
        list.add("\u8272\u60c5\u66b4\u9732\u5185\u5bb9");
        list.add("\u653f\u6cbb\u654f\u611f\u8bdd\u9898");
        list.add("\u4eba\u8eab\u653b\u51fb\u7b49\u6076\u610f\u884c\u4e3a");
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    @UiThread(delay = 1000)
    public void onReportSuccess() {
        finish();
    }

    @Override
    public void showLoading() {
        if (!mDialog.isShowing() && !isFinishing()) {
            mDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mDialog.isShowing() && !isFinishing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {

    }

    private class ReportAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReportListItem item;
            if (convertView == null) {
                item = ReportListItem_.build(ReportActivity.this);
            } else {
                item = (ReportListItem) convertView;
            }
            if (lvTypes.isItemChecked(position)) {
                item.setIvCheckVisibility(View.VISIBLE);
            } else {
                item.setIvCheckVisibility(View.GONE);
            }
            item.setText(getItem(position));
            return item;
        }
    }
}
