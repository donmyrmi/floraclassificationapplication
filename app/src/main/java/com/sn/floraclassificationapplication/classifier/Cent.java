package com.sn.floraclassificationapplication.classifier;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class Cent {
    private int x,y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Cent(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Cent(Bitmap bi)
    {
        find_centroid(bi);
    }

    public void find_centroid(Bitmap bi)
    {
        int[][] cpq = new int[][] { {0,0},{1,0},{0,1} };
        int h = bi.getHeight();
        int w = bi.getWidth();

        int p,q;
        int[] centroid = new int[3];

        for (int c=0; c<3; c++)
        {
            p = cpq[c][0];
            q = cpq[c][1];
            centroid[c] = 0;

            for (int xc = 0; xc<w; xc++){
                for (int yc = 0; yc<h; yc++) {
                    int pixel = bi.getPixel(x, y);
                    if (Color.alpha(pixel) != 0)
                        centroid[c] += Math.pow(xc,p) * Math.pow(yc,q) * pixel;
                }
            }
        }
        x = centroid[1] / centroid[0];
        y = centroid[2] / centroid[0];
    }
}
