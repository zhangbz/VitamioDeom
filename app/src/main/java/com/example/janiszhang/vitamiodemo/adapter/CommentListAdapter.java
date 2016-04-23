package com.example.janiszhang.vitamiodemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.janiszhang.vitamiodemo.R;
import com.example.janiszhang.vitamiodemo.bean.comment;

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
            viewHolder.comment = (TextView) convertView.findViewById(R.id.tv_comment);
            viewHolder.author = (TextView) convertView.findViewById(R.id.comment_author);
            viewHolder.time = (TextView) convertView.findViewById(R.id.submint_time);
            viewHolder.score = (RatingBar) convertView.findViewById(R.id.score);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(item.getType()==1) {
            viewHolder.comment.setVisibility(View.VISIBLE);
            viewHolder.score.setVisibility(View.GONE);
            viewHolder.comment.setText(item.getContent());
        } else {
            viewHolder.comment.setVisibility(View.GONE);
            viewHolder.score.setVisibility(View.VISIBLE);
            viewHolder.score.setRating(item.getScore());
        }
        viewHolder.author.setText(item.getAuthorName());
        viewHolder.time.setText(item.getCreatedAt());

        return convertView;
    }

    class ViewHolder {
        private TextView author;
        private TextView time;
        private TextView comment;
        private RatingBar score;
    }
}
