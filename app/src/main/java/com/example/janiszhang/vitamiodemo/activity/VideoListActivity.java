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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//android.support.v7.widget.Toolbar
        setSupportActionBar(toolbar);//别忘了,别忘了,别忘了!!!!!!!
        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle("节目列表");//设置主标题
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.inflateMenu(R.menu.options);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if(menuItemId == R.id.action_logout) {
                    BmobUser.logOut(VideoListActivity.this);   //清除缓存用户对象
                    BmobUser currentUser = BmobUser.getCurrentUser(VideoListActivity.this); // 现在的currentUser是null了
                    Intent intent = new Intent(VideoListActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });




        mData = new ArrayList<>();
        mMyAdapter = new MyAdapter<VideoData>(this,R.layout.list_item,mData);

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
//                intent.putExtra("video",  (Serializable)mData.get(position));
                startActivity(intent);
            }
        });

//        new MyAsyncTask().execute();

        BmobQuery<VideoData> query = new BmobQuery<>();
//            //查询playerName叫“比目”的数据
//            query.addWhereEqualTo("playerName", "比目");
//            //返回50条数据，如果不加上这条语句，默认返回10条数据
//            query.setLimit(50);
        //执行查询方法
//        final List<VideoData> mVideoDatas = new ArrayList<>();//null不行!!!!!!

//            query.addWhereEqualTo("videoName", "宫12");
                query.order("-createdAt");
        query.findObjects(VideoListActivity.this, new FindListener<VideoData>() {
            @Override
            public void onSuccess(List<VideoData> object) {
                // TODO Auto-generated method stub
                Log.i("zhangbz", "查询成功");
                ShowToast("查询成功：共" + object.size() + "条数据。");
                for (VideoData videoData : object) {
                    mData.add(videoData);
                    Log.i("zhangbz", videoData.getVideoName());
                }
                mMyAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                ShowToast("查询失败：" + msg);
                Log.i("zhangbz", "查询失败");
            }
        });
    }

    //@Override
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i("zhangbzzhangbz", "oncreateOptionsMenu");
//        setHasOptionMenu(true);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(VideoListActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.i("zhangbzz", "onQueryTextSubmit");
                for (int i = 0;i<mData.size();i++){
//                    Log.i("zhangbz", mData.get(i).getVideoName());
                    if(mData.get(i).getVideoName().contains(query)) {
                        mVideoList.setSelection(i);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("zhangbzz", "onQueryTextChange");
                return false;
            }
        });
//        Log.d("Tag", "menu create");
//        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {//设置打开关闭动作监听
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                Toast.makeText(VideoListActivity.this, "onExpand", Toast.LENGTH_LONG).show();
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Toast.makeText(VideoListActivity.this, "Collapse", Toast.LENGTH_LONG).show();
//                return true;
//            }
//        });
        return true;
    }


    //    class MyAsyncTask extends AsyncTask<Void,Integer,List<VideoData>> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//                mProgressDialog = new ProgressDialog(VideoListActivity.this);
//                mProgressDialog.setTitle("提示");
//                mProgressDialog.setMessage("Loading...");
//                mProgressDialog.setCancelable(true);
//                mProgressDialog.show();
//            Log.i("zhangbz", "onPreExecute");
//        }
//
//        @Override
//        protected List<VideoData> doInBackground(Void... params) {
//            BmobQuery<VideoData> query = new BmobQuery<>();
////            //查询playerName叫“比目”的数据
////            query.addWhereEqualTo("playerName", "比目");
////            //返回50条数据，如果不加上这条语句，默认返回10条数据
////            query.setLimit(50);
//            //执行查询方法
//            final List<VideoData> mVideoDatas = new ArrayList<>();//null不行!!!!!!
//
////            query.addWhereEqualTo("videoName", "宫12");
//            query.findObjects(VideoListActivity.this, new FindListener<VideoData>() {
//                @Override
//                public void onSuccess(List<VideoData> object) {
//                    // TODO Auto-generated method stub
//                    Log.i("zhangbz", "查询成功");
//                    ShowToast("查询成功：共" + object.size() + "条数据。");
//                    for (VideoData videoData : object) {
////                        //获得playerName的信息
////                        gameScore.getPlayerName();
////                        //获得数据的objectId信息
////                        gameScore.getObjectId();
////                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
////                        gameScore.getCreatedAt();
//                        mVideoDatas.add(videoData);
//                        Log.i("zhangbz", videoData.getVideoName());
//                    }
//
//                }
//                @Override
//                public void onError(int code, String msg) {
//                    // TODO Auto-generated method stub
//                    ShowToast("查询失败：" + msg);
//                    Log.i("zhangbz", "查询失败");
//                }
//            });
//            return mVideoDatas;
//        }
//
//        @Override
//        protected void onPostExecute(List<VideoData> videoDatas) {
//            super.onPostExecute(videoDatas);
//            mProgressDialog.dismiss();
//            Log.i("zhangbz", "onPostExecute");
//            for (VideoData videoData : videoDatas) {
//                mData.add(videoData);
//                Log.i("zhangbz", ""+ mData.size());
//            }
////            mMyAdapter.notifyDataSetChanged();
//            mMyAdapter = new ArrayAdapter<VideoData>(VideoListActivity.this,R.layout.list_item,mData);
//            mVideoList.setAdapter(mMyAdapter);
//        }
//    }
}
