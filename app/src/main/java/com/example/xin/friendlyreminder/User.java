package com.example.xin.friendlyreminder;

/**
 * Created by xin on 2017/11/18.
 */

public class User {
    /*
    connection in mysql
     */
    private int userID;
    private String userName;//用户名(not_Judge)
    private String userPassword;//密码
    private String userPhone;//手机号(11数字)

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

}
