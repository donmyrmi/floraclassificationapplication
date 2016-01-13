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
    private String floweringLocationsString;
    public FlowerLocation_Repo(Context context) {
        dbHelper = DBController.getInstance(context);
    }

    public int insert(AbstractDBFlower DBFlower) {

        FlowerLocation flowerLocation = (FlowerLocation)DBFlower;
        convertFloweringLocationToString(flowerLocation.locations);
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(flowerLocation.KEY_ID,flowerLocation.flower_ID);
        values.put(flowerLocation.KEY_locations,floweringLocationsString);

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
        FlowerLocation tempLoc = new FlowerLocation();
        String tempStr;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> flowerLocation = new HashMap<String, Object>();
                flowerLocation.put("id", cursor.getInt(cursor.getColumnIndex(FlowerLocation.KEY_ID)));
                tempStr = cursor.getString(cursor.getColumnIndex(FlowerLocation.KEY_locations));
                convertStringToFloweringLocations(tempLoc,tempStr);
                flowerLocation.put("locations",tempLoc.locations);
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

        FlowerLocation flowerLocation = new FlowerLocation();
        String tempStr;

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                flowerLocation.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerLocation.KEY_ID));
                tempStr = cursor.getString((cursor.getColumnIndex(FlowerLocation.KEY_locations)));
                convertStringToFloweringLocations(flowerLocation, tempStr);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return flowerLocation;
    }

    public void addLocation(int flowerId,FloweringLocation loc)
    {
        FlowerLocation flowerLoc = getAttributesById(flowerId);
        FloweringLocation location = new FloweringLocation(loc.getLongitudeMin(),loc.getLatitudeMin(),loc.getLongitudeMax(),loc.getLatitudeMin());
        flowerLoc.locations.add(location);
        updateRaw(flowerLoc);
    }

    public void updateRaw(FlowerLocation flowerLoc)
    {
        delete(flowerLoc.flower_ID);
        insert(flowerLoc);
    }
    public void convertToLocation(byte[] bytes,FlowerLocation flowerLocation) {
        int numOfLocations = bytes.length/2;
        int i = 0;
        int j = 0;

        for(;i < numOfLocations;i++)
        {
            for (FloweringLocation loc :
                    flowerLocation.locations) {
                loc.setLongitudeMax(ByteBuffer.wrap(bytes).getDouble(j));
                j += 8;
                loc.setLatitudeMax(ByteBuffer.wrap(bytes).getDouble(j));
                j += 8;
                loc.setLongitudeMin(ByteBuffer.wrap(bytes).getDouble(j));
                j += 8;
                loc.setLatitudeMin(ByteBuffer.wrap(bytes).getDouble(j));
                j += 8;
            }
        }
    }

    public void setParams(AbstractDBFlower DBFlower,Cursor cursor)
    {
        String tempStr;
        FlowerLocation flowerLocation = (FlowerLocation)DBFlower;
        flowerLocation.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerLocation.KEY_ID));
        tempStr = cursor.getString((cursor.getColumnIndex(FlowerLocation.KEY_locations)));
        convertStringToFloweringLocations(flowerLocation,tempStr);
    }

    public void convertFloweringLocationToString(ArrayList<FloweringLocation> floweringLocations)
    {
        int i = 1;
        for (FloweringLocation fl :
                floweringLocations) {
            if(i == 1)
            {
                floweringLocationsString = String.valueOf(fl.getLongitudeMax());
                i = 0;
            }
            else
            {
                floweringLocationsString += String.valueOf(fl.getLongitudeMax());
            }
            floweringLocationsString += "::";
            floweringLocationsString += String.valueOf(fl.getLongitudeMin());
            floweringLocationsString += "::";
            floweringLocationsString += String.valueOf(fl.getLatitudeMax());
            floweringLocationsString += "::";
            floweringLocationsString += String.valueOf(fl.getLatitudeMin());
            floweringLocationsString += "::";


        }

    }

    public void convertStringToFloweringLocations(FlowerLocation flowerLocation, String strToConvert)
    {
        String[] strToPars;
        int i,j;
        double longitudeMax;
        double longitudeMin;
        double latitudeMax;
        double latitudeMin;
        strToPars = strToConvert.split("\t|\r\n");
        int numOfLocations = strToPars.length/4;
        for(i=0,j=0; i < numOfLocations; i++)
        {
            longitudeMax = Double.parseDouble(strToPars[j++]);
            longitudeMin = Double.parseDouble(strToPars[j++]);
            latitudeMax = Double.parseDouble(strToPars[j++]);
            latitudeMin = Double.parseDouble(strToPars[j++]);
            FloweringLocation fl = new FloweringLocation(longitudeMax,longitudeMin,latitudeMax,latitudeMin);
            flowerLocation.locations.add(fl);

        }
    }

public String getFloweringLocationsString()
{
    return floweringLocationsString;
}
}

