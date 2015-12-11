package com.sn.floraclassificationapplication.flowerdatabase;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;

import java.util.List;

/**
 * Created by Sapir on 05-Dec-15.
 */
public class FlowerGeneralAtt {

    //Labels table name
    public static final String TABLE = "FlowerGeneralAtt";
    //Label table column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_months = "months";
    public static final String KEY_momentsWeight = "momentsWeight";
    public static final String KEY_colorWeight = "colorWeight";

    //attributes to keep data
    public int flower_ID;
    public String name;
    public int months;
    public float momentsWeight;
    public float colorWeight;

    public FlowerGeneralAtt(){}
}
