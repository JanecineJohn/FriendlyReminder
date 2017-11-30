package com.example.xin.friendlyreminder.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.javabean.User;
import com.example.xin.friendlyreminder.adapters.FriendsAdapter;
import com.example.xin.friendlyreminder.utils.RefreshFrags;

import java.util.List;

/**
 * Created by xin on 2017/11/5.
 */

public class FriendsListFrag extends Fragment{

    public List<User> friendsList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friendslist,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipe_friendlistfrag);
        recyclerView = view.findViewById(R.id.recyc_friendlistfrag);

        //请求数据成功后，将数据转为对象集合传入handler，在此处进行UI更新
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    friendsList = (List<User>) msg.obj;
                    LinearLayoutManager layoutManager =
                            new LinearLayoutManager(getActivity());//创建布局实例
                    recyclerView.setLayoutManager(layoutManager);//设置布局
                    recyclerView.setAdapter(new FriendsAdapter(friendsList));//设置适配器，将数据显示出来
                }
            }
        };

        swipeRefreshLayout.setColorSchemeResources(R.color.colorSelect);//等待刷新的圈的颜色
        //为下拉刷新设置动作监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshFrags refreshFrags = new RefreshFrags();//创建工具类实例，此工具类有向服务器请求数据的方法
                refreshFrags.refresh_FriendsListFrag(handler);//调用请求好友列表方法
                swipeRefreshLayout.setRefreshing(false);//刷新完成，隐藏刷新控件
            }
        });
    }
}
