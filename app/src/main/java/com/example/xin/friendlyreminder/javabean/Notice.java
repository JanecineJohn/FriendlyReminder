package com.example.xin.friendlyreminder.javabean;

/**
 * Created by dell on 2018/2/8.
 * {"eventType":"SET_TIPS","data":{"fromId":14,"toId":14,"noticeTitle":"1234","noticeContent":"Hello World","noticeTime":1516848560468}}
 */

public class Notice {
    private int fromId;//发送方
    private int toId;//接收方
    private String noticeTitle;//提醒标题
    private String noticeContent;//提醒内容
    private long noticeTime;//时间戳

    public Notice(){}
    public Notice(int fromId,int toId,String noticeTitle,String noticeContent,long noticeTime){
        this.fromId = fromId;
        this.toId = toId;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeTime = noticeTime;
    }
    public int getFromId() {return fromId;}
    public void setFromId(int fromId) {this.fromId = fromId;}

    public int getToId() {return toId;}
    public void setToId(int toId) {this.toId = toId;}

    public String getNoticeTitle() {return noticeTitle;}
    public void setNoticeTitle(String noticeTitle) {this.noticeTitle = noticeTitle;}

    public String getNoticeContent() {return noticeContent;}
    public void setNoticeContent(String noticeContent) {this.noticeContent = noticeContent;}

    public long getNoticeTime() {return noticeTime;}
    public void setNoticeTime(long noticeTime) {this.noticeTime = noticeTime;}
}
