package com.gzsll.hupu.greendao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static final int VERSION = 51;
    public static final String GREEN_DAO_CODE_PATH = "../TLint/app/src/main/java";

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(VERSION, "com.gzsll.hupu.db");

        Entity forum = schema.addEntity("Forum");

        forum.addIdProperty();
        forum.addStringProperty("fid");
        forum.addStringProperty("name");
        forum.addStringProperty("logo");
        forum.addStringProperty("description");
        forum.addStringProperty("backImg");
        forum.addStringProperty("forumId");
        forum.addStringProperty("categoryName");
        forum.addIntProperty("weight");


        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("userName");
        user.addStringProperty("uid");
        user.addStringProperty("token");
        user.addStringProperty("icon");
        user.addIntProperty("sex");
        user.addStringProperty("cookie");
        user.addStringProperty("registerTime");
        user.addStringProperty("location");
        user.addStringProperty("school");
        user.addStringProperty("threadUrl");
        user.addStringProperty("postUrl");
        user.addStringProperty("nickNameUrl");


        File f = new File(GREEN_DAO_CODE_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }

        new DaoGenerator().generateAll(schema, f.getAbsolutePath());
    }
}
