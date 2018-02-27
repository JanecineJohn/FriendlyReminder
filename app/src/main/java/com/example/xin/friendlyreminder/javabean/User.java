package com.example.xin.friendlyreminder.javabean;

import android.graphics.drawable.Drawable;

/**
 * Created by xin on 2017/11/18.
 * 新增了一个属性，letter，不知道会不会有影响
 */

public class User {
    /*
    connection in mysql
     */
    private int userId;
    private String userName;//用户名(not_Judge)
    private String userPassword;//密码
    private String userPhone;//手机号(11数字)
    private Drawable userIcon;//用户头像，先使用默认
    private String letter;//此属性用于区分这个名字是英语汉语，还是特殊字符(#),又或是需要放到顶部的(↑)

    public int getUserID() {
        return userId;
    }

    public void setUserID(int userID) {
        this.userId = userID;
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

    public Drawable getUserIcon(){
        return userIcon;
    }
    public void  setUserIcon(Drawable userIcon){
        this.userIcon = userIcon;
    }

    public String getLetter(){
        return letter;
    }
    public void setLetter(String letter){
        this.letter = letter;
    }

}
