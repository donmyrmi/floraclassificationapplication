package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Repository for DB class FlowerGeneralAtt
 * Created by Sapir on 29-Nov-15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class FlowerInDB_Repo {
    private DBController dbHelper;

    public FlowerInDB_Repo(Context context) {
        dbHelper = new DBController(context);
    }

    public int insert(FlowerGeneralAtt FlowerGeneralAtt) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FlowerGeneralAtt.KEY_ID,FlowerGeneralAtt.KEY_NAME);
        //values.put(FlowerInDB.KEY_flowerImage,FlowerInDB.KEY_hu8MomentsMax);//TODO::SAPIR:bitmap,huMoment - reference or full array?
        //values.put(FlowerInDB.KEY_hu8MomentsMin,FlowerInDB.KEY_colorMax);
        //values.put(FlowerInDB.KEY_colorMin,FlowerInDB.KEY_months);
        values.put(FlowerGeneralAtt.KEY_months,FlowerGeneralAtt.KEY_momentsWeight);
        values.put(FlowerGeneralAtt.KEY_colorWeight,"");
        // Inserting Row
        long FlowerGeneralAtt_Id = db.insert(FlowerGeneralAtt.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) FlowerGeneralAtt_Id;
    }

    public void delete(int flower_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(FlowerGeneralAtt.TABLE, FlowerGeneralAtt.KEY_ID + "= ?", new String[] { String.valueOf(flower_Id) });
        db.close(); // Closing database connection
    }

    public void update(FlowerGeneralAtt flower) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FlowerGeneralAtt.KEY_ID, FlowerGeneralAtt.KEY_NAME);
        //values.put(FlowerInDB.KEY_flowerImage,FlowerInDB.KEY_hu8MomentsMax);//TODO::SAPIR:bitmap,huMoment - reference or full array?
        //values.put(FlowerInDB.KEY_hu8MomentsMin,FlowerInDB.KEY_colorMax);
        //values.put(FlowerInDB.KEY_colorMin,FlowerInDB.KEY_months);
        values.put(FlowerGeneralAtt.KEY_months,FlowerGeneralAtt.KEY_momentsWeight);
        values.put(FlowerGeneralAtt.KEY_colorWeight,"");

        //update Row
        db.update(FlowerGeneralAtt.TABLE, values, FlowerGeneralAtt.KEY_ID + "= ?", new String[]{String.valueOf(FlowerGeneralAtt.KEY_ID)});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getFlowerGeneralAttList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerGeneralAtt.KEY_ID + "," +
                FlowerGeneralAtt.KEY_NAME + "," +
                FlowerGeneralAtt.KEY_months + "," +
                FlowerGeneralAtt.KEY_momentsWeight + "," +
                FlowerGeneralAtt.KEY_colorWeight +
                " FROM " + FlowerGeneralAtt.TABLE;

        //FlowerInDB FlowerInDB = new FlowerInDB();
        ArrayList<HashMap<String, String>> FlowerInDBList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> flowerGeneralAtt = new HashMap<String, String>();
                flowerGeneralAtt.put("id", cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_ID)));
                flowerGeneralAtt.put("name", cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_NAME)));
                FlowerInDBList.add(flowerGeneralAtt);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return FlowerInDBList;
    }

    public FlowerGeneralAtt getFlowerById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerInDB.KEY_ID + "," +
                FlowerInDB.KEY_NAME + "," +
                FlowerGeneralAtt.KEY_months + "," +
                FlowerGeneralAtt.KEY_momentsWeight + "," +
                FlowerGeneralAtt.KEY_colorWeight +
                " FROM " + FlowerGeneralAtt.TABLE
                + " WHERE " +
                FlowerGeneralAtt.KEY_ID + "=?";

        int iCount =0;
        FlowerGeneralAtt flowerGeneralAtt = new FlowerGeneralAtt();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                flowerGeneralAtt.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerInDB.KEY_ID));
                flowerGeneralAtt.name = cursor.getString(cursor.getColumnIndex(FlowerInDB.KEY_NAME));
                //flowerInDB.flowerImage = cursor.getBitMap(cursor.getColumnIndex(FlowerInDB.KEY_flowerImage));//TODO::SAPIR:check how to get bitmap
                flowerGeneralAtt.months = cursor.getInt(cursor.getColumnIndex(FlowerInDB.KEY_months));
                flowerGeneralAtt.momentsWeight = cursor.getFloat(cursor.getColumnIndex(FlowerGeneralAtt.KEY_momentsWeight));
                flowerGeneralAtt.colorWeight = cursor.getFloat(cursor.getColumnIndex(FlowerGeneralAtt.KEY_colorWeight));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return flowerGeneralAtt;
    }
}
