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
 * Created by Nadav on 19-Nov-15.
 */
public class ShowValues {
    private final Flower flower;
    private ImageController ic;
    private List<String> List_file;
    private ListView listView;
    private int numberOfFlowers;
    private ResultList adapter;

    private AppCompatActivity mainActivity;

    public ShowValues(AppCompatActivity mainActivity, Flower currentFlower) {

        this.mainActivity = mainActivity;
        this.flower = currentFlower;
        List_file =new ArrayList<String>();
        ic = ImageController.getInstance();
    }

    private void CreateListView()
    {


        DBController dbc = DBController.getInstance(mainActivity);
        //dbc.loadTestFlowerDB(flower);

        dbc.calculateFlowerRanks(flower);
        dbc.sortFlowerByRanks();

        List<FlowerInDB> dbFLowers = dbc.getFlowerInDB();
        int dbFlowerSize = dbFLowers.size();
        numberOfFlowers = dbFlowerSize + 8 + 3;

        String[] strUpValues = new String[numberOfFlowers];
        String[] strDownValues = new String[numberOfFlowers];
        String[] strRankValues = new String[numberOfFlowers];
        int[] imgValues = new int[numberOfFlowers];
        double[] moments = flower.getHu8Moments();
        boolean[] isRGBs = new boolean[numberOfFlowers];
        int i = 0;
        Random rand = new Random();
        for (FlowerInDB f : dbFLowers) {
            strUpValues[i] = f.getName();
            String fImage = "mipmap/fl" + f.getFlower_ID() + "c";
            imgValues[i] = mainActivity.getResources().getIdentifier(fImage, null, mainActivity.getPackageName());
            //imgValues[i] = R.mipmap.f1;
            strDownValues[i] = "match:";
            isRGBs[i] = false;
            strRankValues[i] = String.valueOf((int)f.getRank());
            i++;
        }
        // flower details for testing only!
        for (int j = 0; j<8; j++)
        {
            strUpValues[j + dbFlowerSize] = "HU"+(j+1);
            //strDownValues[j + dbFlowerSize] = String.format("%.24f", moments[j]);
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

    public void show() {

        mainActivity.setContentView(R.layout.flower_values);
        listView = (ListView)mainActivity.findViewById(R.id.listView);
        ImageView segFlowerImg = (ImageView)mainActivity.findViewById(R.id.segFlower);
        segFlowerImg.setImageBitmap(flower.getFlowerImage());
        CreateListView();
    }

    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month];
    }

    public void updateMoments() {
        adapter.notifyDataSetChanged();
    }
}
