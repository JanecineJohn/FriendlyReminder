package com.example.xin.friendlyreminder.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dell on 2018/2/6.
 */

public class userInfoManager {

    SharedPreferences pref;//读
    SharedPreferences.Editor editor;//写

    public userInfoManager(Context context){
        pref = context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    }
    /**
     * editor.putInt("userId",data.getInt("userId"));//将用户ID写进文件
     editor.putString("userName",data.getString("userName"));
     editor.putString("userPhone",data.getString("userPhone"));
     */

    /**
     * 返回登录用户的用户信息
     */
    public int getMyUserId(){
        int userId = pref.getInt("userId",-1);
        return userId;
    }

    public String getMyUserName(){
        String userName = pref.getString("userName","");
        return userName;
    }

    public String getMyUserPhone(){
        String userPhone = pref.getString("userPhone","");
        return userPhone;
    }

    /**
     * 写入登录用户的用户信息
     */
    public void setMyUserMessage(int userId,String userName,String userPhone){
        pref.edit().putInt("userId",userId)
                .putString("userName",userName)
                .putString("userPhone",userPhone)
                .apply();
    }
    public void setMyUserId(int userId){
        pref.edit().putInt("userId",userId).apply();
    }
    public void setMyUserName(String userName){
        pref.edit().putString("userName",userName).apply();
    }
    public void setMyUserPhone(String userPhone){
        pref.edit().putString("userPhone",userPhone).apply();
    }

    /**
     * 清除账号信息
     */
    public void clearMyUserMessage(){
        pref.edit().clear().apply();
    }
}
