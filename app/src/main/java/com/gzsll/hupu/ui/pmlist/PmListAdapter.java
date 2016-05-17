package com.gzsll.hupu.ui.pmlist;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gzsll.hupu.R;
import com.gzsll.hupu.bean.Pm;
import com.gzsll.hupu.ui.pmdetail.PmDetailActivity;
import com.gzsll.hupu.widget.LabelCardView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by sll on 2016/5/6.
 */
public class PmListAdapter extends RecyclerView.Adapter<PmListAdapter.ViewHolder> {

  @Inject Activity mActivity;

  @Inject public PmListAdapter() {
  }

  private List<Pm> pms = new ArrayList<>();

  public void bind(List<Pm> pms) {
    this.pms = pms;
    notifyDataSetChanged();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_pm, parent, false));
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    Pm pm = pms.get(position);
    holder.mPm = pm;
    if (!TextUtils.isEmpty(pm.header)) {
      holder.ivIcon.setImageURI(Uri.parse(pm.header));
    }
    holder.tvContent.setText(pm.content);
    holder.tvName.setText(pm.nickname);
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date(Long.valueOf(pm.last_time) * 1000);
    holder.tvTime.setText(format.format(date));
    if (!TextUtils.isEmpty(pm.unread) && pm.unread.equals("1")) {
      holder.cardView.setLabelText("NEW");
      holder.cardView.setLabelVisual(true);
    } else {
      holder.cardView.setLabelVisual(false);
    }
  }

  @Override public int getItemCount() {
    return pms.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    Pm mPm;

    @Bind(R.id.tvName) TextView tvName;
    @Bind(R.id.tvContent) TextView tvContent;
    @Bind(R.id.tvTime) TextView tvTime;
    @Bind(R.id.ivIcon) SimpleDraweeView ivIcon;
    @Bind(R.id.listItem) LabelCardView cardView;

    @OnClick(R.id.listItem) void listItemClick() {
      PmDetailActivity.startActivity(mActivity, mPm.puid, mPm.nickname);
    }

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
