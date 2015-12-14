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
    public static final String KEY_flowerImage = "flowerImage";
    public static final String KEY_hu8MomentsMax = "hu8MomentsMax";
    public static final String KEY_hu8MomentsMin = "hu8MomentsMin";
    public static final String KEY_red = "red";
    public static final String KEY_redMin = "redMin";
    public static final String KEY_redMax = "redMax";
    public static final String KEY_redRange = "redRange";
    public static final String KEY_green = "green";
    public static final String KEY_greenMin = "greenMin";
    public static final String KEY_greenMax = "greenMax";
    public static final String KEY_greenRange = "greenRange";
    public static final String KEY_blue = "blue";
    public static final String KEY_blueMin = "blueMin";
    public static final String KEY_blueMax = "blueMax";
    public static final String KEY_blueRange = "blueRange";

    //attributes to keep data
    public  int flower_ID;
    public int flowerImageIndex;//public byte[] flowerImage;//public Bitmap flowerImage;
    double[] hu8MomentsMax;
    double[] hu8MomentsMin;
    //public Color colorMax;
    //public  Color colorMin;
    public int red;
    public int redMax;
    public int redMin;
    public int redRange;
    public int green;
    public int greenMax;
    public int greenMin;
    public int greenRange;
    public int blue;
    public int blueMax;
    public int blueMin;
    public int blueRange;
}
