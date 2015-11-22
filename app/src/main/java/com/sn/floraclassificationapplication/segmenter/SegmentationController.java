package com.sn.floraclassificationapplication.segmenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
    private TextView confTextView;
    private Button buttonYes, buttonNo;
    private View.OnClickListener onClickListenerNo, onClickListenerYes;
    private Thread thread;

    public static SegmentationController getInstance() {
        return ourInstance;
    }

    public SegmentationController() {
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
        buttonYes = (Button) activity.findViewById(R.id.confYesButton);
        buttonNo = (Button) activity.findViewById(R.id.confNoButton);
        confTextView = (TextView) activity.findViewById(R.id.confTextView);

        flowerSegments = new Bitmap[K];
        flowerView = (ImageView) activity.findViewById(R.id.flowerView);
        hideDialog();
    }

    private void confirmSegmentation() {
        flowerSegments = null;
        bg_bi = null;
        flowerView.setImageBitmap(output);
        confTextView.setText("Confirm segmentation?");
        confTextView.setVisibility(TextView.VISIBLE);
        onClickListenerYes = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segmentedFlower.setFlowerImage(output);
                continueToClassifier();
            }
        };
        buttonYes.setOnClickListener(onClickListenerYes);
        buttonYes.setVisibility(Button.VISIBLE);

        onClickListenerNo = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                segment(segmentedFlower);
            }
        };
        buttonNo.setOnClickListener(onClickListenerNo);
        buttonNo.setVisibility(Button.VISIBLE);
    }


    private void continueToClassifier() {
        hideDialog();
        bg_bi = null;

        thread = new Thread(){
            @Override
            public void run() {
                try {
                    segmentedFlower.setFlowerImage(output);
                    output = null;
                } catch (Exception e) {}
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        segmentedFlower.classify();
                    }
                });
            }
        };
        thread.start();

    }


    private void cutOutImage(Bitmap bi) {

        bg_bi = ic.toGrayscale(bi);
        bg_bi = ic.makeTransparent(bg_bi, 33);
        flowerView.setImageBitmap(bg_bi);

        flowerSegments = km.KMeans(bi, K);
        askSegmentPart();
    }

    private void askSegmentPart() {
        Bitmap temp = ic.overlay(flowerSegments[currentSegment], output);
        flowerView.setImageBitmap(ic.overlay(bg_bi, temp));

        confTextView.setText("Is the highlighted image is part of the flower? [" + (currentSegment + 1) + "/" + K + "]");
        confTextView.setVisibility(TextView.VISIBLE);
        onClickListenerYes = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output = ic.overlay(output, flowerSegments[currentSegment]);
                continueSegmenting();
            }
        };
        buttonYes.setOnClickListener(onClickListenerYes);
        buttonYes.setVisibility(Button.VISIBLE);

        onClickListenerNo = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueSegmenting();
            }
        };
        buttonNo.setOnClickListener(onClickListenerNo);
        buttonNo.setVisibility(Button.VISIBLE);
    }

    public void continueSegmenting()
    {
        hideDialog();
        if (currentSegment < K-1)
        {
            currentSegment++;
            askSegmentPart();
        } else {
            confirmSegmentation();
        }
    }

    public void hideDialog() {
        buttonYes.setVisibility(Button.GONE);
        buttonNo.setVisibility(Button.GONE);
        confTextView.setVisibility(TextView.GONE);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }



}
