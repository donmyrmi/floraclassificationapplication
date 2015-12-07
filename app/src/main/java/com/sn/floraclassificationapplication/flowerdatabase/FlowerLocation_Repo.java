package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Created by Sapir on 05-Dec-15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FlowerLocation_Repo {

    private DBController dbHelper;

    public FlowerLocation_Repo(Context context) {
        dbHelper = new DBController(context);
    }

    public int insert(FlowerLocation flowerLocation) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(flowerLocation.KEY_ID,flowerLocation.KEY_locations);
        //values.put(FlowerInDB.KEY_flowerImage,FlowerInDB.KEY_hu8MomentsMax);//TODO::SAPIR:bitmap,huMoment - reference or full array?
        //values.put(FlowerInDB.KEY_hu8MomentsMin,FlowerInDB.KEY_colorMax);
        //values.put(FlowerInDB.KEY_colorMin,FlowerInDB.KEY_months);

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

    public void update(FlowerLocation flowerLocation) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(flowerLocation.KEY_ID, flowerLocation.KEY_locations);
        //update Row
        db.update(flowerLocation.TABLE, values, flowerLocation.KEY_ID + "= ?", new String[]{String.valueOf(flowerLocation.KEY_ID)});
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getFlowerLocation() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerLocation.KEY_ID + "," +
                FlowerLocation.KEY_locations +
                " FROM " + FlowerLocation.TABLE;

        //FlowerInDB FlowerInDB = new FlowerInDB();
        ArrayList<HashMap<String, String>> Flower_Location = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> flowerLocation = new HashMap<String, String>();
                flowerLocation.put("id", cursor.getString(cursor.getColumnIndex(FlowerLocation.KEY_ID)));
                flowerLocation.put("name", cursor.getString(cursor.getColumnIndex(FlowerLocation.KEY_locations)));
                Flower_Location.add(flowerLocation);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return Flower_Location;
    }

    public FlowerLocation getLocationById(int Id){
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
                //flowerLocation.locations = cursor.getBlob((cursor.getColumnIndex(FlowerLocation.KEY_locations)));//TODO::SAPIR:IMPLEMENT
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return flowerLocation;
    }



    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }




}

