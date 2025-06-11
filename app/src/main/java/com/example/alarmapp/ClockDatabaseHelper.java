package com.example.alarmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ClockDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "world_clock.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "timezones";

    public ClockDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, tz TEXT UNIQUE)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addTimeZone(String timeZoneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tz", timeZoneId);
        try {
            db.insertOrThrow(TABLE_NAME, null, values);
            return true;
        } catch (SQLiteConstraintException e) {
            return false;
        }
    }
    public boolean deleteTimeZone(String zoneId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NAME, "tz = ?", new String[]{zoneId});
        return rows > 0;
    }

    public List<ClockItem> getAllTimeZones() {
        List<ClockItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tz FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new ClockItem(cursor.getString(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
