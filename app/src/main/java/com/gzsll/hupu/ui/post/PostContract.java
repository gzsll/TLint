package com.gzsll.hupu.ui.post;

import com.gzsll.hupu.bean.BaseError;
import com.gzsll.hupu.bean.Exam;
import com.gzsll.hupu.ui.BasePresenter;
import com.gzsll.hupu.ui.BaseView;

import java.util.ArrayList;

/**
 * Created by sll on 2016/5/11.
 */
public interface PostContract {

    interface View extends BaseView {
        void renderError(BaseError error);

        void renderExam(Exam exam);

        void showLoading();

        void hideLoading();

        void postSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void checkPermission(int type, String fid, String tid);

        void parse(ArrayList<String> paths);

        void comment(String tid, String fid, String pid, String content);

        void post(String fid, String content, String title);
    }
}
