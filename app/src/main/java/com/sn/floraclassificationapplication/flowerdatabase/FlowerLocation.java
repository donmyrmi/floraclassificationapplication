package com.sn.floraclassificationapplication.flowerdatabase;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sapir on 05-Dec-15.
 */
public class FlowerLocation extends AbstractDBFlower{
    //Labels table name
    public static final String TABLE = "FlowerLocation";
    //Label table column names
    public static final String KEY_ID = "flower_ID";
    public static final String KEY_locations = "locations";

    public int flower_ID;
    public ArrayList<FloweringLocation> locations;

    public FlowerLocation()
    {
        locations = new ArrayList<FloweringLocation>();

    }
}
