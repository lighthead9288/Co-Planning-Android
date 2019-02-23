package com.example.lighthead.androidcustomcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by u0227 on 31.07.2018.
 */
public class Task /*<TNotifyMethod>*/
{
    private String Name;

    //_______________________________________________

    private String Comment;

    private String Date;

    private String Time;

    private double Duration;

    // private StandartNotify Notify;

    private boolean Visible;

    private boolean Editable;

    private boolean Completed;

    public String GetName()
    {
        return Name;
    }

    public String GetComment()
    {
        return Comment;
    }

    public String GetDate()
    {
        return Date;
    }

    public String GetTime()
    {
        return Time;
    }

    public double GetDuration()
    {
        return Duration;
    }


    public Boolean GetVisibility()
    {
        return Visible;
    }

    public Boolean GetEditable()
    {
        return Editable;
    }

    public Boolean GetCompleted() {return Completed;}

    public void SetName(String name)
    {
        Name = name;
    }

    public void SetComment(String comment)
    {
        Comment = comment;
    }

    public void SetDate(int year, int month, int date)
    {
        Date = ConvertDateAndTime.ConvertToStringDate(year, month, date);
    }

    public void SetTime(int hours, int minutes)
    {
        Time = ConvertDateAndTime.ConvertToStringTime(hours, minutes);
    }

    public void SetDuration(double duration)
    {
        Duration = duration;
    }


    public void SetVisibility(boolean visibility)
    {
        Visible = visibility;
    }

    public void SetEditable(boolean editable)
    {
        Editable = editable;
    }

    public void SetCompleted(boolean completed) { Completed = completed;}




    public Task(String name)
    {
        SetName(name);
    }



  /*  public static class TaskBuilder
    {
        private String Name;

        //_______________________________________________

        private String Comment;

        private Date Date;

        private Timer Time;

        private double Duration = 0;



        private boolean Visible = true;

        private boolean Editable = false;
        private DBHelper dbHelper;
        private SQLiteDatabase db;
        private ContentValues contentValues;




        public TaskBuilder(String name, Context context)
        {
            dbHelper = new DBHelper(context);
            db = dbHelper.getReadableDatabase();

            Name = name;
            contentValues.put(DBHelper.COLUMN_Name, name);
        }

        public TaskBuilder SetComment(String comment)
        {
            Comment = comment;
            contentValues.put(DBHelper.COLUMN_Comment, comment);
            return this;
        }

        public TaskBuilder SetDate(Date date)
        {
            Date = date;
            contentValues.put(DBHelper.COLUMN_Date, date.toString());
            return this;
        }

        public TaskBuilder SetTime(Timer time)
        {
            Time = time;
            contentValues.put(DBHelper.COLUMN_Time, time.toString());
            return this;
        }

        public TaskBuilder SetDuration(double val)
        {
            Duration = val;
            contentValues.put(DBHelper.COLUMN_Duration, val);
            return this;
        }


        public TaskBuilder SetVisibility(boolean val)
        {
            Visible = val;
            contentValues.put(DBHelper.COLUMN_Visibility, val);
            return this;
        }

        public TaskBuilder SetEditable(boolean val)
        {
            Editable = val;
            contentValues.put(DBHelper.COLUMN_Editable, val);
            return this;
        }

        public Task Build()
        {
            return new Task(this);
        }


    }*/



}


/*
!!!!!!!!!!!!!!!!!!!!!!!!!!USAGE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
* Task task = new Task.TaskBuilder("Task 1","Describing of Task 1").Build();
        System.out.println(task.GetName() + "\n"+ task.GetComment() + "\n" + task.GetDuration());

        Task task2 = new Task.TaskBuilder("Task 2", "Describing of task 2").SetPriority(TaskPriorities.High).SetVisibility(false).Build();
        System.out.println(task2.GetName() + "\n" + task2.GetComment() + "\n" + task2.GetDuration()
                + "\n" + task2.GetPriority() + "\n" + task2.GetVisibility());


        task2 = new Task.TaskBuilder("Task 2", "Describing of task 2").SetPriority(TaskPriorities.Low).Build();

        System.out.println(task2.GetName() + "\n" + task2.GetComment() + "\n" + task2.GetDuration()
                + "\n" + task2.GetPriority() + "\n" + task2.GetVisibility());
* */






