package com.example.lighthead.androidcustomcalendar.helpers.taskWrappers;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;

import java.util.Calendar;
import java.util.Comparator;

public class TaskComparable extends TaskDBWrapper {

    private Calendar DateTimeFrom = Calendar.getInstance();
    private Calendar DateTimeTo = Calendar.getInstance();

    public TaskComparable(TaskDBWrapper task) {
        super(task);

        this.SetId(task.GetId());
        this.SetServerTaskNumber(task.GetServerTaskNumber());


        String taskStrDateFrom = task.GetDateFrom();
        String taskStrTimeFrom = task.GetTimeFrom();

        if (!(taskStrDateFrom==null)) {

            int taskIntYear = ConvertDateAndTime.GetYearFromStringDate(taskStrDateFrom);
            int taskIntMonth = ConvertDateAndTime.GetMonthFromStringDate(taskStrDateFrom);
            int taskIntDate = ConvertDateAndTime.GetDayFromStringDate(taskStrDateFrom);


            int taskIntHours = 0;
            int taskIntMinutes = 0;

            if (!(taskStrTimeFrom==null)) {
                taskIntHours = ConvertDateAndTime.GetHourFromStringTime(taskStrTimeFrom);
                taskIntMinutes = ConvertDateAndTime.GetMinutesFromStringTime(taskStrTimeFrom);
            }


            SetDateTimeFrom(taskIntYear, taskIntMonth - 1, taskIntDate, taskIntHours, taskIntMinutes);
        }


        String taskStrDateTo = task.GetDateTo();
        String taskStrTimeTo = task.GetTimeTo();

        if (!(taskStrDateTo==null)) {

            int taskIntYear = ConvertDateAndTime.GetYearFromStringDate(taskStrDateTo);
            int taskIntMonth = ConvertDateAndTime.GetMonthFromStringDate(taskStrDateTo);
            int taskIntDate = ConvertDateAndTime.GetDayFromStringDate(taskStrDateTo);


            int taskIntHours = 0;
            int taskIntMinutes = 0;

            if (!(taskStrTimeTo==null)) {
                taskIntHours = ConvertDateAndTime.GetHourFromStringTime(taskStrTimeTo);
                taskIntMinutes = ConvertDateAndTime.GetMinutesFromStringTime(taskStrTimeTo);
            }


            SetDateTimeTo(taskIntYear, taskIntMonth - 1, taskIntDate, taskIntHours, taskIntMinutes);
        }

    }

    public void SetDateTimeFrom(int year, int month, int date, int hours, int minutes) {

        DateTimeFrom.set(year, month-1, date, hours, minutes);
    }

    public Calendar GetDateTimeFrom(){
        return DateTimeFrom;
    }


    public void SetDateTimeTo(int year, int month, int date, int hours, int minutes) {

        DateTimeTo.set(year, month-1, date, hours, minutes);
    }

    public Calendar GetDateTimeTo(){
        return DateTimeTo;
    }




    public static Comparator<TaskComparable> DateTimeFromComparator = new Comparator<TaskComparable>() {
        @Override
        public int compare(TaskComparable o1, TaskComparable o2) {
            Calendar calendarTask1 = o1.GetDateTimeFrom();
            Calendar calendarTask2 = o2.GetDateTimeFrom();
            return calendarTask1.compareTo(calendarTask2);
        }
    };

    public static Comparator<TaskComparable> DateTimeToComparator = new Comparator<TaskComparable>() {
        @Override
        public int compare(TaskComparable o1, TaskComparable o2) {
            Calendar calendarTask1 = o1.GetDateTimeTo();
            Calendar calendarTask2 = o2.GetDateTimeTo();
            return calendarTask1.compareTo(calendarTask2);
        }
    };



    public static Comparator<TaskComparable> CompletedComparator = new Comparator<TaskComparable>() {
        @Override
        public int compare(TaskComparable o1, TaskComparable o2) {
            boolean completed1 = o1.GetCompleted();
            boolean completed2 = o2.GetCompleted();

            int intCompleted1 = completed1?1:0;
            int intCompleted2 = completed2?1:0;

            return intCompleted1-intCompleted2;
        }
    };



}
