package com.sn.floraclassificationapplication.classifier;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sn.floraclassificationapplication.Flower;
import com.sn.floraclassificationapplication.MainActivity;
import com.sn.floraclassificationapplication.R;
import com.sn.floraclassificationapplication.flowerdatabase.DBController;
import com.sn.floraclassificationapplication.flowerdatabase.FlowerInDB;
import com.sn.floraclassificationapplication.segmenter.ImageController;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Handles the result screen.
 */
public class ShowValues {
    private static final boolean DEVELOPER_MODE = false; // mode to add classified flower details to the end list
    private final Flower flower;
    private ListView listView;
    private int numberOfFlowers;
    private ResultList adapter;

    private AppCompatActivity mainActivity;

    /**
     * Initialize the result screen
     * @param mainActivity
     * @param currentFlower
     */
    public ShowValues(AppCompatActivity mainActivity, Flower currentFlower) {

        this.mainActivity = mainActivity;
        this.flower = currentFlower;
    }

    /**
     * Create and fill the result list
     */
    private void CreateListView()
    {
        DBController dbc = DBController.getInstance(mainActivity);
        //dbc.loadTestFlowerDB(flower);

        dbc.calculateFlowerRanks(flower);
        dbc.sortFlowerByRanks();

        List<FlowerInDB> dbFLowers = dbc.getFlowerInDB();
        int dbFlowerSize = dbFLowers.size();
        numberOfFlowers = dbFlowerSize;

        if (DEVELOPER_MODE) {
            numberOfFlowers += 8 + 3; //
        }

        String[] strUpValues = new String[numberOfFlowers];
        String[] strDownValues = new String[numberOfFlowers];
        String[] strRankValues = new String[numberOfFlowers];
        int[] imgValues = new int[numberOfFlowers];
        double[] moments = flower.getHu8Moments();
        boolean[] isRGBs = new boolean[numberOfFlowers];
        int i = 0;

        // add details for each flower in database.
        for (FlowerInDB f : dbFLowers) {
            strUpValues[i] = f.getName();
            String fImage = "mipmap/fl" + f.getFlower_ID() + "c";
            imgValues[i] = mainActivity.getResources().getIdentifier(fImage, null, mainActivity.getPackageName());
            strDownValues[i] = "";
            isRGBs[i] = false;
            strRankValues[i] = String.valueOf((int)f.getRank());
            i++;
        }

        // add classified flower data is in developr mode
        if (DEVELOPER_MODE) {
            fillDeveloperAttributes(dbFlowerSize,strUpValues,strDownValues,strRankValues,moments,isRGBs,imgValues);
        }

        // initialize result list
        adapter = new ResultList(mainActivity, strUpValues, imgValues, strDownValues, strRankValues, isRGBs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /*
                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(mainActivity.getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();*/

            }
        });
    }

    /**
     * FOR DEVELOPER MODE ONLY.
     * Add developer details - segmented flower attributes.
     * @param dbFlowerSize - size of the list
     * @param strUpValues - Upper text
     * @param strDownValues - Lower text
     * @param strRankValues - Ranks
     * @param moments - flower moments to fill
     * @param isRGBs - boolean of is the image a RGB. if yes color will be shown instead of image
     * @param imgValues - values of image ids. if isRGB, value of color.
     */
    private void fillDeveloperAttributes(int dbFlowerSize, String[] strUpValues, String[] strDownValues, String[] strRankValues, double [] moments, boolean[] isRGBs, int [] imgValues ) {
        for (int j = 0; j<8; j++)
        {
            strUpValues[j + dbFlowerSize] = "HU"+(j+1);
            strDownValues[j + dbFlowerSize] = String.valueOf(moments[j]);
            isRGBs[j + dbFlowerSize] = false;
            imgValues[j + dbFlowerSize] = R.mipmap.f1;
            strRankValues[j+dbFlowerSize] = "0";
        }

        strUpValues[8 + dbFlowerSize] = "GPS location";
        strDownValues[8 + dbFlowerSize] = flower.getLocation();
        isRGBs[8 + dbFlowerSize] = false;
        imgValues[8 + dbFlowerSize] = R.mipmap.f1;
        strRankValues[8+dbFlowerSize] = "0";

        strUpValues[9 + dbFlowerSize] = "Month";
        strDownValues[9 + dbFlowerSize] = getMonth(flower.getMonth());
        isRGBs[9 + dbFlowerSize] = false;
        imgValues[9 + dbFlowerSize] = R.mipmap.f1;
        strRankValues[9 +dbFlowerSize] = "0";

        strUpValues[10 + dbFlowerSize] = "Color average";
        isRGBs[10 + dbFlowerSize] = true;
        strDownValues[10 + dbFlowerSize] = "("+Color.red(flower.getColor())+","+Color.green(flower.getColor())+","+Color.blue(flower.getColor())+")";
        strRankValues[10 + dbFlowerSize] = "0";
        imgValues[10 + dbFlowerSize] = flower.getColor();
        // end of flower details
    }

    /**
     * Show the result after initializing
     */
    public void show() {

        mainActivity.setContentView(R.layout.flower_values);
        listView = (ListView)mainActivity.findViewById(R.id.listView);
        ImageView segFlowerImg = (ImageView)mainActivity.findViewById(R.id.segFlower);
        segFlowerImg.setImageBitmap(flower.getFlowerImage());
        CreateListView();
    }

    // get month name for DEV MODE
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public void updateMoments() {
        adapter.notifyDataSetChanged();
    }
}
