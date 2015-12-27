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
        testWeights();
    }

    public FlowerInDB() {
        hu8MomentsMax = new double[HU_MOMENTS_NUM];
        hu8MomentsMin = new double[HU_MOMENTS_NUM];
        momentsWeight = new float[HU_MOMENTS_NUM];
        testWeights();
    }

    public boolean checkMonth(int checkedMonth)
    {
        return ((months & 1 << checkedMonth) != 0);
    }

    private void testWeights() {
        for (int i=0 ; i < 8; i++)
            momentsWeight[i] = 0.075f;
        colorWeight = 0.15f;
        dateWeight = 0.1f;
        locationWeight = 0.15f;

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

    public class CustomComparator implements Comparator<FlowerInDB> {
        @Override
        public int compare(FlowerInDB o1, FlowerInDB o2) {
            return (int) (o1.getRank() - o2.getRank());
        }
    }

    public void calculateRankFromFlower(Flower flower) {
        float temp = 0;
        double [] flowerHues = flower.getHu8Moments();

        // calculate rank of hu moments by weights
        for (int i = 0; i < 8; i++) {
            temp += calculateHuDistanceWeight(flowerHues[i], hu8MomentsMin[i], hu8MomentsMax[i], momentsWeight[i]);
        }
        // calculate rank of RGB colors by weights
        temp += calculateDistanceWeight(Color.red(flower.getColor()),redMin, redMax, colorWeight);
        temp += calculateDistanceWeight(Color.green(flower.getColor()),greenMin, greenMax, colorWeight);
        temp += calculateDistanceWeight(Color.blue(flower.getColor()),blueMin, blueMax, colorWeight);

        // calculate rank of flowering dates
        //if (flower.getMonth())
        if (checkMonth(flower.getMonth()))
            temp += dateWeight;

        // calculate rank of GPS location
        double locMin = Double.MAX_VALUE;
        for (FloweringLocation floweringLocation : locations) {
            double dist = floweringLocation.checkDistance(flower.getLatitude(), flower.getLongitude());
            if (dist < locMin)
                locMin = dist;
        }
        temp += Math.pow(2, locMin) * locationWeight;
        rank = temp * 100;
    }

    private double calculateDistanceWeight(int value, int min, int max, double weight) {
        double temp = 0;
        int range = max - min;
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

    private double calculateHuDistanceWeight(double value, double min, double max, double weight) {
        double temp = 0;
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
