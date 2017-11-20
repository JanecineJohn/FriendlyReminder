package com.example.xin.friendlyreminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xin on 2017/11/5.
 */

public class myNewsFrag extends Fragment{

    private SwipeRefreshLayout swipeRefresh;//下拉刷新控件
    private RecyclerView myNewsRecyc;
    View view;//自己的view
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mynews,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefresh = view.findViewById(R.id.swipe_mynewsfrag);
        myNewsRecyc = view.findViewById(R.id.recyc_mynewsfrag);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //在此写更新列表的逻辑，或者调用方法更新
                //setRecyc();//为recyclerview组件添加适配器，填充内容
                swipeRefresh.setRefreshing(false);//刷新完成，隐藏刷新控件
            }
        });
    }

//    private void setRecyc(){
//                RefreshFrags refreshFrags = new RefreshFrags();//新建一个RefreshFrags(更新好友列表或我的信息)实例,并传入此Fragment的上下文
//                List<String> newsList = refreshFrags.refresh_myNewsFrag();//用实例的更新我的信息方法获得一个消息集合
//                LinearLayoutManager layoutManager =
//                        new LinearLayoutManager(getActivity());
//                myNewsRecyc.setLayoutManager(layoutManager);//设置recyclerview的布局方式
//                myNewsRecyc.setAdapter(new NewsAdapter(newsList));//设置recyclerview的适配器
//    }

}
