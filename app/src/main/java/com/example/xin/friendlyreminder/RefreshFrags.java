package com.example.xin.friendlyreminder;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xin on 2017/11/7.
 * 此类负责向服务器发送请求，接收返回的集合：
 *      应该新开一个线程更新，否则容易导致ANR异常
 *
 * 有两个方法：
 *      refresh_myNewsFrag：负责实现我的消息的数据请求
 *      refresh_FriendsListFrag：负责实现好友列表的数据请求
 */

public class RefreshFrags {

    //Context mContext;//接收一个上下文，用于在活动界面更新组件

//    public RefreshFrags(Context context){
//        mContext = context;
//    }

    /**返回我的消息集合*/
    public List<String> refresh_myNewsFrag(){

        final List<String> testList = new ArrayList<>();/**测试用*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<100;i++){
                    testList.add("在我的信息页面的第" + i + "条消息");
                }
            }
        }).start();

        return testList;
    }

    /**返回好友列表集合*/
    public void refresh_FriendsListFrag(final Handler handler){
        //下面是测试代码
        User user = new User();
        user.setUserID(1);
        //上面是测试代码

        final String url = "http://192.168.1.104:10001/getFriend";//服务器地址

        //异步执行返回的call实例，获取服务器数据
        dataRequest(user,url).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.i("请求失败：","请求失败");
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {

                try {
                    List<User> list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("friendData");
                    Gson resGson = new Gson();//用Gson的方法将JSON数据解析为User对象
                    User eachUser;//创建一个User实例接收每个JSON数据转化得到的User
                    for (int i=0; i<jsonArray.length();i++){
                        //String  eachUser_S= jsonArray.getJSONObject(i).toString();

                        //将服务器返回的已有好友JSON数据转为User类
                        eachUser = resGson.fromJson
                                (jsonArray.getJSONObject(i).toString(), User.class);
                        list.add(eachUser);
                    }
                    /**将list集合放到handler的msg的obj中返回*/
                    Message message = new Message();
                    message.obj = list;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //传入本机用户及请求数据的接口.
    //此方法将本机用户和请求接口封装为http请求，返回一个call实例
    private Call dataRequest(User user,String url){
        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        OkHttpClient client = new OkHttpClient();//创建okhttp实例
        RequestBody body = RequestBody.create(JSON,jsonUser);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        okhttp3.Call call = client.newCall(request);//以request为参数创建一个call实例
        return call;
    }
}
