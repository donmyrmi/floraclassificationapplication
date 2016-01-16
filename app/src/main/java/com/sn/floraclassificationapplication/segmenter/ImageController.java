package com.sn.floraclassificationapplication.segmenter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.sn.floraclassificationapplication.Flower;
import com.sn.floraclassificationapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Nadav on 18-Nov-15.
 */
public class ImageController {
    private static ImageController ourInstance = new ImageController();
    private static float CLEAN_THRESHOLD = 0.1f;

    public static ImageController getInstance() {
        return ourInstance;
    }

    private ImageController() {
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        if (bmpOriginal == null)
            return null;
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public Bitmap makeTransparent(Bitmap src, int value) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = new Paint();
        paint.setAlpha(value);
        canvas.drawBitmap(src, 0, 0, paint);
        return transBitmap;
    }

    public Bitmap overlay(Bitmap bmp1,Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);

        Matrix m=new Matrix();
        canvas.drawBitmap(bmp1, m, null);

        canvas.drawBitmap(bmp2, m, null);
        return bmOverlay;
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = scaleBitmapImage.getWidth();
        int targetHeight = scaleBitmapImage.getHeight();
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

    public Bitmap rotateImage(int degree, Bitmap b) {

        if (degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                    matrix, true);
        }
        return  b;
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public void saveImageToDevice(Bitmap bi, File filename) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bi.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap ConvertFileToBitMap(File file, AppCompatActivity activity){
        Bitmap bitmap;
        Flower flower = new Flower(activity);
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                bitmapOptions);
        ImageView viewImage = (ImageView) activity.findViewById(R.id.flowerView);
        viewImage.setImageBitmap(bitmap);
        String path = android.os.Environment.getExternalStorageDirectory() + File.separator
                + "Phoenix" + File.separator + "default";
        flower.setFlowerImage(bitmap);
        file.delete();
        return bitmap;
    }

    public void getRoundedImageAndSave(AppCompatActivity activity, int [] flowers) {
        for (int i : flowers) {
            String fImage = "mipmap/fl" + i;
            String outFilename = "fl" + i + "c.png";
            File file = new File(activity.getFilesDir(), outFilename);
            int rid = activity.getResources().getIdentifier(fImage, null, activity.getPackageName());
            Bitmap tempBi = decodeSampledBitmapFromResource(activity.getResources(), rid, 256, 256);
            tempBi = getRoundedShape(tempBi);
            saveImageToDevice(tempBi, file);
        }
    }
}
