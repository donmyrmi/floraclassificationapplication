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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB_Controller = DBController.getInstance(this);
        ImageView flowerView = (ImageView) findViewById(R.id.flowerView);
        flowerView.setImageResource(R.drawable.daffodil);
        DB_Controller.getFlowers();

        init();
        //getRoundedImageAndSave();

    }

    public void init()
    {
        segmentedFlower = TestFlower();
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

    private void SelectGalleryImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        if (resultCode == RESULT_OK ) {
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            if (requestCode == REQUEST_CAMERA_IMAGE) {
                Bitmap imageBitmap = ConvertFileToBitMap(imageFile);

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
                            segmentedFlower.setLongitude(convertToDegree(longitude));
                            segmentedFlower.setLatitude(convertToDegree(latitude));
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

    private double convertToDegree(String stringDMS){
        double result;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0/D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0/M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0/S1;

        result = new Double(FloatD + (FloatM/60) + (FloatS/3600));

        return result;
    };

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

    public void getRoundedImageAndSave() {
        int [] flowers = {1,2,4,5,6};
        for (int i : flowers) {
            String fImage = "mipmap/fl" + i;
            String outFilename = "fl" + i + "c.png";
            File file = new File(getFilesDir(), outFilename);
            int rid = getResources().getIdentifier(fImage, null, getPackageName());
            Bitmap tempBi = ic.decodeSampledBitmapFromResource(getResources(), rid, 256, 256);
            tempBi = ic.getRoundedShape(tempBi);
            ic.saveImageToDevice(tempBi, file);
        }
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
