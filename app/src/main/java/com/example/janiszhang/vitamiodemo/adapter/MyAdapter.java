package com.example.janiszhang.vitamiodemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.janiszhang.vitamiodemo.R;
import com.example.janiszhang.vitamiodemo.activity.MainActivity;
import com.example.janiszhang.vitamiodemo.activity.VideoListActivity;
import com.example.janiszhang.vitamiodemo.bean.VideoData;

import java.util.List;

/**
 * Created by janiszhang on 2016/4/3.
 */
public class MyAdapter<V> extends ArrayAdapter<VideoData>{

    public MyAdapter(Context context, int textViewResourceId, List<VideoData> dataList) {
        super(context, textViewResourceId, dataList);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final VideoData item = (VideoData) getItem(position);
        ViewHolder mViewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,null);
            mViewHolder = new ViewHolder();
            mViewHolder.videoName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.parent = (TextView) convertView.findViewById(R.id.parent);
            mViewHolder.status = (TextView) convertView.findViewById(R.id.status);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if(position%3 ==0) {
            mViewHolder.parent.setVisibility(View.VISIBLE);
            mViewHolder.parent.setText(item.getParent());
            mViewHolder.status.setText("正在播出");
        }else{
            mViewHolder.parent.setVisibility(View.GONE);
            mViewHolder.status.setText("即将播出");
        }
        mViewHolder.videoName.setText(item.getVideoName());
        return convertView;//super.getView(position, convertView, parent);
    }


    class ViewHolder {
        private TextView videoName;
        private TextView parent;
        private TextView status;
    }
}
