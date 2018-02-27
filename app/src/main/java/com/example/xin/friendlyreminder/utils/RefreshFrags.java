package com.example.xin.friendlyreminder.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.javabean.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Callback;
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
    public void refresh_FriendsListFrag(User user,String url,final Handler handler){

        //异步执行返回的call实例，获取服务器数据
        new httpRequest().dataRequest(user,url).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.i("请求失败：","请求失败");
                Message message= new Message();
                message.what = 0;
                message.obj = "请求数据失败，请检查网络";
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                try {
                    String responseMsg = response.body().string();
                    List<User> list = new ArrayList<>();
                    Gson resGson = new Gson();
                    JSONObject jsonObject = new JSONObject(responseMsg);
                    /**获取好友申请数据，并转化为User类对象，添加到集合中*/
                    if (jsonObject.getString("requestData").equals("null")){
                        //如果为空，不作任何操作
                    }else {
                        JSONArray requestData = jsonObject.getJSONArray("requestData");//获取好友请求
                        for (int i=0;i<requestData.length();i++){
                            User eachUser = resGson.fromJson
                                    (requestData.getJSONObject(i).toString(),User.class);
                            eachUser.setLetter("↑");
                            list.add(eachUser);
                        }
                    }

                    /**获取已有好友数据,并转化为一个个User类对象，添加到集合中*/
                    if (jsonObject.getString("friendData").equals("null")){
                        //如果为空，不作任何操作
                    }else {
                        JSONArray jsonArray = jsonObject.getJSONArray("friendData");//获取已有好友数据
                        for (int i=0; i<jsonArray.length();i++){
                            //将服务器返回的已有好友JSON数据转为User类
                            User eachUser = resGson.fromJson
                                    (jsonArray.getJSONObject(i).toString(), User.class);
                            //Log.i("jsonObject：",jsonArray.getJSONObject(i).toString());
                            /**可以在此对letter对象赋值*/
                            //获取用户名的拼音首字母
                            String firstSpell = ChineseToEnglish.getFirstSpell(eachUser.getUserName());//将中文转换为拼音,第一个字取全拼,后面的字取首字母
                            String substring = firstSpell.substring(0, 1).toUpperCase();//提取出首字母，并转换为大写
                            //将拼音首字母与A-Z作匹配，区分这个名字是汉语或字母，还是特殊字符
                            if(substring.matches("[A-Z]")){
                                eachUser.setLetter(substring);//letter属性，用来对各个名字排序
                            }else {
                                eachUser.setLetter("#");
                            }
                            list.add(eachUser);
                        }
                    }
                    /**按如此优先级排序：↑,A-Z,# */
                    /**CompareSort()继承了Comparator<User>,*/
                    Collections.sort(list, new CompareSort());//设置好letter属性后，根据letter属性为集合排序
                    //将list集合放到handler的msg的obj中返回
                    Message message = new Message();
                    message.what = 1;
                    message.obj = list;
                    handler.sendMessage(message);
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
    /**下面方法已废弃，暂未删除*/
    //将得到的集合进行letter字段赋值和排序
    /*private List<User> listComparator(List<User> list){
        for (int i=0;i<list.size();i++){
            User user = list.get(i);//依次获取每个user对象
            //获取用户名的拼音首字母
            String firstSpell = ChineseToEnglish.getFirstSpell(user.getUserName());//将中文转换为拼音,第一个字取全拼,后面的字取首字母
            String substring = firstSpell.substring(0, 1).toUpperCase();//提取出首字母，并转换为大写
            //将拼音首字母与A-Z作匹配，区分这个名字是汉语或字母，还是特殊字符
            if(substring.matches("[A-Z]")){
                user.setLetter(substring);//letter属性，用来对各个名字排序
            }else {
                user.setLetter("#");
            }
        }

        //按如此优先级排序：↑,A-Z,#
        //CompareSort()继承了Comparator<User>
        Collections.sort(list, new CompareSort());//设置好letter属性后，根据letter属性为集合排序
        return list;
    }*/

    //模拟一些数据，当返回失败的时候调用
    private List<User> createData(){
        List<User> data = new ArrayList<>();
        for(int i=0;i<20;i++){
            User user1 = new User();
            user1.setUserName("刘海");
            user1.setUserPhone("12345678910");
            data.add(user1);
            User user2 = new User();
            user2.setUserName("张三");
            user2.setUserPhone("12345678910");
            data.add(user2);
            User user3 = new User();
            user3.setUserName("王五");
            user3.setUserPhone("12345678910");
            data.add(user3);
        }
        return data;
    }
}
