package com.example.xin.friendlyreminder.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.adapters.FPAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;//标签栏
    private ViewPager viewPager;//滑动页面
    private FPAdapter fpAdapter;//适配器，适配Fragment和viewPager

    private String[] titles = new String[]{"我的消息","好友列表"};
    private int[] icons = new int[]{R.drawable.ic_sms_black_24dp,
            R.drawable.ic_people_black_24dp};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getSupportActionBar().hide();
            setContentView(R.layout.activity_main);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            fpAdapter = new FPAdapter(getSupportFragmentManager());//此类负责将Fragment和tab联系起来

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
}
