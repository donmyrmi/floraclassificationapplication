package com.sn.floraclassificationapplication.classifier;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class RGBColorAverage {
    private static RGBColorAverage ourInstance = new RGBColorAverage();

    public static RGBColorAverage getInstance() {
        return ourInstance;
    }

    public static int cal_rgb_averages(Bitmap flowerImage) {
        int rs,gs,bs, pixelCount=0;
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
        rs /= pixelCount;
        gs /= pixelCount;
        bs /= pixelCount;

        return Color.rgb(rs,gs,bs);
    }
}
