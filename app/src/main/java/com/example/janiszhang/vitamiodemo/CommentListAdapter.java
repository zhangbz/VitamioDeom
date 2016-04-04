package com.example.janiszhang.vitamiodemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janiszhang on 2016/4/4.
 */
public class CommentListAdapter extends ArrayAdapter<comment> {

    public CommentListAdapter(Context context, int textViewResourceId, List<comment> dataList) {
        super(context, textViewResourceId, dataList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        comment item = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment);
            viewHolder.author = (TextView) convertView.findViewById(R.id.comment_author);
            viewHolder.time = (TextView) convertView.findViewById(R.id.submint_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.comment.setText(item.getContent());
        viewHolder.author.setText(item.getAuthorName());
        viewHolder.time.setText(item.getCreatedAt());
        return convertView;
    }

    class ViewHolder {
        private TextView author;
        private TextView time;
        private TextView comment;
    }
}
