package com.gzsll.hupu.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Thread;
import com.gzsll.hupu.helper.SettingPrefHelper;
import com.gzsll.hupu.ui.activity.ContentActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sll on 2016/3/9.
 */
public class ThreadListAdapter extends RecyclerView.Adapter<ThreadListAdapter.ViewHolder> {


    @Inject
    Activity mActivity;
    @Inject
    SettingPrefHelper mSettingPrefHelper;


    @Inject
    public ThreadListAdapter() {

    }


    private List<Thread> threads = new ArrayList<>();


    public void bind(List<Thread> threads) {
        this.threads = threads;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_thread, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Thread thread = threads.get(position);
        holder.thread = thread;
        holder.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSettingPrefHelper.getTitleSize());
        if (thread.lightReply > 0) {
            holder.tvLight.setText(String.valueOf(thread.lightReply));
            holder.tvLight.setVisibility(View.VISIBLE);
        } else {
            holder.tvLight.setVisibility(View.GONE);
        }
        holder.tvReply.setText(thread.replies);
        holder.tvTitle.setText(thread.title);
        holder.tvSingleTime.setVisibility(View.VISIBLE);
        holder.tvSummary.setVisibility(View.GONE);
        holder.grid.setVisibility(View.GONE);
        holder.tvSingleTime.setText(thread.time);
    }

//    protected void buildMultiPic(final GroupThread thread, final GridLayout gridLayout) {
//        if (mSettingPrefHelper.getLoadPic()) {
//            gridLayout.setVisibility(View.VISIBLE);
//            final int count = thread.getCover().size();
//            final List<String> pics = new ArrayList<String>();
//            for (int i = 0; i < count; i++) {
//                SimpleDraweeView imageView = (SimpleDraweeView) gridLayout.getChildAt(i);
//                imageView.setVisibility(View.VISIBLE);
//                final Cover threadPic = thread.getCover().get(i);
//                pics.add(threadPic.getUrl());
//                imageView.setImageURI(Uri.parse(mSettingPrefHelper.getLoadOriginPic() ? threadPic.getUrl() : threadPic.getUrlSmall()));
//                imageView.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ImagePreviewActivity_.intent(mActivity).extraPic(threadPic.getUrl()).extraPics(pics).start();
//                    }
//                });
//            }
//
//            if (count < 9) {
//                for (int i = 8; i > count - 1; i--) {
//                    SimpleDraweeView pic = (SimpleDraweeView) gridLayout.getChildAt(i);
//                    pic.setVisibility(View.GONE);
//                }
//            }
//        } else {
//            gridLayout.setVisibility(GONE);
//        }
//    }

    @Override
    public int getItemCount() {
        return threads.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvSummary)
        TextView tvSummary;
        @Bind(R.id.grid)
        GridLayout grid;
        @Bind(R.id.tvSingleTime)
        TextView tvSingleTime;
        @Bind(R.id.tvReply)
        TextView tvReply;
        @Bind(R.id.tvLight)
        TextView tvLight;

        Thread thread;


        @OnClick(R.id.llThreadItem)
        void llThreadItemClick() {
            ContentActivity.startActivity(mActivity, thread.fid, thread.tid, "", 1, thread.title);
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }
}
