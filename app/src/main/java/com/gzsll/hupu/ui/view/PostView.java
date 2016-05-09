package com.gzsll.hupu.ui.view;

import com.gzsll.hupu.bean.Exam;
import com.gzsll.hupu.ui.BaseView;

/**
 * Created by sll on 2016/3/9.
 */
public interface PostView extends BaseView {

    void renderExam(Exam exam);

    void showLoading();

    void hideLoading();

    void postSuccess();
}
