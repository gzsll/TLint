package com.gzsll.hupu.ui.fragment;

import com.gzsll.hupu.R;
import com.gzsll.hupu.presenter.NotificationPresenter;
import com.gzsll.hupu.support.storage.bean.Notification;
import com.gzsll.hupu.ui.activity.BaseActivity;
import com.gzsll.hupu.ui.activity.BrowserActivity_;
import com.gzsll.hupu.ui.activity.ContentActivity_;
import com.gzsll.hupu.ui.adapter.BaseListAdapter;
import com.gzsll.hupu.ui.adapter.NotificationAdapter;
import com.gzsll.hupu.ui.view.NotificationListItem;
import com.gzsll.hupu.view.NotificationView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;

import javax.inject.Inject;

/**
 * Created by sll on 2015/12/12.
 */
@EFragment
public class NotificationFragment extends BaseListFragment<Notification, NotificationListItem> implements NotificationView {

    @Inject
    NotificationPresenter mPresenter;
    @Inject
    NotificationAdapter mAdapter;


    @Override
    protected int inflateContentView() {
        return R.layout.base_list_layout;
    }

    @AfterViews
    void init() {
        mPresenter.setView(this);
        mPresenter.initialize();
        mPresenter.loadNotification();

    }

    @ItemClick(R.id.listView)
    public void notification_listItemClicked(int position) {
        try {
            Notification notification = (Notification) mAdapter.getItem(position);
            if ((notification.type == Notification.TYPE_THREAD) && (notification.tid != null)) {
                ContentActivity_.intent(this).pid(notification.pid).tid(notification.tid).page(Integer.valueOf(notification.page)).fid("34").start();
            } else {
                mPresenter.ignore(notification.nid);
                BrowserActivity_.intent(this).url(notification.href).start();
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onLoadMore() {

    }

    @Override
    protected void onRefresh() {
        mPresenter.loadNotification();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadNotification();
    }

    @Override
    protected BaseListAdapter<Notification, NotificationListItem> getAdapter() {
        mAdapter.setActivity((BaseActivity) getActivity());
        return mAdapter;
    }
}
