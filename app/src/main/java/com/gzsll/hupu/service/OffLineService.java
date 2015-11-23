package com.gzsll.hupu.service;

import android.content.Intent;
import android.os.AsyncTask;

import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.service.annotation.ActionMethod;
import com.gzsll.hupu.service.annotation.IntentAnnotationService;
import com.gzsll.hupu.support.db.Board;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzsll on 2014/9/14 0014.
 */
public class OffLineService extends IntentAnnotationService {

    Logger logger = Logger.getLogger("OffLineService");


    public static final String START_DOWNLOAD = BuildConfig.APPLICATION_ID + ".action.START_DOWNLOAD";


    public static final int INIT = 0;
    public static final int PREPARE = 1;
    public static final int LOAD_THREAD = 2;
    public static final int LOAD_REPLY = 3;
    public static final int LOAD_PICTURE = 4;
    public static final int CANCEL = 5;
    public static final int FINISHED = 6;

    private int mCurrentStatus = INIT;

    private List<Board> boards;
    private List<Board> unOfflineBoards;


    @ActionMethod(START_DOWNLOAD)
    public void startOffline(Intent intent) {
        if (mCurrentStatus == INIT) {
            boards = (List<Board>) intent.getSerializableExtra("boards");
            prepareOffline();
        } else {
            //ignore
        }
    }

    private void prepareOffline() {
        mCurrentStatus = PREPARE;
        unOfflineBoards = new ArrayList<>();
        unOfflineBoards.addAll(boards);

    }

    private boolean isCanceled() {
        return mCurrentStatus == CANCEL || mCurrentStatus == FINISHED;
    }

    class LoadThreadTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return null;
        }
    }
}
