package com.sn.floraclassificationapplication.flowerdatabase;
//based on SQLite Database
/**
 * Created by Nadav and Sapir on 19-Nov-15.
 */
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;

import com.sn.floraclassificationapplication.Flower;
import com.sn.floraclassificationapplication.classifier.Hu8Moments;

import java.util.Comparator;
import java.util.List;

/**
 * Class for each flower in DB.
 */
public class FlowerInDB extends AbstractDBFlower{

    /**
    * SQLite tables and fields
    */

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

    //attributes of the flower in DB
    protected int flower_ID;
    private  String name;
    private int flowerImage;
    private int angle;
    private double[] hu8MomentsMax;
    private double[] hu8MomentsMin;
    private int redMax;
    private int redMin;
    private int greenMax;
    private int greenMin;
    private int blueMax;
    private int blueMin;
    private List<FloweringLocation> locations;
    private  int months;

    private float[] momentsWeight;
    private float colorWeight;
    private float dateWeight;
    private float locationWeight;

    private float rank;

    public FlowerInDB(int fid) {
        flower_ID = fid;
        hu8MomentsMax = new double[HU_MOMENTS_NUM];
        hu8MomentsMin = new double[HU_MOMENTS_NUM];
        momentsWeight = new float[HU_MOMENTS_NUM];
    }

    public int getFlower_ID() {
        return flower_ID;
    }

    public FlowerInDB() {
        hu8MomentsMax = new double[HU_MOMENTS_NUM];
        hu8MomentsMin = new double[HU_MOMENTS_NUM];
        momentsWeight = new float[HU_MOMENTS_NUM];
    }

    /**
     * Month is stored in binary. check if checkedMonth bit is enabled on flowers binary array.
     * @param checkedMonth
     * @return
     */
    public boolean checkMonth(int checkedMonth)
    {
        return ((months & 1 << checkedMonth) != 0);
    }



    public void setHu8MomentsMax(double[] hu8MomentsMax) {
        this.hu8MomentsMax = hu8MomentsMax;
    }

    public void setHu8MomentsMin(double[] hu8MomentsMin) {
        this.hu8MomentsMin = hu8MomentsMin;
    }

    public void setRedMax(int redMax) {
        this.redMax = redMax;
    }

    public void setRedMin(int redMin) {
        this.redMin = redMin;
    }

    public void setGreenMax(int greenMax) {
        this.greenMax = greenMax;
    }

    public void setGreenMin(int greenMin) {
        this.greenMin = greenMin;
    }

    public void setBlueMax(int blueMax) {
        this.blueMax = blueMax;
    }

    public void setBlueMin(int blueMin) {
        this.blueMin = blueMin;
    }

    public float[] getMomentsWeight() {
        return momentsWeight;
    }

    public void setMomentsWeight(float[] momentsWeight) {
        this.momentsWeight = momentsWeight;
    }

    public float getColorWeight() {
        return colorWeight;
    }

    public void setColorWeight(float colorWeight) {
        this.colorWeight = colorWeight;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public List<FloweringLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<FloweringLocation> locations) {
        this.locations = locations;
    }

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public int getFlowerImage() {
        return flowerImage;
    }

    public void setFlowerImage(int flowerImage) {
        this.flowerImage = flowerImage;
    }

    public void setDateWeight(float dateWeight) {
        this.dateWeight = dateWeight;
    }

    public void setLocationWeight(float locationWeight) {
        this.locationWeight = locationWeight;
    }

    /**
     * Calculate rank for this flower based on attributes of segmented flower
     * @param flower - Flower to be classified
     */
    public void calculateRankFromFlower(Flower flower) {
        float temp = 0;
        double [] flowerHues = flower.getHu8Moments();

        // calculate rank of hu moments by weights
        for (int i = 0; i < 8; i++) {
            temp += calculateDoubleDistanceWeight(flowerHues[i], hu8MomentsMin[i], hu8MomentsMax[i], momentsWeight[i]);
        }
        // calculate rank of RGB colors by weights
        temp += calculateDistanceWeight(Color.red(flower.getColor()),redMin, redMax, colorWeight);
        temp += calculateDistanceWeight(Color.green(flower.getColor()),greenMin, greenMax, colorWeight);
        temp += calculateDistanceWeight(Color.blue(flower.getColor()),blueMin, blueMax, colorWeight);

        // calculate rank of flowering dates
        //if (flower.getMonth())
        if (checkMonth(flower.getMonth()))
            temp += dateWeight;
        else if (checkMonth(flower.getMonth()+1%12) || checkMonth(flower.getMonth()-1%12))
            temp += (dateWeight/2);

        // calculate rank of GPS location
        double maxLocationRank = 0;
        for (FloweringLocation floweringLocation : locations) {
            double tempLocationRank = 0;
            tempLocationRank += calculateDoubleDistanceWeight(flower.getLatitude(), floweringLocation.getLatitudeMin(), floweringLocation.getLatitudeMax(), locationWeight / 2);
            tempLocationRank += calculateDoubleDistanceWeight(flower.getLongitude(), floweringLocation.getLongitudeMin(), floweringLocation.getLongitudeMax(), locationWeight/2);
            if (tempLocationRank > maxLocationRank)
                maxLocationRank = tempLocationRank;
        }
        temp += maxLocationRank;
        
        rank = temp;
    }

    /**
     * Calculate variable score based on int values
     * @param value - value to assess
     * @param max - max limit of 100% score
     * @param min - min limit of 100% score
     * @param weight - weight of given attribute
     * @return - weighted score of given value
     */
    private double calculateDistanceWeight(int value, int max, int min, double weight) {
        double temp = 0;
        if (max < min) {
            int z = max;
            max = min;
            min = z;
        }
        int range = max - min;
        if (range == 0)
            range = 1;
        if (value < min) {
            int dist = min - value;
            temp =  1 / Math.pow(2, dist / range) * weight;
        } else if (value > max) {
            int dist = value - max;
            temp =  1 / Math.pow(2, dist / range) * weight;
        } else
            temp =  weight;
        return temp;
    }

    /**
     * Calculate variable score based on double values
     * @param value - value to assess
     * @param max - max limit of 100% score
     * @param min - min limit of 100% score
     * @param weight - weight of given attribute
     * @return - weighted score of given value
     */
    private double calculateDoubleDistanceWeight(double value, double min, double max, double weight) {
        double temp = 0;
        if (max < min) {
            double z = max;
            max = min;
            min = z;
        }
        double range = max - min;
        if (value < min) {
            double dist = min - value;
            temp = 1 / Math.pow(2, dist / range) * weight;
        } else if (value > max) {
            double dist = value - max;
            temp = 1 / Math.pow(2, dist / range) * weight;
        } else
            temp =  weight;
        return temp;
    }

}
