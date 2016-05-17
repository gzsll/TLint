package com.gzsll.hupu.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gzsll.hupu.R;
import com.gzsll.hupu.db.User;
import com.gzsll.hupu.ui.BaseSwipeBackActivity;
import com.gzsll.hupu.ui.login.LoginActivity;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by sll on 2016/3/10.
 */
public class AccountActivity extends BaseSwipeBackActivity implements AccountContract.View {

  public static void startActivity(Activity mActivity) {
    Intent intent = new Intent(mActivity, AccountActivity.class);
    mActivity.startActivity(intent);
  }

  @Inject AccountPresenter mPresenter;
  @Inject AccountAdapter mAdapter;

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.recyclerView) RecyclerView recyclerView;

  @Override public int initContentView() {
    return R.layout.activity_account;
  }

  @Override public void initInjector() {
    DaggerAccountComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .accountModule(new AccountModule())
        .build()
        .inject(this);
  }

  @Override public void initUiAndListener() {
    ButterKnife.bind(this);
    mPresenter.attachView(this);
    initToolBar(toolbar);
    setTitle("账号管理");
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(mAdapter);
  }

  @Override protected boolean isApplyStatusBarTranslucency() {
    return true;
  }

  @Override protected boolean isApplyStatusBarColor() {
    return true;
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_account, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_add) {
      LoginActivity.startActivity(this);
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void renderUserList(List<User> users) {
    mAdapter.bind(users);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mPresenter.detachView();
  }
}
