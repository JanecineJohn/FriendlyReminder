package com.example.xin.friendlyreminder.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.javabean.User;
import com.example.xin.friendlyreminder.utils.userInfoManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dell on 2018/2/8.
 */

public class searchUserAdapter extends RecyclerView.Adapter<searchUserAdapter.ViewHolder> {

    private List<User> searchUserList;//查询用户列表集合
    private Context mContext;
    Handler handler;
    /**
     * 初始化内容集合
     */
    public searchUserAdapter(List<User> searchUserList, Handler handler){
        this.searchUserList = searchUserList;
        this.handler = handler;
    }

    /**
     * 初始化布局(放置内容的容器)(相当于声明)
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout view_user_item;
        ImageView view_user_image_userIcon;
        TextView view_user_text_userName,view_user_text_phone;
        TextView view_user_text_title;
        public ViewHolder(View itemView) {
            super(itemView);
            view_user_item = itemView.findViewById(R.id.item);
            view_user_text_userName = itemView.findViewById(R.id.view_user_text_userName);
            view_user_text_phone = itemView.findViewById(R.id.view_user_text_phone);
            view_user_image_userIcon = itemView.findViewById(R.id.view_user_image_userIcon);
            view_user_text_title = itemView.findViewById(R.id.title);
        }
    }

    /**
     * 创建布局，返回viewholder(相当于实例化)
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        //为view组件加载xml布局文件,使用父环境(MainActivity)的布局加载器
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_user,
                parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * 为每个布局设置对应位置的内容*/
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = searchUserList.get(position);
        holder.view_user_text_userName.setText(user.getUserName());
        holder.view_user_text_phone.setText(user.getUserPhone());
        holder.view_user_image_userIcon.setImageResource(R.mipmap.ic_launcher);
        holder.view_user_text_title.setVisibility(View.GONE);

        holder.view_user_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setTitle("确定添加此用户？")
                        .setMessage("用户ID："+ user.getUserID() + '\n' + "用户名："+user.getUserName())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                friendRequest(user.getUserID());
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
        });
    }

    @Override
    public int getItemCount() {
        return searchUserList.size();
    }

    /**
     * 调用好友请求接收接口，发送好友请求
     */
    private void friendRequest(int friendId){
        String acceptAddress = mContext.getResources().getString(R.string.friendRequestAddress);
        try {
            JSONObject jsonObject = new JSONObject();
            final userInfoManager manager = new userInfoManager(mContext);//获得SharedPreferences用户信息管理器
            jsonObject.put("userId",manager.getMyUserId());
            jsonObject.put("friendId",friendId);
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
                        Message msg = new Message();
                        if (jsonObject.getString("errCode").equals("1000")){
                            //errCode为1000,说明添加好友成功
                            Log.i("请求成功：","成功添加好友");
                            msg.obj = "成功发送好友请求，请等待对方确认";
                        }else {
                            //errCode为其他，说明出错了
                            Log.i("errCode=" + jsonObject.getString("errCode"),jsonObject.getString("errDetail"));
                            msg.obj = "不能重复发送好友申请";
                        }
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
