package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Created by Sapir on 05-Dec-15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class FlowerLocation_Repo extends AbstractFlower_Repo implements RepositoryController{

    private DBController dbHelper;

    public FlowerLocation_Repo(Context context) {
        dbHelper = new DBController(context);
    }

    public int insert(AbstractDBFlower DBFlower) {

        FlowerLocation flowerLocation = (FlowerLocation)DBFlower;
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(flowerLocation.KEY_ID,flowerLocation.id);
        //put flower locations in values
        for (com.sn.floraclassificationapplication.flowerdatabase.Location loc :
                flowerLocation.locations) {
            values.put(flowerLocation.KEY_locations,loc.getLatitude());
            values.put(flowerLocation.KEY_locations,loc.getLongitude());
        }


        // Inserting Row
        long FlowerLocation_Id = db.insert(flowerLocation.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) FlowerLocation_Id;
    }

    public void delete(int flower_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(FlowerLocation.TABLE, FlowerLocation.KEY_ID + "= ?", new String[] { String.valueOf(flower_Id) });
        db.close(); // Closing database connection
    }

    public void update(AbstractDBFlower DBFlower) {

        FlowerLocation flowerLocation = (FlowerLocation)DBFlower;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(flowerLocation.KEY_ID, flowerLocation.KEY_locations);
        //update Row
        db.update(flowerLocation.TABLE, values, flowerLocation.KEY_ID + "= ?", new String[]{String.valueOf(flowerLocation.KEY_ID)});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, Object>>  getAttributesList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerLocation.KEY_ID + "," +
                FlowerLocation.KEY_locations +
                " FROM " + FlowerLocation.TABLE;

        //FlowerInDB FlowerInDB = new FlowerInDB();
        ArrayList<HashMap<String, Object>> Flower_Location = new ArrayList<HashMap<String, Object>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> flowerLocation = new HashMap<String, Object>();
                flowerLocation.put("id", cursor.getInt(cursor.getColumnIndex(FlowerLocation.KEY_ID)));
                flowerLocation.put("locations", cursor.getBlob(cursor.getColumnIndex(FlowerLocation.KEY_locations)));//TODO::SAPIR:the calling method will need to convert byte[] into Location class- double,double
                Flower_Location.add(flowerLocation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return Flower_Location;
    }

    public FlowerLocation getAttributesById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerLocation.KEY_ID + "," +
                FlowerLocation.KEY_locations +
                " FROM " + FlowerLocation.TABLE
                + " WHERE " +
                FlowerLocation.KEY_ID + "=?";

        int iCount =0;
        FlowerLocation flowerLocation = new FlowerLocation();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                flowerLocation.id = cursor.getInt(cursor.getColumnIndex(FlowerLocation.KEY_ID));
                convertToLocation(cursor.getBlob((cursor.getColumnIndex(FlowerLocation.KEY_locations))), flowerLocation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return flowerLocation;
    }
    
    public void convertToLocation(byte[] bytes,FlowerLocation flowerLocation) {
        int numOfLocations = bytes.length/2;
        int i = 0;
        int j = 0;

        for(;i < numOfLocations;i++)
        {
            for (com.sn.floraclassificationapplication.flowerdatabase.Location loc :
                    flowerLocation.locations) {
                loc.setLongitude(ByteBuffer.wrap(bytes).getDouble(j));
                j += 8;
                loc.setLatitude(ByteBuffer.wrap(bytes).getDouble(j));
                j += 8;
            }
        }
    }
}

