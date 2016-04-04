package com.example.janiszhang.vitamiodemo;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by janiszhang on 2016/4/3.
 */
public class VideoData extends BmobObject {//不继承居然也能使用,但是拿不到id

    private String videoName;
    private String videoUrl;
    private BmobRelation likes;

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
