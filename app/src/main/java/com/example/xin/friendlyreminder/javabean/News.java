package com.example.xin.friendlyreminder.javabean;

/**
 * Created by xin on 2017/11/7.
 *
 */

public class News {
    private String fromUserName;//发送人
    private String noticeContent;//提醒内容
    private long noticeTime;//闹钟时间

    public News(String fromUserName,String noticeContent,long noticeTime){
        //将消息初始化为News对象
        this.fromUserName = fromUserName;
        this.noticeContent = noticeContent;
        this.noticeTime = noticeTime;
    }

    public String getFromUserName(){return fromUserName;}
    public String getNoticeContent(){return noticeContent;}
    public long getNoticeTime(){return noticeTime;}

    public void setFromUserName(String fromUserName){this.fromUserName = fromUserName;}
    public void setNoticeContent(String noticeContent){this.noticeContent = noticeContent;}
    public void setNoticeTime(long noticeTime){this.noticeTime = noticeTime;}
}
