package com.example.janiszhang.vitamiodemo.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by janiszhang on 2016/4/3.
 */
public class VideoData extends BmobObject {//不继承居然也能使用,但是拿不到id

    private String videoName;
    private String videoUrl;
    private BmobRelation likes;
    private Boolean status;
    private String parent;


    public Boolean getIsPlaying() {
        return status;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.status = isPlaying;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        parent = parent;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}
