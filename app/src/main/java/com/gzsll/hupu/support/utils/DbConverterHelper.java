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
import com.gzsll.hupu.support.storage.bean.Info;
import com.gzsll.hupu.support.storage.bean.MiniReplyList;
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

    public DBGroupThread convertGroupThread(GroupThread thread) {
        DBGroupThread dbGroupThread = new DBGroupThread();
        dbGroupThread.setCreateAtUnixTime(thread.getCreateAtUnixtime());
        dbGroupThread.setLights(thread.getLights());
        dbGroupThread.setNote(thread.getNote());
        dbGroupThread.setReplies(thread.getReplies());
        dbGroupThread.setServerId(thread.getId());
        dbGroupThread.setTid(thread.getTid());
        dbGroupThread.setTitle(thread.getTitle());
        dbGroupThread.setUsername(thread.getUsername());
        dbGroupThread.setGroupId(thread.getGroupId());
        updateUserInfo(thread.getUserInfo());
        dbGroupThread.setUserId((long) thread.getUserInfo().getUid());
        return dbGroupThread;
    }

    public List<GroupThread> covertDbGroupThreads(List<DBGroupThread> threads) {
        List<GroupThread> groupThreads = new ArrayList<>();
        for (DBGroupThread thread : threads) {
            groupThreads.add(convertDbGroupThread(thread));
        }
        return groupThreads;
    }

    public GroupThread convertDbGroupThread(DBGroupThread thread) {
        GroupThread groupThread = new GroupThread();
        groupThread.setLights(thread.getLights());
        groupThread.setNote(thread.getNote());
        groupThread.setReplies(thread.getReplies());
        groupThread.setId(thread.getServerId());
        groupThread.setTid(thread.getTid());
        groupThread.setTitle(thread.getTitle());
        groupThread.setUsername(thread.getUsername());
        groupThread.setUserInfo(getUserInfo(thread.getUserId()));
        groupThread.setCreateAtUnixtime(thread.getCreateAtUnixTime());
        return groupThread;
    }

    public UserInfo getUserInfo(long uid) {
        List<DBUserInfo> userInfos = mUserInfoDao.queryBuilder().where(DBUserInfoDao.Properties.Uid.eq(uid)).list();
        if (!userInfos.isEmpty()) {
            return convertDbUserInfo(userInfos.get(0));
        }
        return null;
    }

    public Info convertDbGroupsToInfo(DBGroups dbGroups) {
        Info groups = new Info();
        groups.setId(dbGroups.getServerId());
        groups.setGroupName(dbGroups.getGroupName());
        groups.setGroupCover(dbGroups.getGroupCover());
        return groups;
    }

    public DBUserInfo convertUserInfo(UserInfo userInfo) {
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

    public UserInfo convertDbUserInfo(DBUserInfo userInfo) {
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

    public DBThreadInfo convertThreadInfo(ThreadInfo info) {
        DBThreadInfo threadInfo = new DBThreadInfo();
        threadInfo.setServerId(info.getId());
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
        threadInfo.setReplies(info.getReplies());
        updateUserInfo(info.getUserInfo());
        threadInfo.setUserId((long) info.getUid());
        List<DBGroups> groupsList = mGroupsDao.queryBuilder().where(DBGroupsDao.Properties.ServerId.eq(info.getGroupId())).list();
        DBGroups groups = convertGroups(info.getGroups());
        if (!groupsList.isEmpty()) {
            groups.setId(groupsList.get(0).getId());
        }
        mGroupsDao.insertOrReplace(groups);
        threadInfo.setGroupsId(info.getGroupId());
        return threadInfo;
    }

    @Inject
    HtmlHelper mHtmlHelper;


    public ThreadInfo convertDbThreadInfo(DBThreadInfo info) {
        ThreadInfo threadInfo = new ThreadInfo();
        threadInfo.setId(info.getServerId());
        threadInfo.setUid(info.getUid());
        threadInfo.setAttention(info.getAttention());
        threadInfo.setContent(mHtmlHelper.transImgToLocal(info.getContent(), false));
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
        threadInfo.setReplies(info.getReplies());
        List<DBGroups> groupsList = mGroupsDao.queryBuilder().where(DBGroupsDao.Properties.ServerId.eq(info.getGroupId())).list();
        if (!groupsList.isEmpty()) {
            threadInfo.setGroups(convertDbGroups(groupsList.get(0)));
        }
        List<DBUserInfo> userInfos = mUserInfoDao.queryBuilder().where(DBUserInfoDao.Properties.Uid.eq(info.getUserId())).list();
        if (!userInfos.isEmpty()) {
            threadInfo.setUserInfo(convertDbUserInfo(userInfos.get(0)));
        }
        return threadInfo;
    }

    private void updateUserInfo(UserInfo info) {
        List<DBUserInfo> userInfos = mUserInfoDao.queryBuilder().where(DBUserInfoDao.Properties.Uid.eq(info.getUid())).list();
        DBUserInfo userInfo = convertUserInfo(info);
        if (!userInfos.isEmpty()) {
            userInfo.setId(userInfos.get(0).getId());
        }
        mUserInfoDao.insertOrReplace(userInfo);
    }

    public DBGroups convertGroups(Groups groups) {
        DBGroups dbGroups = new DBGroups();
        dbGroups.setGroupName(groups.getGroupName());
        dbGroups.setServerId(groups.getId());
        dbGroups.setGroupCover(groups.getGroupCover());
        return dbGroups;
    }

    public Groups convertDbGroups(DBGroups dbGroups) {
        Groups groups = new Groups();
        groups.setId(dbGroups.getServerId());
        groups.setGroupName(dbGroups.getGroupName());
        groups.setGroupCover(dbGroups.getGroupCover());
        return groups;
    }

    @Inject
    DBMiniReplyListItemDao mMiniReplyDao;
    @Inject
    DBThreadReplyItemDao mReplyDao;

    public DBThreadReplyItem convertThreadReplyItem(ThreadReplyItem item, boolean isHot) {
        DBThreadReplyItem replyItem = new DBThreadReplyItem();
        replyItem.setServerId(item.getId());
        replyItem.setContent(item.getContent());
        replyItem.setCreateAt(item.getCreate_at());
        replyItem.setFloor(item.getFloor());
        replyItem.setGroupThreadId(item.getGroupThreadId());
        replyItem.setIsHot(isHot);
        replyItem.setLights(item.getLights());
        replyItem.setPid(item.getPid());
        if (!item.getMiniReplyList().getLists().isEmpty()) {
            for (MiniReplyListItem miniReplyListItem : item.getMiniReplyList().getLists()) {
                DBMiniReplyListItem dbMiniReplyListItem = convertMiniReplyListItem(miniReplyListItem, item.getId());
                List<DBMiniReplyListItem> dbMiniReplyListItems = mMiniReplyDao.queryBuilder().where(DBMiniReplyListItemDao.Properties.ServerId.eq(miniReplyListItem.getId())).list();
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

    public ThreadReplyItem convertDbThreadReplyItem(DBThreadReplyItem item) {
        ThreadReplyItem replyItem = new ThreadReplyItem();
        replyItem.setId(item.getServerId());
        replyItem.setContent(item.getContent());
        replyItem.setCreate_at(item.getCreateAt());
        replyItem.setFloor(item.getFloor());
        replyItem.setGroupThreadId(item.getGroupThreadId());
        replyItem.setLights(item.getLights());
        replyItem.setPid(item.getPid());
        replyItem.setUserInfo(getUserInfo(item.getUserId()));
        List<DBMiniReplyListItem> miniReplyListItems = mMiniReplyDao.queryBuilder().where(DBMiniReplyListItemDao.Properties.ParentReplyId.eq(item.getServerId())).list();
        if (!miniReplyListItems.isEmpty()) {
            replyItem.setMiniReplyList(convertDbMiniReplies(miniReplyListItems));
        }
        return replyItem;

    }

    public List<ThreadReplyItem> convertDbThreadReplyItems(List<DBThreadReplyItem> items) {
        List<ThreadReplyItem> replyItems = new ArrayList<>();
        for (DBThreadReplyItem item : items) {
            replyItems.add(convertDbThreadReplyItem(item));
        }
        return replyItems;
    }

    public DBMiniReplyListItem convertMiniReplyListItem(MiniReplyListItem item, long parentId) {
        DBMiniReplyListItem replyListItem = new DBMiniReplyListItem();
        replyListItem.setPid(item.getPid());
        replyListItem.setGroupThreadId(item.getGroupThreadId());
        replyListItem.setContent(item.getContent());
        replyListItem.setFormatTime(item.getFormatTime());
        replyListItem.setServerId(item.getId());
        updateUserInfo(item.getUserInfo());
        replyListItem.setUserId((long) item.getUserInfo().getUid());
        replyListItem.setParentReplyId(parentId);
        return replyListItem;
    }


    public MiniReplyListItem convertDbMiniReplyListItem(DBMiniReplyListItem item) {
        MiniReplyListItem replyListItem = new MiniReplyListItem();
        replyListItem.setPid(item.getPid());
        replyListItem.setGroupThreadId(item.getGroupThreadId());
        replyListItem.setContent(item.getContent());
        replyListItem.setFormatTime(item.getFormatTime());
        replyListItem.setId(item.getServerId());
        replyListItem.setUserInfo(getUserInfo(item.getUserId()));
        return replyListItem;
    }

    public MiniReplyList convertDbMiniReplies(List<DBMiniReplyListItem> miniReplyListItems) {
        MiniReplyList replyList = new MiniReplyList();
        List<MiniReplyListItem> miniReplyListItemList = new ArrayList<>();
        for (DBMiniReplyListItem item : miniReplyListItems) {
            miniReplyListItemList.add(convertDbMiniReplyListItem(item));
        }
        replyList.setLists(miniReplyListItemList);
        return replyList;
    }
}
