package com.thenewboston.thenewbostonvideotutorials.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import com.thenewboston.navigation.NavigationDrawer;
import com.thenewboston.navigation.NavigationDrawerAdapter;
import com.thenewboston.navigation.NavigationDrawerItem;
import com.thenewboston.thenewbostonvideotutorials.R;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    //ArrayLists to hold NavigationDrawer items
    private ArrayList<NavigationDrawerItem> parentItems = new ArrayList<>();
    private VideoCategoriesFragment fragment;
    private String[] subjectTitles = {"Beauty", "Business", "Computer Science", "Cooking", "Humanities", "Math", "Science", "Social Sciences"};
    private int[] subjectIds = {8, 6, 0, 7, 5, 2, 3, 4};
    private int[] shapeColors = {0xff3a829b, 0xffa94545, 0xffaf6363, 0xffc89351, 0xff69aecb, 0xff6e8f5a, 0xff425e90, 0xffa2c5d8};
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Toolbar to act as Actionbar for this Activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Add NavigationDrawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationDrawer drawerFragment = (NavigationDrawer) getSupportFragmentManager().findFragmentById(R.id.left_drawer);
        drawerFragment.setUp(R.id.left_drawer, drawerLayout, toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Create ExpandableList and set properties
        ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);

        //Set Parent and Children items for navigation
        setParentItems();
        initCategoriesFragment();

        //Create the Adapter
        final NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(this, parentItems, null);
        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        expandableList.setAdapter(adapter);

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                showCategoriesFragmentFor(subjectIds[groupPosition]);
                setTitle(subjectTitles[groupPosition]);
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }

    private void initCategoriesFragment() {
        fragment = VideoCategoriesFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.mainFragment, fragment).commit();
        int currentSubjectIndex = 2;
        showCategoriesFragmentFor(subjectIds[currentSubjectIndex]);
        setTitle(subjectTitles[currentSubjectIndex]);
    }

    private void showCategoriesFragmentFor(int subjectId) {
        fragment.setSubjectId(subjectId);
    }

    //Add parent items
    public void setParentItems() {
        for (int i = 0; i < subjectTitles.length; i++) {
            String subjectTitle = subjectTitles[i];
            NavigationDrawerItem item = new NavigationDrawerItem(subjectTitle, -1);
            item.setShapeColor(shapeColors[i]);
            parentItems.add(item);
        }
    }


}