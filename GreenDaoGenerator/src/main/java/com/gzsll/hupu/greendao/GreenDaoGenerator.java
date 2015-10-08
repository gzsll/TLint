package com.gzsll.hupu.greendao;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {

    public static final int VERSION = 40;
    public static final String GREEN_DAO_CODE_PATH = "../HuPu-TL/src/main/greendao";

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

        File f = new File(GREEN_DAO_CODE_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }

        new DaoGenerator().generateAll(schema, f.getAbsolutePath());
    }
}
