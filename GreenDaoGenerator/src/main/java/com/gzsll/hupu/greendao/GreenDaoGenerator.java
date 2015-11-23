package com.gzsll.hupu.greendao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static final int VERSION = 41;
    public static final String GREEN_DAO_CODE_PATH = "../TLint/src/main/greendao";

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(VERSION, "com.hupu.db");

        Entity board = schema.addEntity("Board");

        board.addIdProperty();
        board.addLongProperty("BoardId");
        board.addStringProperty("CategoryName");
        board.addLongProperty("CategoryId");
        board.addStringProperty("BoardName");
        board.addStringProperty("BoardIcon");
        board.addLongProperty("GroupId");
        board.addIntProperty("BoardIndex");


        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("userName");
        user.addStringProperty("uid");
        user.addStringProperty("token");
        user.addStringProperty("icon");
        user.addIntProperty("syncTime");
        user.addIntProperty("sex");
        user.addIntProperty("level");
        user.addBooleanProperty("isLogin");


        Entity groupThread = schema.addEntity("GroupThread");
        groupThread.addIdProperty();
        groupThread.addIntProperty("lights");
        groupThread.addStringProperty("username");
        groupThread.addLongProperty("createAtUnixtime");
        groupThread.addStringProperty("title");
        groupThread.addStringProperty("note");
        groupThread.addIntProperty("replies");
        groupThread.addIntProperty("tid");


        Entity cover = schema.addEntity("Cover");
        cover.addStringProperty("urlSmall");
        cover.addIntProperty("height");
        cover.addStringProperty("url");
        cover.addIntProperty("width");

        Property coverPro = cover.addLongProperty("coverId").getProperty();
        groupThread.addToMany(cover, coverPro).setName("cover");

        Entity userInfo = schema.addEntity("UserInfo");
        userInfo.addIdProperty();
        userInfo.addStringProperty("username");
        userInfo.addIntProperty("synctime");
        userInfo.addIntProperty("uid");
        userInfo.addStringProperty("icon");
        userInfo.addIntProperty("replyNum");
        userInfo.addIntProperty("postNum");
        userInfo.addStringProperty("groups");
        userInfo.addIntProperty("sex");
        userInfo.addIntProperty("favoriteNum");
        userInfo.addIntProperty("level");

        Property property = groupThread.addLongProperty("uid").getProperty();
        userInfo.addToMany(groupThread, property).setName("threads");





        File f = new File(GREEN_DAO_CODE_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }

        new DaoGenerator().generateAll(schema, f.getAbsolutePath());
    }
}
