package com.thenewboston.navigation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.thenewboston.thenewbostonvideotutorials.R;
import java.util.ArrayList;

public class NavigationDrawerAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private ArrayList<Object> childItems;
    private ArrayList<NavigationDrawerItem> parentItems;
    private Context context;

    //Constructor
    public NavigationDrawerAdapter(Context context, ArrayList<NavigationDrawerItem> parents, ArrayList<Object> children) {
        this.context = context;
        this.parentItems = parents;
        this.childItems = children;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
    }

    //Called automatically for each child view (implement per requirement)
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildMenuHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.navigation_drawer_child, parent, false);
            holder = new ChildMenuHolder();
            holder.imageViewChildIcon = (ImageView) convertView.findViewById(R.id.childImage);
            holder.textViewChildName = (TextView) convertView.findViewById(R.id.textViewChild);
            holder.textViewBadge = (TextView) convertView.findViewById(R.id.badgeTextView);
            holder.layoutBadge = (LinearLayout) convertView.findViewById(R.id.childBadgeLayout);
            convertView.setTag(holder);
        } else {
            holder = (ChildMenuHolder) convertView.getTag();
        }

        //Set the text
        NavigationDrawerItem item = (NavigationDrawerItem) getChild(groupPosition, childPosition);
        holder.textViewChildName.setText(item.getItemTitle());
        holder.textViewBadge.setText(item.getBadgeNumber() + "");
        holder.layoutBadge.setVisibility(item.getBadgeNumber() > 0 ? View.VISIBLE : View.GONE);
        holder.imageViewChildIcon.setImageResource(item.getItemIconResourceId());
        return convertView;
    }


    //Called automatically for each parent item (implement per requirement)
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupMenuHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.navigation_drawer_parent, parent, false);
            holder = new GroupMenuHolder();
            holder.imageViewGroupIcon = (ImageView) convertView.findViewById(R.id.imageViewGroupIcon);
            holder.textViewGroupName = (CheckedTextView) convertView.findViewById(R.id.textViewGroupName);
            holder.imageViewExpanded = (ImageView) convertView.findViewById(R.id.imageViewExpanded);
            holder.leftColorShapeView = convertView.findViewById(R.id.leftColorShapeView);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
            holder.textViewGroupName.setTypeface(face);
            convertView.setTag(holder);
        } else {
            holder = (GroupMenuHolder) convertView.getTag();
        }

        holder.textViewGroupName.setText(parentItems.get(groupPosition).getItemTitle());
        holder.textViewGroupName.setChecked(isExpanded);

        if (parentItems.get(groupPosition).getItemIconResourceId() == -1) {
            holder.imageViewGroupIcon.setVisibility(View.GONE);
        } else {
            holder.imageViewGroupIcon.setVisibility(View.VISIBLE);
            holder.imageViewGroupIcon.setImageResource(parentItems.get(groupPosition).getItemIconResourceId());
        }

        holder.leftColorShapeView.setBackgroundColor(parentItems.get(groupPosition).getShapeColor());
        holder.imageViewExpanded.setVisibility(getChildrenCount(groupPosition) > 0 ? View.VISIBLE : View.GONE);
        holder.imageViewExpanded.setImageResource(isExpanded ? R.drawable.menu_icon_group_expanded : R.drawable.menu_icon_group_collapsed);
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ((ArrayList<Object>) childItems.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childItems != null ? ((ArrayList<String>) childItems.get(groupPosition)).size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupMenuHolder {
        View leftColorShapeView;
        ImageView imageViewGroupIcon;
        CheckedTextView textViewGroupName;
        ImageView imageViewExpanded;
    }

    class ChildMenuHolder {
        ImageView imageViewChildIcon;
        TextView textViewChildName;
        TextView textViewBadge;
        LinearLayout layoutBadge;
    }


}