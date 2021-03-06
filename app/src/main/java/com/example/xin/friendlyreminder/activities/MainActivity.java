package com.example.xin.friendlyreminder.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.adapters.DividerItemDecoration;
import com.example.xin.friendlyreminder.adapters.FPAdapter;
import com.example.xin.friendlyreminder.adapters.FromMeNewsAdapter;
import com.example.xin.friendlyreminder.interfaces.getNewsListener;
import com.example.xin.friendlyreminder.javabean.News;
import com.example.xin.friendlyreminder.utils.RefreshFrags;
import com.example.xin.friendlyreminder.utils.userInfoManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements getNewsListener {

    private android.support.v7.widget.Toolbar toolbar;//活动栏
    private TabLayout tabLayout;//标签栏
    private ViewPager viewPager;//滑动页面
    private FPAdapter fpAdapter;//适配器，适配Fragment和viewPager
    private DrawerLayout drawerLayout;//滑动菜单
    private RecyclerView recyclerView;
    private List<News> newsList = new ArrayList<>();
    private FromMeNewsAdapter newsAdapter;
    String url;//向后台请求的地址(noticeList)
    int listTypeFromMe = 1;

    private String[] titles = new String[]{"我的消息","好友列表"};
    private int[] icons = new int[]{R.drawable.ic_sms_black_24dp,
            R.drawable.ic_people_black_24dp};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //getSupportActionBar().hide();
            setContentView(R.layout.activity_main);
            url = getResources().getString(R.string.noticeListAddress);//初始化地址
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            fpAdapter = new FPAdapter(getSupportFragmentManager());//此类负责将Fragment和tab联系起来

            setSupportActionBar(toolbar);
            initView();

            viewPager.setAdapter(fpAdapter);
            tabLayout.setupWithViewPager(viewPager);
            for (int i=0;i<titles.length;i++){
                //tabLayout.getTabAt(i).setIcon(icons[i]).setText(titles[i]);
                tabLayout.getTabAt(i).setCustomView(getTabView(i));
            }
    }
    private View getTabView(int position){
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab,null);
        ((ImageView) view.findViewById(R.id.tab_iv)).setImageResource(icons[position]);
        ((TextView) view.findViewById(R.id.tab_tv)).setText(titles[position]);
        return view;
    }
    private void initView(){
        LinearLayout headLinear = (LinearLayout) findViewById(R.id.headLinear);
        ImageView userBt = (ImageView) findViewById(R.id.user_Bt);
        ImageView addBt = (ImageView) findViewById(R.id.add_Bt);
        Button logOut = (Button) findViewById(R.id.log_out);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        TextView userId = (TextView) findViewById(R.id.userId_tv);
        TextView userName = (TextView) findViewById(R.id.userName_tv);
        TextView userPhone = (TextView) findViewById(R.id.userPhone_tv);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_Recycler);
        final userInfoManager manager = new userInfoManager(MainActivity.this);

        userBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"点击了用户按钮",Toast.LENGTH_SHORT).show();
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addActivity = new Intent(MainActivity.this,searchActivity.class);
                startActivity(addActivity);
            }
        });

        headLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        newsAdapter = new FromMeNewsAdapter(newsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));//设置布局
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(newsAdapter);//设置适配器，将数据显示出来
        new RefreshFrags().refresh_myNewsFrag(new userInfoManager(MainActivity.this).getMyUserId(),listTypeFromMe,url,MainActivity.this);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("注销账号")
                        .setMessage("确定要登出此账号吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                manager.clearMyUserMessage();
                                startActivity(new Intent(MainActivity.this,GuideActivity.class));
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            }
        });
        userId.setText("用户ID：" + manager.getMyUserId());
        userName.setText("用户名：" + manager.getMyUserName());
        userPhone.setText("电话号码：" + manager.getMyUserPhone());
    }

    @Override
    public void getNewsDone(List<News> list) {
        this.newsList.addAll(list);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newsAdapter.notifyDataSetChanged();
            }
        });
    }
}
