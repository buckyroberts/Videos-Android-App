package com.thenewboston.thenewbostonvideotutorials.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thenewboston.thenewbostonvideotutorials.objects.VideoCategoryItem;
import com.thenewboston.thenewbostonvideotutorials.objects.VideoItem;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VideoAPIManager {

    public static void getCategoriesFor(int subjectId, final VideoCategoryLoaderListener listener) {
        String apiEntry = "/api/v1/api.php?TYPE=video&ACTION=getCategories&subject=" + subjectId;
        CustomHttpClient.get(apiEntry, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Iterator<String> catIds = response.keys();
                HashMap<VideoCategoryItem, ArrayList<VideoCategoryItem>> results = new HashMap<VideoCategoryItem, ArrayList<VideoCategoryItem>>();
                do {
                    String catId = catIds.next();
                    try {
                        String catName = response.getJSONObject(catId).getString("categoryName");
                        JSONArray subCategories = response.getJSONObject(catId).getJSONArray("categories");

                        VideoCategoryItem groupItem = new VideoCategoryItem();
                        groupItem.categoryId = Integer.valueOf(catId);
                        groupItem.categoryName = catName;

                        ArrayList<VideoCategoryItem> array = new ArrayList<VideoCategoryItem>();
                        for (int i = 0; i < subCategories.length(); i++) {
                            array.add(VideoCategoryItem.fromJSON(subCategories.getJSONObject(i)));
                        }
                        results.put(groupItem, array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while (catIds.hasNext());
                listener.onSuccess(results);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (throwable != null)
                    listener.onFailure(new Error(throwable.getMessage()));
                else
                    listener.onFailure(new Error("Status Code - " + statusCode));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (throwable != null)
                    listener.onFailure(new Error(throwable.getMessage()));
                else
                    listener.onFailure(new Error("Status Code - " + statusCode));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (throwable != null)
                    listener.onFailure(new Error(throwable.getMessage()));
                else
                    listener.onFailure(new Error("Status Code - " + statusCode));
            }

        });
    }

    public static void getVideosFor(int categoryId, final VideoLoaderListener listener) {
        String apiEntry = "/api/v1/api.php?TYPE=video&ACTION=getVideos&cat=" + categoryId;
        CustomHttpClient.get(apiEntry, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                ArrayList<VideoItem> results = new ArrayList<VideoItem>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject videoDictionary = response.getJSONObject(i);
                        results.add(VideoItem.fromJSONDictionary(videoDictionary));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listener.onSuccess(results);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailure(null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.onFailure(null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                listener.onFailure(null);
            }

        });
    }

    public interface VideoLoaderListener {
        public abstract void onSuccess(ArrayList<VideoItem> results);
        public abstract void onFailure(Error error);
    }

    public interface VideoCategoryLoaderListener {
        public abstract void onSuccess(HashMap<VideoCategoryItem, ArrayList<VideoCategoryItem>> results);
        public abstract void onFailure(Error error);
    }


}
