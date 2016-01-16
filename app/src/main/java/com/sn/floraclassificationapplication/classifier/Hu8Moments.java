package com.sn.floraclassificationapplication.classifier;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;

import com.sn.floraclassificationapplication.Flower;

/**
 * Hu 8 moment task
 * Get a segmented image and calculate the 8 moments of it.
 */
public class Hu8Moments extends AsyncTask<Bitmap, Void, double[]> {

    private ShowValues showValuesActivity;
    private double[] moments;
    private static int N_ORDER = 4; // necessary order
    private long cenX,cenY; // centroid X and Y

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

        // Calculate Raw Moments
        for (int p=0; p<2; p++)
            for (int q=0; q<2; q++)
                for (int i=0; i<w; i++)
                    for (int j=0; j<h; j++)
                        if (Color.alpha(bi.getPixel(i,j))!= 0)
                            mpq[p][q] += Math.pow(i,p) * Math.pow(j,q) * bi.getPixel(i, j);

        for (int i=0; i<w; i++)
            for (int j=0; j<h; j++)
                if (Color.alpha(bi.getPixel(i,j))!= 0) {
                    mpq[2][0] += Math.pow(i, 2) * Math.pow(j, 0) * bi.getPixel(i, j);
                    mpq[2][1] += Math.pow(i, 2) * Math.pow(j, 1) * bi.getPixel(i, j);
                    mpq[0][2] += Math.pow(i, 0) * Math.pow(j, 2) * bi.getPixel(i, j);
                    mpq[1][2] += Math.pow(i, 1) * Math.pow(j, 2) * bi.getPixel(i, j);

                    mpq[0][3] += Math.pow(i, 0) * Math.pow(j, 3) * bi.getPixel(i, j);
                    mpq[3][0] += Math.pow(i, 3) * Math.pow(j, 0) * bi.getPixel(i, j);
                }

        // Calculate Central Moments
        double[][] cpq = new double[N_ORDER][N_ORDER];
        cpq[0][0] = mpq[0][0];
        cpq[0][1] = 0;
        cpq[1][0] = 0;
        cpq[1][1] = mpq[1][1] - cenX * mpq[0][1];
        cpq[2][0] = mpq[2][0] - cenX * mpq[1][0];
        cpq[0][2] = mpq[0][2] - cenY * mpq[0][1];
        cpq[2][1] = mpq[2][1] - 2 * cenX * mpq[1][1] - cenY * mpq[2][0] + 2 * Math.pow(cenX,2) * mpq[0][1];
        cpq[1][2] = mpq[1][2] - 2 * cenY * mpq[1][1] - cenX * mpq[0][2] + 2 * Math.pow(cenY,2) * mpq[1][0];
        cpq[3][0] = mpq[3][0] - 3 * cenY * mpq[2][0] + 2 * Math.pow(cenX,2) * mpq[1][0];
        cpq[0][3] = mpq[0][3] - 3 * cenY * mpq[0][2] + 2 * Math.pow(cenY,2) * mpq[0][1];

        // Calculate Scale invariant moments
        double[][] scl = new double[N_ORDER][N_ORDER];
        scl[2][0] = cpq[2][0] / Math.pow(cpq[0][0],1+((2+0)/2));
        scl[0][2] = cpq[0][2] / Math.pow(cpq[0][0],1+((0+2)/2));
        scl[1][1] = cpq[1][1] / Math.pow(cpq[0][0],1+((1+1)/2));
        scl[3][0] = cpq[3][0] / Math.pow(cpq[0][0],1+((3+0)/2));
        scl[0][3] = cpq[0][3] / Math.pow(cpq[0][0],1+((0+3)/2));
        scl[1][2] = cpq[1][2] / Math.pow(cpq[0][0],1+((1+2)/2));
        scl[2][1] = cpq[2][1] / Math.pow(cpq[0][0],1+((2+1)/2));

        // Calculate Hu set of invariant moments
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

    /**
     * Find the X and Y centroid
     * @param bi
     */
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
        // go to the result screen
        showValuesActivity.show();
    }
}


