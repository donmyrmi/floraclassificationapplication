package com.sn.floraclassificationapplication.classifier;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.sn.floraclassificationapplication.Flower;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class Hu8Moments{

    private static Hu8Moments ourInstance = new Hu8Moments();
    public static Hu8Moments getInstance() {
        return ourInstance;
    }

    public double[] cal_moments(Bitmap bi) {
        double[] moments = new double[8];
        Cent cent = new Cent(bi);

        int h = bi.getHeight();
        int w = bi.getWidth();

        int n_order = 4;

        double[][] mpq = new double[n_order][n_order];

        for (int p=0; p<n_order; p++)
            for (int q=0; q<n_order; q++)
                for (int i=0; i<w; i++)
                    for (int j=0; j<h; j++)
                        if (Color.alpha(bi.getPixel(i,j))!=00)
                            mpq[p][q] += Math.pow(i-cent.getX(),p) * Math.pow(j - cent.getY(),q) * bi.getPixel(i, j);

        double[][] scl = new double[n_order][n_order];
        for (int i=0; i<n_order; i++)
            for (int j=0; j<n_order; j++)
                scl[i][j] = mpq[i][j] / (Math.pow(mpq[0][0], 1+ ((i + j) / 2)));

        moments[0] = scl[2][0] + scl[0][2];
        moments[1] = Math.pow((scl[2][0] - scl[0][2]),2) + 4 * Math.pow(scl[1][1],2);
        moments[2] = Math.pow((scl[3][0] - 3* scl[1][2]),2) + Math.pow((3 * scl[2][1] - scl[0][3]),2);
        moments[3] = Math.pow((scl[3][0] + scl[1][2]),2) + Math.pow((scl[2][1] + scl[0][3]),2);
        moments[4] = (scl[3][0] - 3 * scl[1][2]) * (scl[3][0] + scl[1][2]) * ( Math.pow((scl[3][0] + scl[1][2]),2) - 3 * Math.pow((scl[2][1]+scl[0][3]),2)) + (3*scl[2][1]-scl[0][3]) * (scl[3][1]+scl[0][3])*( 3*Math.pow((scl[3][0] + scl[1][2]), 2) - Math.pow((scl[2][1] + scl[0][3]),2) );
        moments[5] = (scl[2][0] - scl[0][2]) * ( Math.pow((scl[3][0] + scl[1][2]),2) - Math.pow((scl[2][1] + scl[0][3]),2) ) + 4*scl[1][1]*(scl[3][0] + scl[1][2])*(scl[2][1] + scl[0][3]);
        moments[6] = (3 * scl[2][1] - scl[0][3]) * (scl[3][0] + scl[1][2]) * ( Math.pow((scl[3][0] + scl[1][2]),2) - 3 * Math.pow((scl[2][1]+scl[0][3]),2) ) - (scl[3][0]-3*scl[1][2]) * (scl[2][1]+scl[0][3])*( 3*Math.pow((scl[3][0] + scl[1][2]), 2) - Math.pow((scl[2][1] + scl[0][3]),2) );
        moments[7] = scl[1][1] * ( Math.pow((scl[3][0] + scl[1][2] ),2) - Math.pow((scl[0][3] + scl[2][1]),2) ) - (scl[2][0] - scl[0][2]) * (scl[3][0] + scl[1][2]) * (scl[0][3] + scl[2][1]);

        return moments;
    }

    public double HuDistance(double[] hu1, double[] hu2)
    {
        double sum = 0;
        for (int i=0; i<8; i++) {
            sum += Math.pow(hu1[i] - hu2[i], 2);
        }
        return Math.sqrt(sum);
    }
}


