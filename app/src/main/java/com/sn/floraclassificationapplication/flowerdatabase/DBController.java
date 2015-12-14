package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Created by Nadav and Sapir on 19-Nov-15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController extends SQLiteOpenHelper{
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FlowersCRUD.db";

    public DBController(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Create all necessary tables

        String CREATE_TABLE_FlowerGeneralAtt = "CREATE TABLE " + FlowerGeneralAtt.TABLE  + "("
                + FlowerGeneralAtt.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + FlowerGeneralAtt.KEY_NAME + " TEXT PRIMARY KEY, "
                + FlowerGeneralAtt.KEY_months + " INTEGER, "
                + FlowerGeneralAtt.KEY_momentsWeight + " REAL, "
                + FlowerGeneralAtt.KEY_colorWeight + " REAL, ";

        database.execSQL(CREATE_TABLE_FlowerGeneralAtt);

        String CREATE_TABLE_FlowerLocation = "CREATE TABLE " + FlowerLocation.TABLE + "("
                + FlowerLocation.KEY_ID  + " INTEGER, "
                + FlowerLocation.KEY_locations + " BLOB, ";

        database.execSQL(CREATE_TABLE_FlowerLocation);

        String CREATE_TABLE_FlowerAttributes = "CREATE TABLE " + FlowerAttributes.TABLE + "("
                + FlowerAttributes.KEY_ID + "INTEGER PRIMARY KEY, "
                + FlowerAttributes.KEY_flowerImage + "INTEGER, "
                + FlowerAttributes.KEY_hu8MomentsMax + "DOUBLE[], "
                + FlowerAttributes.KEY_hu8MomentsMin + "DOUBLE[], "
                + FlowerAttributes.KEY_red + "INTEGER, "
                + FlowerAttributes.KEY_redMax + "INTEGER, "
                + FlowerAttributes.KEY_redMin + "INTEGER, "
                + FlowerAttributes.KEY_redRange + "INTEGER, "
                + FlowerAttributes.KEY_green + "INTEGER, "
                + FlowerAttributes.KEY_greenMax + "INTEGER, "
                + FlowerAttributes.KEY_greenMin + "INTEGER, "
                + FlowerAttributes.KEY_greenRange + "INTEGER, "
                + FlowerAttributes.KEY_blue + "INTEGER, "
                + FlowerAttributes.KEY_blueMax + "INTEGER, "
                + FlowerAttributes.KEY_blueMin + "INTEGER, "
                + FlowerAttributes.KEY_blueRange + "INTEGER, ";

        database.execSQL(CREATE_TABLE_FlowerAttributes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        database.execSQL("DROP TABLE IF EXISTS " + FlowerInDB.TABLE);

        // Create tables again
        onCreate(database);

    }


//    private FlowerInDB[] flowerInDB;
//    private static DBController ourInstance = new DBController();
//
//    public static DBController getInstance() {
//        return ourInstance;
//    }
//
//    private DBController() {
//        loadFlowerFromDB();
//    }
//
//    private void loadFlowerFromDB() {
//        flowerInDB = new FlowerInDB[10];
//        for (int i=0; i<10; i++)
//            flowerInDB[i] = new FlowerInDB(i);
//    }

}
