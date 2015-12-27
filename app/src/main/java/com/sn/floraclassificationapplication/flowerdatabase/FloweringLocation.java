package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Created by Nadav on 25-Dec-15.
 */
public class FloweringLocation {
    private double longitudeMax;
    private double longitudeMin;
    private double latitudeMax;
    private double latitudeMin;

    public FloweringLocation(double lonMin, double latMin, double lonMax, double latMax) {
        longitudeMax = lonMax;
        latitudeMax = latMax;
        longitudeMin = lonMin;
        latitudeMin = latMin;
    }

    public double getLongitudeMax() {
        return longitudeMax;
    }

    public void setLongitudeMax(double longitudeMax) {
        this.longitudeMax = longitudeMax;
    }

    public double getLongitudeMin() {
        return longitudeMin;
    }

    public void setLongitudeMin(double longitudeMin) {
        this.longitudeMin = longitudeMin;
    }

    public double getLatitudeMax() {
        return latitudeMax;
    }

    public void setLatitudeMax(double latitudeMax) {
        this.latitudeMax = latitudeMax;
    }

    public double getLatitudeMin() {
        return latitudeMin;
    }

    public void setLatitudeMin(double latitudeMin) {
        this.latitudeMin = latitudeMin;
    }
}
