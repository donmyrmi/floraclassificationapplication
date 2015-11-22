package com.sn.floraclassificationapplication.classifier;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.sn.floraclassificationapplication.Flower;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class RGBColorAverage extends AsyncTask<Bitmap, Void, Integer> {

    private ShowValues showValuesActivity;

    public RGBColorAverage(ShowValues showValues) {
        this.showValuesActivity = showValues;
    }

    public static int cal_rgb_averages(Bitmap flowerImage) {
        long rs,gs,bs, pixelCount=0;
        rs=gs=bs=0;
        int h = flowerImage.getHeight();
        int w = flowerImage.getWidth();

        for (int x=0; x < w; x++) {
            for (int y=0; y<h; y++) {
                int tempPixel = flowerImage.getPixel(x, y);
                if (Color.alpha(tempPixel)>0) {
                    rs+=Color.red(tempPixel);
                    gs+=Color.green(tempPixel);
                    bs+=Color.blue(tempPixel);
                    pixelCount++;
                }

            }
        }
        int rTot = (int)(rs / pixelCount);
        int gTot = (int)(gs / pixelCount);
        int bTot = (int)(bs / pixelCount);

        return Color.rgb(rTot,gTot,bTot);
    }

    @Override
    protected Integer doInBackground(Bitmap... params) {
        Bitmap bi = (Bitmap) params[0];
        return cal_rgb_averages(bi);
    }
}
