package com.sn.floraclassificationapplication.flowerdatabase;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import com.sn.floraclassificationapplication.classifier.Hu8Moments;
import java.util.List;

public class FlowerInDB {
    private int id;
    private String name;
    private Bitmap flowerImage;
    double[] hu8MomentsMax;
    double[] hu8MomentsMin;
    private Color colorMax;
    private Color colorMin;
    private List<Location> locations;
    private int months;

    public FlowerInDB(int i) {
        this.id = i;
        name = "test";
        hu8MomentsMax = new double[8];
        hu8MomentsMin = new double[8];
        months = 123;
    }

    public void setData(Bitmap bi) {
        this.flowerImage = bi;
    }

    public boolean checkMonth(int checkedMonth)
    {
        if (checkedMonth>12 || checkedMonth<0)
            return false;
        return ((months>>checkedMonth) & 1) != 0;
    }
}
