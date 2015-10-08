package com.gzsll.hupu.storage.bean;

import java.util.List;

/**
 * Created by sll on 2015/7/1.
 */
public class AddReplyResult {


    /**
     * uid : 4847679
     * status : 200
     * data : {"id":62289424,"lights":0,"content":"sdfasdfasdfasdfadsfadsfadsf","groupThreadId":1749267,"addtime":1435759984,"isLight":0,"create_at":"刚刚","formatTime":"刚刚","pid":0,"userInfo":{"uid":4847679,"icon":"http://i1.hoopchina.com.cn/user/679/4847679/4847679_big_3.jpg","sex":0,"username":"pursll","level":14,"postNum":0,"synctime":1434097794,"badge":{"small":["http://bbsmobile.hupucdn.com/1432198236.8985-post-gray.png","http://bbsmobile.hupucdn.com/1432198524.5019-reply-gray.png","http://bbsmobile.hupucdn.com/1432198901.1229-light-gray.png"]},"favoriteNum":4,"groups":"8","banned":0,"replyNum":0}}
     * msg : ok
     */
    private int uid;
    private int status;
    private DataEntity data;
    private String msg;

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUid() {
        return uid;
    }

    public int getStatus() {
        return status;
    }

    public DataEntity getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public class DataEntity {
        /**
         * id : 62289424
         * lights : 0
         * content : sdfasdfasdfasdfadsfadsfadsf
         * groupThreadId : 1749267
         * addtime : 1435759984
         * isLight : 0
         * create_at : 刚刚
         * formatTime : 刚刚
         * pid : 0
         * userInfo : {"uid":4847679,"icon":"http://i1.hoopchina.com.cn/user/679/4847679/4847679_big_3.jpg","sex":0,"username":"pursll","level":14,"postNum":0,"synctime":1434097794,"badge":{"small":["http://bbsmobile.hupucdn.com/1432198236.8985-post-gray.png","http://bbsmobile.hupucdn.com/1432198524.5019-reply-gray.png","http://bbsmobile.hupucdn.com/1432198901.1229-light-gray.png"]},"favoriteNum":4,"groups":"8","banned":0,"replyNum":0}
         */
        private int id;
        private int lights;
        private String content;
        private int groupThreadId;
        private int addtime;
        private int isLight;
        private String create_at;
        private String formatTime;
        private int pid;
        private UserInfoEntity userInfo;

        public void setId(int id) {
            this.id = id;
        }

        public void setLights(int lights) {
            this.lights = lights;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setGroupThreadId(int groupThreadId) {
            this.groupThreadId = groupThreadId;
        }

        public void setAddtime(int addtime) {
            this.addtime = addtime;
        }

        public void setIsLight(int isLight) {
            this.isLight = isLight;
        }

        public void setCreate_at(String create_at) {
            this.create_at = create_at;
        }

        public void setFormatTime(String formatTime) {
            this.formatTime = formatTime;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public void setUserInfo(UserInfoEntity userInfo) {
            this.userInfo = userInfo;
        }

        public int getId() {
            return id;
        }

        public int getLights() {
            return lights;
        }

        public String getContent() {
            return content;
        }

        public int getGroupThreadId() {
            return groupThreadId;
        }

        public int getAddtime() {
            return addtime;
        }

        public int getIsLight() {
            return isLight;
        }

        public String getCreate_at() {
            return create_at;
        }

        public String getFormatTime() {
            return formatTime;
        }

        public int getPid() {
            return pid;
        }

        public UserInfoEntity getUserInfo() {
            return userInfo;
        }

        public class UserInfoEntity {
            /**
             * uid : 4847679
             * icon : http://i1.hoopchina.com.cn/user/679/4847679/4847679_big_3.jpg
             * sex : 0
             * username : pursll
             * level : 14
             * postNum : 0
             * synctime : 1434097794
             * badge : {"small":["http://bbsmobile.hupucdn.com/1432198236.8985-post-gray.png","http://bbsmobile.hupucdn.com/1432198524.5019-reply-gray.png","http://bbsmobile.hupucdn.com/1432198901.1229-light-gray.png"]}
             * favoriteNum : 4
             * groups : 8
             * banned : 0
             * replyNum : 0
             */
            private int uid;
            private String icon;
            private int sex;
            private String username;
            private int level;
            private int postNum;
            private int synctime;
            private BadgeEntity badge;
            private int favoriteNum;
            private String groups;
            private int banned;
            private int replyNum;

            public void setUid(int uid) {
                this.uid = uid;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public void setPostNum(int postNum) {
                this.postNum = postNum;
            }

            public void setSynctime(int synctime) {
                this.synctime = synctime;
            }

            public void setBadge(BadgeEntity badge) {
                this.badge = badge;
            }

            public void setFavoriteNum(int favoriteNum) {
                this.favoriteNum = favoriteNum;
            }

            public void setGroups(String groups) {
                this.groups = groups;
            }

            public void setBanned(int banned) {
                this.banned = banned;
            }

            public void setReplyNum(int replyNum) {
                this.replyNum = replyNum;
            }

            public int getUid() {
                return uid;
            }

            public String getIcon() {
                return icon;
            }

            public int getSex() {
                return sex;
            }

            public String getUsername() {
                return username;
            }

            public int getLevel() {
                return level;
            }

            public int getPostNum() {
                return postNum;
            }

            public int getSynctime() {
                return synctime;
            }

            public BadgeEntity getBadge() {
                return badge;
            }

            public int getFavoriteNum() {
                return favoriteNum;
            }

            public String getGroups() {
                return groups;
            }

            public int getBanned() {
                return banned;
            }

            public int getReplyNum() {
                return replyNum;
            }

            public class BadgeEntity {
                /**
                 * small : ["http://bbsmobile.hupucdn.com/1432198236.8985-post-gray.png","http://bbsmobile.hupucdn.com/1432198524.5019-reply-gray.png","http://bbsmobile.hupucdn.com/1432198901.1229-light-gray.png"]
                 */
                private List<String> small;

                public void setSmall(List<String> small) {
                    this.small = small;
                }

                public List<String> getSmall() {
                    return small;
                }
            }
        }
    }
}
