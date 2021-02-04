package com.dreamguys.truelysell.datamodel;

/**
 * Created by Hari on 15-05-2018.
 */

public class DateItem extends ListItem {

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getType() {
        return TYPE_DATE;
    }
}
