package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Flowering location of a flower
 */
public class FloweringLocation {
    private double longitudeMax;
    private double longitudeMin;
    private double latitudeMax;
    private double latitudeMin;

    /**
     * Constructor for flowering location.
     * Location is measured in a quadrilateral of Longitude and latitude min/max
     * @param lonMax Longitude max
     * @param lonMin Longitude min
     * @param latMax Latitude max
     * @param latMin Latitude min
     */
    public FloweringLocation(double lonMax, double lonMin, double latMax, double latMin) {
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
