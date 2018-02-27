package com.example.xin.friendlyreminder.utils;

import android.util.Log;

import com.example.xin.friendlyreminder.javabean.User;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by xin on 2017/12/3.
 */

public class httpRequest {

    //传入本机用户及请求数据的接口.
    //此方法将本机用户和请求接口封装为http请求，返回一个call实例
    public Call dataRequest(Object requestObj, String url){
        Gson gson = new Gson();
        String json = gson.toJson(requestObj);//将传入的类对象转成Json格式的字符串
        //Log.i("jsonString",json);
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        OkHttpClient client = new OkHttpClient();//创建okhttp实例
        RequestBody body = RequestBody.create(JSON,json);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        okhttp3.Call call = client.newCall(request);//以request为参数创建一个call实例
        return call;
    }
}
