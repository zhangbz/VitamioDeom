package com.example.janiszhang.vitamiodemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.janiszhang.vitamiodemo.R;
import com.example.janiszhang.vitamiodemo.adapter.LikesListAdapter;
import com.example.janiszhang.vitamiodemo.bean.VideoData;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by janiszhang on 2016/4/28.
 */
public class PersonCentorActivity extends BaseActivity {

    private TextView mUserName;
    private BmobUser mCurrentUser;
    private Button mLogoutButton;
    private ArrayList<VideoData> mData;
    private LikesListAdapter<VideoData> mMyAdapter;
    private ListView mVideoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_centor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.person_centor_toolbar);//android.support.v7.widget.Toolbar
        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle("个人中心");//设置主标题,写在setSupportActionBar(toolbar);后面无效
        toolbar.setTitleTextColor(Color.WHITE);

        mUserName = (TextView) findViewById(R.id.user_name);
        mLogoutButton = (Button) findViewById(R.id.btn_logout);
        mCurrentUser = BmobUser.getCurrentUser(PersonCentorActivity.this);

        mUserName.setText(mCurrentUser.getUsername());
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut(PersonCentorActivity.this);   //清除缓存用户对象
                Intent intent = new Intent(PersonCentorActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mData = new ArrayList<>();
        mMyAdapter = new LikesListAdapter<VideoData>(this,R.layout.likes_list_item,mData);

        mVideoList = (ListView) findViewById(R.id.likes_list);
        mVideoList.setAdapter(mMyAdapter);
        mVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!mData.get(position).getIsPlaying()) {
                    ShowToast("即将播出,请稍后...");
                    return;
                }
                Intent intent = new Intent(PersonCentorActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("videoName", mData.get(position).getVideoName());
                bundle.putString("videoUrl", mData.get(position).getVideoUrl());
                bundle.putString("id", mData.get(position).getObjectId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        BmobQuery<VideoData> query = new BmobQuery<>();
        query.order("-createdAt");

        query.addWhereEqualTo("likes", mCurrentUser);//成功!!!!
        query.findObjects(PersonCentorActivity.this, new FindListener<VideoData>() {
            @Override
            public void onSuccess(List<VideoData> object) {
                // TODO Auto-generated method stub
                Log.i("zhangbz", "查询成功");
                ShowToast("查询成功：共" + object.size() + "条数据。");
                for (VideoData videoData : object) {
                    mData.add(videoData);
                }
                mMyAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ShowToast("查询失败：" + msg);
            }
        });
    }
}
