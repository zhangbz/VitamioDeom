package com.example.janiszhang.vitamiodemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by janiszhang on 2016/4/3.
 */
public class VideoListActivity extends BaseActivity{

    private ListView mVideoList;
    private List<VideoData> mData;
    private ArrayAdapter<VideoData> mMyAdapter;
    private ProgressDialog mProgressDialog;
    private List<VideoData> mVideoDatas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        mData = new ArrayList<>();
        mMyAdapter = new ArrayAdapter<VideoData>(this,R.layout.list_item,mData);

        mVideoList = (ListView) findViewById(R.id.ll_video_list);
        mVideoList.setAdapter(mMyAdapter);

        new MyAsyncTask().execute();
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,List<VideoData>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                mProgressDialog = new ProgressDialog(VideoListActivity.this);
                mProgressDialog.setTitle("提示");
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
            Log.i("zhangbz", "onPreExecute");
        }

        @Override
        protected List<VideoData> doInBackground(Void... params) {
            BmobQuery<VideoData> query = new BmobQuery<>();
//            //查询playerName叫“比目”的数据
//            query.addWhereEqualTo("playerName", "比目");
//            //返回50条数据，如果不加上这条语句，默认返回10条数据
//            query.setLimit(50);
            //执行查询方法
            mVideoDatas = null;
            query.findObjects(VideoListActivity.this, new FindListener<VideoData>() {
                @Override
                public void onSuccess(List<VideoData> object) {
                    // TODO Auto-generated method stub
                    ShowToast("查询成功：共" + object.size() + "条数据。");
                    for (VideoData videoData : object) {
//                        //获得playerName的信息
//                        gameScore.getPlayerName();
//                        //获得数据的objectId信息
//                        gameScore.getObjectId();
//                        //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
//                        gameScore.getCreatedAt();
                        mVideoDatas.add(videoData);
                        Log.i("zhangbz", videoData.getVideoName());
                    }

                }
                @Override
                public void onError(int code, String msg) {
                    // TODO Auto-generated method stub
                    ShowToast("查询失败：" + msg);
                }
            });
            return mVideoDatas;
        }

        @Override
        protected void onPostExecute(List<VideoData> videoDatas) {
            super.onPostExecute(videoDatas);
            mProgressDialog.dismiss();

            for (VideoData videoData : videoDatas) {
                mData.add(videoData);
            }
            mMyAdapter.notifyDataSetChanged();
        }
    }
}
