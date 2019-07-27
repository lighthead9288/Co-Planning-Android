package com.example.lighthead.androidcustomcalendar.models;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;

/**
 * Created by u0227 on 31.07.2018.
 */
public class Task /*<TNotifyMethod>*/
{
    private String name;

    //_______________________________________________

    private String comment;

    protected String dateFrom;

    protected String timeFrom;

    protected String dateTo;

    protected String timeTo;

    // private StandartNotify Notify;

    private boolean visibility;

    private boolean editable;

    private boolean completed;

    private String taskNumber;

    public String GetName()
    {
        return name;
    }

    public String GetComment()
    {
        return comment;
    }

    public String GetDateFrom()
    {
        return dateFrom;
    }

    public String GetTimeFrom()
    {
        return timeFrom;
    }

    public String GetDateTo() {return dateTo;}

    public String GetTimeTo() {return timeTo;}


    public Boolean GetVisibility()
    {
        return visibility;
    }

    public Boolean GetEditable()
    {
        return editable;
    }

    public Boolean GetCompleted() {return completed;}
	
	public String GetServerTaskNumber() {return taskNumber;}

    public void SetName(String inpName)
    {
        name = inpName;
    }

    public void SetComment(String inpComment)
    {
        comment = inpComment;
    }

    public void SetDateFrom(int year, int month, int date)
    {
        dateFrom = ConvertDateAndTime.ConvertToStringDate(year, month, date);
    }

    public void SetTimeFrom(int hours, int minutes)
    {
        timeFrom = ConvertDateAndTime.ConvertToStringTime(hours, minutes);
    }

    public void SetDateTo(int year, int month, int date)
    {
        dateTo = ConvertDateAndTime.ConvertToStringDate(year, month, date);
    }

    public void SetTimeTo(int hours, int minutes)
    {
        timeTo = ConvertDateAndTime.ConvertToStringTime(hours, minutes);
    }


    public void SetVisibility(boolean inpVisibility)
    {
        visibility = inpVisibility;
    }

    public void SetEditable(boolean inpEditable)
    {
        editable = inpEditable;
    }

    public void SetCompleted(boolean inpCompleted) { completed = inpCompleted;}

    public void SetServerTaskNumber(String serverTaskNumber) {
        taskNumber = serverTaskNumber;
    }

    


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






