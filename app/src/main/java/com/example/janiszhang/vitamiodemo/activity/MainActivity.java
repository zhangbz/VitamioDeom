package com.example.janiszhang.vitamiodemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.example.janiszhang.vitamiodemo.R;
import com.example.janiszhang.vitamiodemo.adapter.CommentListAdapter;
import com.example.janiszhang.vitamiodemo.bean.VideoData;
import com.example.janiszhang.vitamiodemo.bean.comment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

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
    private String mVideoName;
    private LinearLayout mChooseLl;
    private RelativeLayout mCommentRl;
    private RelativeLayout mScoreRl;
    private Button mCommentBtn;
    private Button mScoreBtn;
    private RatingBar mRatingBar;
    private Button mScoreSubmit;
    private boolean flag = false;
    private boolean mLikeFlag = false;
    private long mDuration = -1;
    private long mCurrentPosition;
    private int mUnit;
    private int mNum1;
    private int mNum2;
    private float mTemp;
    private ArrayList<String> mStrings;
    private String mVideoUrl;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.video_layout);

        final String userId = BmobUser.getCurrentUser(MainActivity.this).getObjectId();
        GetDataFromIntent();

        //视频设置
        FrameLayout llvideo = (FrameLayout) findViewById(R.id.ll);
        mVideoView = (VideoView) findViewById(R.id.surface_view);

        mVideoView.setVideoURI(Uri.parse(mVideoUrl));

        mMediaController = new MediaController(this, true, llvideo);//        new MediaController(this);
        mMediaController.show(5000);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
        mVideoView.requestFocus();

        final ArrayList<comment> commentListData = new ArrayList<comment>();

        //查询评论
        BmobQuery<comment> query = new BmobQuery<>();
        VideoData videoData = new VideoData();
        videoData.setObjectId(mId);
        query.addWhereEqualTo("video", new BmobPointer(videoData));
        query.include("author,createdAt");
        query.order("-createdAt");
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


        //toolbar相关
        Toolbar toolbar = (Toolbar) findViewById(R.id.video_toolbar);//android.support.v7.widget.Toolbar
        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle(mVideoName);//设置主标题,写在setSupportActionBar(toolbar);后面无效
        toolbar.setTitleTextColor(Color.WHITE);
//        setSupportActionBar(toolbar);//加上这句话,menu显示不出来

        toolbar.inflateMenu(R.menu.video_options);//设置右上角的填充菜单
        final ActionMenuItemView love = (ActionMenuItemView) findViewById(R.id.action_love);
        //查询是否已经收藏
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<BmobUser> query2 = new BmobQuery<BmobUser>();
                VideoData videoData1 = new VideoData();
                videoData1.setObjectId(mId);
                //likes是Post表中的字段，用来存储所有喜欢该帖子的用户
                query2.addWhereRelatedTo("likes", new BmobPointer(videoData1));
                query2.findObjects(MainActivity.this, new FindListener<BmobUser>() {

                    @Override
                    public void onSuccess(List<BmobUser> object) {
                        // TODO Auto-generated method stub
                        Log.i("life", "查询个数：" + object.size());
                        for (BmobUser user : object) {
                            if (TextUtils.equals(user.getObjectId(), userId)) {
                                if (love != null) {
                                    love.setText("已收藏");
                                    love.setTextColor(Color.RED);
                                    mLikeFlag = true;
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(int code, String msg) {
                        // TODO Auto-generated method stub
                        Log.i("life", "查询失败：" + code + "-" + msg);
                    }
                });
            }
        }).start();

        //toolbar监听设置
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_love) {
                    if (!mLikeFlag) {
                        BmobUser user = BmobUser.getCurrentUser(MainActivity.this);
                        VideoData videoData = new VideoData();
                        videoData.setObjectId(mId);
                        BmobRelation relation = new BmobRelation();
                        relation.add(user);
                        videoData.setLikes(relation);
                        videoData.update(MainActivity.this, new UpdateListener() {

                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                Log.i("life", "多对多关联添加成功");
                            }

                            @Override
                            public void onFailure(int arg0, String arg1) {
                                // TODO Auto-generated method stub
                                Log.i("life", "多对多关联添加失败");
                            }
                        });
                        mLikeFlag = true;
                        love.setText("已收藏");
                        love.setTextColor(Color.RED);
                    } else {
                        Log.i("zhangbz", "mLikeFlag = " + mLikeFlag);
                        BmobUser user = BmobUser.getCurrentUser(MainActivity.this);
                        VideoData videoData = new VideoData();
                        videoData.setObjectId(mId);
                        BmobRelation relation = new BmobRelation();
                        relation.remove(user);
                        videoData.setLikes(relation);
                        videoData.update(MainActivity.this, new UpdateListener() {

                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                Log.i("life", "关联关系删除成功");
                            }

                            @Override
                            public void onFailure(int arg0, String arg1) {
                                // TODO Auto-generated method stub
                                Log.i("life", "关联关系删除失败：" + arg0 + "-" + arg1);
                            }
                        });
                        mLikeFlag = false;
                        love.setText("收藏");
                        love.setTextColor(Color.BLACK);
                    }
                }
                if (menuItemId == R.id.action_comment) {
                    Intent intent1 = new Intent(MainActivity.this, CommentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", mId);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
                return true;
            }
        });


        mChooseLl = (LinearLayout) findViewById(R.id.ll_choose);
        mCommentRl = (RelativeLayout) findViewById(R.id.rl_comment);
        mScoreRl = (RelativeLayout) findViewById(R.id.rl_score);

        mCommentBtn = (Button) findViewById(R.id.bt_comment);
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooseLl.setVisibility(View.INVISIBLE);
                mScoreRl.setVisibility(View.INVISIBLE);
                mCommentRl.setVisibility(View.VISIBLE);
                flag = true;
            }
        });

        mScoreBtn = (Button) findViewById(R.id.bt_score);
        mScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooseLl.setVisibility(View.INVISIBLE);
                mScoreRl.setVisibility(View.VISIBLE);
                mCommentRl.setVisibility(View.INVISIBLE);
                flag = true;
            }
        });

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mRatingBar.setStepSize(1);
        mScoreSubmit = (Button) findViewById(R.id.score_submit);
        mScoreSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUnit = getUnit();
                BmobUser user = BmobUser.getCurrentUser(MainActivity.this);
                VideoData videoData = new VideoData();
                videoData.setObjectId(mId);


                comment comment = new comment();
                comment.setContent("");
                comment.setAuthor(user);
                comment.setVideo(videoData);
                comment.setAuthorName(user.getUsername());
                comment.setVideoName(mVideoName);
                comment.setType(2);
                comment.setScore((int) mRatingBar.getRating());
                comment.setUnit(mUnit);
                comment.save(MainActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ShowToast("评分成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ShowToast("评分失败:" + s);
                    }
                });

                /**
                 * 以下代码用于批量生成数据,如果你对现有数据不满意,可以删除掉,使用下面的代码重新生成
                 */
//                for (int i = 0; i<300; i++) {
//                    comment comment = new comment();
//                    comment.setContent("");
//                    comment.setAuthor(user);
//                    comment.setVideo(videoData);
//                    comment.setAuthorName(user.getUsername());
//                    comment.setVideoName(mVideoName);
//                    comment.setType(2);
////                    comment.setScore((int) mRatingBar.getRating());
////                    comment.setUnit(mUnit);
//                    comment.setScore(getRandomScore());
//                    comment.setUnit(getRandomUnit());
//                    comment.save(MainActivity.this, new SaveListener() {
//                        @Override
//                        public void onSuccess() {
//                            ShowToast("评分成功");
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//                            ShowToast("评分失败:" + s);
//                        }
//                    });
//                }
//                mComment.setText("");
//                mRatingBar.setRating(3);
                resetBottom();
            }
        });

        //评论和评分按钮的事件设置
        mComment = (EditText) findViewById(R.id.ed_comment);
        mSubmit = (Button) findViewById(R.id.submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUnit = getUnit();
                BmobUser user = BmobUser.getCurrentUser(MainActivity.this);
                VideoData videoData = new VideoData();
                videoData.setObjectId(mId);
                comment comment = new comment();
                comment.setContent(mComment.getText().toString());
                comment.setAuthor(user);
                comment.setVideo(videoData);
                comment.setAuthorName(user.getUsername());
                comment.setVideoName(mVideoName);
                comment.setType(1);
                comment.setScore(0);
                comment.setUnit(mUnit);
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


//                for (int i = 0;i<300;i++) {
//                    comment comment = new comment();
//                    comment.setContent(getRandomContent());
//                    comment.setAuthor(user);
//                    comment.setVideo(videoData);
//                    comment.setAuthorName(user.getUsername());
//                    comment.setVideoName(mVideoName);
//                    comment.setType(1);
//                    comment.setScore(0);
//                    comment.setUnit(getRandomUnit());
//                    comment.save(MainActivity.this, new SaveListener() {
//                        @Override
//                        public void onSuccess() {
//                            ShowToast("评论成功");
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//                            ShowToast("评论失败:" + s);
//                        }
//                    });
//                }
                mComment.setText("");
                resetBottom();
            }
        });
        //数据监听
        mRtd.start(this, new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                if (mRtd.isConnected()) {//发现如果是成员变量就不需要是final的
                    mRtd.subTableUpdate("comment");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                ShowToast("数据发生变化了");
                if (BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    if (data.optString("videoName").equals(mVideoName)) {
                        commentListData.add(new comment(data.optString("content"), data.optString("authorName"), data.optInt("type"), data.optInt("score")));//(String content, BmobUser author, VideoData video, String authorName, String videoName)
                        mCommentListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        initCommentTestData();
    }

    private void GetDataFromIntent() {
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mId = bundle.getString("id");
        mVideoUrl = bundle.getString("videoUrl");
        mVideoName = bundle.getString("videoName");
    }

    //用于判断属于那个时段,你可以自行添加
    private int getUnit() {
        int unit;
        if (mDuration == -1) {
            mDuration = mVideoView.getDuration();
        }
        mCurrentPosition = mVideoView.getCurrentPosition();
        mNum1 = (int) (mCurrentPosition / 60000);
        mNum2 = (int) (mDuration / 60000);
        mTemp = (float) ((mNum1 * 1.0) / mNum2);

        if (mTemp <= 0.25) {
            unit = 1;
        } else if (mTemp > 0.25 && mTemp <= 0.5) {
            unit = 2;
        } else if (mTemp > 0.5 && mTemp <= 0.75) {
            unit = 3;
        } else {
            unit = 4;
        }
        Log.i("duration", "durantion = " + mDuration + " ; currentPosition = " + mCurrentPosition + " ;temp = " + mTemp + " ; unit = " + unit);
        return unit;
    }

    //用于生成随机数据
    private String getRandomContent() {

        Random random = new Random();
        int index = random.nextInt(10);
        return mStrings.get(index);
    }

    private int getRandomUnit() {
        Random random = new Random();
        return random.nextInt(5);
    }

    //在一定范围内生成随机数.
    //比如此处要求在[0 - n)内生成随机数.
    //注意:包含0不包含n
    private int getRandomScore() {

        Random random = new Random();
        return random.nextInt(6);

    }

    //这里的字符串会在随机评论时用到,你也可以自己添加修改
    private void initCommentTestData() {
        mStrings = new ArrayList<>();
        mStrings.add("好看!!!");
        mStrings.add("不好看.");
        mStrings.add("男神,我爱你");
        mStrings.add("女神嫁给我!");
        mStrings.add("还可以");
        mStrings.add("弃坑");
        mStrings.add("期待下一集");
        mStrings.add("不想看了");
        mStrings.add("好看好看");
        mStrings.add("李楠,我爱你,我是来表白的...");
    }

    private void resetBottom() {
        mCommentRl.setVisibility(View.INVISIBLE);
        mScoreRl.setVisibility(View.INVISIBLE);
        mChooseLl.setVisibility(View.VISIBLE);
        flag = false;
    }

    @Override
    public void onBackPressed() {

        if (flag) {
            resetBottom();
        } else {
            super.onBackPressed();
        }
    }
}