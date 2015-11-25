package com.gzsll.hupu.greendao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static final int VERSION = 45;
    public static final String GREEN_DAO_CODE_PATH = "../TLint/src/main/greendao";

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(VERSION, "com.gzsll.hupu.support.db");

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


        Entity groupThread = schema.addEntity("DBGroupThread");
        groupThread.addIdProperty();
        groupThread.addIntProperty("lights");
        groupThread.addStringProperty("username");
        groupThread.addLongProperty("createAtUnixTime");
        groupThread.addStringProperty("title");
        groupThread.addStringProperty("note");
        groupThread.addIntProperty("replies");
        groupThread.addIntProperty("tid");
        groupThread.addLongProperty("serverId");
        groupThread.addLongProperty("userId"); //关联UserInfo
        groupThread.addLongProperty("coverId");
        groupThread.addLongProperty("groupId");


        Entity cover = schema.addEntity("DBCover");
        cover.addIdProperty();
        cover.addStringProperty("urlSmall");
        cover.addIntProperty("height");
        cover.addStringProperty("url");
        cover.addIntProperty("width");


        Entity userInfo = schema.addEntity("DBUserInfo");
        userInfo.addIdProperty();
        userInfo.addStringProperty("username");
        userInfo.addIntProperty("syncTime");
        userInfo.addIntProperty("uid");
        userInfo.addStringProperty("icon");
        userInfo.addIntProperty("replyNum");
        userInfo.addIntProperty("postNum");
        userInfo.addStringProperty("groups");
        userInfo.addIntProperty("sex");
        userInfo.addIntProperty("favoriteNum");
        userInfo.addIntProperty("level");


        Entity threadInfo = schema.addEntity("DBThreadInfo");
        threadInfo.addIdProperty();
        threadInfo.addLongProperty("serverId");
        threadInfo.addIntProperty("uid");
        threadInfo.addIntProperty("lastReplyTime");
        threadInfo.addIntProperty("special");
        threadInfo.addIntProperty("lights");
        threadInfo.addIntProperty("attention");
        threadInfo.addIntProperty("type");
        threadInfo.addIntProperty("zan");
        threadInfo.addIntProperty("digest");
        threadInfo.addLongProperty("createAtUnixTime");
        threadInfo.addIntProperty("replies");
        threadInfo.addLongProperty("groupId");
        threadInfo.addLongProperty("tid");
        threadInfo.addStringProperty("sharedImg");
        threadInfo.addStringProperty("username");
        threadInfo.addStringProperty("createAt");
        threadInfo.addStringProperty("title");
        threadInfo.addStringProperty("note");
        threadInfo.addStringProperty("content");
        threadInfo.addLongProperty("userId");
        threadInfo.addLongProperty("groupsId");


        Entity groups = schema.addEntity("DBGroups");
        groups.addIdProperty();
        groups.addStringProperty("groupName");
        groups.addLongProperty("serverId");
        groups.addStringProperty("groupCover");


        Entity threadReplyItem = schema.addEntity("DBThreadReplyItem");
        threadReplyItem.addIdProperty();
        threadReplyItem.addLongProperty("serverId");
        threadReplyItem.addLongProperty("pid");
        threadReplyItem.addStringProperty("createAt");
        threadReplyItem.addIntProperty("lights");
        threadReplyItem.addIntProperty("floor");
        threadReplyItem.addLongProperty("groupThreadId");
        threadReplyItem.addLongProperty("userId");
        threadReplyItem.addStringProperty("content");
        threadReplyItem.addBooleanProperty("isHot");

        Entity miniReplyListItem = schema.addEntity("DBMiniReplyListItem");
        miniReplyListItem.addIdProperty();
        miniReplyListItem.addLongProperty("serverId");
        miniReplyListItem.addLongProperty("pid");
        miniReplyListItem.addStringProperty("formatTime");
        miniReplyListItem.addLongProperty("groupThreadId");
        miniReplyListItem.addLongProperty("userId");
        miniReplyListItem.addStringProperty("content");
        miniReplyListItem.addLongProperty("parentReplyId");


        File f = new File(GREEN_DAO_CODE_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }

        new DaoGenerator().generateAll(schema, f.getAbsolutePath());
    }
}
