package com.gzsll.hupu.presenter;

import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gzsll.hupu.Constants;
import com.gzsll.hupu.support.storage.bean.UploadInfo;
import com.gzsll.hupu.view.UploadView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/6/23.
 */
public class UploadPresenter extends Presenter<UploadView> {

    @Inject
    TransferManager transferManager;

    private List<PutObjectRequest> requests = new ArrayList<>();

    @Override
    public void initialize() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }


    public Upload uploadFile(UploadInfo uploadInfo, ProgressListener progressListener) {
        File file = new File(uploadInfo.uploadPath);
        PutObjectRequest withGeneralProgressListener = new PutObjectRequest(Constants.BOX_BUCKET_NAME, file.getName(), file).withCannedAcl(CannedAccessControlList.PublicRead).withGeneralProgressListener(progressListener);
        uploadInfo.url = "http://bbsmobile.hupucdn.com/" + file.getName();
        Upload upload = transferManager.upload(withGeneralProgressListener);
        requests.add(withGeneralProgressListener);
        return upload;
    }


}
