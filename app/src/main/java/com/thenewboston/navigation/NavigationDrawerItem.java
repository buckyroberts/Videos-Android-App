package com.thenewboston.navigation;

public class NavigationDrawerItem {

    private String itemTitle;
    private int itemIconResourceId;
    private int badgeNumber;
    private int shapeColor;

    public NavigationDrawerItem(String title, int icon) {
        itemTitle = title;
        itemIconResourceId = icon;
    }

    public int getItemIconResourceId() {
        return itemIconResourceId;
    }

    public void setItemIconResourceId(int itemIconResourceId) {
        this.itemIconResourceId = itemIconResourceId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(int color) {
        shapeColor = color;
    }

}
