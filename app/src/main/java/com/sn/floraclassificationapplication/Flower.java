package com.sn.floraclassificationapplication;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import com.sn.floraclassificationapplication.classifier.Hu8Moments;
import com.sn.floraclassificationapplication.classifier.RGBColorAverage;
import com.sn.floraclassificationapplication.classifier.ShowValues;
import com.sn.floraclassificationapplication.segmenter.ImageController;
import com.sn.floraclassificationapplication.segmenter.SegmentationController;



/**
 * Created by Nadav on 19-Nov-15.
 */
public class Flower {
    private int id;
    private String name;
    private Bitmap flowerImage;
    private Bitmap grayImage;
    private double[] hu8Moments;
    private int rgbColorAverage;
    private double latitude, longitude;
    private int month;
    private Hu8Moments hu8MomentCalculator;
    private SegmentationController sm = SegmentationController.getInstance();
    private ImageController ic = ImageController.getInstance();
    private static AppCompatActivity activity;
    private ShowValues showValues;

    public Flower(AppCompatActivity activity)  {
        hu8Moments = new double[8];
        this.activity = activity;
        sm = new SegmentationController();
        sm.setActivity(activity);
        showValues = new ShowValues(activity, this);
    }

    public double getLatitude() { return latitude;}

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return  longitude;}

    public void setLongitude(double longitude) { this.longitude = longitude; }

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

    public String getLocation() {
        return "lat:"+latitude+",lon:"+longitude;
    }

    public void setLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public Bitmap getFlowerImage() {
        return flowerImage;
    }

    public void setFlowerImage(Bitmap flowerImage) {
        if (flowerImage.getWidth() > 1280) {
            flowerImage = ic.getResizedBitmap(flowerImage,1280 *flowerImage.getHeight()/flowerImage.getWidth(), 1280 );
        }
        this.flowerImage = flowerImage.copy(flowerImage.getConfig(),true);
    }

    public int getColor() {
        return rgbColorAverage;
    }

    public void setColor(int color) {
        this.rgbColorAverage = color;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int setToMonth) {
        if (setToMonth < 0 || setToMonth >= 12)
            return;
        month = setToMonth;
    }

    public double[] getHu8Moments() {
        return hu8Moments;
    }

    public void classify() {

        rgbColorAverage = RGBColorAverage.cal_rgb_averages(flowerImage);

        hu8MomentCalculator = new Hu8Moments(showValues, hu8Moments);
        hu8MomentCalculator.execute(grayImage);

        //showValues.show();
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
