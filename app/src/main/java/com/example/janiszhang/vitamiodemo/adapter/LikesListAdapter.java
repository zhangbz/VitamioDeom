package com.example.janiszhang.vitamiodemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.janiszhang.vitamiodemo.R;
import com.example.janiszhang.vitamiodemo.bean.VideoData;

import java.util.List;

/**
 * Created by janiszhang on 2016/4/3.
 */
public class LikesListAdapter<V> extends ArrayAdapter<VideoData>{

//    private int resourceId;


    public LikesListAdapter(Context context, int textViewResourceId, List<VideoData> dataList) {
        super(context, textViewResourceId, dataList);
//        resourceId = textViewResourceId;
    }
    String lastParent = null;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final VideoData item = (VideoData) getItem(position);
        ViewHolder mViewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.likes_list_item,null);
            mViewHolder = new ViewHolder();
            mViewHolder.videoName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.parent = (TextView) convertView.findViewById(R.id.parent);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.videoName.setText(item.getVideoName());
        mViewHolder.parent.setText(item.getParent());
        return convertView;//super.getView(position, convertView, parent);
    }


    class ViewHolder {
        private TextView videoName;
        private TextView parent;
    }
}
