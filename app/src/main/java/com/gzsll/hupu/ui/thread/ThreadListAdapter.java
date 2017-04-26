package com.gzsll.hupu.ui.thread;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.TextView;

import com.gzsll.hupu.R;
import com.gzsll.hupu.db.ReadThread;
import com.gzsll.hupu.db.ReadThreadDao;
import com.gzsll.hupu.db.Thread;
import com.gzsll.hupu.ui.content.ContentActivity;
import com.gzsll.hupu.util.ResourceUtil;
import com.gzsll.hupu.util.SettingPrefUtil;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sll on 2016/3/9.
 */
public class ThreadListAdapter extends RecyclerView.Adapter<ThreadListAdapter.ViewHolder> {


    private Activity mActivity;
    private ReadThreadDao mReadThreadDao;

    @Inject
    public ThreadListAdapter(Activity mActivity, ReadThreadDao mReadThreadDao) {
        this.mActivity = mActivity;
        this.mReadThreadDao = mReadThreadDao;
        threads = Collections.emptyList();
    }

    private List<Thread> threads;

    public void bind(List<Thread> threads) {
        this.threads = threads;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_thread, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Thread thread = threads.get(position);
        holder.thread = thread;
        holder.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, SettingPrefUtil.getTitleSize(mActivity));
        holder.tvTitle.setText(Html.fromHtml(thread.getTitle()));
        if (thread.getLightReply() != null && thread.getLightReply() > 0) {
            holder.tvLight.setText(String.valueOf(thread.getLightReply()));
            holder.tvLight.setVisibility(View.VISIBLE);
        } else {
            holder.tvLight.setVisibility(View.GONE);
        }
        holder.tvReply.setText(thread.getReplies());

        holder.tvSingleTime.setVisibility(View.VISIBLE);
        holder.tvSummary.setVisibility(View.GONE);
        holder.grid.setVisibility(View.GONE);
        if (thread.getForum() != null) {
            holder.tvSingleTime.setText(thread.getForum().getName());
        } else {
            holder.tvSingleTime.setText(thread.getTime());
        }
        Observable.just(thread.getTid())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return mReadThreadDao.queryBuilder().where(ReadThreadDao.Properties.Tid.eq(s)).count()
                                > 0;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            holder.tvTitle.setTextColor(
                                    ResourceUtil.getThemeAttrColor(mActivity, android.R.attr.textColorSecondary));
                        } else {
                            holder.tvTitle.setTextColor(
                                    ResourceUtil.getThemeAttrColor(mActivity, android.R.attr.textColorPrimary));
                        }
                    }
                });
        showItemAnim(holder.cardView, position);
    }

    private int mLastPosition = -1;

    public void showItemAnim(final View view, final int position) {
        if (position > mLastPosition) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.item_bottom_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    view.setAlpha(1);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(animation);
            mLastPosition = position;
        }
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

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvSummary)
        TextView tvSummary;
        @BindView(R.id.grid)
        GridLayout grid;
        @BindView(R.id.tvSingleTime)
        TextView tvSingleTime;
        @BindView(R.id.tvReply)
        TextView tvReply;
        @BindView(R.id.tvLight)
        TextView tvLight;
        @BindView(R.id.cardView)
        CardView cardView;

        Thread thread;

        @OnClick(R.id.llThreadItem)
        void llThreadItemClick() {
            Observable.just(thread.getTid())
                    .doOnNext(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            mReadThreadDao.queryBuilder()
                                    .where(ReadThreadDao.Properties.Tid.eq(s))
                                    .buildDelete()
                                    .executeDeleteWithoutDetachingEntities();
                            ReadThread readThread = new ReadThread(null, s);
                            mReadThreadDao.insert(readThread);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            notifyDataSetChanged();
                            ContentActivity.startActivity(mActivity, thread.getFid(), thread.getTid(), "", 1);
                        }
                    });
        }

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
