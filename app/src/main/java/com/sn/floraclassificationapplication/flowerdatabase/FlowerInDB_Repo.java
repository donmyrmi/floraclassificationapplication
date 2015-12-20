package com.sn.floraclassificationapplication.flowerdatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Sapir on 20-Dec-15.
 */
public class FlowerInDB_Repo extends AbstractFlower_Repo{

    private DBController dbHelper;

    public FlowerInDB_Repo(Context context) {
        dbHelper = new DBController(context);
    }
    public ArrayList<AbstractDBFlower> getFlowerByLocation(Location requestedLocation)
    {
        int id;
        ArrayList<AbstractDBFlower> flowersByLocation = new ArrayList<AbstractDBFlower>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                FlowerLocation.KEY_ID + "," +
                FlowerLocation.KEY_locations +
                " FROM " + FlowerLocation.TABLE
                + " WHERE " +
                FlowerLocation.KEY_locations + "=?";


        //get Id's of all location matching flowers
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(requestedLocation) } );
        int numOfMatches = cursor.getCount();
        int i = 0;
        int[] flowerLocId = new int[numOfMatches];
        FlowerInDB[] matchingFlowers = new FlowerInDB[numOfMatches];
        if (cursor.moveToFirst()) {
            do {
                flowerLocId[i] = cursor.getInt(cursor.getColumnIndex(FlowerLocation.KEY_ID));
                matchingFlowers[i].flower_ID = flowerLocId[i];
                i++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        //get all attributes of those flowers
        FlowerGeneralAtt flowerGeneralAtt;
        FlowerLocation flowerLocation = new FlowerLocation();
        //flowerGeneralAtt = flowerGeneralAtt_repo.getFlowerById(id);

        return flowersByLocation;
    }
}
