package com.gzsll.hupu.storage.bean;

/**
 * Created by sll on 2015/5/28.
 */
public class Topic {


    /**
     * lights : 2
     * id : 1526485
     * groupId : 1
     * groupName : 火箭专区
     * createAt : 2个月前
     * title : 平胸而论：你认为今夏贝弗利，特里，约什，布鲁尔能留下几个
     * replies : 75
     * userInfo : {"uid":18843045,"icon":"http://i1.hoopchina.com.cn/user/045/18843045/18843045_big_2.jpg","sex":1,"username":"拼图阿里扎","level":13,"postNum":43,"synctime":1437877505,"badge":{"small":["http://bbsmobile.hupucdn.com/1432198236.8985-post-gray.png","http://bbsmobile.hupucdn.com/1432198524.5019-reply-gray.png","http://bbsmobile.hupucdn.com/1432198901.1229-light-gray.png"]},"favoriteNum":0,"groups":"6","banned":0,"replyNum":0}
     * groups : {"uid":0,"groupName":"火箭专区","orderBy":99990,"categoryId":2,"groupCover":"http://i1.hoopchina.com.cn/blogfile/201504/09/BbsImg142856991444643_3884*2564big.jpg","groupArticle":17325,"pid":44,"groupAvator":"http://b1.hoopchina.com.cn/images/hupu_app/group/nba/1_18.png?v=5","groupNote":"红色国度，火箭球迷的乐园！这里有关于火箭的一切内容和话题，也等待着你的添砖加瓦！","id":1,"categoryName":"球队分区","createAt":1402038962,"groupBoardId":1,"colorStyle":1,"color":"CC0000","groupMember":118555}
     */
    private int lights;
    private int id;
    private int groupId;
    private String groupName;
    private String createAt;
    private String title;
    private int replies;
    private UserInfo userInfo;
    private Group groups;

    public void setLights(int lights) {
        this.lights = lights;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setGroups(Group groups) {
        this.groups = groups;
    }

    public int getLights() {
        return lights;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public String getTitle() {
        return title;
    }

    public int getReplies() {
        return replies;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public Group getGroups() {
        return groups;
    }


}
