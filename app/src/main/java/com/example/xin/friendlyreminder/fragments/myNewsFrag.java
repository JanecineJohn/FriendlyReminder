package com.example.xin.friendlyreminder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.adapters.DividerItemDecoration;
import com.example.xin.friendlyreminder.adapters.ToMeNewsAdapter;
import com.example.xin.friendlyreminder.interfaces.getNewsListener;
import com.example.xin.friendlyreminder.javabean.News;
import com.example.xin.friendlyreminder.utils.RefreshFrags;
import com.example.xin.friendlyreminder.utils.userInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 2017/11/5.
 */

public class myNewsFrag extends Fragment implements getNewsListener {

    private SwipeRefreshLayout swipeRefresh;//下拉刷新控件
    RecyclerView myNewsRecyc;
    List<News> newsList = new ArrayList<>();
    ToMeNewsAdapter toMeNewsAdapter;
    View view;//自己的view
    Context mContext;
    userInfoManager uiManager;//个人信息管理(sharedpreference)
    String url;//向后台请求的地址(noticeList)
    int listTypeTOME = 2;//listType：别人发送给我的提醒
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mynews,container,false);
        mContext = getActivity();
        uiManager = new userInfoManager(mContext);//初始化用户信息管理对象
        url = getResources().getString(R.string.noticeListAddress);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefresh = view.findViewById(R.id.swipe_mynewsfrag);
        swipeRefresh.setColorSchemeResources(R.color.colorSelect);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //在此写更新列表的逻辑，或者调用方法更新
                //setRecyc();//为recyclerview组件添加适配器，填充内容
                swipeRefresh.setRefreshing(false);//刷新完成，隐藏刷新控件
            }
        });

        toMeNewsAdapter = new ToMeNewsAdapter(newsList);
        myNewsRecyc = view.findViewById(R.id.recyc_mynewsfrag);
        myNewsRecyc.setLayoutManager(new LinearLayoutManager(mContext));//设置布局
        myNewsRecyc.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        myNewsRecyc.setAdapter(toMeNewsAdapter);//设置适配器，将数据显示出来
        new RefreshFrags().refresh_myNewsFrag(uiManager.getMyUserId(),listTypeTOME,url,myNewsFrag.this);
    }

    /**
     * 重写getNewsListener接口的方法，当发生事件回调时，
     * 按此方法的逻辑运行代码*/
    @Override
    public void getNewsDone(List<News> newsList) {
//        for (News news : newsList)
//            Log.i("在碎片中展示获得的集合：",news.getNoticeContent());
        this.newsList.addAll(newsList);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toMeNewsAdapter.notifyDataSetChanged();
            }
        });

    }


}
