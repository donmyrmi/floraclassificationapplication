package com.sn.floraclassificationapplication.flowerdatabase;

/**
 * Created by Nadav and Sapir on 19-Nov-15.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sn.floraclassificationapplication.Flower;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DBController extends SQLiteOpenHelper{
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private List<FlowerInDB> flowerInDB;
    private static DBController ourInstance;
    private static final int DATABASE_VERSION = 1;
    private static FlowerGeneralAtt_Repo flowerGeneralAtt_repo;
    private static FlowerAttributes_Repo flowerAttributes_repo;
    private static FlowerLocation_Repo flowerLocation_repo;
    // Database Name & path
    private static final String DATABASE_NAME = "FlowersCRUD.db";
    private static String DB_FILEPATH = "/data/data/com.sn.floraclassificationapplication/databases/FlowersCRUD.db";
    private static SQLiteDatabase db;
    public static boolean dbSuccess;

    private DBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
        //loadTestFlowerDB();
    }

    public static DBController getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DBController(context);
            flowerGeneralAtt_repo = new FlowerGeneralAtt_Repo(context);
            flowerAttributes_repo = new FlowerAttributes_Repo(context);
            flowerLocation_repo = new FlowerLocation_Repo(context);
            try
            {
                dbSuccess = ourInstance.importDatabase("/storage/sdcard/FlowersCRUD.sql");
            }
            catch (Exception IOException)
            {
                System.out.print("IOExecption occurred");
            }

        }
        return ourInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Create all necessary tables
        String CREATE_TABLE_FlowerGeneralAtt = "CREATE TABLE " + FlowerGeneralAtt.TABLE  + "("
                + FlowerGeneralAtt.KEY_ID  + " INTEGER PRIMARY KEY, "
                + FlowerGeneralAtt.KEY_NAME + " TEXT, "
                + FlowerGeneralAtt.KEY_months + " INTEGER, "
                + FlowerGeneralAtt.KEY_momentsWeight + " REAL, "
                + FlowerGeneralAtt.KEY_colorWeight + " REAL, "
                + FlowerGeneralAtt.KEY_dateWeight + " REAL, "
                + FlowerGeneralAtt.KEY_locationWeight + " REAL)";

        database.execSQL(CREATE_TABLE_FlowerGeneralAtt);

        String CREATE_TABLE_FlowerLocation = "CREATE TABLE " + FlowerLocation.TABLE + "("
                + FlowerLocation.KEY_ID  + " INTEGER PRIMARY KEY, "
                + FlowerLocation.KEY_locations + " BLOB)";

        database.execSQL(CREATE_TABLE_FlowerLocation);

        String CREATE_TABLE_FlowerAttributes = "CREATE TABLE " + FlowerAttributes.TABLE + "("
                + FlowerAttributes.KEY_ID + "INTEGER PRIMARY KEY, "
                + FlowerAttributes.KEY_angle + "INTEGER, "
                + FlowerAttributes.KEY_hu8MomentsMax + "BLOB, "
                + FlowerAttributes.KEY_hu8MomentsMin + "BLOB, "
                + FlowerAttributes.KEY_redMax + "INTEGER, "
                + FlowerAttributes.KEY_redMin + "INTEGER, "
                + FlowerAttributes.KEY_greenMax + "INTEGER, "
                + FlowerAttributes.KEY_greenMin + "INTEGER, "
                + FlowerAttributes.KEY_blueMax + "INTEGER, "
                + FlowerAttributes.KEY_blueMin + "INTEGER)";

        database.execSQL(CREATE_TABLE_FlowerAttributes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        database.execSQL("DROP TABLE IF EXISTS " + FlowerGeneralAtt.TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + FlowerAttributes.TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + FlowerLocation.TABLE);

        // Create tables again
        onCreate(database);

    }

    public boolean importDatabase(String dbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            this.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
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

    public void loadTestFlowerDB(Flower flower) {
        flowerInDB = new ArrayList<FlowerInDB>();
        for (int i=0; i<10; i++) {
            flowerInDB.add(testFlowerInDB(i,1, flower));
            flowerInDB.add(testFlowerInDB(i,2, flower));
        }
        Collections.sort(flowerInDB, new Comparator<FlowerInDB>() {
            public int compare(FlowerInDB o1, FlowerInDB o2) {
                return (int) (o2.getRank() - o1.getRank());
            }
        });
    }

    private FlowerInDB testFlowerInDB(int fid, int angle, Flower flower)  {
        Random rand = new Random();
        FlowerInDB testF = new FlowerInDB(fid);

        testF.setAngle(angle);
        int num;

        testF.setName("Flower"+fid);

        num = rand.nextInt(8191);
        testF.setMonths(num);

        num = rand.nextInt(206)+50;
        testF.setRedMax(num);
        testF.setRedMin(num-50);

        num = rand.nextInt(206)+50;
        testF.setGreenMax(num);
        testF.setGreenMin(num-50);

        num = rand.nextInt(206)+50;
        testF.setBlueMax(num);
        testF.setBlueMin(num-50);

        double[] husMax = new double[8];
        double[] husMin = new double[8];
        for (int i=0; i<8; i++) {
            husMax[i] = Math.random();
            husMin[i] = husMax[i] * 0.95;
        }
        testF.setHu8MomentsMax(husMax);
        testF.setHu8MomentsMin(husMin);

        List<FloweringLocation> locations = new ArrayList<FloweringLocation>();

        double lonMin = 28 + Math.random() * 4;
        double lonMax =  lonMin * 1.05;
        double latMin = 28 + Math.random() * 4;
        double latMax =  latMin * 1.05;
        double rad = 0.2 + Math.random();
        locations.add(new FloweringLocation(lonMin, latMin, lonMax, latMax));

        testF.setLocations(locations);

        testF.calculateRankFromFlower(flower);

        return testF;
    }

    public List<FlowerInDB> getFlowerInDB() {
        return flowerInDB;
    }

    public ArrayList<FlowerInDB> getFlowers()
    {

        FlowerGeneralAtt flowerGeneralAtt = new FlowerGeneralAtt();
        FlowerAttributes flowerAttributes = new FlowerAttributes();
        FlowerLocation flowerLocation = new FlowerLocation();

        SQLiteDatabase db = getReadableDatabase();

        ArrayList<FlowerInDB> flowersList = new ArrayList<FlowerInDB>();
        String selectQuery = "SELECT * FROM " + FlowerGeneralAtt.TABLE + "NATURAL JOIN " + FlowerAttributes.TABLE
                        + "NATURAL JOIN " + FlowerLocation.TABLE;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                FlowerInDB flower = new FlowerInDB();
                flowerGeneralAtt_repo.setParams(flowerGeneralAtt,cursor);
                flowerAttributes_repo.setParams(flowerAttributes,cursor);
                flowerLocation_repo.setParams(flowerLocation,cursor);
                flower.flower_ID = flowerGeneralAtt.flower_ID;
                flower.setName(flowerGeneralAtt.name);
                flower.setHu8MomentsMax(flowerAttributes.hu8MomentsMax);
                flower.setHu8MomentsMin(flowerAttributes.hu8MomentsMin);
                flower.setRedMax(flowerAttributes.redMax);
                flower.setRedMin(flowerAttributes.redMin);
                flower.setGreenMax(flowerAttributes.greenMax);
                flower.setGreenMin(flowerAttributes.greenMin);
                flower.setBlueMax(flowerAttributes.blueMax);
                flower.setBlueMin(flowerAttributes.blueMin);
                flower.setMonths(flowerGeneralAtt.months);
                flower.setMomentsWeight(flowerAttributes.momentsWeight);
                //flower.setColorWeight(flowerGeneralAtt.colorWeight);TODO::SAPIR - VERIFY IF WE NEED WEIGHT FOR EACH RGB COLORS, OR UNITED?
                flower.setDateWeight(flowerGeneralAtt.dateWeight);
                flower.setLocations(flowerLocation.locations);


            } while (cursor.moveToNext());
        }

        return flowersList;
    }
}
//    public Cursor query (String table, String[] columns, String selection, String[] selectionArgs,
//                         String groupBy, String having, String orderBy, String limit)