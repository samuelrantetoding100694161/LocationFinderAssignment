package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String LOCATION_TABLE = "Locations";
    private static final String ID = "ID";
    private static final String ADDRESS = "address";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    public DatabaseHelper(Context context){
        super(context, LOCATION_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){//create table for the database locations
        String createTable = "CREATE TABLE " + LOCATION_TABLE + " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ADDRESS + " TEXT, "  +
                LATITUDE + " TEXT, "  +
                LONGITUDE+ " TEXT) ";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldverse, int newverse){//avoid duplication of table
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE);
        onCreate(db);
    }

    // Retrieving address of the latitude and longitude
    public Cursor getAddress(String lat, String longi) {
        SQLiteDatabase db = this.getWritableDatabase();
        //select sql statement
        String query = "SELECT * FROM " + LOCATION_TABLE + " WHERE "+ LATITUDE +"='"+lat+"' AND " + LONGITUDE + "='"+ longi +"'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    //Retrieving latitude and longitude of the address
    public Cursor getLatLong(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        //select sql statement
        String query = "SELECT * FROM " + LOCATION_TABLE + " WHERE "+ ADDRESS +"='"+address+"'";
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    // Insert the location data to the existing database
    public long addLocation(String address, String lat, String longi){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues addContent = new ContentValues();
        addContent.put(ADDRESS, address);//parse the value of the address
        addContent.put(LATITUDE, lat);//value of latitude
        addContent.put(LONGITUDE, longi);//value of longitude

        long add = db.insert(LOCATION_TABLE, null, addContent);//insert method for inserting to database
        return add;
    }

    // Update row of data existed in the database
    public int updateLocation(String address, String lat, String longi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateContent = new ContentValues();
        updateContent.put(ADDRESS, address);//update address
        //update latitude
        updateContent.put(LATITUDE, lat);
        //update longitude
        updateContent.put(LONGITUDE, longi);

        int success = db.update(LOCATION_TABLE, updateContent, ADDRESS+" = ?", new String[]{address});
        return success;
    }

    // Delete row of location data
    public int deleteLocation(String address){
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(LOCATION_TABLE,ADDRESS+"=?",new String[]{address});//delete method
        return delete;
    }

    // Boolean method to check whether the database is empty or not
    public boolean isEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, LOCATION_TABLE) == 0;
    }
}
