package com.sn.floraclassificationapplication.flowerdatabase;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Nadav on 19-Nov-15.
 */
public class DBController {
    private FlowerInDB[] flowerInDB;
    private static DBController ourInstance = new DBController();
    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;

    public static DBController getInstance() {
        return ourInstance;
    }

    private DBController() {
        loadFlowerFromDB();
    }

    private void loadFlowerFromDB() {
        flowerInDB = new FlowerInDB[10];
        for (int i=0; i<10; i++)
            flowerInDB[i] = new FlowerInDB(i);
    }

    public SQLiteDatabase getWritableDatabase() {
        return writableDatabase;
    }

    public SQLiteDatabase getReadableDatabase() {
        return readableDatabase;
    }
}
