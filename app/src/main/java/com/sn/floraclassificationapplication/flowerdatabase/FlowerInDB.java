package com.sn.floraclassificationapplication.flowerdatabase;
//based on SQLite Database
/**
 * Created by Nadav and Sapir on 19-Nov-15.
 */
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import com.sn.floraclassificationapplication.classifier.Hu8Moments;
import java.util.List;

public class FlowerInDB {
    //Labels table name
    public static final String TABLE = "FlowerInDB";
    //Label table column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_flowerImage = "flowerImage";
    public static final String KEY_hu8MomentsMax = "hu8MomentsMax";
    public static final String KEY_hu8MomentsMin = "hu8MomentsMin";
    public static final String KEY_colorMax = "colorMax";
    public static final String KEY_colorMin = "colorMin";
    public static final String KEY_months = "months";

    //attributes to keep data
    public  int flower_ID;
    public  String name;
    public  int flowerImageIndex;
    double[] hu8MomentsMax;
    double[] hu8MomentsMin;
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
    //public  Color colorMax;
    //public  Color colorMin;
    public double longitude;
    public double latitude;
    //public List<Location> locations;
    public  int months;

    public FlowerInDB(int i) {
        //this.flower_ID = i;
        name = "test";
        hu8MomentsMax = new double[8];
        hu8MomentsMin = new double[8];
        months = 123;
    }

    public void setData(Bitmap bi) {
        //this.flowerImage = bi;
    }

    public boolean checkMonth(int checkedMonth)
    {
        if (checkedMonth>12 || checkedMonth<0)
            return false;
        return ((months>>checkedMonth) & 1) != 0;
    }
}
