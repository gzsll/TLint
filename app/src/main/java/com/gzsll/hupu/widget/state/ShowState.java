package com.gzsll.hupu.widget.state;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by sll on 2015/3/13.
 */
public interface ShowState {

    /**
     * 显示该状态
     *
     * @param animate 是否动画
     */
    public void show(boolean animate);

    /**
     * 隐藏该状态
     *
     * @param animate 是否动画
     */
    public void dismiss(boolean animate);

    /**
     * 设置FragmentView
     */
    public void setFragmentView(View fragmentView);

    /**
     * 进入动画
     */
    public void setAnimIn(Animation in);

    /**
     * 退出动画
     */
    public void setAnimOut(Animation out);
}
