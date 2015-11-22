package com.sn.floraclassificationapplication.classifier;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.sn.floraclassificationapplication.Flower;

public class Hu8Moments extends AsyncTask<Bitmap, Void, double[]> {

    private ShowValues showValuesActivity;
    private double[] moments;
    private static int N_ORDER = 4;
    private long cenX,cenY;

    public Hu8Moments(ShowValues showValues, double[] moments) {
        this.showValuesActivity = showValues;
        this.moments = moments;
    }

    public static double HuDistance(double[] hu1, double[] hu2)
    {
        double sum = 0;
        for (int i=0; i<8; i++) {
            sum += Math.pow(hu1[i] - hu2[i], 2);
        }
        return Math.sqrt(sum);
    }

    @Override
    protected double[] doInBackground(Bitmap... params) {
        Bitmap bi = params[0];
        find_centroid(bi);

        int h = bi.getHeight();
        int w = bi.getWidth();

        double[][] mpq = new double[N_ORDER][N_ORDER];

        for (int p=0; p<N_ORDER; p++)
            for (int q=0; q<N_ORDER; q++)
                for (int i=0; i<w; i++)
                    for (int j=0; j<h; j++)
                        if (Color.alpha(bi.getPixel(i,j))!=00)
                            mpq[p][q] += Math.pow(i-cenX,p) * Math.pow(j - cenY,q) * bi.getPixel(i, j);

        double[][] scl = new double[N_ORDER][N_ORDER];
        for (int i=0; i<N_ORDER; i++)
            for (int j=0; j<N_ORDER; j++)
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

    public void find_centroid(Bitmap bi)
    {
        int[][] cpq = new int[][] { {1,0},{0,1} };
        int h = bi.getHeight();
        int w = bi.getWidth();
        long[] centroid = new long[3];

        for (int xc = 0; xc<w; xc++) {
            for (int yc = 0; yc < h; yc++) {
                centroid[0] += bi.getPixel(xc,yc);
            }
        }

        for (int xc = 0; xc<w; xc++) {
            for (int yc = 0; yc < h; yc++) {
                centroid[1] += (xc * bi.getPixel(xc,yc));
            }
        }

        for (int xc = 0; xc<w; xc++) {
            for (int yc = 0; yc < h; yc++) {
                centroid[2] += (yc * bi.getPixel(xc,yc));
            }
        }

        cenX = centroid[1] / centroid[0];
        cenY = centroid[2] / centroid[0];
    }

    protected void onPostExecute(double... params) {
        //showValuesActivity.updateMoments();
        showValuesActivity.show();
    }
}


