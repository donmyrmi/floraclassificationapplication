package com.sn.floraclassificationapplication.segmenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.sn.floraclassificationapplication.Flower;
import com.sn.floraclassificationapplication.R;

import java.io.ByteArrayOutputStream;

public class SegmentationController {
    private static final int BACKGROUND_TRANSPARENCY = 33;
    private static int CLEAN_IMAGE_REQUEST = 1;
    private static SegmentationController ourInstance = new SegmentationController();
    private ImageController ic;
    private KMeans km;
    private static int K = 6;
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
    CopyImageSegmentTask copyImageSegmentTask;
    ProgressDialog mProgressDialog;


    public static SegmentationController getInstance() {
        return ourInstance;
    }

    public SegmentationController() {
        ic = ImageController.getInstance();
        km = KMeans.getInstance();
    }

    /**
     * Start the segmentation process in the segmentation controller
     * @param segmentedFlower - The flower to be segmented.
     */
    public void segment(Flower segmentedFlower) {
        init();
        this.segmentedFlower = segmentedFlower;
        Bitmap bi = segmentedFlower.getFlowerImage();
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        output = Bitmap.createBitmap(bi.getWidth(), bi.getHeight(), conf);

        cutOutImage(bi);
    }

    /**
     * initialize the controller.
     */
    public void init()
    {
        currentSegment = 0;
        buttonYes = (Button) activity.findViewById(R.id.confYesButton);
        buttonNo = (Button) activity.findViewById(R.id.confNoButton);
        buttonYes.setVisibility(View.VISIBLE);
        buttonNo.setVisibility(View.VISIBLE);
        confTextView = (TextView) activity.findViewById(R.id.confTextView);

        flowerSegments = new Bitmap[K];
        flowerView = (ImageView) activity.findViewById(R.id.flowerView);
        hideDialog();
    }

    /**
     * Ask confirmation for the combined segments.
     */
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

    /**
     * Popup cleaning option after segmentation, and setup buttons
     */
    private void askCleanImage() {

        flowerView.setImageBitmap(output);
        confTextView.setText("Does this image needs cleaning?");
        confTextView.setVisibility(TextView.VISIBLE);
        onClickListenerYes = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowerSegments = null;
                bg_bi = null;
                CleanActivity cleanActivity = new CleanActivity(segmentedFlower,output, activity);
                cleanActivity.show();
            }
        };
        buttonYes.setOnClickListener(onClickListenerYes);
        buttonYes.setVisibility(Button.VISIBLE);

        onClickListenerNo = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSegmentation();
            }
        };
        buttonNo.setOnClickListener(onClickListenerNo);
        buttonNo.setVisibility(Button.VISIBLE);

    }

    /**
     * Runs after confirmation of the combined segmentation.
     * hide dialog boxes, and run ASync task to set image and continue.
     */
    private void continueToClassifier() {
        hideDialog();
        bg_bi = null;

        SetImagesToClassifier copyAndSet = new SetImagesToClassifier();
        copyAndSet.execute();
    }

    /**
     * create a black and white image for the background, and start ASync task for the segmentation
     * @param bi the image to cut from.
     */
    private void cutOutImage(Bitmap bi) {

        bg_bi = ic.toGrayscale(bi);
        bg_bi = ic.makeTransparent(bg_bi, BACKGROUND_TRANSPARENCY);
        flowerView.setImageBitmap(bg_bi);

        CutOutTask copyAndSet = new CutOutTask(bi);
        copyAndSet.execute();

    }

    /**
     * Show segmented part on the transparent background, ask if to include this part
     */
    private void askSegmentPart() {
        Bitmap temp = ic.overlay(flowerSegments[currentSegment], bg_bi);
        flowerView.setImageBitmap(ic.overlay(bg_bi, temp));

        confTextView.setText("Is the highlighted image is part of the flower? [" + (currentSegment + 1) + "/" + K + "]");
        confTextView.setVisibility(TextView.VISIBLE);
        onClickListenerYes = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
                copyImageSegmentTask = new CopyImageSegmentTask();
                copyImageSegmentTask.execute();
            }
        };
        buttonYes.setOnClickListener(onClickListenerYes);
        buttonYes.setVisibility(Button.VISIBLE);

        onClickListenerNo = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
                if (currentSegment < K-1)
                {
                    currentSegment++;
                    askSegmentPart();
                } else {
                    askCleanImage();
                }
            }
        };
        buttonNo.setOnClickListener(onClickListenerNo);
        buttonNo.setVisibility(Button.VISIBLE);
    }

    /**
     * Hide question buttons and text.
     */
    public void hideDialog() {
        buttonYes.setVisibility(Button.GONE);
        buttonNo.setVisibility(Button.GONE);
        confTextView.setVisibility(TextView.GONE);
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private class CopyImageSegmentTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            // before
        }

        protected Void doInBackground(Void... strings) {
            output = ic.overlay(output, flowerSegments[currentSegment]);
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            // Executes whenever publishProgress is called from doInBackground
        }

        protected void onPostExecute(Void result) {
            // This method is executed in the UIThread
            // Continue to the next segmented part, or finish
            if (currentSegment < K-1)
            {
                currentSegment++;
                askSegmentPart();
            } else {
                askCleanImage();
            }
        }
    }

    /**
     *
     */
    private class SetImagesToClassifier extends AsyncTask<String, Void, Bitmap> {
        protected void onPreExecute() {
            Toast.makeText(activity, "Computing... please hold.", Toast.LENGTH_LONG).show();
        }

        protected Bitmap doInBackground(String... strings) {
            // save image + BW image.
            segmentedFlower.setFlowerImage(output);
            segmentedFlower.setGrayImage(ic.toGrayscale(segmentedFlower.getFlowerImage()));
            return output;
        }


        protected void onPostExecute(Bitmap result) {
            // This method is executed in the UIThread
            // continue to classify segmented flower
            segmentedFlower.classify();
        }
    }

    /**
     * run K-Means ASync. continue to ask about parts.
     */
    private class CutOutTask extends AsyncTask<Void, Void, Void> {
        private Bitmap bi;

        protected void onPreExecute() {
            Toast.makeText(activity, "Segmenting... please hold.", Toast.LENGTH_LONG).show();
        }

        public CutOutTask(Bitmap bi) {
            this.bi = bi;
        }
        protected Void doInBackground(Void... voids) {
            // Some long-running task like downloading an image.
            flowerSegments = km.KMeans(bi, K);
            return null;
        }

        protected void onPostExecute(Void result) {
            askSegmentPart();
        }
    }
}


