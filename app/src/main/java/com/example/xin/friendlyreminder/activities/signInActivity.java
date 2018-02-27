package com.example.xin.friendlyreminder.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.javabean.User;
import com.example.xin.friendlyreminder.utils.httpRequest;
import com.example.xin.friendlyreminder.utils.userInfoManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class signInActivity extends AppCompatActivity {

    EditText userName,password;
    Button signIn,signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView(){
        userName = (EditText) findViewById(R.id.user_Et);
        password = (EditText) findViewById(R.id.password_Et);
        signIn = (Button) findViewById(R.id.signIn);//登陆
        signUp = (Button) findViewById(R.id.signUp);//注册


        /**登陆注册均要判断用户名密码是否符合要求*/
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signInActivity.this,signUpActivity.class);
                startActivity(intent);
                signInActivity.this.finish();
            }
        });
    }

    //向后台发送消息进行登陆
    public void signIn(){
        User user = new User();
        user.setUserName(userName.getText().toString());
        user.setUserPassword(password.getText().toString());
        final String url = getResources().getString(R.string.signInAddress);
        new httpRequest().dataRequest(user,url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Log.i("登陆界面","成功收到返回失败消息");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(signInActivity.this,"请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String responseMessage = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseMessage);
                    //Log.i("登录接口",responseMessage);
                    if (jsonObject.getString("errCode").equals("1000")){
                        //如果账号密码无误，则登录成功，把服务器返回的数据写入文件
                        JSONObject data = new JSONObject(jsonObject.getString("data"));
                        new userInfoManager(signInActivity.this).setMyUserMessage(data.getInt("userId"),
                                data.getString("userName"),data.getString("userPhone"));
                        //数据写入文件后，跳到引导界面
                        Intent intent = new Intent(signInActivity.this,GuideActivity.class);
                        startActivity(intent);
                        signInActivity.this.finish();
                        /**用来查看写入文件的数据*/
//                        SharedPreferences pref = getSharedPreferences("userInfo",MODE_PRIVATE);
//                        Log.i("写入文件的数据：", pref.getInt("userId",0)+ "/" + pref.getString("userName","0") + "/" + pref.getString("userPhone","0"));
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(signInActivity.this,responseMessage,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
