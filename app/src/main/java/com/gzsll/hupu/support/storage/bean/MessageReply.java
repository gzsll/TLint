package com.gzsll.hupu.support.storage.bean;

/**
 * Created by sll on 2015/9/8 0008.
 */
public class MessageReply {

    private ThreadInfo threadInfo;
    private Groups groupInfo;
    private int num;
    private int isread;

    public ThreadInfo getThreadInfo() {
        return threadInfo;
    }

    public void setThreadInfo(ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
    }

    public Groups getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(Groups groupInfo) {
        this.groupInfo = groupInfo;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }
}
