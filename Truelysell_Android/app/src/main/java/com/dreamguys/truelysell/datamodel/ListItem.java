package com.dreamguys.truelysell.datamodel;

/**
 * Created by Hari on 15-05-2018.
 */

public abstract class ListItem {

    public static final int TYPE_DATE = 0;
    public static final int TYPE_GENERAL = 1;

    abstract public int getType();

}
