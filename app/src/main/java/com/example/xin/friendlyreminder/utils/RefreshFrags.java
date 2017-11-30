package com.example.xin.friendlyreminder.utils;

import android.os.Handler;
import android.os.Message;

import com.example.xin.friendlyreminder.javabean.User;
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
    /**此方法异步，在另一线程*/
    public void refresh_FriendsListFrag(final Handler handler){
        //下面是测试代码
        User user = new User();
        user.setUserID(1);
        //上面是测试代码

        final String url = "http://192.168.1.104:10001/api/friendList";//服务器地址

        //异步执行返回的call实例，获取服务器数据
        dataRequest(user,url).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                //Log.i("请求失败：","请求失败");
                List<User> fail = new ArrayList<>();
                User test = new User();
                test.setUserName("请求失败");
                test.setUserPhone("以下为测试代码:");
                fail.add(test);
                for (int i=0;i<50;i++){
                    User failUser = new User();
                    failUser.setUserName("用户名"+i);
                    failUser.setUserPhone("电话号码："+i);
                    fail.add(failUser);
                }
                Message message= new Message();

                message.what = 1;
                message.obj = fail;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    List<User> list = new ArrayList<>();
                    Gson resGson = new Gson();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    /**获取好友申请数据，并转化为User类对象，添加到集合中*/

                    /**获取已有好友数据,并转化为一个个User类对象，添加到集合中*/
                    JSONArray jsonArray = jsonObject.getJSONArray("friendData");//获取已有好友数据
                    User eachUser;//创建一个User实例接收每个JSON数据转化得到的User
                    for (int i=0; i<jsonArray.length();i++){
                        //将服务器返回的已有好友JSON数据转为User类
                        eachUser = resGson.fromJson
                                (jsonArray.getJSONObject(i).toString(), User.class);
                        list.add(eachUser);
                    }
                    //将list集合放到handler的msg的obj中返回
                    Message message = new Message();
                    message.what = 1;//1代表这个列表集合里的内容是已有好友
                    message.obj = list;
                    handler.sendMessage(message);

                    /**获取好友申请数据,并转化为一个个friend_request类对象，添加到集合中*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**重构未测试*/
    /**将某个jsonObject(传入参数)的某一个数组(传入参数)解析成类对象(类型作为参数)(未实现)添加到集合并以特定标识(传入参数)传到fragment中*/
    private void getFriendData(JSONObject jsonObject,String nameJsonArray,int what,final Handler handler){
        try {
            List<User> list = new ArrayList<>();
            Gson resGson = new Gson();
            JSONArray jsonArray = jsonObject.getJSONArray(nameJsonArray);//获取已有好友数据
            User eachUser;//创建一个User实例接收每个JSON数据转化得到的User
            for (int i=0; i<jsonArray.length();i++){
                //将服务器返回的已有好友JSON数据转为User类
                eachUser = resGson.fromJson
                        (jsonArray.getJSONObject(i).toString(), User.class);
                list.add(eachUser);
            }
            //将list集合放到handler的msg的obj中返回
            Message message = new Message();
            message.what = what;//1代表这个列表集合里的内容是已有好友
            message.obj = list;
            handler.sendMessage(message);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //传入本机用户及请求数据的接口.
    //此方法将本机用户和请求接口封装为http请求，返回一个call实例
    private Call dataRequest(User user,String url){
        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);//将传入的类对象转成Json格式的字符串
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
