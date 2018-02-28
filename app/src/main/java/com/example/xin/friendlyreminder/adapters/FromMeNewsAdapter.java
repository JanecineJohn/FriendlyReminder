package com.example.xin.friendlyreminder.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xin.friendlyreminder.R;
import com.example.xin.friendlyreminder.javabean.News;

import java.util.List;

/**
 * Created by dell on 2018/2/28.
 */

public class FromMeNewsAdapter extends RecyclerView.Adapter<FromMeNewsAdapter.ViewHolder>{

    List<News> newsList;
    Context mContext;

    /**构造函数*/
    public FromMeNewsAdapter(List<News> newsList){
        this.newsList = newsList;
    }

    //为NewsAdapter创建ViewHolder，作用是容纳组件，并提高效率
    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout newsLinear;
        TextView noticeTime,noticeContent;
        public ViewHolder(View itemView) {
            super(itemView);
            newsLinear = itemView.findViewById(R.id.news_linear);
            noticeTime = itemView.findViewById(R.id.news_noticeTime_tv);
            noticeContent = itemView.findViewById(R.id.news_noticeContent_tv);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        //为view组件加载xml布局文件,使用父环境(MainActivity)的布局加载器
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_news_tome,
                parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsList.get(position);
        //还需进行运算，将时间戳换为时分
        long noticeTime = news.getNoticeTime();
        long noticeHour = noticeTime/60/60;
        long noticeMin = (noticeTime - noticeHour*60*60)/60;
//        holder.noticeTime.setText(news.getNoticeTime()+"");
        holder.noticeTime.setText(noticeHour + "时" + noticeMin + "分");
        holder.noticeContent.setText("发给" + news.getFromUserName() + "的提醒" + '\n' + "内容：" + news.getNoticeContent());
        holder.newsLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"暂未实现此功能",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}