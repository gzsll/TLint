package com.gzsll.hupu.ui.messagelist;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzsll.hupu.R;
import com.gzsll.hupu.api.forum.ForumApi;
import com.gzsll.hupu.bean.BaseData;
import com.gzsll.hupu.bean.Message;
import com.gzsll.hupu.otto.MessageReadEvent;
import com.gzsll.hupu.ui.content.ContentActivity;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * Created by sll on 2016/3/11.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    @Inject
    ForumApi mForumApi;
    @Inject
    Activity mActivity;
    @Inject
    Bus mBus;

    @Inject
    public MessageListAdapter() {
    }


    private List<Message> messages = new ArrayList<>();


    public void bind(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_message, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.message = message;
        holder.tvTime.setText(message.time);
        holder.tvCategory.setText(message.catergory);
        holder.tvInfo.setText(message.info);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvCategory)
        TextView tvCategory;
        @Bind(R.id.tvInfo)
        TextView tvInfo;
        @Bind(R.id.tvTime)
        TextView tvTime;

        Message message;

        @OnClick(R.id.listItem)
        void listItemClick() {
            ContentActivity.startActivity(mActivity, "", message.tid, message.pid, Integer.valueOf(message.page), "");
            mForumApi.delMessage(message.id).subscribe(new Action1<BaseData>() {
                @Override
                public void call(BaseData baseData) {
                    if (baseData != null && baseData.status == 200) {
                        messages.remove(message);
                        notifyDataSetChanged();
                        mBus.post(new MessageReadEvent());
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {

                }
            });
        }


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
