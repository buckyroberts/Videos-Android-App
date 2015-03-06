package com.thenewboston.thenewbostonvideotutorials.objects;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoCategoryItem {

    public int categoryId;
    public int parentId;
    public int subjectId;
    public String categoryName;
    public String categoryDesc;
    public int videoCount;
    public int forumCategoryId;

    public static VideoCategoryItem fromJSON(JSONObject videoCategoryObject) {
        VideoCategoryItem item = new VideoCategoryItem();
        try {
            item.categoryId = videoCategoryObject.getInt("categoryID");
            item.parentId = videoCategoryObject.getInt("parentID");
            item.subjectId = videoCategoryObject.getInt("subjectID");
            item.categoryName = videoCategoryObject.getString("categoryName");
            item.categoryDesc = videoCategoryObject.getString("categoryDescription");
            item.videoCount = videoCategoryObject.getInt("videosCount");
            item.forumCategoryId = videoCategoryObject.getInt("forumCategoryID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

}
