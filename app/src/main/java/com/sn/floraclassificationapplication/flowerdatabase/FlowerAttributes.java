package com.sn.floraclassificationapplication.flowerdatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Sapir on 05-Dec-15.
 */
public class FlowerAttributes extends AbstractDBFlower{
    //Labels table name
    public static final String TABLE = "FlowerAttributes";
    //Label table column names
    public static final String KEY_ID = "id";
    public static final String KEY_angle = "angle";
    public static final String KEY_hu8MomentsMax = "hu8MomentsMax";
    public static final String KEY_hu8MomentsMin = "hu8MomentsMin";
    public static final String KEY_redMin = "redMin";
    public static final String KEY_redMax = "redMax";
    public static final String KEY_greenMin = "greenMin";
    public static final String KEY_greenMax = "greenMax";
    public static final String KEY_blueMin = "blueMin";
    public static final String KEY_blueMax = "blueMax";

    //attributes to keep data
    public  int flower_ID;
    public  int angle;
    double[] hu8MomentsMax;
    double[] hu8MomentsMin;
    public int redMax;
    public int redMin;
    public int greenMax;
    public int greenMin;
    public int blueMax;
    public int blueMin;
}
