package com.example.xin.friendlyreminder;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;//标签栏
        private ViewPager viewPager;//滑动页面
        private FPAdapter fpAdapter;//适配器，适配Fragment和viewPager
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
    }
}
