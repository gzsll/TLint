package com.gzsll.hupu.bean;

/**
 * Created by sll on 2015/3/8.
 */
public class LoginData {

    /**
     * {"result":{"uid":"1952333","username":"pursll","nickname":"pursll","token":"MTk1MjMzMw==|MTQ1MTMxMTk0OA==|e35adbf409b0eca72843b112c5f7c85c","balance":0,"hupuDollor_balance":1,"follow":{"lids":[1,12,3,2,7,6,5,4,9],"tids":{"1":[24,27,9],"2":[]}},"bind":[{"channel":2,"status":1,"is_bind":0,"bind_name":""},{"channel":1,"status":1,"is_bind":1,"bind_name":"18*******62"},{"channel":3,"status":1,"is_bind":1,"bind_name":""}],"show_bind":0},"is_login":1}
     */
    public LoginResult result;
    public int is_login;
}
