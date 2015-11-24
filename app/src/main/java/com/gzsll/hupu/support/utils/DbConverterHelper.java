package com.gzsll.hupu.support.utils;

import com.gzsll.hupu.support.db.DBGroupThread;
import com.gzsll.hupu.support.db.DBGroups;
import com.gzsll.hupu.support.db.DBGroupsDao;
import com.gzsll.hupu.support.db.DBMiniReplyListItem;
import com.gzsll.hupu.support.db.DBMiniReplyListItemDao;
import com.gzsll.hupu.support.db.DBThreadInfo;
import com.gzsll.hupu.support.db.DBThreadReplyItem;
import com.gzsll.hupu.support.db.DBThreadReplyItemDao;
import com.gzsll.hupu.support.db.DBUserInfo;
import com.gzsll.hupu.support.db.DBUserInfoDao;
import com.gzsll.hupu.support.storage.bean.GroupThread;
import com.gzsll.hupu.support.storage.bean.Groups;
import com.gzsll.hupu.support.storage.bean.MiniReplyListItem;
import com.gzsll.hupu.support.storage.bean.ThreadInfo;
import com.gzsll.hupu.support.storage.bean.ThreadReplyItem;
import com.gzsll.hupu.support.storage.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sll on 2015/11/24.
 */
public class DbConverterHelper {

    @Inject
    DBUserInfoDao mUserInfoDao;

    public DBGroupThread converGroupThread(GroupThread thread) {
        DBGroupThread dbGroupThread = new DBGroupThread();
        dbGroupThread.setCreateAtUnixTime((long) thread.getCreateAtUnixtime());
        dbGroupThread.setLights(thread.getLights());
        dbGroupThread.setNote(thread.getNote());
        dbGroupThread.setReplies(thread.getReplies());
        dbGroupThread.setServerId(thread.getServerId());
        dbGroupThread.setTid(thread.getTid());
        dbGroupThread.setTitle(thread.getTitle());
        dbGroupThread.setUsername(thread.getUsername());
        updateUserInfo(thread.getUserInfo());
        dbGroupThread.setUserId((long) thread.getUserInfo().getUid());
        return dbGroupThread;
    }

    public List<GroupThread> coverDbGroupThreads(List<DBGroupThread> threads) {
        List<GroupThread> groupThreads = new ArrayList<>();
        for (DBGroupThread thread : threads) {
            groupThreads.add(converDbGroupThread(thread));
        }
        return groupThreads;
    }

    public GroupThread converDbGroupThread(DBGroupThread thread) {
        GroupThread groupThread = new GroupThread();
        groupThread.setLights(thread.getLights());
        groupThread.setNote(thread.getNote());
        groupThread.setReplies(thread.getReplies());
        groupThread.setServerId(thread.getServerId());
        groupThread.setTid(thread.getTid());
        groupThread.setTitle(thread.getTitle());
        groupThread.setUsername(thread.getUsername());
        List<DBUserInfo> userInfos = mUserInfoDao.queryBuilder().where(DBUserInfoDao.Properties.Uid.eq(thread.getUserId())).list();
        if (!userInfos.isEmpty()) {
            groupThread.setUserInfo(converDbUserInfo(userInfos.get(0)));
        }
        return groupThread;
    }

    public DBUserInfo converUserInfo(UserInfo userInfo) {
        DBUserInfo dbUserInfo = new DBUserInfo();
        dbUserInfo.setUsername(userInfo.getUsername());
        dbUserInfo.setFavoriteNum(userInfo.getFavoriteNum());
        dbUserInfo.setGroups(userInfo.getGroups());
        dbUserInfo.setIcon(userInfo.getIcon());
        dbUserInfo.setLevel(userInfo.getLevel());
        dbUserInfo.setPostNum(userInfo.getPostNum());
        dbUserInfo.setReplyNum(userInfo.getReplyNum());
        dbUserInfo.setSex(userInfo.getSex());
        dbUserInfo.setSyncTime(userInfo.getSynctime());
        dbUserInfo.setUid(userInfo.getUid());
        return dbUserInfo;
    }

    public UserInfo converDbUserInfo(DBUserInfo userInfo) {
        UserInfo dbUserInfo = new UserInfo();
        dbUserInfo.setUsername(userInfo.getUsername());
        dbUserInfo.setFavoriteNum(userInfo.getFavoriteNum());
        dbUserInfo.setGroups(userInfo.getGroups());
        dbUserInfo.setIcon(userInfo.getIcon());
        dbUserInfo.setLevel(userInfo.getLevel());
        dbUserInfo.setPostNum(userInfo.getPostNum());
        dbUserInfo.setReplyNum(userInfo.getReplyNum());
        dbUserInfo.setSex(userInfo.getSex());
        dbUserInfo.setSynctime(userInfo.getSyncTime());
        dbUserInfo.setUid(userInfo.getUid());
        return dbUserInfo;
    }

    @Inject
    DBGroupsDao mGroupsDao;

    public DBThreadInfo converThreadInfo(ThreadInfo info) {
        DBThreadInfo threadInfo = new DBThreadInfo();
        threadInfo.setUid(info.getUid());
        threadInfo.setAttention(info.getAttention());
        threadInfo.setContent(info.getContent());
        threadInfo.setCreateAt(info.getCreateAt());
        threadInfo.setCreateAtUnixTime(info.getCreateAtUnixtime());
        threadInfo.setDigest(info.getDigest());
        threadInfo.setGroupId(info.getGroupId());
        threadInfo.setLastReplyTime(info.getLastReplyTime());
        threadInfo.setLights(info.getLights());
        threadInfo.setSharedImg(info.getSharedImg());
        threadInfo.setSpecial(info.getSpecial());
        threadInfo.setTid(info.getTid());
        threadInfo.setTitle(info.getTitle());
        threadInfo.setZan(info.getZan());
        threadInfo.setNote(info.getNote());
        updateUserInfo(info.getUserInfo());
        threadInfo.setUserId((long) info.getUid());
        List<DBGroups> groupsList = mGroupsDao.queryBuilder().where(DBGroupsDao.Properties.ServerId.eq(info.getGroupId())).list();
        DBGroups groups = converGroups(info.getGroups());
        if (!groupsList.isEmpty()) {
            groups.setId(groupsList.get(0).getId());
        }
        mGroupsDao.insertOrReplace(groups);
        threadInfo.setGroupsId(info.getGroupId());
        return threadInfo;
    }


    public ThreadInfo converDbThreadInfo(DBThreadInfo info) {
        ThreadInfo threadInfo = new ThreadInfo();
        threadInfo.setUid(info.getUid());
        threadInfo.setAttention(info.getAttention());
        threadInfo.setContent(info.getContent());
        threadInfo.setCreateAt(info.getCreateAt());
        threadInfo.setCreateAtUnixtime(info.getCreateAtUnixTime());
        threadInfo.setDigest(info.getDigest());
        threadInfo.setGroupId(info.getGroupId());
        threadInfo.setLastReplyTime(info.getLastReplyTime());
        threadInfo.setLights(info.getLights());
        threadInfo.setSharedImg(info.getSharedImg());
        threadInfo.setSpecial(info.getSpecial());
        threadInfo.setTid(info.getTid());
        threadInfo.setTitle(info.getTitle());
        threadInfo.setZan(info.getZan());
        threadInfo.setNote(info.getNote());
        List<DBGroups> groupsList = mGroupsDao.queryBuilder().where(DBGroupsDao.Properties.ServerId.eq(info.getGroupId())).list();
        if (!groupsList.isEmpty()) {
            threadInfo.setGroups(converDbGroups(groupsList.get(0)));
        }
        List<DBUserInfo> userInfos = mUserInfoDao.queryBuilder().where(DBUserInfoDao.Properties.Uid.eq(info.getUserId())).list();
        if (!userInfos.isEmpty()) {
            threadInfo.setUserInfo(converDbUserInfo(userInfos.get(0)));
        }
        return threadInfo;
    }

    private void updateUserInfo(UserInfo info) {
        List<DBUserInfo> userInfos = mUserInfoDao.queryBuilder().where(DBUserInfoDao.Properties.Uid.eq(info.getUid())).list();
        DBUserInfo userInfo = converUserInfo(info);
        if (!userInfos.isEmpty()) {
            userInfo.setId(userInfos.get(0).getId());
        }
        mUserInfoDao.insertOrReplace(userInfo);
    }

    public DBGroups converGroups(Groups groups) {
        DBGroups dbGroups = new DBGroups();
        dbGroups.setGroupName(groups.getGroupName());
        dbGroups.setServerId(groups.getServerId());
        return dbGroups;
    }

    public Groups converDbGroups(DBGroups dbGroups) {
        Groups groups = new Groups();
        groups.setServerId(dbGroups.getServerId());
        groups.setGroupName(dbGroups.getGroupName());
        return groups;
    }

    @Inject
    DBMiniReplyListItemDao mMiniReplyDao;
    @Inject
    DBThreadReplyItemDao mReplyDao;

    public DBThreadReplyItem converThreadReplyItem(ThreadReplyItem item, boolean isHot) {
        DBThreadReplyItem replyItem = new DBThreadReplyItem();
        replyItem.setServerId(item.getId());
        replyItem.setContent(item.getContent());
        replyItem.setCreateAt(item.getCreate_at());
        replyItem.setFloor(item.getFloor());
        replyItem.setGroupThreadId((long) item.getGroupThreadId());
        replyItem.setIsHot(isHot);
        replyItem.setLights(item.getLights());
        replyItem.setPid((long) item.getPid());
        if (!item.getMiniReplyList().getLists().isEmpty()) {
            for (MiniReplyListItem miniReplyListItem : item.getMiniReplyList().getLists()) {
                DBMiniReplyListItem dbMiniReplyListItem = converMiniReplyListItem(miniReplyListItem, item.getId());
                List<DBMiniReplyListItem> dbMiniReplyListItems = mMiniReplyDao.queryBuilder().where(DBMiniReplyListItemDao.Properties.Id.eq(miniReplyListItem.getId())).list();
                if (!dbMiniReplyListItems.isEmpty()) {
                    dbMiniReplyListItem.setId(dbMiniReplyListItems.get(0).getId());
                }
                mMiniReplyDao.insertOrReplace(dbMiniReplyListItem);
            }
        }
        updateUserInfo(item.getUserInfo());
        replyItem.setUserId((long) item.getUserInfo().getUid());
        return replyItem;

    }


    public DBMiniReplyListItem converMiniReplyListItem(MiniReplyListItem item, long parentId) {
        DBMiniReplyListItem replyListItem = new DBMiniReplyListItem();
        replyListItem.setPid((long) item.getPid());
        replyListItem.setGroupThreadId((long) item.getGroupThreadId());
        replyListItem.setContent(item.getContent());
        replyListItem.setFormatTime(item.getFormatTime());
        replyListItem.setServerId(item.getId());
        replyListItem.setUserId((long) item.getUserInfo().getUid());
        replyListItem.setParentReplyId(parentId);
        return replyListItem;
    }
}
