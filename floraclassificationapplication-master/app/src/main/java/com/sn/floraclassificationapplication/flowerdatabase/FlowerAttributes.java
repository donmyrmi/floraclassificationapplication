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
public class FlowerAttributes {
    //Labels table name
    public static final String TABLE = "FlowerAttributes";
    //Label table column names
    public static final String KEY_ID = "id";
    public static final String KEY_ANGLE = "angle";
    public static final String KEY_flowerImage = "flowerImage";
    public static final String KEY_hu8MomentsMax = "hu8MomentsMax";
    public static final String KEY_hu8MomentsMin = "hu8MomentsMin";
    public static final String KEY_colorMax = "colorMax";
    public static final String KEY_colorMin = "colorMin";

    //attributes to keep data
    public  int flower_ID;
    public  int angle;
    public byte[] flowerImage;//public Bitmap flowerImage;
    double[] hu8MomentsMax;
    double[] hu8MomentsMin;
    public Color colorMax;
    public  Color colorMin;

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
