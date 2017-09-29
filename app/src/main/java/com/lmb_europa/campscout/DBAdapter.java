package com.lmb_europa.campscout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AleksandraPC on 29-Aug-17.
 */

public class DBAdapter {

    public static final String DATABASE_NAME = "CampScoutDB";
    public static final String DATABASE_TABLE = "MyPlaces";
    public static final int DATABASE_VERSION = 1;

    public static final String PLACE_ID = "ID";
    public static final String PLACE_NUM = "NumOfPlace";
    public static final String PLACE_COORDS = "ImgCoordsOfPlaces"; //String je placeholder
    public static final String SDATE = "StartDates";
    public static final String EDATE = "EndDates";

    private SQLiteDatabase db;

    private final Context context;
    private DBHelper dbHelper;

    public DBAdapter(Context cont) {
        context = cont;
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    //self explanatory
    public long insertEntry(MyPlace myPlace) {
         ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_NUM, myPlace.getPlaceNum());
        contentValues.put(PLACE_COORDS, myPlace.getPlaceCoords());
        contentValues.put(SDATE, myPlace.getStartDate());
        contentValues.put(EDATE, myPlace.getEndDate());

        long id = -1;
        db.beginTransaction();
        try {
            id = db.insert(DATABASE_TABLE, null, contentValues);
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e)
        {
            Log.w("DBAdapter Message", e.getMessage());
        }
        finally {
            db.endTransaction();
        }

        return id;
    }

    //funkcija ce da vrati sve entrije u bazi...ne jede leba, moze da bude korisna
    public ArrayList<MyPlace> getAllEntries(){
        ArrayList<MyPlace> myPlaces = null;
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(DATABASE_TABLE, null, null, null, null,null, null);
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e){
            Log.w("DBAdapter Message", e.getMessage());
        }
        finally {
            db.endTransaction();
        }
        if (cursor!=null){
            myPlaces = new ArrayList<MyPlace>();
            MyPlace myPlace = null;
            while(cursor.moveToNext()){
                myPlace = new MyPlace(cursor.getInt(cursor.getColumnIndex(DBAdapter.PLACE_NUM)));
                myPlace.setPlaceCoords(cursor.getString(cursor.getColumnIndex(DBAdapter.PLACE_COORDS)));
                myPlace.setStartDate(cursor.getString(cursor.getColumnIndex(DBAdapter.SDATE)));
                myPlace.setEndDate(cursor.getString(cursor.getColumnIndex(DBAdapter.EDATE)));
                myPlaces.add(myPlace);
            }
        }

        return myPlaces;
    }

    //funkcija ce da vrati samo jedan Entry iz baze...korisno, korisno
    public MyPlace getEntry(){
        MyPlace myPlace = null;
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(DATABASE_TABLE, null, null, null, null,null, null);
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e){
            Log.w("DBAdapter Message", e.getMessage());
        }
        finally {
            db.endTransaction();
        }
        if (cursor!=null){
            if(cursor.moveToFirst()){
                myPlace = new MyPlace(cursor.getInt(cursor.getColumnIndex(DBAdapter.PLACE_NUM)));
                myPlace.setPlaceID(cursor.getLong(cursor.getColumnIndex(DBAdapter.PLACE_ID)));
                myPlace.setPlaceCoords(cursor.getString(cursor.getColumnIndex(DBAdapter.PLACE_COORDS)));
                myPlace.setStartDate(cursor.getString(cursor.getColumnIndex(DBAdapter.SDATE)));
                myPlace.setEndDate(cursor.getString(cursor.getColumnIndex(DBAdapter.EDATE)));

            }
        }
        return myPlace;
    }

    //Ovo ce da bude funkcija koja ce imena slika da vraca...a mozda i nece...al hocu da je imam ako se predomislim pa stavim imena slika u bazu
    public MyPlace getNameEntries(){
        MyPlace myPlace = null;
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(DATABASE_TABLE, null, null, null, null,null, null);
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e){
            Log.w("DBAdapter Message", e.getMessage());
        }
        finally {
            db.endTransaction();
        }
        if (cursor!=null){
            if(cursor.moveToFirst()){
                myPlace = new MyPlace(cursor.getInt(cursor.getColumnIndex(DBAdapter.PLACE_NUM)));
                myPlace.setPlaceID(cursor.getLong(cursor.getColumnIndex(DBAdapter.PLACE_ID)));
                myPlace.setPlaceCoords(cursor.getString(cursor.getColumnIndex(DBAdapter.PLACE_COORDS)));
                myPlace.setStartDate(cursor.getString(cursor.getColumnIndex(DBAdapter.SDATE)));
                myPlace.setEndDate(cursor.getString(cursor.getColumnIndex(DBAdapter.EDATE)));

            }
        }
        return myPlace;
    }

    //funkcija apdejtuje datume u bazi...ovo je bitna funkcija to treba da ima
    public int updateEntry(long id, MyPlace myPlace) {
        String where = PLACE_ID + "=" + id;

        ContentValues contentValues = new ContentValues();

        contentValues.put(SDATE, myPlace.getStartDate());
        contentValues.put(EDATE, myPlace.getEndDate());

        return db.update(DATABASE_TABLE, contentValues, where, null);
    }

    //Why? For the glory of elfak of course!
    public boolean removeEntry(long id){
        boolean success = false;
        db.beginTransaction();

        try {
            success = db.delete(DATABASE_TABLE, PLACE_ID + "=" + id, null) > 0;
            db.setTransactionSuccessful();
        }
        catch (SQLiteException e) {
            Log.w("DBAdapter Message", e.getMessage());
        }
        finally {
            db.endTransaction();
        }

        return success;
    }

}
