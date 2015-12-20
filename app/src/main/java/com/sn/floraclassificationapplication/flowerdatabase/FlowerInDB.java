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

public class FlowerInDB extends AbstractDBFlower{
    //Labels table name
    public static final String TABLE = "FlowerInDB";
    //Label table column names
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_flowerImage = "flowerImageIndex";
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
    public static final String KEY_locations = "locations";
    public static final String KEY_months = "months";
    public static final String KEY_momentsWeight = "momentsWeight";
    public static final String KEY_colorWeight = "colorWeight";

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
    public List<com.sn.floraclassificationapplication.flowerdatabase.Location> locations;
    public  byte[] months;
    public float[] momentsWeight;
    public float[] colorWeight;

    public FlowerInDB(int i) {
        //this.flower_ID = i;
        name = "test";
        hu8MomentsMax = new double[HU_MOMENTS_NUM];
        hu8MomentsMin = new double[HU_MOMENTS_NUM];
        momentsWeight = new float[HU_MOMENTS_NUM];
        colorWeight = new float[NUM_OF_COLORS];

    }

    public void setData(Bitmap bi) {
        //this.flowerImage = bi;
    }

    public boolean checkMonth(byte[] checkedMonths)
    {
        for (byte month :
                checkedMonths) {
            if(month < 0 && month > 12)
                return false;
        }
        return true;
    }
}
