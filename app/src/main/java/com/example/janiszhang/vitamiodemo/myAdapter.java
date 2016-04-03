package com.example.janiszhang.vitamiodemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by janiszhang on 2016/4/3.
 */
public class myAdapter extends ArrayAdapter{

//    private int resourceId;


    public myAdapter(Context context,int textViewResourceId,List<VideoData> dataList) {
        super(context, textViewResourceId, dataList);
//        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VideoData item = (VideoData) getItem(position);
        ViewHolder mViewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,null);
            mViewHolder = new ViewHolder();
            mViewHolder.videoName = (TextView) convertView.findViewById(R.id.vido_name);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.videoName.setText(item.getVideoName());
        return super.getView(position, convertView, parent);
    }


    class ViewHolder {
        private TextView videoName;
    }
}
