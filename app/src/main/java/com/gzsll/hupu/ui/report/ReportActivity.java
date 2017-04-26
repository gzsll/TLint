package com.gzsll.hupu.ui.report;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gzsll.hupu.R;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/11.
 */
public class ReportActivity extends BaseSwipeBackActivity implements ReportContract.View {

    public static void startActivity(Context mContext, String tid, String pid) {
        Intent intent = new Intent(mContext, ReportActivity.class);
        intent.putExtra("tid", tid);
        intent.putExtra("pid", pid);
        mContext.startActivity(intent);
    }

    @Inject
    ReportPresenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lvTypes)
    ListView lvTypes;
    @BindView(R.id.etContent)
    EditText etContent;

    private List<String> list = new ArrayList<>();
    private ReportAdapter adapter;
    private int type = 1;
    private MaterialDialog mDialog;
    private String tid;
    private String pid;

    @Override
    public int initContentView() {
        return R.layout.activity_report;
    }

    @Override
    public void initInjector() {
        DaggerReportComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .reportModule(new ReportModule())
                .build()
                .inject(this);
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        initToolBar(toolbar);
        setTitle("举报");
        pid = getIntent().getStringExtra("pid");
        tid = getIntent().getStringExtra("tid");
        mDialog =
                new MaterialDialog.Builder(this).title("提示").content("正在举报").progress(true, 0).build();
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
    protected boolean isApplyStatusBarColor() {
        return true;
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
    public void reportSuccess() {

    }

    @OnClick(R.id.btCommit)
    void btCommitClick() {
        mPresenter.submitReports(tid, pid, String.valueOf(type), etContent.getText().toString());
    }

    public class ReportAdapter extends BaseAdapter {

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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ReportActivity.this)
                        .inflate(R.layout.item_list_report, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (lvTypes.isItemChecked(position)) {
                holder.ivCheck.setVisibility(View.VISIBLE);
            } else {
                holder.ivCheck.setVisibility(View.GONE);
            }
            holder.tvType.setText(getItem(position));
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tvType)
            TextView tvType;
            @BindView(R.id.ivCheck)
            ImageView ivCheck;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
