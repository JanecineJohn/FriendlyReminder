package com.example.xin.friendlyreminder.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.activities.GuideActivity;
import com.example.xin.friendlyreminder.javabean.Notice;
import com.example.xin.friendlyreminder.utils.userInfoManager;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClientService extends Service {
    WebSocketClient mWebSocketClient;
    String address;//webSocket的连接地址

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    //内部类，里面提供方法，返回此service对象
    public class MyBinder extends Binder {
        public WebSocketClientService getService(){
            return WebSocketClientService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**测试*/
//        new AlertDialog.Builder(WebSocketClientService.this)
//                .setMessage("后台被启动，OnCreate")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mWebSocketClient == null){
            int userId = new userInfoManager(this).getMyUserId();
            address = getResources().getString(R.string.webSocketAddress) + userId;
            Log.i("连接后台的地址：",address);
            connect();//连接

            /**测试*/
//            new AlertDialog.Builder(WebSocketClientService.this)
//                    .setMessage("OnStartCommand,调用了连接方法")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    })
//                    .show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"后台被关闭",Toast.LENGTH_LONG).show();
        closeConnect();
    }

    //连接服务器
    private void connect(){
        new Thread(){
            @Override
            public void run() {
                try {
                    mWebSocketClient = new WebSocketClient(new URI(address)) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            Log.d("webSocket相关信息---->","onOpen，建立webSocket连接");
                        }

                        @Override
                        public void onMessage(String s) {
                            //当有服务端发送来消息的时候，回调此函数
                            Log.d("webSocket相关信息---->","onMessage，服务端发送过来的信息为：" + s);
                            setNotification(s);
                        }

                        @Override
                        public void onClose(int i, String s, boolean b) {
                            //连接断开
                            Log.d("webSocket相关信息---->","onClose，连接断开"+ i + "/" + s + "/" + b);
                            Toast.makeText(WebSocketClientService.this,"回调onClose方法",Toast.LENGTH_LONG).show();
                            closeConnect();
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("webSocket相关信息---->","onError，出错：" + e);
                            Toast.makeText(WebSocketClientService.this,"回调onError方法",Toast.LENGTH_LONG).show();
                        }
                    };
                    mWebSocketClient.connect();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //断开连接
    private void closeConnect(){
        try {
            mWebSocketClient.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mWebSocketClient = null;
        }
    }
    //发送信息
    public void sendMsg(String msg){
        mWebSocketClient.send(msg);
    }

    /**
     * 接收到提醒，弹出通知
     */
    private void setNotification(String s){
        Notice notice = new Gson().fromJson(s,Notice.class);//这句可能是导致出错的源头
        Intent intent = new Intent(this, GuideActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(notice.getNoticeTitle())
                .setContentText(notice.getNoticeContent())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.clock)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setFullScreenIntent(pi,true)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }
}

