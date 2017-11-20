package com.example.xin.friendlyreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**如有需要，可将记录打开次数改为记录用户名密码，保证必须有用户名密码登陆了才能使用后续功能*/
public class GuideActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;//记录打开次数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        //查看count文件
        sharedPreferences = getSharedPreferences("count",MODE_PRIVATE);
        //查看count值，即打开次数，如果无返回值，则返回0
        int count = sharedPreferences.getInt("count",0);
        //判断程序是第几次运行，如果是第一次运行则跳转到引导界面
        if (count==0){
            Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
            startActivity(intent);
        }else {
//            /**开发用*/
//            Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
            Intent intent = new Intent(GuideActivity.this,MainActivity.class);
            startActivity(intent);
        }

        //用读写方式打开sharedPreferences对象对应的count文件
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count",++count);//修改打开次数
        editor.apply();//提交修改操作
        GuideActivity.this.finish();//将此活动弹栈，释放资源
    }
}
