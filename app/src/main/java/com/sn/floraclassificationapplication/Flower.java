package com.sn.floraclassificationapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;

import com.sn.floraclassificationapplication.classifier.Classifier;
import com.sn.floraclassificationapplication.classifier.Hu8Moments;
import com.sn.floraclassificationapplication.classifier.RGBColorAverage;
import com.sn.floraclassificationapplication.classifier.ShowValues;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class Flower {
    private int id;
    private String name;
    private Bitmap flowerImage;
    private double[] hu8Moments;
    private int color;
    private List<Location> locations;
    private int month;
    private List<Classifier> classifiers;
    Hu8Moments hu8MomentController = Hu8Moments.getInstance();
    RGBColorAverage rgbColorAverage = RGBColorAverage.getInstance();

    public Flower() {

        locations = new ArrayList<Location>();
        classifiers = new ArrayList<Classifier>();
        hu8Moments = new double[8];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public Bitmap getFlowerImage() {
        return flowerImage;
    }

    public void setFlowerImage(Bitmap flowerImage) {
        this.flowerImage = flowerImage;
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int setToMonth) {
        if (setToMonth < 0 || setToMonth > 12)
            return;
        month = 1 << setToMonth;
    }

    public double[] getHu8Moments() {
        return hu8Moments;
    }

    public void calcHu8Moments() {
        this.hu8Moments = hu8MomentController.cal_moments(flowerImage);
    }

    private void calcRGBAverages() {
        this.color = rgbColorAverage.cal_rgb_averages(flowerImage);
    }

    public void classify(AppCompatActivity mainActivity) {

        calcHu8Moments();
        calcRGBAverages();

        ShowValues showValues = new ShowValues(mainActivity, this);
        showValues.show();

    }
}
