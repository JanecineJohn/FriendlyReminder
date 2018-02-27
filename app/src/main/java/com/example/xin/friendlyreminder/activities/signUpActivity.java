package com.example.xin.friendlyreminder.activities;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class signUpActivity extends AppCompatActivity {

    EditText userName_signUp,password_signUp,userPhone_signUp;
    Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView(){
        userName_signUp = (EditText) findViewById(R.id.userName_signUp);
        password_signUp = (EditText) findViewById(R.id.password_signUp);
        userPhone_signUp = (EditText) findViewById(R.id.userPhone_signUp);
        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }
    public void signUp(){
        User user = new User();
        user.setUserName(userName_signUp.getText().toString());
        user.setUserPassword(password_signUp.getText().toString());
        user.setUserPhone(userPhone_signUp.getText().toString());
        final String url = getResources().getString(R.string.signUpAddress);
        new httpRequest().dataRequest(user,url).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Log.i("注册界面","收到返回失败消息"+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(signUpActivity.this,"请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseMessage = response.body().string();
                Log.i("返回信息：",responseMessage);
                try {
                    JSONObject jsonObject = new JSONObject(responseMessage);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(signUpActivity.this,responseMessage,Toast.LENGTH_LONG).show();
                        }
                    });
                    if (jsonObject.getString("errCode").equals("1000")){
                        Intent intent = new Intent(signUpActivity.this,signInActivity.class);
                        startActivity(intent);
                        signUpActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
