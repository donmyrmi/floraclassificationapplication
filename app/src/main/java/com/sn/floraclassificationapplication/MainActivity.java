package com.sn.floraclassificationapplication;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.sn.floraclassificationapplication.segmenter.SegmentationController;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    SegmentationController sgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    public void init()
    {
        sgm = SegmentationController.getInstance(this);

        Flower segmentedFlower = TestFlower();
        sgm.segment(segmentedFlower);
    }

    public Flower TestFlower()
    {
        GPSTracker gps;
        Flower segmentedFlower = new Flower();
        segmentedFlower.setFlowerImage(BitmapFactory.decodeResource(getResources(), R.mipmap.f1));

        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            double lat = gps.getLatitude(); // returns latitude
            double lng = gps.getLongitude(); // returns longitude
            Toast.makeText(getApplicationContext(), "lat" + lat + " long" + lng, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        segmentedFlower.addLocation(gps.getLocation());

        Calendar cal=Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        segmentedFlower.setMonth(month);

        return segmentedFlower;
    }
}
