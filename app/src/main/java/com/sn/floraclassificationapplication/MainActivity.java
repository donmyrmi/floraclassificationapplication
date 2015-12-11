package com.sn.floraclassificationapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button btn1;
    private Button btn2;
    private Flower segmentedFlower;

    static final int REQUEST_CAMERA_IMAGE = 1;
    static final int REQUEST_GALLERY_IMAGE = 1;
    private File imageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    public void init()
    {
        segmentedFlower = TestFlower();
        //segmentedFlower.segmentAndClassify();

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

    private void SelectGalleryImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    public void TakePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_CAMERA_IMAGE || requestCode == REQUEST_GALLERY_IMAGE) && resultCode == RESULT_OK ) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap imageBitmap = ConvertFileToBitMap(imageFile);
            segmentedFlower.setFlowerImage(imageBitmap);
            segmentedFlower.segmentAndClassify();
        }
    }

    public Bitmap ConvertFileToBitMap(File file){
        Bitmap bitmap;
        Flower flower = new Flower(this);
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                bitmapOptions);
        ImageView viewImage = (ImageView) findViewById(R.id.flowerView);
        viewImage.setImageBitmap(bitmap);
        String path = android.os.Environment.getExternalStorageDirectory() + File.separator
                + "Phoenix" + File.separator + "default";
        flower.setFlowerImage(bitmap);
        file.delete();
        return bitmap;
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
}
