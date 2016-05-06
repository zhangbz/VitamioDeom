package com.example.janiszhang.vitamiodemo.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.example.janiszhang.vitamiodemo.adapter.MyAdapter;
import com.example.janiszhang.vitamiodemo.R;
import com.example.janiszhang.vitamiodemo.bean.VideoData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by janiszhang on 2016/4/3.
 * 添加toolbar和searchview的坑:
 * 一开始想用actionbar,但因为各种原因(应该是库的问题),searchview始终不能独立显示出来,
 * 后来改用toolbar,解决了这个问题. (新的东西肯定支持的更好,所以不要贪简单去用老的东西了)
 *
 * searchView:
 * oncreate中没有setSupportActionBar(toolbar);的话,退出功能可以使用,但是searchView不能使用,
 * 原因是onCreateOptionsMenu没有被调用.(没有系统的学习searchview,导致模仿失败,这种弯路虽然难以避免,但以后还是尽量少走)
 */
public class VideoListActivity extends BaseActivity {

    private ListView mVideoList;
    private List<VideoData> mData;
    private ArrayAdapter<VideoData> mMyAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        InitToolbar();

        InitVideoList();

        //查询数据
        BmobQuery<VideoData> query = new BmobQuery<>();
        query.order("-createdAt");
        query.findObjects(VideoListActivity.this, new FindListener<VideoData>() {
            @Override
            public void onSuccess(List<VideoData> object) {
                // TODO Auto-generated method stub
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

    private void InitVideoList() {
        mData = new ArrayList<>();
        mMyAdapter = new MyAdapter<VideoData>(this, R.layout.list_item,mData);

        mVideoList = (ListView) findViewById(R.id.ll_video_list);
        mVideoList.setAdapter(mMyAdapter);
        mVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!mData.get(position).getIsPlaying()){
                    ShowToast("即将播出,请稍后...");
                    return;
                }
                Intent intent = new Intent(VideoListActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("videoName", mData.get(position).getVideoName());
                bundle.putString("videoUrl", mData.get(position).getVideoUrl());
                bundle.putString("id", mData.get(position).getObjectId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void InitToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//android.support.v7.widget.Toolbar

        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle("节目列表");//设置主标题
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);//别忘了,别忘了,别忘了!!!!!!!

        toolbar.inflateMenu(R.menu.options);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if(menuItemId == R.id.action_person_centor) {
                    Intent intent = new Intent(VideoListActivity.this, PersonCentorActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    //searchView相关,本地搜索并定位
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                for (int i = 0;i<mData.size();i++){
                    if(mData.get(i).getVideoName().contains(query)) {
                        mVideoList.setSelection(i);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
}
