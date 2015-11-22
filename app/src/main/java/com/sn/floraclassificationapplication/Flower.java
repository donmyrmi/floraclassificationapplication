package com.sn.floraclassificationapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.sn.floraclassificationapplication.classifier.Classifier;
import com.sn.floraclassificationapplication.classifier.Hu8Moments;
import com.sn.floraclassificationapplication.classifier.RGBColorAverage;
import com.sn.floraclassificationapplication.classifier.ShowValues;
import com.sn.floraclassificationapplication.segmenter.ImageController;
import com.sn.floraclassificationapplication.segmenter.SegmentationController;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nadav on 19-Nov-15.
 */
public class Flower {
    private int id;
    private String name;
    private Bitmap flowerImage;
    private Bitmap grayImage;
    private double[] hu8Moments;
    private int color;
    private List<Location> locations;
    private int month;
    private Hu8Moments hu8MomentCalculator;
    private RGBColorAverage rgbColorAverageCalculator;
    private SegmentationController sm = SegmentationController.getInstance();
    private ImageController ic = ImageController.getInstance();
    private static AppCompatActivity activity;
    private ShowValues showValues;

    public Flower(AppCompatActivity activity)  {
        locations = new ArrayList<Location>();
        //classifiers = new ArrayList<Classifier>();
        hu8Moments = new double[8];
        this.activity = activity;
        sm = new SegmentationController();
        sm.setActivity(activity);
        showValues = new ShowValues(activity, this);
    }

    public AppCompatActivity getActivity() {
        return activity;
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
        this.flowerImage = flowerImage.copy(flowerImage.getConfig(),true);
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
        //this.hu8Moments = hu8MomentController.cal_moments(grayImage);
    }

    public void classify() {
        //Toast.makeText(activity, "Calculating...",Toast.LENGTH_SHORT);

        hu8MomentCalculator = new Hu8Moments();
        hu8MomentCalculator.execute(grayImage);

        rgbColorAverageCalculator = new RGBColorAverage();
        rgbColorAverageCalculator.execute(flowerImage);

        showValues.show();
    }



    public void setGrayImage(Bitmap BWimage) {
        this.grayImage = BWimage.copy(BWimage.getConfig(),true);
    }

    public Bitmap getGrayImage() {
        return grayImage;
    }

    public void segmentAndClassify() {
       sm.segment(this);
    }
}
