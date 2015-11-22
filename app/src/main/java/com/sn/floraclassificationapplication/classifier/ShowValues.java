package com.sn.floraclassificationapplication.classifier;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class ShowValues {
    private final Flower flower;
    private List<String> List_file;
    private ListView listView;
    private static int NUMBER_OF_VARIABLES = 8+3;
    private ResultList adapter;

    private AppCompatActivity mainActivity;

    public ShowValues(AppCompatActivity mainActivity, Flower currentFlower) {

        this.mainActivity = mainActivity;
        this.flower = currentFlower;
        List_file =new ArrayList<String>();
    }

    private void CreateListView()
    {
        String[] strUpValues = new String[NUMBER_OF_VARIABLES];
        String[] strDownValues = new String[NUMBER_OF_VARIABLES];
        int[] imgValues = new int[NUMBER_OF_VARIABLES];
        double[] moments = flower.getHu8Moments();
        for (int i=0; i<8; i++)
        {
            strUpValues[i] = "HU"+(i+1);
            strDownValues[i] = Double.toString(moments[i]);
        }
        strUpValues[8] = "R-avg";
        strUpValues[9] = "G-avg";
        strUpValues[10] = "B-avg";
        strDownValues[8] = Integer.toString(Color.red(flower.getColor()));
        strDownValues[9] = Integer.toString(Color.green(flower.getColor()));
        strDownValues[10] = Integer.toString(Color.blue(flower.getColor()));

        for (int i=0; i< NUMBER_OF_VARIABLES; i++) {
            imgValues[i] = R.mipmap.f1;
        }



        adapter = new ResultList(mainActivity, strUpValues, imgValues, strDownValues);

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

    public void updateMoments() {
        adapter.notifyDataSetChanged();
    }
}
