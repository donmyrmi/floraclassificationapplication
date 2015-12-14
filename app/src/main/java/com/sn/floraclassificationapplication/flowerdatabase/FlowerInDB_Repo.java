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

    public int insert(FlowerGeneralAtt flowerGeneralAtt) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FlowerGeneralAtt.KEY_ID,flowerGeneralAtt.flower_ID);
        values.put(FlowerGeneralAtt.KEY_NAME,flowerGeneralAtt.name);
        values.put(FlowerGeneralAtt.KEY_months,flowerGeneralAtt.months);
        values.put(FlowerGeneralAtt.KEY_momentsWeight,flowerGeneralAtt.momentsWeight);
        values.put(FlowerGeneralAtt.KEY_colorWeight,flowerGeneralAtt.colorWeight);
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

    public void update(FlowerGeneralAtt flowerGeneralAtt) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FlowerGeneralAtt.KEY_ID,flowerGeneralAtt.flower_ID);
        values.put(FlowerGeneralAtt.KEY_NAME,flowerGeneralAtt.name);
        values.put(FlowerGeneralAtt.KEY_months,flowerGeneralAtt.months);
        values.put(FlowerGeneralAtt.KEY_momentsWeight,flowerGeneralAtt.momentsWeight);
        values.put(FlowerGeneralAtt.KEY_colorWeight,flowerGeneralAtt.colorWeight);

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
                flowerGeneralAtt.put("months", cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_months)));
                flowerGeneralAtt.put("momentsWeight", cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_momentsWeight)));
                flowerGeneralAtt.put("colorWeight", cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_colorWeight)));
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
