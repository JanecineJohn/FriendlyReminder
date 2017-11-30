package com.example.xin.friendlyreminder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.javabean.User;

import java.util.List;

/**
 * Created by xin on 2017/11/7.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{

    private Context mContext;
    private List<User> newsList;//接收得到的好友列表集合

    public FriendsAdapter(List<User> newsList){
        //获取传递过来的消息集合
        this.newsList = newsList;
        //Log.i("集合大小：",this.newsList.size()+"");
    }

    //为NewsAdapter创建ViewHolder，作用是容纳组件，并提高效率
    public class ViewHolder extends RecyclerView.ViewHolder{
        View view_user;
        ImageView view_user_image_userIcon;
        TextView view_user_text_userName,view_user_text_phone;
        public ViewHolder(View itemView) {
            super(itemView);
            view_user = itemView;
            view_user_text_userName = itemView.findViewById(R.id.view_user_text_userName);
            view_user_text_phone = itemView.findViewById(R.id.view_user_text_phone);
            view_user_image_userIcon = itemView.findViewById(R.id.view_user_image_userIcon);
        }
    }
    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        //为view组件加载xml布局文件,使用父环境(MainActivity)的布局加载器
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_user,
                parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder holder, final int position) {
        final User user = newsList.get(position);
        holder.view_user_text_userName.setText(user.getUserName());
        holder.view_user_text_phone.setText(user.getUserPhone());
        holder.view_user_image_userIcon.setImageResource(R.drawable.test);
        /**test*/
        /**为viewHolder设置点击事件*/
        holder.view_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,user.getUserName(),Toast.LENGTH_SHORT).show();
            }
        });
//        if (position<3){
//            holder.view_user_bt_addFriend.setVisibility(View.VISIBLE);
//            holder.view_user_bt_addFriend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(mContext,"点击了位置"+ position + "的添加按钮",Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
