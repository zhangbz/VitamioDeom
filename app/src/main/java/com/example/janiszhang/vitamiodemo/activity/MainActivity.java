package com.example.janiszhang.vitamiodemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.janiszhang.vitamiodemo.adapter.CommentListAdapter;
import com.example.janiszhang.vitamiodemo.R;
import com.example.janiszhang.vitamiodemo.bean.VideoData;
import com.example.janiszhang.vitamiodemo.bean.comment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 *
 * Android���ܲ�����
 *
 * @author ũ�񲮲�
 * @version 2012-5-22
 *
 */
public class MainActivity extends BaseActivity {

    //    private String path = Environment.getExternalStorageDirectory()// /storage/emulated/0
//            + "/test2.mp4";
    private VideoView mVideoView;
    private MediaController mMediaController;
    private EditText mComment;
    private Button mSubmit;
    private String mId;
    private ListView mCommentList;
    private CommentListAdapter mCommentListAdapter;
    private BmobRealTimeData mRtd = new BmobRealTimeData();
    ;
    private String mVideoName;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.video_layout);
        FrameLayout llvideo = (FrameLayout) findViewById(R.id.ll);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mId = bundle.getString("id");
        String videoUrl = bundle.getString("videoUrl");
        mVideoName = bundle.getString("videoName");
        mVideoView.setVideoURI(Uri.parse(videoUrl));

        mMediaController = new MediaController(this, true, llvideo);//        new MediaController(this);
        mMediaController.show(5000);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        mVideoView.requestFocus();

        final ArrayList<comment> commentListData = new ArrayList<comment>();

        BmobQuery<comment> query = new BmobQuery<>();
        VideoData videoData = new VideoData();
        videoData.setObjectId(mId);
        query.addWhereEqualTo("video", new BmobPointer(videoData));
        query.include("author,createdAt");
//        query.order("-createdAt");
        query.findObjects(this, new FindListener<comment>() {
            @Override
            public void onSuccess(List<comment> list) {
                ShowToast("查询成功：共" + list.size() + "条数据。");
                for (comment comment : list) {
                    commentListData.add(comment);
                }
            }

            @Override
            public void onError(int i, String s) {
                ShowToast("查询失败：" + s);
            }
        });

        mCommentList = (ListView) findViewById(R.id.comment_list);
        mCommentListAdapter = new CommentListAdapter(this, R.layout.comment_list_item, commentListData);
        mCommentList.setAdapter(mCommentListAdapter);

        mComment = (EditText) findViewById(R.id.comment);
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser user = BmobUser.getCurrentUser(MainActivity.this);
                VideoData videoData = new VideoData();
                videoData.setObjectId(mId);
                comment comment = new comment();
                comment.setContent(mComment.getText().toString());
                comment.setAuthor(user);
                comment.setVideo(videoData);
                comment.setAuthorName(user.getUsername());
                comment.setVideoName(mVideoName);
                comment.save(MainActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ShowToast("评论成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ShowToast("评论失败:" + s);
                    }
                });
                mComment.setText("");
            }
        });

        //数据监听
        mRtd.start(this, new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                Log.i("zhangbz", "连接成功");
                if (mRtd.isConnected()) {//发现如果是成员变量就不需要是final的
                    mRtd.subTableUpdate("comment");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                ShowToast("数据发生变化了");
                Log.i("zhangbz", "数据发生变化了");
                if (BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data.optString("videoName").equals(mVideoName)) {
                        commentListData.add(new comment(data.optString("content"), data.optString("authorName")));//(String content, BmobUser author, VideoData video, String authorName, String videoName)
                        mCommentListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


}