package com.dreamguys.truelysell.datamodel;

public class DAOAppTheme {

    private String appColor;
    private boolean isSelected;

    public String getAppColor() {
        return appColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setAppColor(String appColor) {
        this.appColor = appColor;
    }
}
