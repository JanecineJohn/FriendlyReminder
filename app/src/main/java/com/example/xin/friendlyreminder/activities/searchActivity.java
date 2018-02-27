package com.example.xin.friendlyreminder.activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.adapters.DividerItemDecoration;
import com.example.xin.friendlyreminder.adapters.FriendsAdapter;
import com.example.xin.friendlyreminder.adapters.searchUserAdapter;
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

public class searchActivity extends AppCompatActivity {

    String url;
    Gson gson = new Gson();
    RecyclerView searchRecycler;
    final List<User> searchUserList = new ArrayList<>();
    searchUserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        url = getResources().getString(R.string.searchUserAddress);
        initView();
    }

    private void initView(){
        //初始化Handler
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(searchActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
            }
        };
        //初始化其他组件
        ImageView backBt = (ImageView) findViewById(R.id.back_bt);
        ImageView searchBt = (ImageView) findViewById(R.id.search_bt);
        EditText searchEt = (EditText) findViewById(R.id.search_et);
        //初始化重要组件，recyclerview
        searchRecycler = (RecyclerView) findViewById(R.id.search_recyclerview);
        searchRecycler.setLayoutManager(new LinearLayoutManager(searchActivity.this));//设置布局
        searchRecycler.addItemDecoration(new DividerItemDecoration(searchActivity.this,DividerItemDecoration.VERTICAL_LIST));//设置分割线
        adapter = new searchUserAdapter(searchUserList,handler);
        searchRecycler.setAdapter(adapter);//设置适配器，将数据显示出来

        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchActivity.this.finish();
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()!=0){
                    try {
                        String json = new JSONObject().put("keyword",editable.toString()).toString();
                        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                        RequestBody body = RequestBody.create(JSON,json);
                        final Request request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .build();
                        new OkHttpClient().newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(searchActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String responseMsg = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseMsg);
                                    if (jsonObject.getString("errCode").equals("1000")){
                                        searchUserList.clear();//每次更新数据集，都清空searchUserList
                                        if (!jsonObject.getString("data").equals("null")){
                                            JSONArray data = jsonObject.getJSONArray("data");//获取好友请求
                                            Log.i("data数组内容：",data.toString());
                                            for (int i=0;i<data.length();i++){
                                                User user = gson.fromJson(data.getJSONObject(i).toString(),User.class);
                                                searchUserList.add(user);
                                            }
                                        }
                                        //把集合元素显示出来
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.notifyDataSetChanged();
                                            }
                                        });
                                    }else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(searchActivity.this,"搜索操作太频繁，请稍后重试",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //搜索输入框的值对应的用户
            }
        });
    }
}
