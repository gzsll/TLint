package com.gzsll.hupu.ui.activity;

import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.ReplyDetailPresenter;
import com.gzsll.hupu.support.storage.bean.MiniReplyListItem;
import com.gzsll.hupu.support.storage.bean.ThreadReplyItem;
import com.gzsll.hupu.support.utils.ReplyViewHelper;
import com.gzsll.hupu.support.utils.ResourceHelper;
import com.gzsll.hupu.ui.adapter.MiniReplyAdapter;
import com.gzsll.hupu.view.ReplyDetailView;
import com.gzsll.hupu.widget.ProgressBarCircularIndeterminate;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/8/21.
 */
@EActivity(R.layout.activity_reply_detail)
public class ReplyDetailActivity extends BaseSwipeBackActivity implements ReplyDetailView {

    @Extra
    ThreadReplyItem replyItem;


    @ViewById
    SimpleDraweeView ivIcon;
    @ViewById
    TextView tvUserName;
    @ViewById
    TextView tvUser;
    @ViewById
    TextView tvTime;
    @ViewById
    TextView tvReplyLight;
    @ViewById
    LinearLayout llContent;
    @ViewById
    View deliver;
    @ViewById
    RecyclerView recyclerView;
    @ViewById
    RelativeLayout rlLight;
    @ViewById
    LinearLayout llLoading;
    @ViewById
    ProgressBarCircularIndeterminate progress;
    @ViewById
    TextView tvLoading;
    @ViewById
    FloatingActionMenu floatingMenu;
    @ViewById
    FloatingActionButton floatingComment, floatingQuote;


    @ViewById
    Toolbar toolbar;
    @Inject
    ReplyViewHelper mReplyViewHelper;
    @Inject
    MiniReplyAdapter adapter;
    @Inject
    ReplyDetailPresenter mPresenter;
    @Inject
    ResourceHelper resourceHelper;

    MaterialDialog dialog;

    @AfterViews
    void init() {
        setTitle("回复详情");
        initToolBar(toolbar);
        mPresenter.setView(this);
        mPresenter.initialize();
        ivIcon.setImageURI(Uri.parse(replyItem.getUserInfo().getIcon()));
        tvUserName.setText(replyItem.getUserInfo().getUsername());
        tvTime.setText(replyItem.getCreate_at());
        tvUser.setVisibility(View.GONE);
        rlLight.setVisibility(View.GONE);
        llContent.removeAllViews();
        mReplyViewHelper.addToView(mReplyViewHelper.compileContent(replyItem.getContent()), llContent);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setActivity(this);
        adapter.setFloorer(replyItem.getUserInfo().getUid());
        recyclerView.setAdapter(adapter);
        mPresenter.onMiniReplyReceive(replyItem.getGroupThreadId() + "", replyItem.getId() + "", 1);
        dialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("正在发送.....")
                .progress(true, 0).build();
        resourceHelper.setFabBtnColor(this, floatingComment);
        resourceHelper.setFabBtnColor(this, floatingQuote);
        resourceHelper.setFabMenuColor(this, floatingMenu);
    }


    @Override
    public void showLoginView() {

    }

    @Override
    public void onMiniRepliesLoading() {
        recyclerView.setVisibility(View.GONE);
        llLoading.setVisibility(View.VISIBLE);

    }

    @Override
    public void loadMiniRepliesFinish() {
        recyclerView.setVisibility(View.VISIBLE);
        llLoading.setVisibility(View.GONE);
    }

    @Override
    public void renderMiniReplies(List<MiniReplyListItem> replyItems) {
        adapter.updateItems(replyItems);
    }

    @UiThread
    @Override
    public void showLoading() {
        if (!dialog.isShowing() && !isFinishing()) {
            dialog.show();
        }
    }

    @UiThread
    @Override
    public void hideLoading() {
        if (dialog.isShowing() && !isFinishing()) {
            dialog.dismiss();
        }
    }


    @UiThread
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Click
    void floatingComment() {
        PostActivity_.intent(this).type(Constants.TYPE_REPLY).title(replyItem.getContent()).groupThreadId(replyItem.getGroupThreadId() + "").groupReplyId(replyItem.getId() + "").start();
        floatingMenu.toggle(true);
    }

    @Click
    void floatingQuote() {
        PostActivity_.intent(this).type(Constants.TYPE_QUOTE).title(replyItem.getContent()).groupThreadId(replyItem.getGroupThreadId() + "").quoteId(replyItem.getId() + "").start();
        floatingMenu.toggle(true);
    }

    @Override
    protected boolean isApplyKitKatTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }


}
