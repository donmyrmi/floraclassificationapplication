package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Created by Nadav on 25-Dec-15.
 */
public class FloweringLocation {
    private double longitude;
    private double latitude;
    private double radius;

    public FloweringLocation(double lon, double lat, double rad) {
        longitude = lon;
        latitude = lat;
        radius = rad;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longtitude) {
        this.longitude = longtitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double checkDistance(double lati, double lon) {
        Double dist = Math.pow(lon-longitude,2) + Math.pow(lati - latitude, 2);
        Math.sqrt(dist);
        if (radius >= dist)
            return 1;
        return radius/dist;
    }
}
