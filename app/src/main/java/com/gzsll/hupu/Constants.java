package com.gzsll.hupu;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sll on 2015/8/21.
 */
public class Constants {

    public static final int TYPE_COMMENT = 1001;
    public static final int TYPE_FEEDBACK = 1002;
    public static final int TYPE_AT = 1003;
    public static final int TYPE_POST = 1004;
    public static final int TYPE_REPLY = 1005;
    public static final int TYPE_QUOTE = 1006;
    public static final int TYPE_RECOMMEND = 0;
    public static final int TYPE_COLLECT = -2;
    public static final String THREAD_TYPE_HOT = "2";  //热帖
    public static final String THREAD_TYPE_NEW = "1";//最新的帖子
    public static final int NAV_SETTING = 10000;
    public static final int NAV_FEEDBACK = 10001;
    public static final int NAV_ABOUT = 10002;
    public static final String BOX_APP_KEY = "U8DCMNBEFLP1E9W60BIP";
    public static final String BOX_APP_SECRETE = "+m/4pmmh05RRxpZo/Hor1wPMQFihwJ+QinHGRdR0";
    public static final String BOX_BUCKET_NAME = "/img/Imgup";
    public static final String BOX_END_POINT = "http://s3.hupu.com";
    public static final String BOX_END_POINT_NEW = "http://bbs-test.mobileapi.hupu.com/";
    public static final String BOX_KEY_NAME = "SJW28AP753KGAWB0FN6Q";
    public final static String[] FORUMS_IDS = {
            "0", "1", "232", "174", "233", "234", "4596", "198", "41"
    };
    public final static Integer[] NAV_IDS = {
            R.id.nav_my, R.id.nav_nba, R.id.nav_cba, R.id.nav_gambia, R.id.nav_equipment,
            R.id.nav_fitness, R.id.nav_football, R.id.nav_intel_football, R.id.nav_sport
    };
    public static final Map<Integer, String> mNavMap = new HashMap<>();
    public static final String UPDATE_URL = "http://www.pursll.com/update.json";
    public static String Cookie = "";
    public static int NAV_TOPIC_LIST = -2;
    public static int NAV_TOPIC_FAV = -1;
    public static int NAV_THREAD_RECOMMEND = -3;

    static {
        for (int i = 0; i < NAV_IDS.length; i++) {
            mNavMap.put(NAV_IDS[i], FORUMS_IDS[i]);
        }
    }
}
