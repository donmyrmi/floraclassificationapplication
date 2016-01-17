package com.sn.floraclassificationapplication.flowerdatabase;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;

import java.util.List;

/**
 * General attributes of a flower in the database:
 * Flower id
 * Flower name
 * Flowering months and weight
 * Locations and location weight
 */
public class FlowerGeneralAtt extends AbstractDBFlower {


    //Labels table name
    public static final String TABLE = "FlowerGeneralAtt";
    //Label table column names
    public static final String KEY_ID = "flower_ID";
    public static final String KEY_NAME = "name";
    public static final String KEY_months = "months";
    public static final String KEY_dateWeight = "dateWeight";
    public static final String KEY_locationWeight = "locationWeight";

    //attributes to keep data
    public int flower_ID;
    public String name;
    public int months;
    public float dateWeight;
    public float locationWeight;

    public FlowerGeneralAtt(){}
}
