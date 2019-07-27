package com.example.lighthead.androidcustomcalendar.helpers.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.models.Task;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskDBWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    public void ClearAll() {
        db.execSQL("delete from " + DBHelper.TABLE);
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


/*

    public ArrayList<TaskDBWrapper> CursorToArrayList(Cursor userCursor, Calendar leftBorder, Calendar rightBorder) {
        ArrayList<TaskDBWrapper> resList = new ArrayList<>();
        for(userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {

            String name = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Name));
            Task task = new Task(name);

            String dateFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateFrom));
            if (dateFrom!=null) {


                GregorianCalendar curTaskDateFrom = new GregorianCalendar();

                int intYear = ConvertDateAndTime.GetYearFromStringDate(dateFrom);
                int intMonth = ConvertDateAndTime.GetMonthFromStringDate(dateFrom);
                int intDate = ConvertDateAndTime.GetDayFromStringDate(dateFrom);

                String timeFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeFrom));

                int intHours;
                int intMinutes;
                if ((timeFrom!=null)) {
                    intHours = ConvertDateAndTime.GetHourFromStringTime(timeFrom);
                    intMinutes = ConvertDateAndTime.GetMinutesFromStringTime(timeFrom);

                    curTaskDateFrom.set(intYear, intMonth-1, intDate, intHours, intMinutes, rightBorder.getTime().getSeconds());
                    task.SetTimeFrom(intHours, intMinutes);
                }
                else {
                    intHours = rightBorder.getTime().getHours();
                    intMinutes = rightBorder.getTime().getMinutes();
                    curTaskDateFrom.set(intYear, intMonth-1, intDate, intHours, intMinutes, rightBorder.getTime().getSeconds());
                    curTaskDateFrom.add(Calendar.SECOND, -1);
                }

                task.SetDateFrom(intYear, intMonth, intDate);


                if ((curTaskDateFrom.after(leftBorder))&&(curTaskDateFrom.before(rightBorder))) {

                    String comment = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Comment));
                    if (comment != null)
                        task.SetComment(comment);

                    String visibility = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Visibility));
                    if (visibility != null)
                        task.SetVisibility(visibility.equals("1") ? true : false);

                    String editable = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Editable));
                    if (editable != null)
                        task.SetEditable(editable.equals("1") ? true : false);

                    String completed = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Complete));
                    if (completed != null)
                        task.SetCompleted(completed.equals("1")? true: false);

                    long id = userCursor.getLong(userCursor.getColumnIndex(DBHelper.COLUMN_ID));
                    TaskDBWrapper taskDBWrapper = new TaskDBWrapper(task);
                    taskDBWrapper.SetId(id);

                    resList.add(taskDBWrapper);

                }

            }
        }
        return resList;
    }
*/


    public ArrayList<TaskDBWrapper> CursorToArrayList(Cursor userCursor, Calendar leftBorder, Calendar rightBorder) {
        ArrayList<TaskDBWrapper> resList = new ArrayList<>();
        for(userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {

            String name = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Name));

            Task task = new Task(name);

            String dateFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateFrom));
            GregorianCalendar curTaskDateFrom = null;
            if ((dateFrom!=null)&&(!dateFrom.equals(""))) {

                curTaskDateFrom = new GregorianCalendar();

                int intYear;
                int intMonth;
                int intDate;
                try {
                    intYear = ConvertDateAndTime.GetYearFromStringDate(dateFrom);
                    intMonth = ConvertDateAndTime.GetMonthFromStringDate(dateFrom);
                    intDate = ConvertDateAndTime.GetDayFromStringDate(dateFrom);
                }
                catch (Exception e) {
                    intYear = ConvertDateAndTime.GetYearFromISOStringDate(dateFrom);
                    intMonth = ConvertDateAndTime.GetMonthFromISOStringDate(dateFrom);
                    intDate = ConvertDateAndTime.GetDayFromISOStringDate(dateFrom);
                }

                String timeFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeFrom));

                int intHours;
                int intMinutes;
                if ((timeFrom != null)&&(!timeFrom.equals(""))) {
                    try {
                        intHours = ConvertDateAndTime.GetHourFromStringTime(timeFrom);
                        intMinutes = ConvertDateAndTime.GetMinutesFromStringTime(timeFrom);
                    }
                    catch(Exception e) {
                        intHours = ConvertDateAndTime.GetHourFromISOStringTime(timeFrom);
                        intMinutes = ConvertDateAndTime.GetMinutesFromISOStringTime(timeFrom);
                    }

                    curTaskDateFrom.set(intYear, intMonth - 1, intDate, intHours, intMinutes, rightBorder.getTime().getSeconds());
                    task.SetTimeFrom(intHours, intMinutes);
                } else {
                    intHours = rightBorder.getTime().getHours();
                    intMinutes = rightBorder.getTime().getMinutes();
                    curTaskDateFrom.set(intYear, intMonth - 1, intDate, intHours, intMinutes, rightBorder.getTime().getSeconds());
                    curTaskDateFrom.add(Calendar.SECOND, -1);
                }

                task.SetDateFrom(intYear, intMonth, intDate);
            }


            String dateTo = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateTo));
            GregorianCalendar curTaskDateTo = null;
            if ((dateTo!=null)&&(!dateTo.equals(""))) {

                curTaskDateTo = new GregorianCalendar();

                int intYear;
                int intMonth;
                int intDate;
                try {
                    intYear = ConvertDateAndTime.GetYearFromStringDate(dateTo);
                    intMonth = ConvertDateAndTime.GetMonthFromStringDate(dateTo);
                    intDate = ConvertDateAndTime.GetDayFromStringDate(dateTo);
                }
                catch (Exception e) {
                    intYear = ConvertDateAndTime.GetYearFromISOStringDate(dateTo);
                    intMonth = ConvertDateAndTime.GetMonthFromISOStringDate(dateTo);
                    intDate = ConvertDateAndTime.GetDayFromISOStringDate(dateTo);
                }

                String timeTo = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeTo));

                int intHours;
                int intMinutes;
                if ((timeTo != null)&&(!timeTo.equals(""))) {
                    try {
                        intHours = ConvertDateAndTime.GetHourFromStringTime(timeTo);
                        intMinutes = ConvertDateAndTime.GetMinutesFromStringTime(timeTo);
                    }
                    catch(Exception e) {
                        intHours = ConvertDateAndTime.GetHourFromISOStringTime(timeTo);
                        intMinutes = ConvertDateAndTime.GetMinutesFromISOStringTime(timeTo);
                    }

                    curTaskDateTo.set(intYear, intMonth - 1, intDate, intHours, intMinutes, rightBorder.getTime().getSeconds());
                    task.SetTimeTo(intHours, intMinutes);
                } else {
                    intHours = rightBorder.getTime().getHours();
                    intMinutes = rightBorder.getTime().getMinutes();
                    curTaskDateTo.set(intYear, intMonth - 1, intDate, intHours, intMinutes, rightBorder.getTime().getSeconds());
                    curTaskDateTo.add(Calendar.SECOND, -1);
                }

                task.SetDateTo(intYear, intMonth, intDate);
            }



            boolean condition = false;
            if (curTaskDateFrom!=null) {
                condition = ((curTaskDateFrom.after(leftBorder))&&(curTaskDateFrom.before(rightBorder)));
            }
            if (curTaskDateTo!=null) {
                condition = condition||(((curTaskDateTo.after(leftBorder))&&(curTaskDateTo.before(rightBorder))));
            }


                if (condition) {

                    String comment = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Comment));
                    if (comment != null)
                        task.SetComment(comment);

                    String visibility = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Visibility));
                    if (visibility != null)
                        task.SetVisibility(visibility.equals("1") ? true : false);

                    String editable = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Editable));
                    if (editable != null)
                        task.SetEditable(editable.equals("1") ? true : false);

                    String completed = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_Complete));
                    if (completed != null)
                        task.SetCompleted(completed.equals("1")? true: false);

                    long id = userCursor.getLong(userCursor.getColumnIndex(DBHelper.COLUMN_ID));
                    String taskNumber = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_ServerTaskNumber));

                    TaskDBWrapper taskDBWrapper = new TaskDBWrapper(task);
                    taskDBWrapper.SetId(id);
                    taskDBWrapper.SetServerTaskNumber(taskNumber);

                    resList.add(taskDBWrapper);

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

            String dateFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateFrom));
            if ((dateFrom!=null)&&(!dateFrom.equals(""))) {
              /*  String [] dateVals = date.split("\\.");
                int intDate = Integer.parseInt(dateVals[0]);
                int intMonth = Integer.parseInt(dateVals[1]);
                int intYear = Integer.parseInt(dateVals[2]);*/
              int intYear;
              int intMonth;
              int intDate;
              try {
                  intYear = ConvertDateAndTime.GetYearFromStringDate(dateFrom);
                  intMonth = ConvertDateAndTime.GetMonthFromStringDate(dateFrom);
                  intDate = ConvertDateAndTime.GetDayFromStringDate(dateFrom);
              }
              catch (Exception e) {
                  intYear = ConvertDateAndTime.GetYearFromISOStringDate(dateFrom);
                  intMonth = ConvertDateAndTime.GetMonthFromISOStringDate(dateFrom);
                  intDate = ConvertDateAndTime.GetDayFromISOStringDate(dateFrom);
              }

                task.SetDateFrom(intYear, intMonth, intDate);
            }

            String timeFrom = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeFrom));
            if ((timeFrom!=null)&&(!timeFrom.equals(""))) {
               /* String[] timeVals = time.split("\\-");
                int intHours = Integer.parseInt(timeVals[0]);
                int intMinutes = Integer.parseInt(timeVals[1]);*/
               int intHours;
               int intMinutes;
                try {
                    intHours = ConvertDateAndTime.GetHourFromStringTime(timeFrom);
                    intMinutes = ConvertDateAndTime.GetMinutesFromStringTime(timeFrom);
                }
                catch(Exception e) {
                    intHours = ConvertDateAndTime.GetHourFromISOStringTime(timeFrom);
                    intMinutes = ConvertDateAndTime.GetMinutesFromISOStringTime(timeFrom);
                }
                task.SetTimeFrom(intHours, intMinutes);
            }

            String dateTo = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_DateTo));
            if ((dateTo!=null)&&(!dateTo.equals(""))) {
              /*  String [] dateVals = date.split("\\.");
                int intDate = Integer.parseInt(dateVals[0]);
                int intMonth = Integer.parseInt(dateVals[1]);
                int intYear = Integer.parseInt(dateVals[2]);*/
                int intYear;
                int intMonth;
                int intDate;
                try {
                    intYear = ConvertDateAndTime.GetYearFromStringDate(dateTo);
                    intMonth = ConvertDateAndTime.GetMonthFromStringDate(dateTo);
                    intDate = ConvertDateAndTime.GetDayFromStringDate(dateTo);
                }
                catch (Exception e) {
                    intYear = ConvertDateAndTime.GetYearFromISOStringDate(dateTo);
                    intMonth = ConvertDateAndTime.GetMonthFromISOStringDate(dateTo);
                    intDate = ConvertDateAndTime.GetDayFromISOStringDate(dateTo);
                }
                task.SetDateTo(intYear, intMonth, intDate);
            }

            String timeTo = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_TimeTo));
            if ((timeTo!=null)&&(!timeTo.equals(""))) {
               /* String[] timeVals = time.split("\\-");
                int intHours = Integer.parseInt(timeVals[0]);
                int intMinutes = Integer.parseInt(timeVals[1]);*/
                int intHours;
                int intMinutes;
                try {
                    intHours = ConvertDateAndTime.GetHourFromStringTime(timeTo);
                    intMinutes = ConvertDateAndTime.GetMinutesFromStringTime(timeTo);
                }
                catch(Exception e) {
                    intHours = ConvertDateAndTime.GetHourFromISOStringTime(timeTo);
                    intMinutes = ConvertDateAndTime.GetMinutesFromISOStringTime(timeTo);
                }
                task.SetTimeTo(intHours, intMinutes);
            }


           /* Double duration = userCursor.getDouble(userCursor.getColumnIndex(DBHelper.COLUMN_Duration));
            if (duration!=null)
                task.SetDuration(duration);*/

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
            String taskNumber = userCursor.getString(userCursor.getColumnIndex(DBHelper.COLUMN_ServerTaskNumber));

            TaskDBWrapper taskDBWrapper = new TaskDBWrapper(task);
            taskDBWrapper.SetId(id);
            taskDBWrapper.SetServerTaskNumber(taskNumber);

            resList.add(taskDBWrapper);

        }

        return resList;
    }

    private void UpdateContentValues(Task task) {

        contentValues.clear();

        contentValues.put(DBHelper.COLUMN_Name, task.GetName());
        contentValues.put(DBHelper.COLUMN_Comment, task.GetComment());
        contentValues.put(DBHelper.COLUMN_DateFrom, task.GetDateFrom());
        contentValues.put(DBHelper.COLUMN_TimeFrom, task.GetTimeFrom());

        contentValues.put(DBHelper.COLUMN_Visibility, task.GetVisibility());
        contentValues.put(DBHelper.COLUMN_Editable, task.GetEditable());
        contentValues.put(DBHelper.COLUMN_Complete, task.GetCompleted());
        contentValues.put(DBHelper.COLUMN_DateTo, task.GetDateTo());
        contentValues.put(DBHelper.COLUMN_TimeTo, task.GetTimeTo());
        contentValues.put(DBHelper.COLUMN_ServerTaskNumber, task.GetServerTaskNumber());
    }
}
