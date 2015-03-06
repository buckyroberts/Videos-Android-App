/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thenewboston.thenewbostonvideotutorials.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.thenewboston.thenewbostonvideotutorials.R;
import com.thenewboston.thenewbostonvideotutorials.api.DeveloperKey;
import com.thenewboston.thenewbostonvideotutorials.api.VideoAPIManager;
import com.thenewboston.thenewbostonvideotutorials.objects.VideoItem;
import java.util.ArrayList;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Sample activity showing how to properly enable custom fullscreen behavior.
 * <p/>
 * This is the preferred way of handling fullscreen because the default fullscreen implementation
 * will cause re-buffering of the video.
 */
public class VideoPlayActivity extends YouTubeFailureRecoveryActivity implements
        View.OnClickListener,
        YouTubePlayer.OnFullscreenListener {

    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
    public static String CATEGORY_ID = "categoryId";
    public static String CATEGORY_NAME = "categoryName";
    private LinearLayout baseLayout;
    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private View otherViews;
    private boolean fullscreen;
    private LinearLayout navigationBar;
    private LinearLayout playControlLayout;
    private TextView currentVideoTitle, currentVideoTitle2;
    private Button prevVideo, nextVideo;
    private Button prevVideo2, nextVideo2;
    private ListView allVideosListView;
    private ArrayList<VideoItem> videos;
    private int currentIndex = -1;
    private TextView navigationTitleTextView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_videoplayer);
        findViewsAndBind();
        setClickListeners();
        doLayout();
        initImageLoader();
        int categoryId = getIntent().getIntExtra(VideoPlayActivity.CATEGORY_ID, -1);
        String categoryName = getIntent().getStringExtra(VideoPlayActivity.CATEGORY_NAME);
        navigationTitleTextView.setText(categoryName + " Tutorials");
        loadVideosFor(categoryId);
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
    }

    private void findViewsAndBind() {
        navigationBar = (LinearLayout) findViewById(R.id.navigationBar);
        backButton = (ImageButton) findViewById(R.id.navigationLeftButton);
        navigationTitleTextView = (TextView) findViewById(R.id.navigationTitleTextView);

        baseLayout = (LinearLayout) findViewById(R.id.layout);
        playerView = (YouTubePlayerView) findViewById(R.id.player);
        otherViews = findViewById(R.id.other_views);

        currentVideoTitle = (TextView) findViewById(R.id.currentVideoTitle);
        prevVideo = (Button) findViewById(R.id.prevVideoButton);
        nextVideo = (Button) findViewById(R.id.nextVideoButton);

        currentVideoTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf"));
        playControlLayout = (LinearLayout) findViewById(R.id.fullscreenPlayControlLayout);
        currentVideoTitle2 = (TextView) findViewById(R.id.currentVideoTitle2);
        prevVideo2 = (Button) findViewById(R.id.prevVideoButton2);
        nextVideo2 = (Button) findViewById(R.id.nextVideoButton2);
        allVideosListView = (ListView) findViewById(R.id.allVideoslistView);

        playerView.initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    private void setClickListeners() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        allVideosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCurrentVideo(position);
            }
        });

        nextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videos == null) return;
                setCurrentVideo(Math.min(currentIndex + 1, videos.size() - 1));
            }
        });

        prevVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentVideo(Math.max(0, currentIndex - 1));
            }
        });

        nextVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videos == null) return;
                setCurrentVideo(Math.min(currentIndex + 1, videos.size() - 1));
            }
        });

        prevVideo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentVideo(Math.max(0, currentIndex - 1));
            }
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        this.player = player;
        setControlsEnabled();

        // Specify that we want to handle fullscreen behavior ourselves.
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        player.setOnFullscreenListener(this);
        if (!wasRestored && videos != null && videos.size() > 0 && currentIndex > 0) {
            player.cueVideo(videos.get(currentIndex).getCode());
        }
        setFullscreenMode(true);
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onClick(View v) {
        player.setFullscreen(!fullscreen);
    }

    public void setFullscreenMode(boolean isChecked) {
        int controlFlags = player.getFullscreenControlFlags();
        setRequestedOrientation(PORTRAIT_ORIENTATION);
        controlFlags |= YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
        player.setFullscreenControlFlags(controlFlags);
    }

    private void doLayout() {
        LinearLayout.LayoutParams playerParams =
                (LinearLayout.LayoutParams) playerView.getLayoutParams();
        if (fullscreen) {
            // When in fullscreen, the visibility of all other views than the player should be set to
            // GONE and the player should be laid out across the whole screen.
            playerParams.width = LayoutParams.MATCH_PARENT;
            playerParams.height = LayoutParams.MATCH_PARENT;
            // playControlLayout.setVisibility(View.VISIBLE);
            navigationBar.setVisibility(View.GONE);
            otherViews.setVisibility(View.GONE);
        } else {
            // This layout is up to you - this is just a simple example (vertically stacked boxes in
            // portrait, horizontally stacked in landscape).
            playControlLayout.setVisibility(View.GONE);
            navigationBar.setVisibility(View.VISIBLE);
            otherViews.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams otherViewsParams = otherViews.getLayoutParams();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerParams.width = otherViewsParams.width = 0;
                playerParams.height = WRAP_CONTENT;
                otherViewsParams.height = MATCH_PARENT;
                playerParams.weight = 1;
                baseLayout.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                playerParams.width = otherViewsParams.width = MATCH_PARENT;
                playerParams.height = WRAP_CONTENT;
                playerParams.weight = 0;
                otherViewsParams.height = 0;
                baseLayout.setOrientation(LinearLayout.VERTICAL);
            }
            setControlsEnabled();
        }
    }

    private void setCurrentVideo(int index) {
        if (videos == null || videos.size() < 1) {
            prevVideo.setEnabled(false);
            nextVideo.setEnabled(false);
            prevVideo2.setEnabled(false);
            nextVideo2.setEnabled(false);
            return;
        }
        if (currentIndex == index) return;

        currentIndex = index;
        currentVideoTitle.setText((index + 1) + " - " + videos.get(index).getTitle());
        currentVideoTitle2.setText((index + 1) + " - " + videos.get(index).getTitle());
        if (player != null)
            player.cueVideo(videos.get(index).getCode());

        prevVideo.setEnabled(currentIndex > 0);
        nextVideo.setEnabled(currentIndex < videos.size() - 1);
        prevVideo2.setEnabled(currentIndex > 0);
        nextVideo2.setEnabled(currentIndex < videos.size() - 1);
    }

    private void loadVideosFor(int categoryId) {
        VideoAPIManager.getVideosFor(categoryId, new VideoAPIManager.VideoLoaderListener() {
            @Override
            public void onSuccess(ArrayList<VideoItem> results) {
                videos = results;
                currentIndex = -1;
                setCurrentVideo(0);
                createVideoListAdapter();
            }
            @Override
            public void onFailure(Error error) {
            }
        });
    }

    private void createVideoListAdapter() {
        VideosListAdapter listAdapter = new VideosListAdapter(this.videos);
        allVideosListView.setAdapter(listAdapter);
    }

    private void setControlsEnabled() {
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        fullscreen = isFullscreen;
        doLayout();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doLayout();
    }

    class VideosListAdapter implements ListAdapter {

        private ArrayList<VideoItem> _videos;

        VideosListAdapter(ArrayList<VideoItem> videos) {
            _videos = videos;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return position != currentIndex;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return _videos != null ? _videos.size() : 0;
        }

        @Override
        public VideoItem getItem(int position) {
            return _videos != null ? _videos.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return _videos != null ? _videos.get(position).getVideoId() : 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final VideoRowHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_videolist, null);
                holder = new VideoRowHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.videoTitleTextView);
                holder.imageView = (ImageView) convertView.findViewById(R.id.videoPreviewThumb);
                holder.textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"));
                convertView.setTag(holder);
            } else {
                holder = (VideoRowHolder) convertView.getTag();
            }

            holder.textView.setText((position + 1) + " - " + getItem(position).getTitle());
            loadImageFor(holder, position);
            return convertView;
        }

        private void loadImageFor(final VideoRowHolder holder, final int position) {
            final String thumbUrl = getYoutubeVideoThumbUrl(getItem(position).getCode());
            holder.imageView.setTag(thumbUrl);
            holder.imageView.setImageBitmap(null);
            ImageLoader.getInstance().loadImage(
                    thumbUrl,
                    new ImageSize((int) getResources().getDimension(R.dimen.videoThumbWidth),
                            (int) getResources().getDimension(R.dimen.videoThumbHeight)),
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            (new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(10, 0);
                                        loadImageFor(holder, position);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })).run();
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            (new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(10, 0);
                                        loadImageFor(holder, position);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })).run();

                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, Bitmap loadedImage) {
                            if (holder.imageView.getTag().equals(imageUri)) {
                                holder.imageView.setImageBitmap(loadedImage);
                            } else {
                                (new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(10, 0);
                                            loadImageFor(holder, position);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })).run();
                            }
                        }
                    });
        }

        private String getYoutubeVideoThumbUrl(String vid) {
            return String.format("http://img.youtube.com/vi/%s/0.jpg", vid);
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        class VideoRowHolder {
            ImageView imageView;
            TextView textView;
        }

    }


}
