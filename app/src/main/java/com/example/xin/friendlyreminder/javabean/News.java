package com.example.xin.friendlyreminder.javabean;

/**
 * Created by xin on 2017/11/7.
 *
 */

public class News {
    private String news;

    public News(String news){
        //将消息初始化为News对象
        this.news = news;
    }

    public String getNews() {
        return news;
    }
}
