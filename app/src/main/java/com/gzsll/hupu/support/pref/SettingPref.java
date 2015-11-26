package com.gzsll.hupu.support.pref;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.DefaultString;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by sll on 2015/5/16.
 */
@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface SettingPref {

    @DefaultInt(9)
    int ThemeIndex();


    @DefaultString("gzsll")
    String PicSavePath();


    @DefaultBoolean(false)
    boolean NightModel();

    @DefaultBoolean(false)
    boolean ShowPicOnlyWifi();

    @DefaultBoolean(true)
    boolean PushNotification();

    @DefaultBoolean(true)
    boolean Offline();


    @DefaultBoolean(false)
    boolean AutoClean();

    @DefaultBoolean(true)
    boolean AutoLoad();

    @DefaultBoolean(true)
    boolean AutoUpdate();

    @DefaultString("来自 TL For Android")
    String TAIL();


}
