package com.example.xin.friendlyreminder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;

public class LoginActivity extends AppCompatActivity {

    EditText user,password;
    Button signIn,signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        user = (EditText) findViewById(R.id.user_Et);
        password = (EditText) findViewById(R.id.password_Et);
        signIn = (Button) findViewById(R.id.signIn);//登陆
        signUp = (Button) findViewById(R.id.signUp);//注册

        /**登陆注册均要判断用户名密码是否符合要求*/
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"暂未实现登陆功能",
                        Toast.LENGTH_SHORT).show();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this,"暂未实现注册功能",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
