package com.example.xin.friendlyreminder.utils;

import com.example.xin.friendlyreminder.javabean.User;

import java.util.Comparator;

/**
 * Created by xin on 2017/11/30.
 * 排序类
 * //@标签代表A前面的那些，#代表除了A-Z以外的其他标签
 * 返回1，表示：将user1放后面，排序为user2，user1
 * 返回-1，表示：将user1放前面，排序为user1，user2
 */

public class CompareSort implements Comparator<User> {
    @Override
    public int compare(User user1, User user2) {
        if(user1.getLetter().equals("↑") || user2.getLetter().equals("↑")){
            //通讯录前面的ｉｔｅｍ(公众号，标签......)
            return user1.getLetter().equals("↑") ? -1:1;
        }
        //如果两个都不是letter为@，就进入下面的判断
        //user1属于#标签，放到后面
        else if(!user1.getLetter().matches("[A-z]+")){
            return 1;//user1的letter是#，返回1
            //user2属于#标签，放到后面
        }else if(!user2.getLetter().matches("[A-z]+")){
            return -1;
        }else {
            return user1.getLetter().compareTo(user2.getLetter());//比较字母大小，字母小的那个放前面(user1小于user2，返回-1,user1排在前)
        }
    }
}