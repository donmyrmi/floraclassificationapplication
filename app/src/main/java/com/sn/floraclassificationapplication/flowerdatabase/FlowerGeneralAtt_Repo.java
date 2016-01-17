package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Repository for DB class FlowerGeneralAtt
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sn.floraclassificationapplication.Flower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FlowerGeneralAtt_Repo extends AbstractFlower_Repo implements RepositoryController{
    private DBController dbHelper;
    private HashMap<String,Object> tempFlowerTable = new HashMap<String, Object>();
    private String momentsWeightString;
    private String colorsWeightString;

    public FlowerGeneralAtt_Repo(Context context) {
        dbHelper = DBController.getInstance(context);
    }

    /**
     * Insert new row of flower attributes
     * @param DBFlower flower to insert
     * @return status
     */
    public int insert(AbstractDBFlower DBFlower) {

        FlowerGeneralAtt flowerGeneralAtt = (FlowerGeneralAtt)DBFlower;
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FlowerGeneralAtt.KEY_ID,flowerGeneralAtt.flower_ID);
        values.put(FlowerGeneralAtt.KEY_NAME,flowerGeneralAtt.name);
        values.put(FlowerGeneralAtt.KEY_months,flowerGeneralAtt.months);
        values.put(FlowerGeneralAtt.KEY_dateWeight,flowerGeneralAtt.dateWeight);
        values.put(FlowerGeneralAtt.KEY_locationWeight,flowerGeneralAtt.locationWeight);
        // Inserting Row
        long FlowerGeneralAtt_Id = db.insert(FlowerGeneralAtt.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) FlowerGeneralAtt_Id;
    }

    /**
     * Delete a row from the db table
     * @param flower_Id the flower id to delete
     */
    public void delete(int flower_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(FlowerGeneralAtt.TABLE, FlowerGeneralAtt.KEY_ID + "= ?", new String[]{String.valueOf(flower_Id)});
        db.close(); // Closing database connection
    }

    /**
     * Update a table entry of a flower
     * @param DBFlower the flower to update
     */
    public void update(AbstractDBFlower DBFlower) {

        FlowerGeneralAtt flowerGeneralAtt = (FlowerGeneralAtt)DBFlower;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FlowerGeneralAtt.KEY_ID,flowerGeneralAtt.flower_ID);
        values.put(FlowerGeneralAtt.KEY_NAME,flowerGeneralAtt.name);
        values.put(FlowerGeneralAtt.KEY_months,flowerGeneralAtt.months);

        //update Row
        db.update(FlowerGeneralAtt.TABLE, values, FlowerGeneralAtt.KEY_ID + "= ?", new String[]{String.valueOf(FlowerGeneralAtt.KEY_ID)});
        db.close(); // Closing database connection
    }

    /**
     * Retrieve flower attributes from the database to ArrayList of <String, Object>
     * @return the ArrayList of data
     */
    public ArrayList<HashMap<String, Object>>  getAttributesList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerGeneralAtt.KEY_ID + "," +
                FlowerGeneralAtt.KEY_NAME + "," +
                FlowerGeneralAtt.KEY_months + "," +
                FlowerGeneralAtt.KEY_dateWeight + "," +
                FlowerGeneralAtt.KEY_locationWeight +
                " FROM " + FlowerGeneralAtt.TABLE;

        //FlowerInDB FlowerInDB = new FlowerInDB();
        ArrayList<HashMap<String, Object>> FlowerInDBList = new ArrayList<HashMap<String, Object>>();
        FlowerGeneralAtt flower = new FlowerGeneralAtt();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, Object> flowerGeneralAtt = new HashMap<String, Object>();
                flowerGeneralAtt.put("id", cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_ID)));
                flowerGeneralAtt.put("name", cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_NAME)));
                flowerGeneralAtt.put("months", cursor.getInt(cursor.getColumnIndex(FlowerGeneralAtt.KEY_months)));
                flowerGeneralAtt.put("dateWeight",cursor.getFloat(cursor.getColumnIndex(FlowerGeneralAtt.KEY_dateWeight)));
                flowerGeneralAtt.put("locationWeight",cursor.getFloat(cursor.getColumnIndex(FlowerGeneralAtt.KEY_locationWeight)));
                FlowerInDBList.add(flowerGeneralAtt);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return FlowerInDBList;
    }

    /**
     * retrieve flower attributes from the db
     * @param Id id of the flower
     * @return FlowerAttributes class
     */
    public FlowerGeneralAtt getAttributesById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerInDB.KEY_ID + "," +
                FlowerInDB.KEY_NAME + "," +
                FlowerGeneralAtt.KEY_months + "," +
                FlowerGeneralAtt.KEY_dateWeight + "," +
                FlowerGeneralAtt.KEY_locationWeight +
                " FROM " + FlowerGeneralAtt.TABLE
                + " WHERE " +
                FlowerGeneralAtt.KEY_ID + "=?";

        FlowerGeneralAtt flowerGeneralAtt = new FlowerGeneralAtt();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                flowerGeneralAtt.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerGeneralAtt.KEY_ID));
                flowerGeneralAtt.name = cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_NAME));
                flowerGeneralAtt.months = cursor.getInt(cursor.getColumnIndex(FlowerGeneralAtt.KEY_months));
                flowerGeneralAtt.dateWeight = cursor.getFloat(cursor.getColumnIndex(FlowerGeneralAtt.KEY_dateWeight));
                flowerGeneralAtt.locationWeight = cursor.getFloat(cursor.getColumnIndex(FlowerGeneralAtt.KEY_locationWeight));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return flowerGeneralAtt;
    }

    /**
     * @param values_grades - flower parameter string and it's weight
     * @param flowerToAnalyze - the flower to compare to DB
     * @return list of matching flower and their matching percentage
     */
    public ArrayList<HashMap<FlowerInDB,Object>> analyzeFlower(ArrayList<HashMap<String,Object>> values_grades,Flower flowerToAnalyze)
    {
        ArrayList<HashMap<FlowerInDB,Object>> gradedResultList = new ArrayList<HashMap<FlowerInDB,Object>>();
        flowerToAnalyze.segmentAndClassify();


            return gradedResultList;
        }

    /**
     * Retrieve data from cursor and fill FlowerGeneralAtt according to the data retrieved
     * @param DBFlower Flower to insert data to
     * @param cursor Cursor to retrieve data from
     */
    public void setParams(AbstractDBFlower DBFlower, Cursor cursor)
    {
        FlowerGeneralAtt flowerGeneralAtt = (FlowerGeneralAtt)DBFlower;

        flowerGeneralAtt.flower_ID = cursor.getInt(cursor.getColumnIndex(FlowerGeneralAtt.KEY_ID));
        flowerGeneralAtt.name = cursor.getString(cursor.getColumnIndex(FlowerGeneralAtt.KEY_NAME));
        flowerGeneralAtt.months = cursor.getInt(cursor.getColumnIndex(FlowerGeneralAtt.KEY_months));
        flowerGeneralAtt.dateWeight = cursor.getFloat(cursor.getColumnIndex(FlowerGeneralAtt.KEY_dateWeight));
        flowerGeneralAtt.locationWeight = cursor.getFloat(cursor.getColumnIndex(FlowerGeneralAtt.KEY_locationWeight));

    }
}
