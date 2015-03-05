package com.thenewboston.thenewbostonvideotutorials.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 1/23/15.
 */
public class VideoItem {
    private int videoId;
    private int categoryId;
    private String title;
    private String code;

    public static VideoItem fromJSONDictionary(JSONObject videoDictionary) {
        VideoItem item = new VideoItem();
        try {
            item.videoId = Integer.valueOf(videoDictionary.getString("videoID"));
            item.categoryId = Integer.valueOf(videoDictionary.getString("categoryID"));
            item.title = videoDictionary.getString("title");
            item.code = videoDictionary.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
