package com.lmb_europa.campscout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by AleksandraPC on 29-Aug-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    //SQL naredba za kreiranje nove tabele
    private static final String DATABASE_CREATE = "create table" + DBAdapter.DATABASE_TABLE + " ("
            + DBAdapter.PLACE_ID + " integer primary key autoincrement, "
            + DBAdapter.PLACE_NUM + " integer unique not null, "
            + DBAdapter.PLACE_COORDS + " text not null, "
            + DBAdapter.SDATE + " text, "
            + DBAdapter.EDATE + " text)";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
        }
        catch (SQLiteException e) {
            Log.w("Helper Message", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + DBAdapter.DATABASE_TABLE);
        onCreate(db);
    }
}
