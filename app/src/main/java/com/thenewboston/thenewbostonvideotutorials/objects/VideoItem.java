package com.thenewboston.thenewbostonvideotutorials.objects;

import org.json.JSONException;
import org.json.JSONObject;

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

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }


}
