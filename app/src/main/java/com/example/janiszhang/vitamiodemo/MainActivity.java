package com.example.janiszhang.vitamiodemo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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

    private String path = Environment.getExternalStorageDirectory()// /storage/emulated/0
            + "/test2.mp4";
    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    /**
     * �������
     */
    private int mMaxVolume;
    /**
     * ��ǰ����
     */
    private int mVolume = -1;
    /**
     * ��ǰ����
     */
    private float mBrightness = -1f;
    /**
     * ��ǰ����ģʽ
     */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;//����ʶ��
    private MediaController mMediaController;

    /**
     * ��ʱ����
     */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;
        setContentView(R.layout.video_layout);
        FrameLayout llvideo = (FrameLayout) findViewById(R.id.ll);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
//        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
//        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
//        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

//        mVideoView.setVideoPath(path);
        mVideoView.setVideoURI(Uri.parse("http://7xsknj.com1.z0.glb.clouddn.com/test2.mp4"));

//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                Log.i()
//            }
//        });

//        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        mMaxVolume = mAudioManager
//                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        mMediaController = new MediaController(this,true,llvideo);//        new MediaController(this);
        mMediaController.show(5000);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        mVideoView.requestFocus();

//        mGestureDetector = new GestureDetector(this, new MyGestureListener());
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (mGestureDetector.onTouchEvent(event))
//            return true;
//
//        // �������ƽ���
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//            case MotionEvent.ACTION_UP:
//                endGesture();
//                break;
//        }
//
//        return super.onTouchEvent(event);
//    }
//
//    /**
//     * ���ƽ���
//     */
//    private void endGesture() {
//        mVolume = -1;
//        mBrightness = -1f;
//
//        // ����
//        mDismissHandler.removeMessages(0);
//        mDismissHandler.sendEmptyMessageDelayed(0, 500);
//    }
//
//    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        /**
//         * ˫��
//         */
//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
//            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
//                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
//            else
//                mLayout++;
//            if (mVideoView != null)
//                mVideoView.setVideoLayout(mLayout, 0);
//            return true;
//        }
//
//        /**
//         * ����
//         */
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2,
//                                float distanceX, float distanceY) {
//            float mOldX = e1.getX(), mOldY = e1.getY();
//            int y = (int) e2.getRawY();
//            Display disp = getWindowManager().getDefaultDisplay();
//            int windowWidth = disp.getWidth();
//            int windowHeight = disp.getHeight();
//
//            if (mOldX > windowWidth * 4.0 / 5)// �ұ߻���
//                onVolumeSlide((mOldY - y) / windowHeight);
//            else if (mOldX < windowWidth / 5.0)// ��߻���
//                onBrightnessSlide((mOldY - y) / windowHeight);
//
//            return super.onScroll(e1, e2, distanceX, distanceY);
//        }
//    }
//
//    /**
//     * �����ı�������С
//     *
//     * @param percent
//     */
//    private void onVolumeSlide(float percent) {
//        if (mVolume == -1) {
//            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//            if (mVolume < 0)
//                mVolume = 0;
//
//            // ��ʾ
//            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
//            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
//        }
//
//        int index = (int) (percent * mMaxVolume) + mVolume;
//        if (index > mMaxVolume)
//            index = mMaxVolume;
//        else if (index < 0)
//            index = 0;
//
//        // �������
//        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
//
//        // �������
//        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
//        lp.width = findViewById(R.id.operation_full).getLayoutParams().width
//                * index / mMaxVolume;
//        mOperationPercent.setLayoutParams(lp);
//    }
//
//    /**
//     * �����ı�����
//     *
//     * @param percent
//     */
//    private void onBrightnessSlide(float percent) {
//        if (mBrightness < 0) {
//            mBrightness = getWindow().getAttributes().screenBrightness;
//            if (mBrightness <= 0.00f)
//                mBrightness = 0.50f;
//            if (mBrightness < 0.01f)
//                mBrightness = 0.01f;
//
//            // ��ʾ
//            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
//            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
//        }
//        WindowManager.LayoutParams lpa = getWindow().getAttributes();
//        lpa.screenBrightness = mBrightness + percent;
//        if (lpa.screenBrightness > 1.0f)
//            lpa.screenBrightness = 1.0f;
//        else if (lpa.screenBrightness < 0.01f)
//            lpa.screenBrightness = 0.01f;
//        getWindow().setAttributes(lpa);
//
//        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
//        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
//        mOperationPercent.setLayoutParams(lp);
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        if (mVideoView != null)
//            mVideoView.setVideoLayout(mLayout, 0);
//        super.onConfigurationChanged(newConfig);
//    }
}


/**
 * 学长的问题,稍后再解决
 */

//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
//            return;
//        setContentView(R.layout.activity_main);
//
//        Goods good = new Goods("book", 100);
//        Producer p1 = new Producer(good);
//        Producer p2 = new Producer(good);
//        Consumer c1 = new Consumer(good);
//        Consumer c2 = new Consumer(good);
//
//        Thread t1 = new Thread(p1);
//        Thread t2 = new Thread(p2);
//        Thread t3 = new Thread(c1);
//        Thread t4 = new Thread(c2);
//
//        t1.start();
//        //t2.start();
//        //t3.start();
//        t4.start();
//    }
//}
//public class LockPractice {
//
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        Goods good = new Goods("book", 100);
//        Producer p1 = new Producer(good);
//        Producer p2 = new Producer(good);
//        Consumer c1 = new Consumer(good);
//        Consumer c2 = new Consumer(good);
//
//        Thread t1 = new Thread(p1);
//        Thread t2 = new Thread(p2);
//        Thread t3 = new Thread(c1);
//        Thread t4 = new Thread(c2);
//
//        t1.start();
//        //t2.start();
//        //t3.start();
//        t4.start();
//    }
//
//}

//class Goods {
//    String name;
//    volatile boolean flag = true;
//    private int total;
//    private int left = 0;
//
//    Goods(String name, int total){
//        this.name = name;
//        this.total = total;
//    }
//
//    public int getTotal() {
//        return total;
//    }
//
//    public void setTotal(int total) {
//        this.total = total;
//    }
//
//    public int getLeft() {
//        return left;
//    }
//
//    public void setLeft(int left) {
//        this.left = left;
//    }
//}
//
//class Producer implements Runnable {
//    Goods good;
//    Producer(Goods good) {
//        this.good = good;
//    }
//
//    public void produce() {
//        int num = 0;
//        //System.out.println("producing");
//        while(true) {
//
//
//            Log.i("zhangbz", "produce while(true)");
//
//
//            //flag = false
//            if(!good.flag) {
//                System.out.println("p.flag");
//                System.out.println("producing");
//                //flag = false, continue
//                continue;
//            }
//            //flag = true,
//            synchronized(good) {
//
//                int total = good.getTotal();
//                if(total>0) {
//                    good.flag = false;
//                    num++;
//                    good.setLeft(good.getLeft()+1);
//                    good.setTotal(total-1);
//                    System.out.println("producing:"+good.name+"/"+num+" produced;"+good.getLeft()+" left" + "; total = " +  good.getTotal());
//                    System.out.println("p_if:"+good.flag);
//                }
//                else {
//                    System.out.println("production finished");
//                    System.out.println("p_else:"+good.flag);
//                    break;
//                }
//            }
//        }
//
//    }
//
//    public void run() {
//        produce();
//    }
//}
//
//class Consumer implements Runnable {
//    Goods good;
//    Consumer(Goods good) {
//        this.good = good;
//    }
//
//    public void consume() {
//        int num = 0;
//        //System.out.println("consuming");
//        while(true) {
////            Log.i("zhangbz", "-------------------------consume while(true)");
//            if(good.flag) {
//                System.out.println("c.flag");
//                System.out.println("consuming con");
//                //flag = true, continue....
//                continue;
//            }
//
//            //flag = false,
//            synchronized(good) {
//
//                int left = good.getLeft();
//                if(left>0) {
//                    good.flag = true;
//                    num++;
//                    good.setLeft(left-1);
//                    System.out.println("--------consuming:"+good.name+"/"+num+" consumed;"+good.getLeft()+" left" + "; total = " +  good.getTotal());
//                    System.out.println("c_if"+good.flag);
//                }
//                else {
//                    //System.out.println("no "+good.name);
//                    System.out.println("c_else"+good.flag);
//                    continue;
//                }
//            }
//        }
//
//    }
//
//    public void run() {
//        consume();
//    }
//}

