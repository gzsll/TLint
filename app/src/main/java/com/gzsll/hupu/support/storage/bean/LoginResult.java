package com.gzsll.hupu.support.storage.bean;

import com.gzsll.hupu.support.db.User;

/**
 * Created by sll on 2015/3/8.
 */
public class LoginResult {

    public LoginError error;
    public LoginSuccess result;


    public class LoginSuccess {
        public User user;
    }


    public class LoginError {
        public String type;
        public String id;
        public String text;
    }
}
