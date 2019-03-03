package com.example.lighthead.androidcustomcalendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DBNAME="coPlanningDB.db";
    private static final int DBVERSION = 7; // версия базы данных
    static final String TABLE = "tasks";

    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_Name = "Name";
    public static final String COLUMN_Comment = "Comment";
    public static final String COLUMN_DateFrom = "DateFrom";
    public static final String COLUMN_TimeFrom = "TimeFrom";
    public static final String COLUMN_DateTo= "DateTo";
    public static final String COLUMN_TimeTo = "TimeTo";

    public static final String COLUMN_Visibility = "Visibility";
    public static final String COLUMN_Editable = "Editable";
    public static final String COLUMN_Complete = "Complete";


    public DBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                       + COLUMN_Name+ " TEXT, "
                                                        + COLUMN_Comment+ " TEXT, "
                                                        + COLUMN_DateFrom+ " TEXT, "
                                                        + COLUMN_TimeFrom+ " TEXT, "
                                                        + COLUMN_DateTo+ " TEXT, "
                                                        + COLUMN_TimeTo+ " TEXT, "
                                                        + COLUMN_Visibility+ " BOOLEAN, "
                                                        + COLUMN_Editable+ " BOOLEAN,"
                                                        + COLUMN_Complete + " BOOLEAN );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("CREATE TEMPORARY TABLE " + TABLE + "_backup" + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_Name+ " TEXT, "
                    + COLUMN_Comment+ " TEXT, "
                    + COLUMN_DateFrom+ " TEXT, "
                    + COLUMN_TimeFrom+ " TEXT, "
                    + COLUMN_Visibility+ " BOOLEAN, "
                    + COLUMN_Editable+ " BOOLEAN,"
                    + COLUMN_Complete + " BOOLEAN );");

            db.execSQL("INSERT INTO " + TABLE+"_backup" +" SELECT " + COLUMN_ID + ","+
                                                                            COLUMN_Name+ "," +
                                                                            COLUMN_Comment+ "," +
                                                                            COLUMN_DateFrom+ "," +
                                                                            COLUMN_TimeFrom+ "," +
                                                                            COLUMN_DateTo+ "," +
                                                                            COLUMN_TimeTo+ "," +
                                                                            COLUMN_Visibility +  " FROM "+TABLE + ";");
            db.execSQL("ALTER TABLE " + TABLE+"_backup" +" ADD COLUMN " + COLUMN_DateTo+ " TEXT");
            db.execSQL("ALTER TABLE " + TABLE+"_backup" +" ADD COLUMN " + COLUMN_TimeTo+ " TEXT");

            Cursor userCursor = db.rawQuery("select * from "+ TABLE+"_backup", null);
            for(userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {
                long id = userCursor.getLong(userCursor.getColumnIndex(DBHelper.COLUMN_ID));
                String name = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Name));
                String comment = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Comment));
                String dateFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateFrom));
                String timeFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeFrom));
                String dateTo = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateTo));
                String timeTo = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeTo));
                String visibility = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Visibility));
                String editable = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Editable));
                String completed = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Complete));



            }




            db.execSQL("DROP TABLE " + TABLE +";\n");

            db.execSQL("CREATE TABLE " + TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_Name+ " TEXT, "
                    + COLUMN_Comment+ " TEXT, "
                    + COLUMN_DateFrom+ " TEXT, "
                    + COLUMN_TimeFrom+ " TEXT, "
                    + COLUMN_Visibility+ " BOOLEAN, "
                    + COLUMN_Editable+ " BOOLEAN, "
                    + COLUMN_Complete+ " BOOLEAN, "
                    + COLUMN_DateTo+ " TEXT,"
                    + COLUMN_TimeTo + " TEXT );");
            db.execSQL("INSERT INTO " + TABLE+" SELECT * FROM " + TABLE+"_backup" +";\n");


            userCursor = db.rawQuery("select * from "+ TABLE, null);
            for(userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {
                long id = userCursor.getLong(userCursor.getColumnIndex(DBHelper.COLUMN_ID));
                String name = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Name));
                String comment = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Comment));
                String dateFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateFrom));
                String timeFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeFrom));
                String dateTo = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateTo));
                String timeTo = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeTo));
                String visibility = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Visibility));
                String editable = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Editable));
                String completed = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Complete));



            }


            db.execSQL("DROP TABLE " + TABLE+"_backup" +";\n");
            db.setTransactionSuccessful();
        }
        finally{
            db.endTransaction();
        }
    }
}
