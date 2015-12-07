package com.sn.floraclassificationapplication;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView viewImage;
    private Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btn1=(Button)findViewById(R.id.confNoButton);
        btn2=(Button)findViewById(R.id.confYesButton);
        //viewImage=(ImageView)findViewById(R.id.viewImage);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectGalleryImage();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakePhoto();
            }
        });
    }

    public void init()
    {
        Flower segmentedFlower = TestFlower();
        segmentedFlower.segmentAndClassify();
    }

    public Flower TestFlower()
    {
        GPSTracker gps;
        Flower segmentedFlower = new Flower(this);
        segmentedFlower.setFlowerImage(BitmapFactory.decodeResource(getResources(), R.mipmap.f1));

        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            double lat = gps.getLatitude(); // returns latitude
            double lng = gps.getLongitude(); // returns longitude
            segmentedFlower.setLocation(gps.getLocation());
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        segmentedFlower.setMonth(month);

        return segmentedFlower;
    }

    public void SelectGalleryImage(){
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    public void TakePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, 1);
    }

        public void ConvertFileToBitMap(File file){
        Bitmap bitmap;
        Flower flower = new Flower(this);
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                bitmapOptions);
        viewImage.setImageBitmap(bitmap);
        String path = android.os.Environment.getExternalStorageDirectory() + File.separator
                + "Phoenix" + File.separator + "default";
        flower.setFlowerImage(bitmap);
        file.delete();
    }
}
