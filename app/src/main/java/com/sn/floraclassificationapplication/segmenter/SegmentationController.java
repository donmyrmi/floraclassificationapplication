package com.sn.floraclassificationapplication.segmenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.sn.floraclassificationapplication.Flower;
import com.sn.floraclassificationapplication.R;


public class SegmentationController {
    private static SegmentationController ourInstance = new SegmentationController();
    private ImageController ic;
    private KMeans km;
    private static int K = 5;
    private int currentSegment;
    private Bitmap output;
    private Bitmap[] flowerSegments;
    private Bitmap bg_bi;
    private static AppCompatActivity activity;
    private ImageView flowerView;
    private Flower segmentedFlower;

    public static SegmentationController getInstance(AppCompatActivity aActivity) {
        activity = aActivity;
        return ourInstance;
    }

    private SegmentationController() {
        ic = ImageController.getInstance();
        km = KMeans.getInstance();
    }

    public void segment(Flower segmentedFlower) {


        init();
        this.segmentedFlower = segmentedFlower;
        Bitmap bi = segmentedFlower.getFlowerImage();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        output = Bitmap.createBitmap(bi.getWidth(), bi.getHeight(), conf);

        cutOutImage(bi);
    }

    public void init()
    {
        currentSegment = 0;


        flowerSegments = new Bitmap[K];
        flowerView = (ImageView) activity.findViewById(R.id.flowerView);
    }

    private void confirmSegmentation() {
        flowerView.setImageBitmap(output);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Confirm");
        builder.setMessage("Is this okay?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                segmentedFlower.setFlowerImage(output);
                dialog.dismiss();
                continueToClassifier();
            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                segment(segmentedFlower);
            }
        });

        AlertDialog alert = builder.create();
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        alert.show();
    }

    private void continueToClassifier() {
        bg_bi = null;
        output = null;
        flowerSegments = null;
        Toast.makeText(activity.getApplicationContext(), "Done! Calculating variables...", Toast.LENGTH_LONG).show();
        segmentedFlower.classify(activity);
    }

    private void cutOutImage(Bitmap bi) {

        bg_bi = ic.toGrayscale(bi);
        bg_bi = ic.makeTransparent(bg_bi, 33);
        flowerView.setImageBitmap(bg_bi);

        flowerSegments = km.KMeans(bi, K);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                askSegmentPart();
            }
        }, 150);
        //askSegmentPart();
    }

    private void askSegmentPart()
    {
        Bitmap temp = ic.overlay(flowerSegments[currentSegment], output);
        flowerView.setImageBitmap(ic.overlay(bg_bi, temp));
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupSegmentDialog();
            }
        }, 1500);
    }

    private void popupSegmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Confirm");
        builder.setMessage("Is the highlighted image is part of the flower? [" + (currentSegment + 1) + "/" + K + "]");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                output = ic.overlay(output, flowerSegments[currentSegment]);
                continueSegmenting();

            }

        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                continueSegmenting();
            }
        });

        builder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        currentSegment--;
                        continueSegmenting();
                    }
                }
        );

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = alert.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        alert.show();
    }

    public void continueSegmenting()
    {
        if (currentSegment < K-1)
        {
            currentSegment++;
            askSegmentPart();
        } else {
            confirmSegmentation();
        }
    }

    public void toastSegmenting() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), "Please hold while I segment the image...", Toast.LENGTH_LONG).show();
            }
        });
        flowerView.setImageBitmap(bg_bi);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }



}
