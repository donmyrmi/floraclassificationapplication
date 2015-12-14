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
import com.sn.floraclassificationapplication.segmenter.ImageController;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class ShowValues {
    private final Flower flower;
    private ImageController ic;
    private List<String> List_file;
    private ListView listView;
    private static int NUMBER_OF_VARIABLES = 8+1+1+1;
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
        String[] strUpValues = new String[NUMBER_OF_VARIABLES];
        String[] strDownValues = new String[NUMBER_OF_VARIABLES];
        int[] imgValues = new int[NUMBER_OF_VARIABLES];
        double[] moments = flower.getHu8Moments();
        boolean[] isRGBs = new boolean[NUMBER_OF_VARIABLES];

        for (int i=0; i<8; i++)
        {
            strUpValues[i] = "HU"+(i+1);
            strDownValues[i] = Double.toString(moments[i]);
        }

        strUpValues[8] = "GPS location";
        strUpValues[9] = "Month";

        strDownValues[8] = flower.getLocation();
        strDownValues[9] = getMonth(flower.getMonth());

        for (int i=0; i< NUMBER_OF_VARIABLES-1; i++) {
            imgValues[i] = R.mipmap.f1;
            isRGBs[i] = false;
        }

        strUpValues[10] = "Color average";
        isRGBs[10] = true;
        strDownValues[10] = "("+Color.red(flower.getColor())+","+Color.green(flower.getColor())+","+Color.blue(flower.getColor())+")";
        imgValues[10] = flower.getColor();

        adapter = new ResultList(mainActivity, strUpValues, imgValues, strDownValues, isRGBs);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(mainActivity.getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

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
