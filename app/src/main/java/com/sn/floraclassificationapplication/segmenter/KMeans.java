package com.sn.floraclassificationapplication.segmenter;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;

public class KMeans {
    private static KMeans ourInstance = new KMeans();

    public static KMeans getInstance() {
        return ourInstance;
    }

    Bitmap original;
    Bitmap result;
    Cluster[] clusters;

    public KMeans() {    }

    public Bitmap[] KMeans(Bitmap image, int k) {
        int w = image.getWidth();
        int h = image.getHeight();

        // create clusters
        int[][] im_clusters = new int[w][h];
        clusters = new Cluster[k];

        //findMinMaxClusters(image, h, w);

        for (int i=0; i<k; i++)
        {
            float tf = (float)(i+1)/(k+2)*256;
            int t = (int) tf;
            int rgb = ((t&0x0ff)<<16)|((t&0x0ff)<<8)|(t&0x0ff);
            clusters[i] = new Cluster(i,rgb);
        }
        // at first loop all pixels will move their clusters
        boolean pixelChangedCluster = true;
        // loop until all clusters are stable!
        while (pixelChangedCluster) {

            for (int i=0; i<k; i++)
                clusters[i].calculateNewCenter();

            pixelChangedCluster = false;
            for (int y=0;y<h;y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = image.getPixel(x, y);
                    if (Color.alpha(pixel) != 0)
                    {
                        int oldCluster = im_clusters[x][y];
                        im_clusters[x][y] = findMinimalCluster(pixel);

                        if (oldCluster != im_clusters[x][y]) {
                            pixelChangedCluster = true;
                            clusters[im_clusters[x][y]].addPixel(pixel);
                        }
                    }
                }
            }

        }

        // create result image
        Bitmap[] result = new Bitmap[k];
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;

        for (int i=0; i<k; i++)
            result[i] = Bitmap.createBitmap(w, h, conf);

        for (int y=0;y<h;y++) {
            for (int x=0; x < w; x++) {

                result[im_clusters[x][y]].setPixel(x, y, image.getPixel(x, y));
            }
        }
        return result;
    }

    private void findMinMaxClusters(Bitmap image, int h, int w) {
        int min = Color.WHITE;
        int max = Color.BLACK;
        for (int y=0;y<h;y++) {
            for (int x = 0; x < w; x++) {
                int temp = image.getPixel(x, y);
                if (temp > Color.alpha(0))
                if (temp < min)
                    min = temp;
                if (temp > max)
                    max = temp;

            }
        }
        clusters[0] = new Cluster(0,min);
        clusters[1] = new Cluster(1,max);
    }

    public int findMinimalCluster(int rgb) {
        int min = 256;
        int i,minI=0;
        for (i=0; i < clusters.length; i++) {
            int distance = clusters[i].distance(rgb);
            if (distance<min) {
                min = distance;
                minI = i;
            }
        }
        return minI;
    }

    class Cluster {
        int id;
        int pixelCount;

        int red_center;
        int green_center;
        int blue_center;

        long reds;
        long greens;
        long blues;

        public Cluster(int id, int color) {
            int r = (color>>16)&0x0ff;
            int g=(color>>8) &0x0ff;
            int b= (color)    &0x0ff;
            red_center = r;
            green_center = g;
            blue_center = b;
        }


        void addPixel(int color) {
            if (Color.alpha(color) != 0)
                return;
            int r = (color>>16)&0x0ff;
            int g=(color>>8) &0x0ff;
            int b= (color)    &0x0ff;
            reds+=r;
            greens+=g;
            blues+=b;
            pixelCount++;
        }

        void removePixel(int color) {
            if (Color.alpha(color) != 0)
                return;
            int r = (color>>16)&0x0ff;
            int g=(color>>8) &0x0ff;
            int b= (color)    &0x0ff;
            reds-=r;
            greens-=g;
            blues-=b;
            pixelCount--;
        }

        int distance(int rgb) {

            int r = (rgb>>16)&0x0ff;
            int g=(rgb>>8) &0x0ff;
            int b= (rgb)    &0x0ff;
            int rx = Math.abs(red_center-r);
            int gx = Math.abs(green_center-g);
            int bx = Math.abs(blue_center-b);
            int d = (rx+gx+bx) / 3;
            return d;
        }

        void calculateNewCenter()
        {
            if (pixelCount == 0)
                return;
            red_center = (int) reds / pixelCount;
            green_center = (int) greens / pixelCount;
            blue_center = (int) blues / pixelCount;
        }
    }
}
