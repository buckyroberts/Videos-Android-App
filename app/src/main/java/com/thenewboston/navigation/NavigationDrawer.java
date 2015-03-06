package com.thenewboston.navigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.thenewboston.thenewbostonvideotutorials.R;

public class NavigationDrawer extends Fragment {

    private DrawerLayout mainLayout;
    private View navigationDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    //Required empty public constructor
    public NavigationDrawer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.navigation_drawer, container, false);
    }

    //Setting up the Navigation Drawer
    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        navigationDrawer = getActivity().findViewById(fragmentId);
        mainLayout = drawerLayout;

        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                mainLayout.openDrawer(navigationDrawer);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        mainLayout.setDrawerListener(actionBarDrawerToggle);
        mainLayout.post(new Runnable() {
            @Override
            public void run() {
                actionBarDrawerToggle.syncState();
            }
        });
    }


}