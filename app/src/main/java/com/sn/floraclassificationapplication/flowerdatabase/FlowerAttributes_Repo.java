package com.sn.floraclassificationapplication.flowerdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sapir on 05-Dec-15.
 */

public class FlowerAttributes_Repo extends AbstractFlower_Repo{

    private DBController dbHelper;
    private String hu8MomentsMaxString;
    private String hu8MomentsMinString;

    public FlowerAttributes_Repo(Context context) {
        dbHelper = new DBController(context);
    }

    public int insert(FlowerAttributes flowerAttributes) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //convert HU sets of moments into string
        convertArrayToString(flowerAttributes);

        values.put(flowerAttributes.KEY_ID, flowerAttributes.flower_ID);
        values.put(flowerAttributes.KEY_flowerImage,flowerAttributes.flowerImageIndex);
        values.put(flowerAttributes.KEY_hu8MomentsMax,hu8MomentsMaxString);
        values.put(flowerAttributes.KEY_hu8MomentsMin,hu8MomentsMinString);

        values.put(flowerAttributes.KEY_red, flowerAttributes.red);
        values.put(flowerAttributes.KEY_redMin,flowerAttributes.redMin);
        values.put(flowerAttributes.KEY_redMax,flowerAttributes.redMax);
        values.put(flowerAttributes.KEY_redRange,flowerAttributes.redRange);

        values.put(flowerAttributes.KEY_green,flowerAttributes.green);
        values.put(flowerAttributes.KEY_greenMin,flowerAttributes.greenMin);
        values.put(flowerAttributes.KEY_greenMax,flowerAttributes.greenMax);
        values.put(flowerAttributes.KEY_greenRange,flowerAttributes.greenRange);

        values.put(flowerAttributes.KEY_blue,flowerAttributes.blue);
        values.put(flowerAttributes.KEY_blueMin,flowerAttributes.blueMin);
        values.put(flowerAttributes.KEY_blueMax,flowerAttributes.blueMax);
        values.put(flowerAttributes.KEY_blueRange,flowerAttributes.blueRange);

        // Inserting Row
        long FlowerAttributes_Id = db.insert(FlowerAttributes.TABLE, null, values);

        db.close(); // Closing database connection
        return (int) FlowerAttributes_Id;
    }

    public void delete(int flower_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(FlowerAttributes.TABLE, FlowerAttributes.KEY_ID + "= ?", new String[]{String.valueOf(flower_Id) });
        db.close(); // Closing database connection
    }

    public void update(FlowerAttributes flowerAttributes) {
//
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(FlowerInDB.KEY_flowerImage,FlowerInDB.KEY_hu8MomentsMax);//TODO::SAPIR:bitmap,huMoment - reference or full array?
//        values.put(FlowerInDB.KEY_hu8MomentsMin,FlowerInDB.KEY_colorMax);
//        values.put(FlowerInDB.KEY_colorMin,"");
//
//        //update Row
//        db.update(FlowerAttributes.TABLE, values, FlowerAttributes.KEY_ID + "= ?", new String[]{String.valueOf(FlowerAttributes.KEY_ID)});
//        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, Object>> getFlowerAttributesList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerAttributes.KEY_ID + "," +
                FlowerAttributes.KEY_flowerImage + "," +
                FlowerAttributes.KEY_hu8MomentsMax + "," +
                FlowerAttributes.KEY_hu8MomentsMin + "," +
                FlowerAttributes.KEY_red + "," +
                FlowerAttributes.KEY_redMax + "," +
                FlowerAttributes.KEY_redMin + "," +
                FlowerAttributes.KEY_redRange + "," +
                FlowerAttributes.KEY_green + "," +
                FlowerAttributes.KEY_greenMax + "," +
                FlowerAttributes.KEY_greenMin + "," +
                FlowerAttributes.KEY_greenRange + "," +
                FlowerAttributes.KEY_blue + "," +
                FlowerAttributes.KEY_blueMax + "," +
                FlowerAttributes.KEY_blueMin + "," +
                FlowerAttributes.KEY_blueRange +
                " FROM " + FlowerAttributes.TABLE;

        //FlowerInDB FlowerInDB = new FlowerInDB();
        ArrayList<HashMap<String, Object>> FlowerAttributesList = new ArrayList<HashMap<String, Object>>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> flowerAttributes = new HashMap<String, Object>();
                flowerAttributes.put("id", cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ID)));
                flowerAttributes.put("flowerImage", cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_flowerImage)));
                flowerAttributes.put("hu8MomentsMax", cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMax)));//TODO::SAPIR: CHECK, returns byte[]
                flowerAttributes.put("hu8MomentsMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMin)));//TODO::SAPIR: CHECK, returns byte[]
                flowerAttributes.put("red",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_red)));
                flowerAttributes.put("redMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_redMin)));
                flowerAttributes.put("redMax",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_redMax)));
                flowerAttributes.put("redRange",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_redRange)));
                flowerAttributes.put("green",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_green)));
                flowerAttributes.put("greenMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_greenMin)));
                flowerAttributes.put("greenMax",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_greenMax)));
                flowerAttributes.put("greenRange",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_greenRange)));
                flowerAttributes.put("blue",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_blue)));
                flowerAttributes.put("blueMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_blueMin)));
                flowerAttributes.put("blueMax",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_blueMax)));
                flowerAttributes.put("blueRange",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_blueRange)));
                FlowerAttributesList.add(flowerAttributes);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return FlowerAttributesList;
    }

    public FlowerAttributes getFlowerAttributesById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerAttributes.KEY_ID + "," +
                FlowerAttributes.KEY_flowerImage + "," +
                FlowerAttributes.KEY_hu8MomentsMax + "," +
                FlowerAttributes.KEY_hu8MomentsMin + "," +
                FlowerAttributes.KEY_red + "," +
                FlowerAttributes.KEY_redMax + "," +
                FlowerAttributes.KEY_redMin + "," +
                FlowerAttributes.KEY_redRange + "," +
                FlowerAttributes.KEY_green + "," +
                FlowerAttributes.KEY_greenMax + "," +
                FlowerAttributes.KEY_greenMin + "," +
                FlowerAttributes.KEY_greenRange + "," +
                FlowerAttributes.KEY_blue + "," +
                FlowerAttributes.KEY_blueMax + "," +
                FlowerAttributes.KEY_blueMin + "," +
                FlowerAttributes.KEY_blueRange +
                " FROM " + FlowerAttributes.TABLE
                + " WHERE " +
                FlowerAttributes.KEY_ID + "=?";

        int iCount =0;
        FlowerAttributes flowerAttributes = new FlowerAttributes();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                flowerAttributes.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ID));
                flowerAttributes.flowerImageIndex = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_flowerImage));
                convertStringToArray(flowerAttributes,cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMax)),HU_SET_MAX);
                convertStringToArray(flowerAttributes,cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMin)),HU_SET_MIN);
                flowerAttributes.red = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_red));
                flowerAttributes.redMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_redMin));
                flowerAttributes.redMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_redMax));
                flowerAttributes.redRange = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_redRange));
                flowerAttributes.green = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_green));
                flowerAttributes.greenMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_greenMin));
                flowerAttributes.greenMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_greenMax));
                flowerAttributes.greenRange = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_greenRange));
                flowerAttributes.blue = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_blue));
                flowerAttributes.blueMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_blueMin));
                flowerAttributes.blueMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_blueMax));
                flowerAttributes.blueRange = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_blueRange));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return flowerAttributes;
    }
    //Converting double[] to string method
    public void convertArrayToString(FlowerAttributes flowerAttributes){

        int i;
        for ( i = 0;i < NUM_OF_MOMENTS;i++){
            hu8MomentsMaxString += String.valueOf(flowerAttributes.hu8MomentsMax[i]);
            hu8MomentsMinString += String.valueOf(flowerAttributes.hu8MomentsMin[i]);
            if(i < 7){
                hu8MomentsMaxString += "::";
                hu8MomentsMinString += "::";
            }
        }
    }
    //Converting string to double[] method
    public void convertStringToArray(FlowerAttributes flowerAttributes,String str,int type){
        String[] strToDouble;
        int i;
        strToDouble = str.split("::");
        for ( i = 0;i < 8;i++) {
            switch (type){
                case HU_SET_MAX:
                {
                    flowerAttributes.hu8MomentsMax[i] = Double.parseDouble(strToDouble[i]);
                    break;
                }
                case HU_SET_MIN:
                {
                    flowerAttributes.hu8MomentsMin[i] = Double.parseDouble(strToDouble[i]);
                    break;
                }
            }

        }

    }
}
