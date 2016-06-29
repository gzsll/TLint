package com.gzsll.hupu.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
import java.io.File;

public class GreenDaoGenerator {

  public static final int VERSION = 53;
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

    Entity thread = schema.addEntity("Thread");
    thread.addStringProperty("tid");
    thread.addStringProperty("title");
    thread.addStringProperty("puid");
    thread.addStringProperty("fid");
    thread.addStringProperty("replies");
    thread.addStringProperty("userName");
    thread.addStringProperty("time");
    thread.addStringProperty("forumName");
    thread.addIntProperty("lightReply");
    thread.addIntProperty("type");

    Entity threadInfo = schema.addEntity("ThreadInfo");
    threadInfo.addStringProperty("tid");
    threadInfo.addStringProperty("pid");
    threadInfo.addIntProperty("page");
    threadInfo.addStringProperty("nopic");
    threadInfo.addIntProperty("postAuthorPuid");
    threadInfo.addStringProperty("time");
    threadInfo.addStringProperty("userImg");
    threadInfo.addStringProperty("author_puid");
    threadInfo.addStringProperty("username");
    threadInfo.addStringProperty("fid");
    threadInfo.addStringProperty("visits");
    threadInfo.addStringProperty("recommend_num");
    threadInfo.addStringProperty("via");
    threadInfo.addStringProperty("update_info");
    threadInfo.addStringProperty("content");
    threadInfo.addStringProperty("title");
    threadInfo.addIntProperty("totalPage");
    threadInfo.addStringProperty("forumName");

    Entity reply = schema.addEntity("ThreadReply");
    reply.addStringProperty("tid");
    reply.addStringProperty("pid");
    reply.addStringProperty("puid");
    reply.addStringProperty("via");
    reply.addStringProperty("content");
    reply.addStringProperty("create_time");
    reply.addStringProperty("update_info");
    reply.addIntProperty("light_count");
    reply.addIntProperty("user_banned");
    reply.addIntProperty("floor");
    reply.addStringProperty("time");
    reply.addStringProperty("userName");
    reply.addStringProperty("userImg");
    reply.addStringProperty("smallcontent");
    reply.addStringProperty("togglecontent");
    reply.addIntProperty("index");
    reply.addBooleanProperty("isLight");
    reply.addStringProperty("quoteHeader");
    reply.addStringProperty("quoteContent");
    reply.addStringProperty("quoteToggle");
    reply.addIntProperty("pageIndex");

    Entity readThread = schema.addEntity("ReadThread");
    readThread.addIdProperty();
    readThread.addStringProperty("tid");

    Entity image = schema.addEntity("ImageCache");
    image.addIdProperty();
    image.addStringProperty("url");
    image.addStringProperty("path");

    File f = new File(GREEN_DAO_CODE_PATH);
    if (!f.exists()) {
      f.mkdirs();
    }

    new DaoGenerator().generateAll(schema, f.getAbsolutePath());
  }
}
