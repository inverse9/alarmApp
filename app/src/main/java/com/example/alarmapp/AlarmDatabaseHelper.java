package com.example.alarmapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class AlarmDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarms.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "alarms";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_ENABLED = "isEnabled";

    public AlarmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TIME + " TEXT," +
                COLUMN_LABEL + " TEXT," +
                COLUMN_ENABLED + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean updateAlarmEnabled(int id, boolean isEnabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isEnabled", isEnabled ? 1 : 0);
        int rows = db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }
    public void deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void updateAlarm(int id, String time, String label, boolean isEnabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_LABEL, label);
        values.put(COLUMN_ENABLED, isEnabled ? 1 : 0);

        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void insertAlarm(String time, String label, boolean isEnabled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_LABEL, label);
        values.put(COLUMN_ENABLED, isEnabled ? 1 : 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<AlarmModel> getAllAlarms() {
        ArrayList<AlarmModel> alarms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_TIME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
                String label = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LABEL));
                boolean isEnabled = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ENABLED)) == 1;

                alarms.add(new AlarmModel(id, time, label, isEnabled));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return alarms;
    }
}
