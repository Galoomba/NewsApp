package com.example.starhood.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Starhood on 7/3/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> dataList;
    private Context context;

    public NewsAdapter(ArrayList datalist, Context context) {
        this.dataList = datalist;
        this.context = context;
    }

    public void addAll(List list) {
        dataList.addAll(list);
    }

    public void clear() {
        int size = this.dataList.size();
        this.dataList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public News getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.news_list_item, parent, false);


        NewsAdapter.NewsViewHolder viewHolder = new NewsAdapter.NewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News data = dataList.get(position);
        holder.Title.setText(data.getaTitle());
        holder.Type.setText(data.getaType());
        holder.Date.setText(data.getaDate());
        holder.Time.setText(data.getaTime());
        holder.Section.setText(data.getaSection());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        protected TextView Title;
        protected TextView Type;
        protected TextView Date;
        protected TextView Time;
        protected TextView Section;


        public NewsViewHolder(View itemView) {
            super(itemView);
            this.Title = (TextView) itemView.findViewById(R.id.Title);
            this.Type = (TextView) itemView.findViewById(R.id.Type);
            this.Date = (TextView) itemView.findViewById(R.id.Date);
            this.Time = (TextView) itemView.findViewById(R.id.Time);
            this.Section = (TextView) itemView.findViewById(R.id.Section);
        }
    }
}
