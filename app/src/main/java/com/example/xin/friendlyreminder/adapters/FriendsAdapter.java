package com.example.xin.friendlyreminder.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.activities.MainActivity;
import com.example.xin.friendlyreminder.activities.signInActivity;
import com.example.xin.friendlyreminder.javabean.Notice;
import com.example.xin.friendlyreminder.javabean.User;
import com.example.xin.friendlyreminder.services.WebSocketClientService;
import com.example.xin.friendlyreminder.utils.ChineseToEnglish;
import com.example.xin.friendlyreminder.utils.CompareSort;
import com.example.xin.friendlyreminder.utils.RefreshFrags;
import com.example.xin.friendlyreminder.utils.httpRequest;
import com.example.xin.friendlyreminder.utils.userInfoManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xin on 2017/11/7.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{

    private Context mContext;
    private Handler handler;
    private List<User> friendsList;//接收得到的好友列表集合
    private String acceptAddress;//接受好友请求的地址
    private String pushNoticeAddress;//发送提醒的地址
    userInfoManager uiManager;

    /**把Handler传进来，用以更新集合*/
    public FriendsAdapter(List<User> friendsList, Handler handler){
        //获取传递过来的消息集合
        this.friendsList = friendsList;
        this.handler = handler;
        //Log.i("集合大小：",this.friendsList.size()+"");
    }

    //为NewsAdapter创建ViewHolder，作用是容纳组件，并提高效率
    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout view_user_item;
        ImageView view_user_image_userIcon;
        TextView view_user_text_userName,view_user_text_phone;
        TextView view_user_text_title;
        public ViewHolder(View itemView) {
            super(itemView);
            view_user_item = itemView.findViewById(R.id.item);
            view_user_text_title = itemView.findViewById(R.id.title);
            view_user_text_userName = itemView.findViewById(R.id.view_user_text_userName);
            view_user_text_phone = itemView.findViewById(R.id.view_user_text_phone);
            view_user_image_userIcon = itemView.findViewById(R.id.view_user_image_userIcon);
        }
    }
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
            uiManager = new userInfoManager(mContext);//获得SharedPreferences用户信息管理器
        }
        //初始化后台地址，可能会有Bug，不行的话就放在开头初始化
        acceptAddress = mContext.getResources().getString(R.string.acceptRequestAddress);
        pushNoticeAddress = mContext.getResources().getString(R.string.pushNoticeAddress);
        //为view组件加载xml布局文件,使用父环境(MainActivity)的布局加载器
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_user,
                parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder holder, final int position) {
        final User user = friendsList.get(position);
        holder.view_user_text_userName.setText(user.getUserName());
        holder.view_user_text_phone.setText(user.getUserPhone());
        holder.view_user_image_userIcon.setImageResource(R.mipmap.ic_launcher);
        /**test*/
        /**为viewHolder设置点击事件*/
        holder.view_user_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getLetter().equals("↑")){
                    new AlertDialog.Builder(mContext)
                            .setTitle("好友申请")
                            .setMessage("确定通过此用户的好友申请吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    accept(user.getUserID());
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //取消无动作
                                }
                            })
                            .show();

                }else{
                    dialogView(user.getUserID());
//                    Notice notice = new Notice(new userInfoManager(mContext).getMyUserId(),user.getUserID(),"测试","Test",System.currentTimeMillis());
//                    pushNotice(notice);
                }
            }
        });
        /**设置字母标题，只有某些地方需要(字母改变了的时候)*/
        if (position==0 && friendsList.get(0).getLetter().equals("↑")){
            holder.view_user_text_title.setVisibility(View.VISIBLE);
            holder.view_user_text_title.setText("好友请求");
        }else if(position == getFirstLetterPosition(position) && !friendsList.get(position).getLetter().equals("↑")){
            holder.view_user_text_title.setVisibility(View.VISIBLE);
            holder.view_user_text_title.setText(friendsList.get(position).getLetter().toUpperCase());
        }else {
            holder.view_user_text_title.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    //下面是寻找第一个item对应的位置的方法
    private int getFirstLetterPosition(int position) {
        //传入一个位置，查看此位置对应字母的首个位置
        String letter = friendsList.get(position).getLetter();//获取此位置下的字母
        int cnAscii = ChineseToEnglish.getCnAscii(letter.toUpperCase().charAt(0));
        int size = friendsList.size();
        for (int i = 0; i < size; i++) {
            if(cnAscii == friendsList.get(i).getLetter().charAt(0)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 顺序遍历所有元素．找到letter下的第一个item对应的position
     * @param letter
     * @return
     */
    public int getScrollPosition(String letter){
        int size = friendsList.size();
        for (int i = 0; i < size; i++) {
//            if(letter.charAt(0) == users.get(i).getLetter().charAt(0)){
//                return i;
//            }
            if (friendsList.get(i).getLetter().equals(letter)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 调用好友请求接收接口，拒绝或接收好友请求
     */
    private void accept(int friendId){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId",uiManager.getMyUserId());
            jsonObject.put("friendId",friendId);
            Log.i("jsonObject内容：",jsonObject.toString());
            MediaType JSON = MediaType.parse("application/json;charset=utf-8");
            OkHttpClient client = new OkHttpClient();//创建okhttp实例
            RequestBody body = RequestBody.create(JSON,jsonObject.toString());
            final Request request = new Request.Builder()
                    .url(acceptAddress)
                    .post(body)
                    .build();
            okhttp3.Call call = client.newCall(request);//以request为参数创建一个call实例
            call.enqueue(new Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    Log.i("请求失败：","请求失败");
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = e.toString();
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    try {
                        final String responseMessage = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseMessage);
                        if (jsonObject.getString("errCode").equals("1000")){
                            //errCode为1000,说明添加好友成功
                            Log.i("请求成功：","成功添加好友");
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }else {
                            //errCode为其他，说明出错了
                            Log.i("errCode=" + jsonObject.getString("errCode"),jsonObject.getString("errDetail"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出对话框，让用户填写姓名学号
    public void dialogView(final int toId){
        View joinClassForm = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout,null);
        final EditText contentEdt = joinClassForm.findViewById(R.id.dialog_content_edt);
        final EditText hourEdt = joinClassForm.findViewById(R.id.dialog_noticeHour_edt);
        final EditText minEdt = joinClassForm.findViewById(R.id.dialog_noticeMin_edt);
        new AlertDialog.Builder(mContext)
                .setTitle("发送提醒")
                .setView(joinClassForm)
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Notice notice = new Notice(uiManager.getMyUserId(),toId,
                                "来自" + uiManager.getMyUserName() + "的消息",
                                contentEdt.getText().toString(),
                                //将填入的时分转为long型秒数
                                toTimeStamp(Integer.parseInt(hourEdt.getText().toString().trim()),Integer.parseInt(minEdt.getText().toString().trim()))
                                );
                        pushNotice(notice);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //取消无动作
                    }
                })
                .show();
    }

    /**
     * 发送提醒
     */
    private void pushNotice(Notice notice){
        new httpRequest().dataRequest(notice,pushNoticeAddress).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = e.toString();
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseMsg = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseMsg);
                    Message msg = new Message();
                    msg.what = 0;
                    if (jsonObject.getString("errCode").equals("1000")){
                        //说明发送提醒成功
                        msg.obj = "成功向对方发送提醒";
                    }else {
                        msg.obj = jsonObject.getString("errDetail");
                    }
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 将时分转换为long型时间戳,以秒为单位*/
    private long toTimeStamp(int hour,int min){
        return (hour*60+min)*60;
    }
}
