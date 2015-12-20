package com.sn.floraclassificationapplication.segmenter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.sn.floraclassificationapplication.Flower;
import com.sn.floraclassificationapplication.R;


/**
 * Created by Nadav on 02-Dec-15.
 */
public class CleanActivity extends AppCompatActivity  {

    private final AppCompatActivity activity;
    private DrawingView dv ;
    private Bitmap image;
    private Button cleanCont, cleanReset;
    private TextView cleanText;
    private Flower flower;
    ImageController ic = ImageController.getInstance();

    public CleanActivity(Flower flower, Bitmap output, AppCompatActivity activity) {
        this.activity = activity;
        this.flower = flower;
        image = output;
    }

    public void show() {
        activity.setContentView(R.layout.clean_flower_layout);

        cleanText = (TextView) activity.findViewById(R.id.cleanTextView);

        dv = (DrawingView) activity.findViewById(R.id.cleanFlowerView);
        dv.setImage(image);

        cleanCont = (Button) activity.findViewById(R.id.cleanContButton);
        cleanCont.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cleanCont.setVisibility(View.GONE);
                cleanReset.setVisibility(View.GONE);
                cleanText.setText("Computing... please hold. this could take a while.");
                dv.setVisibility(View.GONE);
                flower.setFlowerImage(dv.saveImage());
                SetImagesToClassifier copyAndSet = new SetImagesToClassifier();
                copyAndSet.execute();
            }
        });

        cleanReset = (Button) activity.findViewById(R.id.cleanResetButton);
        cleanReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dv.clear();
            }
        });
    }

    private class SetImagesToClassifier extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... strings) {
            // Some long-running task like downloading an image.
            flower.setGrayImage(ic.toGrayscale(flower.getFlowerImage()));
            return null;
        }


        protected void onPostExecute(Void result) {
            flower.classify();
        }
    }
}