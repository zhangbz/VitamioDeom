package com.example.janiszhang.vitamiodemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.janiszhang.vitamiodemo.R;
import com.example.janiszhang.vitamiodemo.adapter.CommentListAdapter;
import com.example.janiszhang.vitamiodemo.bean.VideoData;
import com.example.janiszhang.vitamiodemo.bean.comment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by janiszhang on 2016/4/28.
 * 使用了hellocharts画折线图
 */
public class CommentActivity extends BaseActivity{

    private LineChartView mChart;
    private LineChartData data;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;
    private String mId;
    private float[] scores = {0f,0f,0f,0f,0f};
    private int[] conut = {0,0,0,0,0};//定一个一维的字符串数组
    private List<PointValue> mValues;
    private ListView mCommentList;
    private CommentListAdapter mCommentListAdapter;
    private ArrayList<comment> mCommentListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_layout);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mId = bundle.getString("id");

        mChart = (LineChartView) findViewById(R.id.chart);
        mChart.setOnValueTouchListener(new ValueTouchListener());

        mCommentListData = new ArrayList<comment>();

        //查询评论
        BmobQuery<comment> query = new BmobQuery<>();
        VideoData videoData = new VideoData();
        videoData.setObjectId(mId);
        query.addWhereEqualTo("video", new BmobPointer(videoData));
        query.include("author,createdAt");
        query.order("-createdAt");
//        query.setLimit(500);
        query.findObjects(this, new FindListener<comment>() {
            @Override
            public void onSuccess(List<comment> list) {
                ShowToast("查询成功：共" + list.size() + "条数据。");
                for (comment comment : list) {
                    mCommentListData.add(comment);
                }
                mCommentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                ShowToast("查询失败：" + s);
            }
        });

        mCommentList = (ListView) findViewById(R.id.comment_list_view);
        mCommentListAdapter = new CommentListAdapter(this, R.layout.comment_list_item, mCommentListData);
        mCommentList.setAdapter(mCommentListAdapter);

        generateValues();

    }


    private void generateValues() {

        //查询评论
        BmobQuery<comment> query1 = new BmobQuery<>();
        VideoData videoData = new VideoData();
        videoData.setObjectId(mId);
        query1.addWhereEqualTo("video", new BmobPointer(videoData));

        BmobQuery<comment> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("type", 2);

        List<BmobQuery<comment>> andQuerys = new ArrayList<BmobQuery<comment>>();
        andQuerys.add(query1);
        andQuerys.add(query2);
        BmobQuery<comment> query3 = new BmobQuery<comment>();
        query3.and(andQuerys);
        query3.include("author,createdAt");
        query3.order("-createdAt");
        query3.setLimit(1000);
        query3.findObjects(this, new FindListener<comment>() {
            @Override
            public void onSuccess(List<comment> list) {
                ShowToast("查询成功：共" + list.size() + "条数据...。");
                for (comment comment : list) {
                    scores[comment.getUnit()] +=comment.getScore();
                    conut[comment.getUnit()] +=1;
                    Log.i("comment", "scores[" +comment.getUnit()+"]" + scores[comment.getUnit()] + "  ; conut[" +comment.getUnit()+"]" + conut[comment.getUnit()]);
                }

                //折线图数据
                mValues = new ArrayList<PointValue>();
                mValues.add(new PointValue(0, scores[0] / conut[0]));
                mValues.add(new PointValue(1, scores[1] / conut[1]));
                mValues.add(new PointValue(2, scores[2] / conut[2]));
                mValues.add(new PointValue(3, scores[3] / conut[3]));
                mValues.add(new PointValue(4, scores[4] / conut[4]));

                Line line = new Line(mValues);
                line.setColor(Color.BLUE);
                line.setShape(shape);
                line.setCubic(true);
                line.setFilled(isFilled);
                line.setHasLabels(hasLabels);
                line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);

                List<Line> lines = new ArrayList<Line>();
                lines.add(line);
                data = new LineChartData(lines);

                if (hasAxes) {
                    Axis axisX = new Axis();
                    ArrayList<AxisValue> axisValues = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        axisValues.add(new AxisValue(i).setLabel("时段" + i));
                    }
                    axisX.setMaxLabelChars(5);
                    axisX.setValues(axisValues);
                    Axis axisY = new Axis().setHasLines(true);
                    if (hasAxesNames) {
                        axisX.setName("时段");
                        axisY.setName("评分");
                    }
                    data.setAxisXBottom(axisX);
                    data.setAxisYLeft(axisY);
                } else {
                    data.setAxisXBottom(null);
                    data.setAxisYLeft(null);
                }

                data.setBaseValue(Float.NEGATIVE_INFINITY);
                mChart.setLineChartData(data);

            }

            @Override
            public void onError(int i, String s) {
                ShowToast("查询失败：" + s);
            }
        });
    }

    //点击每个点时,加载对应的评论
    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(CommentActivity.this, "Selected: " + value, Toast.LENGTH_SHORT).show();

            mCommentListData.clear();
            //查询评论
            BmobQuery<comment> query1 = new BmobQuery<>();
            VideoData videoData = new VideoData();
            videoData.setObjectId(mId);
            query1.addWhereEqualTo("video", new BmobPointer(videoData));

            BmobQuery<comment> query2 = new BmobQuery<>();
            query2.addWhereEqualTo("type", 1);

            BmobQuery<comment> query3 = new BmobQuery<>();
            query3.addWhereEqualTo("unit", value.getX());

            List<BmobQuery<comment>> andQuerys = new ArrayList<BmobQuery<comment>>();
            andQuerys.add(query1);
            andQuerys.add(query2);
            andQuerys.add(query3);
            BmobQuery<comment> query4 = new BmobQuery<comment>();
            query4.and(andQuerys);
            query4.include("author,createdAt");
            query4.order("-createdAt");
            query4.setLimit(1000);
            query4.findObjects(CommentActivity.this, new FindListener<comment>() {
                @Override
                public void onSuccess(List<comment> list) {
                    ShowToast("查询成功：共" + list.size() + "条数据。");
                    for (comment comment : list) {
                        mCommentListData.add(comment);
                    }
                    mCommentListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(int i, String s) {
                    ShowToast("查询失败：" + s);
                }
            });
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
