package com.example.xin.friendlyreminder.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.services.WebSocketClientService;
import com.example.xin.friendlyreminder.utils.userInfoManager;

import java.util.List;

/**如有需要，可将记录打开次数改为记录用户名密码，保证必须有用户名密码登陆了才能使用后续功能
 * 曾经做过这样的操作(可能会有无用的sharedPreferences文件残留)：
 *  //查看count文件
    //sharedPreferences = getSharedPreferences("count",MODE_PRIVATE);
    //查看count值，即打开次数，如果无返回值，则返回0
    //int count = sharedPreferences.getInt("count",0);
 */
public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        userInfoManager manager = new userInfoManager(GuideActivity.this);
        int userId = manager.getMyUserId();
        String userName = manager.getMyUserName();
        String userPhone = manager.getMyUserPhone();
        //判断userID的值是否为空，如果是空，则跳转到登陆界面
        if (userId==-1 ||userName.equals("") || userPhone.equals("")){
            //Intent intent = new Intent(GuideActivity.this,MainActivity.class);
            Intent intent = new Intent(GuideActivity.this,signInActivity.class);
            startActivity(intent);
        }else {
            if (!isServiceRunning(GuideActivity.this,"WebSocketClientService")){
                startService(new Intent(GuideActivity.this, WebSocketClientService.class));
            }
            Intent intent = new Intent(GuideActivity.this,MainActivity.class);
            startActivity(intent);
        }
        GuideActivity.this.finish();
    }

    /**
     * 判断服务是否启动,context上下文对象 ，className服务的name
     */
    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
