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

public class FlowerAttributes_Repo extends AbstractFlower_Repo implements RepositoryController{

    private DBController dbHelper;
    private String hu8MomentsMaxString;
    private String hu8MomentsMinString;
    private String hu8MomentsWeightsString;

    public FlowerAttributes_Repo(Context context) {
        dbHelper = DBController.getInstance(context);
    }

    public int insert(AbstractDBFlower DBFlower){
        FlowerAttributes flowerAttributes = (FlowerAttributes)DBFlower;
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //convert HU sets of moments into string
        convertArrayToString(flowerAttributes);

        values.put(flowerAttributes.KEY_ID, flowerAttributes.flower_ID);
        values.put(flowerAttributes.KEY_angle, flowerAttributes.angle);
        values.put(flowerAttributes.KEY_hu8MomentsMax,hu8MomentsMaxString);
        values.put(flowerAttributes.KEY_hu8MomentsMin,hu8MomentsMinString);

        values.put(flowerAttributes.KEY_redMin,flowerAttributes.redMin);
        values.put(flowerAttributes.KEY_redMax,flowerAttributes.redMax);

        values.put(flowerAttributes.KEY_greenMin,flowerAttributes.greenMin);
        values.put(flowerAttributes.KEY_greenMax,flowerAttributes.greenMax);

        values.put(flowerAttributes.KEY_blueMin,flowerAttributes.blueMin);
        values.put(flowerAttributes.KEY_blueMax,flowerAttributes.blueMax);

        values.put(flowerAttributes.KEY_momentsWeight,hu8MomentsWeightsString);
        values.put(flowerAttributes.KEY_momentsWeight,flowerAttributes.colorWeight);

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

    public void update(AbstractDBFlower DBFlower) {
//        FlowerAttributes flowerAttributes = (FlowerAttributes) DBFlower;
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

    public ArrayList<HashMap<String, Object>> getAttributesList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerAttributes.KEY_ID + "," +
                FlowerAttributes.KEY_angle + "," +
                FlowerAttributes.KEY_hu8MomentsMax + "," +
                FlowerAttributes.KEY_hu8MomentsMin + "," +
                FlowerAttributes.KEY_redMax + "," +
                FlowerAttributes.KEY_redMin + "," +
                FlowerAttributes.KEY_greenMax + "," +
                FlowerAttributes.KEY_greenMin + "," +
                FlowerAttributes.KEY_blueMax + "," +
                FlowerAttributes.KEY_blueMin + "," +
                FlowerAttributes.KEY_momentsWeight + "," +
                FlowerAttributes.KEY_colorWeight +
                " FROM " + FlowerAttributes.TABLE;

        //FlowerInDB FlowerInDB = new FlowerInDB();
        ArrayList<HashMap<String, Object>> FlowerAttributesList = new ArrayList<HashMap<String, Object>>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> flowerAttributes = new HashMap<String, Object>();
                flowerAttributes.put("id", cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ID)));
                flowerAttributes.put("angle", cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_angle)));
                flowerAttributes.put("hu8MomentsMax", cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMax)));//TODO::SAPIR: CHECK, returns byte[]
                flowerAttributes.put("hu8MomentsMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMin)));//TODO::SAPIR: CHECK, returns byte[]
                flowerAttributes.put("redMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_redMin)));
                flowerAttributes.put("redMax",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_redMax)));
                flowerAttributes.put("greenMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_greenMin)));
                flowerAttributes.put("greenMax",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_greenMax)));
                flowerAttributes.put("blueMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_blueMin)));
                flowerAttributes.put("blueMax",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_blueMax)));
                flowerAttributes.put("momentsWeight",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_momentsWeight)));
                flowerAttributes.put("colorWeight",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_colorWeight)));
                FlowerAttributesList.add(flowerAttributes);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return FlowerAttributesList;
    }

    public FlowerAttributes getAttributesById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerAttributes.KEY_ID + "," +
                FlowerAttributes.KEY_angle + "," +
                FlowerAttributes.KEY_hu8MomentsMax + "," +
                FlowerAttributes.KEY_hu8MomentsMin + "," +
                FlowerAttributes.KEY_redMax + "," +
                FlowerAttributes.KEY_redMin + "," +
                FlowerAttributes.KEY_greenMax + "," +
                FlowerAttributes.KEY_greenMin + "," +
                FlowerAttributes.KEY_blueMax + "," +
                FlowerAttributes.KEY_blueMin + "," +
                FlowerAttributes.KEY_momentsWeight + "," +
                FlowerAttributes.KEY_colorWeight +
                " FROM " + FlowerAttributes.TABLE
                + " WHERE " +
                FlowerAttributes.KEY_ID + "=?";

        FlowerAttributes flowerAttributes = new FlowerAttributes();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                flowerAttributes.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ID));
                flowerAttributes.angle = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_angle));
                convertStringToArray(flowerAttributes, cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMax)), HU_SET_MAX);
                convertStringToArray(flowerAttributes, cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMin)), HU_SET_MIN);
                flowerAttributes.redMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_redMin));
                flowerAttributes.redMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_redMax));
                flowerAttributes.greenMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_greenMin));
                flowerAttributes.greenMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_greenMax));
                flowerAttributes.blueMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_blueMin));
                flowerAttributes.blueMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_blueMax));
                convertStringToArray(flowerAttributes,cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_momentsWeight)),HU_WEIGHT);
                flowerAttributes.colorWeight = cursor.getFloat(cursor.getColumnIndex(FlowerAttributes.KEY_colorWeight));
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
            hu8MomentsWeightsString+= String.valueOf(flowerAttributes.momentsWeight);

                hu8MomentsMaxString += "::";
                hu8MomentsMinString += "::";
                hu8MomentsWeightsString += "::";
        }
    }
    //Converting string to double[] method
    public void convertStringToArray(FlowerAttributes flowerAttributes,String str,int type){
        String[] strToPars;
        int i;
        strToPars = str.split("::");
        for ( i = 0;i < 8;i++) {
            switch (type){
                case HU_SET_MAX:
                {
                    flowerAttributes.hu8MomentsMax[i] = Double.parseDouble(strToPars[i]);
                    break;
                }
                case HU_SET_MIN:
                {
                    flowerAttributes.hu8MomentsMin[i] = Double.parseDouble(strToPars[i]);
                    break;
                }
                case HU_WEIGHT:
                {
                    flowerAttributes.momentsWeight[i] = Float.parseFloat(strToPars[i]);
                    break;
                }
                default:
                    break;
            }

        }

    }

    public void setParams(AbstractDBFlower DBFlower,Cursor cursor)
    {
        FlowerAttributes flowerAttributes = (FlowerAttributes) DBFlower;

        flowerAttributes.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ID));
        convertStringToArray(flowerAttributes, cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMax)), HU_SET_MAX);
        convertStringToArray(flowerAttributes, cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMin)), HU_SET_MIN);
        flowerAttributes.redMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_redMin));
        flowerAttributes.redMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_redMax));
        flowerAttributes.greenMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_greenMin));
        flowerAttributes.greenMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_greenMax));
        flowerAttributes.blueMin = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_blueMin));
        flowerAttributes.blueMax = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_blueMax));
    }
}
