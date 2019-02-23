package com.example.lighthead.androidcustomcalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME="coPlanningDB.db";
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "tasks";

    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_Name = "Name";
    public static final String COLUMN_Comment = "Comment";
    public static final String COLUMN_Date = "Date";
    public static final String COLUMN_Time = "Time";
    public static final String COLUMN_Duration = "Duration";
    public static final String COLUMN_Visibility = "Visibility";
    public static final String COLUMN_Editable = "Editable";
    public static final String COLUMN_Complete = "Complete";


    public DBHelper(Context context) {
        super(context, DBNAME, null, SCHEMA);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                       + COLUMN_Name+ " TEXT, "
                                                        + COLUMN_Comment+ " TEXT, "
                                                        + COLUMN_Date+ " TEXT, "
                                                        + COLUMN_Time+ " TEXT, "
                                                        + COLUMN_Duration+ " INTEGER, "
                                                        + COLUMN_Visibility+ " BOOLEAN, "
                                                        + COLUMN_Editable+ " BOOLEAN,"
                                                        + COLUMN_Complete + " BOOLEAN );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
