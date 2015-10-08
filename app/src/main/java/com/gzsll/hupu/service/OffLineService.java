package com.gzsll.hupu.service;

import com.gzsll.hupu.BuildConfig;
import com.gzsll.hupu.service.annotation.IntentAnnotationService;

import org.apache.log4j.Logger;

/**
 * Created by gzsll on 2014/9/14 0014.
 */
public class OffLineService extends IntentAnnotationService {

    Logger logger = Logger.getLogger("OffLineService");


    public static final String START_DOWNLOAD = BuildConfig.APPLICATION_ID + ".action.START_DOWNLOAD";


}
