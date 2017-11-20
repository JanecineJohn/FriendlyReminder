package com.example.xin.friendlyreminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xin on 2017/11/7.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private Context mContext;
    private List<User> newsList;//接收得到的好友列表集合

    public NewsAdapter(List<User> newsList){
        //获取传递过来的消息集合
        this.newsList = newsList;
        //Log.i("集合大小：",this.newsList.size()+"");
    }

    //为NewsAdapter创建ViewHolder，作用是容纳组件，并提高效率
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView view_news_text_userName,view_news_text_phone;
        public ViewHolder(View itemView) {
            super(itemView);
            view_news_text_userName = itemView.findViewById(R.id.view_news_text_userName);
            view_news_text_phone = itemView.findViewById(R.id.view_news_text_phone);
        }
    }
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        //为view组件加载xml布局文件,使用父环境(MainActivity)的布局加载器
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_news,
                parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        User user = newsList.get(position);
        holder.view_news_text_userName.setText(user.getUserName());
        holder.view_news_text_phone.setText(user.getUserPhone());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
