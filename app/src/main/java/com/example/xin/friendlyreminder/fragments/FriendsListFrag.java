package com.example.xin.friendlyreminder.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.adapters.DividerItemDecoration;
import com.example.xin.friendlyreminder.adapters.FriendsAdapter;
import com.example.xin.friendlyreminder.customlayout.SideBarView;
import com.example.xin.friendlyreminder.javabean.User;
import com.example.xin.friendlyreminder.services.WebSocketClientService;
import com.example.xin.friendlyreminder.utils.RefreshFrags;
import com.example.xin.friendlyreminder.utils.userInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 2017/11/5.
 */

public class FriendsListFrag extends Fragment implements SideBarView.LetterSelectListener{

    public List<User> friendsList = new ArrayList<>();
    Handler handler;
    private RecyclerView recyclerView;
    FriendsAdapter adapter;
    private LinearLayoutManager layoutManager;//recyclerview的布局管理器，用于跳转到某个位置
    private View view;
    TextView mTip;//滑动导航栏出现提示字母块
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
        //swipeRefreshLayout = view.findViewById(R.id.swipe_friendlistfrag);//已弃用
        recyclerView = view.findViewById(R.id.recyc_friendlistfrag);
        SideBarView sideBarView = view.findViewById(R.id.sidebarview_friendlistfrag);//初始化侧右边的字母条
        mTip = view.findViewById(R.id.tip_friendlistfrag);//滑动右侧导航栏会在中间显示的框
        //设置回调
        sideBarView.setOnLetterSelectListen(this);

        //请求数据成功后，将数据转为对象集合传入handler，在此处进行UI更新
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0){
                    String failmsg = (String) msg.obj;
                    Toast.makeText(getActivity(),failmsg,Toast.LENGTH_SHORT).show();
                }
                if (msg.what == 1){
                    friendsList = (List<User>) msg.obj;
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);//设置布局
                    recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
                    adapter = new FriendsAdapter(friendsList,handler);
                    recyclerView.setAdapter(adapter);//设置适配器，将数据显示出来
                }
                if (msg.what == 2){
                    getFriendList();
                }
            }
        };
        //获取好友列表
        getFriendList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 向服务器发一次请求，获取好友列表
     */
    private void getFriendList(){
        //创建一个user实例
        User user = new User();
        user.setUserID(new userInfoManager(getActivity()).getMyUserId());
        //获取好友列表请求地址
        String friendListUrl = getContext().getResources().getString(R.string.friendListAddress);
        RefreshFrags refreshFrags = new RefreshFrags();//创建工具类实例，此工具类有向服务器请求数据的方法
        refreshFrags.refresh_FriendsListFrag(user,friendListUrl,handler);//调用请求好友列表方法
    }


    @Override
    public void onLetterSelected(String letter) {
        setListviewPosition(letter);
        mTip.setText(letter);
        mTip.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLetterChanged(String letter) {
        setListviewPosition(letter);
        mTip.setText(letter);
    }

    @Override
    public void onLetterReleased(String letter) {
        mTip.setVisibility(View.GONE);
    }

    private void setListviewPosition(String letter){
        int firstLetterPosition = adapter.getScrollPosition(letter);
        if(firstLetterPosition != -1){
            //mListview.setSelection(firstLetterPosition);
            layoutManager.scrollToPositionWithOffset(firstLetterPosition,0);
        }
    }
}
