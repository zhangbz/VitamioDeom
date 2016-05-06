package com.example.janiszhang.vitamiodemo.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by janiszhang on 2016/4/4.
 */
public class comment extends BmobObject{

    private String content;
    private BmobUser author;
    private VideoData video;
    private String authorName;
    private String videoName;
    private int unit;

    private int type;//1:评论 2:评分
    private int score;

    public comment(String content,String authorName, int type,int score) {
        this.content = content;
        this.authorName = authorName;
        this.type = type;
        this.score = score;
    }

    public comment(String content, BmobUser author, VideoData video, String authorName, String videoName, int type, int score,int unit) {
        this.content = content;
        this.author = author;
        this.video = video;
        this.authorName = authorName;
        this.videoName = videoName;
        this.type = type;
        this.score = score;
        this.unit = unit;
    }

    public comment(String tableName, String content, BmobUser author, VideoData video, String authorName, String videoName, int type, int score,int unit) {
        super(tableName);
        this.content = content;
        this.author = author;
        this.video = video;
        this.authorName = authorName;
        this.videoName = videoName;
        this.type = type;
        this.score = score;
        this.unit = unit;
    }

    public comment() {

    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobUser getAuthor() {
        return author;
    }

    public void setAuthor(BmobUser author) {
        this.author = author;
    }

    public VideoData getVideo() {
        return video;
    }

    public void setVideo(VideoData video) {
        this.video = video;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
