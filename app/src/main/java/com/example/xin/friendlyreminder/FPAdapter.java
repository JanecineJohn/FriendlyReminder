package com.example.xin.friendlyreminder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by xin on 2017/11/5.
 */

public class FPAdapter extends FragmentPagerAdapter{
    private String[] titles = new String[]{"我的消息","好友列表"};

    public FPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new myNewsFrag();
        if (position == 1){
            fragment = new FriendsListFrag();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
