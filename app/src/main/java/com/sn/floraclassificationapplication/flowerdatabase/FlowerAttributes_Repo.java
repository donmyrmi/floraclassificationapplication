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

public class FlowerAttributes_Repo {

    private DBController dbHelper;
    private String hu8MomentsMaxString;
    private String hu8MomentsMinString;

    private final int HU_SET_MAX = 1;
    private final int HU_SET_MIN = 0;


    public FlowerAttributes_Repo(Context context) {
        dbHelper = new DBController(context);
    }

    public int insert(FlowerAttributes flowerAttributes) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        convertArrayToString(flowerAttributes);
        
        values.put(flowerAttributes.KEY_ID,flowerAttributes.KEY_ANGLE);
        values.put(flowerAttributes.KEY_flowerImage,flowerAttributes.KEY_hu8MomentsMax);//TODO::SAPIR:bitmap,huMoment - reference or full array?
        values.put(flowerAttributes.KEY_hu8MomentsMin,flowerAttributes.KEY_colorMax);

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

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FlowerAttributes.KEY_ID, FlowerAttributes.KEY_ANGLE);
        values.put(FlowerInDB.KEY_flowerImage,FlowerInDB.KEY_hu8MomentsMax);//TODO::SAPIR:bitmap,huMoment - reference or full array?
        values.put(FlowerInDB.KEY_hu8MomentsMin,FlowerInDB.KEY_colorMax);
        values.put(FlowerInDB.KEY_colorMin,"");

        //update Row
        db.update(FlowerAttributes.TABLE, values, FlowerAttributes.KEY_ID + "= ?", new String[]{String.valueOf(FlowerAttributes.KEY_ID)});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, Object>> getFlowerAttributesList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerAttributes.KEY_ID + "," +
                FlowerAttributes.KEY_ANGLE + "," +
                FlowerAttributes.KEY_flowerImage + "," +
                FlowerAttributes.KEY_hu8MomentsMax + "," +
                FlowerAttributes.KEY_hu8MomentsMin + "," +
                FlowerAttributes.KEY_colorMax + "," +
                FlowerAttributes.KEY_colorMin +
                " FROM " + FlowerAttributes.TABLE;

        //FlowerInDB FlowerInDB = new FlowerInDB();
        ArrayList<HashMap<String, Object>> FlowerAttributesList = new ArrayList<HashMap<String, Object>>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> flowerAttributes = new HashMap<String, Object>();
                flowerAttributes.put("id", cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ID)));
                flowerAttributes.put("angle", cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ANGLE)));
                flowerAttributes.put("flowerImage", cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_flowerImage)));
                flowerAttributes.put("hu8MomentsMax",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMax)));//TODO::SAPIR: CHECK
                flowerAttributes.put("hu8MomentsMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMin)));
                flowerAttributes.put("colorMax",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_colorMax)));
                flowerAttributes.put("colorMin",cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_colorMin)));

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
                FlowerAttributes.KEY_ANGLE + "," +
                FlowerAttributes.KEY_flowerImage + "," +
                FlowerAttributes.KEY_hu8MomentsMax + "," +
                FlowerAttributes.KEY_hu8MomentsMin + "," +
                FlowerAttributes.KEY_colorMax + "," +
                FlowerAttributes.KEY_colorMin +
                " FROM " + FlowerAttributes.TABLE
                + " WHERE " +
                FlowerAttributes.KEY_ID + "=?";

        int iCount =0;
        FlowerAttributes flowerAttributes = new FlowerAttributes();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                flowerAttributes.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ID));
                flowerAttributes.angle = cursor.getInt(cursor.getColumnIndex(FlowerAttributes.KEY_ANGLE));
                flowerAttributes.flowerImage = cursor.getBlob(cursor.getColumnIndex(FlowerAttributes.KEY_flowerImage));
                convertStringToArray(flowerAttributes,cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMax)),HU_SET_MAX);
                convertStringToArray(flowerAttributes,cursor.getString(cursor.getColumnIndex(FlowerAttributes.KEY_hu8MomentsMin)),HU_SET_MIN);
                //flowerAttributes.colorMax = cursor.getClass<Color>(cursor.getColumnIndex(FlowerAttributes.KEY_colorMax));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return flowerAttributes;
    }

    public void convertArrayToString(FlowerAttributes flowerAttributes){

        int i;
        for ( i = 0;i < 8;i++){
            hu8MomentsMaxString += String.valueOf(flowerAttributes.hu8MomentsMax[i]);
            hu8MomentsMinString += String.valueOf(flowerAttributes.hu8MomentsMin[i]);
            if(i < 7){
                hu8MomentsMaxString += "::";
                hu8MomentsMinString += "::";
            }
        }
}

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
