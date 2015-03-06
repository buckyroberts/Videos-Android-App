package com.thenewboston.thenewbostonvideotutorials.ui;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.thenewboston.thenewbostonvideotutorials.R;
import com.thenewboston.thenewbostonvideotutorials.api.VideoAPIManager;
import com.thenewboston.thenewbostonvideotutorials.objects.VideoCategoryItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class VideoCategoriesFragment extends Fragment {

    private int subjectId = -1;
    private CategoriesAdapter mAdapter;

    public static VideoCategoriesFragment newInstance() {
        return new VideoCategoriesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videocategories_list, container, false);
        mAdapter = new CategoriesAdapter();
        ExpandableListView mListView = (ExpandableListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra(VideoPlayActivity.CATEGORY_ID, mAdapter.getChild(groupPosition, childPosition).categoryId);
                intent.putExtra(VideoPlayActivity.CATEGORY_NAME, mAdapter.getChild(groupPosition, childPosition).categoryName);
                startActivity(intent);
                return true;
            }
        });
        return view;
    }

    //Create a sorted set of videos for a subject
    public void setSubjectId(int newSubjectId) {
        if (this.subjectId == newSubjectId)
            return;
        this.subjectId = newSubjectId;
        VideoAPIManager.getCategoriesFor(subjectId, new VideoAPIManager.VideoCategoryLoaderListener() {
            @Override
            public void onSuccess(HashMap<VideoCategoryItem, ArrayList<VideoCategoryItem>> results) {
                mAdapter.setData(results);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Error error) {
                if (getActivity() == null)
                    return;
                Toast.makeText(getActivity(), error != null ? error.getMessage() : "Unknown Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Adapter for video categories
    private class CategoriesAdapter extends BaseExpandableListAdapter {

        private int[] shapeColors = {0xff3a829b, 0xffa94545, 0xff6e8f5a, 0xffc89351, 0xff6b5876, 0xff68abc8, 0xff425e90, 0xffa2c5d8, 0xffca8ea7, 0xff7e3838};
        private SortedSet<VideoCategoryItem> sortedSet;
        private HashMap<VideoCategoryItem, ArrayList<VideoCategoryItem>> categories = null;

        public CategoriesAdapter() {
        }

        //Sort categories alphabetically
        public void setData(HashMap<VideoCategoryItem, ArrayList<VideoCategoryItem>> results) {
            categories = results;
            sortedSet = new TreeSet<VideoCategoryItem>(new Comparator() {
                @Override
                public int compare(Object lhs, Object rhs) {
                    VideoCategoryItem item1 = (VideoCategoryItem) lhs;
                    VideoCategoryItem item2 = (VideoCategoryItem) rhs;
                    return item1.categoryName.compareTo(item2.categoryName);
                }
            });
            sortedSet.addAll(categories.keySet());
        }

        @Override
        public int getGroupCount() {
            if (categories == null) return 0;
            return categories.keySet().size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (categories == null) return 0;
            Object key = sortedSet.toArray()[groupPosition];
            if (categories.get(key) == null) return 0;
            return categories.get(key).size();
        }

        @Override
        public VideoCategoryItem getGroup(int groupPosition) {
            if (categories == null) return null;
            return (VideoCategoryItem) sortedSet.toArray()[groupPosition];
        }

        @Override
        public VideoCategoryItem getChild(int groupPosition, int childPosition) {
            if (categories == null) return null;
            try {
                return categories.get(getGroup(groupPosition)).get(childPosition);
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return getGroup(groupPosition).categoryId;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return getChild(groupPosition, childPosition).categoryId;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_categories_group, parent, false);
            }
            textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(getGroup(groupPosition).categoryName);

            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.listItemLayout);
            layout.setBackgroundColor(shapeColors[groupPosition % shapeColors.length]);

            convertView.findViewById(R.id.shadowView).setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            ImageView indicator = (ImageView) convertView.findViewById(R.id.indicatorImageView);
            indicator.setImageResource(isExpanded ? R.drawable.group_expanded : R.drawable.group_collapsed);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_categories_sub, parent, false);
            }

            convertView.findViewById(R.id.shadowView).setVisibility(childPosition == 0 ? View.VISIBLE : View.GONE);
            textView = (TextView) convertView.findViewById(R.id.textView);
            textView.setText(getChild(groupPosition, childPosition).categoryName);
            convertView.setBackgroundColor(shapeColors[groupPosition % shapeColors.length]);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }


}
