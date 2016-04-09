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

    public comment() {
    }

    public comment(String content,String authorName) {
        this.content = content;
        this.authorName = authorName;
    }

    public comment(String content, BmobUser author, VideoData video, String authorName, String videoName) {
        this.content = content;
        this.author = author;
        this.video = video;
        this.authorName = authorName;
        this.videoName = videoName;
    }

    public comment(String tableName, String content, BmobUser author, VideoData video, String authorName, String videoName) {
        super(tableName);
        this.content = content;
        this.author = author;
        this.video = video;
        this.authorName = authorName;
        this.videoName = videoName;
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

}
