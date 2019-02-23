package com.example.lighthead.androidcustomcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

public class TaskManager {

    DBHelper dbHelper;
    SQLiteDatabase db;
    ContentValues contentValues;

    public TaskManager(Context context) {

        dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        contentValues = new ContentValues();

    }

    public long UpdateTask(Task task, long id) {

        UpdateContentValues(task);

        long result = db.update(DBHelper.TABLE, contentValues, DBHelper.COLUMN_ID + "=" + String.valueOf(id), null);

        return result;
    }

    public long AddTask(Task task){

        UpdateContentValues(task);

        long result = db.insert(DBHelper.TABLE, null, contentValues);

        return result;

    }

    public long DeleteTask(long id) {

        long result = db.delete(DBHelper.TABLE, DBHelper.COLUMN_ID + "=" + String.valueOf(id), null);

        return result;
    }

    public ArrayList<TaskDBWrapper> GetTasks()
    {
        Cursor userCursor = GetUserCursor();

        ArrayList<TaskDBWrapper> resList = CursorToArrayList(userCursor);

        return resList;
    }


    public ArrayList<TaskDBWrapper> GetTasks(Calendar leftBorder, Calendar rightBorder) {
        Cursor userCursor = GetUserCursor();

        ArrayList<TaskDBWrapper> resList = CursorToArrayList(userCursor, leftBorder, rightBorder);


        return resList;

    }




    public ArrayList<TaskDBWrapper> CursorToArrayList(Cursor userCursor, Calendar leftBorder, Calendar rightBorder) {
        ArrayList<TaskDBWrapper> resList = new ArrayList<>();
        for(userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {

            String date = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Date));
            if (date!=null) {
                GregorianCalendar curTaskDate = new GregorianCalendar();
               /* String [] dateVals = date.split("\\.");
                int intDate = Integer.parseInt(dateVals[0]);
                int intMonth = Integer.parseInt(dateVals[1]);
                int intYear = Integer.parseInt(dateVals[2]);*/
                int intYear = ConvertDateAndTime.GetYearFromStringDate(date);
                int intMonth = ConvertDateAndTime.GetMonthFromStringDate(date);
                int intDate = ConvertDateAndTime.GetDayFromStringDate(date);

                String name = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Name));
                Task task = new Task(name);
                String time = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Time));


                int intHours;
                int intMinutes;
                if ((time!=null)) {
                  /*  String[] timeVals = time.split("\\-");
                    intHours = Integer.parseInt(timeVals[0]);
                    intMinutes = Integer.parseInt(timeVals[1]);*/
                    intHours = ConvertDateAndTime.GetHourFromStringTime(time);
                    intMinutes = ConvertDateAndTime.GetMinutesFromStringTime(time);

                    curTaskDate.set(intYear, intMonth-1, intDate, intHours, intMinutes, rightBorder.getTime().getSeconds());
                    task.SetTime(intHours, intMinutes);
                }
                else {
                    intHours = rightBorder.getTime().getHours();
                    intMinutes = rightBorder.getTime().getMinutes();
                    curTaskDate.set(intYear, intMonth-1, intDate, intHours, intMinutes, rightBorder.getTime().getSeconds());
                    curTaskDate.add(Calendar.SECOND, -1);


                }

                task.SetDate(intYear, intMonth, intDate);

                if ((curTaskDate.after(leftBorder))&&(curTaskDate.before(rightBorder))) {

                    String comment = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Comment));
                    if (comment != null)
                        task.SetComment(comment);

                    Double duration = userCursor.getDouble(userCursor.getColumnIndex(DBHelper.COLUMN_Duration));

                    if (duration != null)
                        task.SetDuration(duration);

                    String visibility = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Visibility));
                    if (visibility != null)
                        task.SetVisibility(visibility.equals("1") ? true : false);

                    String editable = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Editable));
                    if (editable != null)
                        task.SetEditable(editable.equals("1") ? true : false);

                    String completed = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Complete));
                    if (completed != null)
                        task.SetCompleted(completed.equals("1")? true: false);


                  //  resList.add(task);

                    long id = userCursor.getLong(userCursor.getColumnIndex(DBHelper.COLUMN_ID));
                    TaskDBWrapper taskDBWrapper = new TaskDBWrapper(task);
                    taskDBWrapper.SetId(id);

                    resList.add(taskDBWrapper);

                }





            }
        }



        return resList;

    }



    private Cursor GetUserCursor(){
        return db.rawQuery("select * from "+ DBHelper.TABLE, null);
    }

    private ArrayList<TaskDBWrapper> CursorToArrayList(Cursor userCursor) {
        ArrayList<TaskDBWrapper> resList = new ArrayList<>();

        for(userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {


            String name = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Name));
            Task task = new Task(name);

            String comment = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Comment));
            if (comment!=null)
                task.SetComment(comment);

            String date = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Date));
            if (date!=null) {
              /*  String [] dateVals = date.split("\\.");
                int intDate = Integer.parseInt(dateVals[0]);
                int intMonth = Integer.parseInt(dateVals[1]);
                int intYear = Integer.parseInt(dateVals[2]);*/
                int intYear = ConvertDateAndTime.GetYearFromStringDate(date);
                int intMonth = ConvertDateAndTime.GetMonthFromStringDate(date);
                int intDate = ConvertDateAndTime.GetDayFromStringDate(date);
                task.SetDate(intYear, intMonth, intDate);
            }

            String time = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Time));
            if (time!=null) {
               /* String[] timeVals = time.split("\\-");
                int intHours = Integer.parseInt(timeVals[0]);
                int intMinutes = Integer.parseInt(timeVals[1]);*/
                int intHours = ConvertDateAndTime.GetHourFromStringTime(time);
                int intMinutes = ConvertDateAndTime.GetMinutesFromStringTime(time);
                task.SetTime(intHours, intMinutes);
            }


            Double duration = userCursor.getDouble(userCursor.getColumnIndex(DBHelper.COLUMN_Duration));
            if (duration!=null)
                task.SetDuration(duration);

            String visibility = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Visibility));
            if (visibility!=null)
                task.SetVisibility(visibility.equals("1")?true:false);

            String editable = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Editable));
            if (editable!=null)
                task.SetEditable(editable.equals("1")?true:false);

            String completed = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Complete));
            if (completed != null)
                task.SetCompleted(completed.equals("1")? true: false);


            //resList.add(task);
            long id = userCursor.getLong(userCursor.getColumnIndex(DBHelper.COLUMN_ID));
            TaskDBWrapper taskDBWrapper = new TaskDBWrapper(name);
            taskDBWrapper.SetId(id);

            resList.add(taskDBWrapper);

        }

        return resList;
    }

    private void UpdateContentValues(Task task) {

        contentValues.clear();

        contentValues.put(DBHelper.COLUMN_Name, task.GetName());
        contentValues.put(DBHelper.COLUMN_Comment, task.GetComment());
        contentValues.put(DBHelper.COLUMN_Date, task.GetDate());
        contentValues.put(DBHelper.COLUMN_Time, task.GetTime());
        contentValues.put(DBHelper.COLUMN_Duration, task.GetDuration());
        contentValues.put(DBHelper.COLUMN_Visibility, task.GetVisibility());
        contentValues.put(DBHelper.COLUMN_Editable, task.GetEditable());
        contentValues.put(DBHelper.COLUMN_Complete, task.GetCompleted());
    }
}
