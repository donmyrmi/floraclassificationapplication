package com.sn.floraclassificationapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sn.floraclassificationapplication.flowerdatabase.DBController;
import com.sn.floraclassificationapplication.segmenter.ImageController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button btn1;
    private Button btn2;
    private Flower segmentedFlower;
    private DBController DB_Controller;

    static final int REQUEST_CAMERA_IMAGE = 1;
    static final int REQUEST_GALLERY_IMAGE = 2;
    private File imageFile;
    private ImageController ic = ImageController.getInstance();


    /**
     * void onCreate(Bundle savedInstanceState)
     * On application main screen launch. Get main objects and call Init();
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB_Controller = DBController.getInstance(this);
        ImageView flowerView = (ImageView) findViewById(R.id.flowerView);
        flowerView.setImageResource(R.drawable.daffodil);
        DB_Controller.getFlowers();
        init();

    }

    /**
     * initialize Flower, DB and camera/gallery buttons
     */
    public void init()
    {
        segmentedFlower = TestFlower();
        DB_Controller.getFlowers();
        //segmentedFlower.segmentAndClassify();

        Button temp = (Button) findViewById(R.id.confYesButton);
        temp.setVisibility(View.GONE);
        temp = (Button) findViewById(R.id.confNoButton);
        temp.setVisibility(View.GONE);

        btn1=(Button)findViewById(R.id.useGalleryButton);
        btn1.setVisibility(View.VISIBLE);
        btn2=(Button)findViewById(R.id.useCameraButton);
        btn2.setVisibility(View.VISIBLE);
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

    /**
     * Start activity to get image from gallery
     */
    private void SelectGalleryImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    /**
     * Start activity to get image from camera
     */
    public void TakePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
    }

    /**
     * camera/gallery activity return:
     * handle returned image.
     * @param requestCode - RESULT_OK if image got back
     * @param resultCode - image source - camera/gallery
     * @param data - image from gallery is exist.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            if (requestCode == REQUEST_CAMERA_IMAGE) {
                Bitmap imageBitmap = ic.ConvertFileToBitMap(imageFile,this);

                if (imageBitmap.getHeight() < imageBitmap.getWidth())
                    imageBitmap = ic.rotateImage(90, imageBitmap);

                segmentedFlower.setFlowerImage(imageBitmap);
                segmentedFlower.segmentAndClassify();
            } else {
                if (requestCode == REQUEST_GALLERY_IMAGE) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap imageBitmap = null;
                    imageBitmap = BitmapFactory.decodeFile(picturePath);

                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(picturePath);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (exif != null) {
                        String datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
                        String latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        String longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        if (latitude != null) {
                            segmentedFlower.setLongitude(GPSTracker.convertToDegree(longitude));
                            segmentedFlower.setLatitude(GPSTracker.convertToDegree(latitude));
                        }
                        if (datetime != null) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            try {
                                calendar.setTime(simpleDateFormat.parse(datetime));
                                int month = calendar.get(Calendar.MONTH);
                                segmentedFlower.setMonth(month);
                            } catch (ParseException e) {}
                        }
                    }
                    segmentedFlower.setFlowerImage(imageBitmap);
                    segmentedFlower.segmentAndClassify();
                }
            }
        }
    }

    /**
     * Create an empty flower, with current month and location.
     * In case of no data, this data will be used.
     * @return the flower
     */
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
