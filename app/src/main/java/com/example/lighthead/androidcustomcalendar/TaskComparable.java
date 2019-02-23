package com.example.lighthead.androidcustomcalendar;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class TaskComparable extends TaskDBWrapper {

    private Calendar DateTime = Calendar.getInstance();

    public TaskComparable(TaskDBWrapper task) {
        super(task);

        this.SetId(task.GetId());


        String taskStrDate = task.GetDate();
        String taskStrTime = task.GetTime();

        if (!(taskStrDate==null)) {

            int taskIntYear = ConvertDateAndTime.GetYearFromStringDate(taskStrDate);
            int taskIntMonth = ConvertDateAndTime.GetMonthFromStringDate(taskStrDate);
            int taskIntDate = ConvertDateAndTime.GetDayFromStringDate(taskStrDate);


            int taskIntHours = 0;
            int taskIntMinutes = 0;

            if (!(taskStrTime==null)) {
                taskIntHours = ConvertDateAndTime.GetHourFromStringTime(taskStrTime);
                taskIntMinutes = ConvertDateAndTime.GetMinutesFromStringTime(taskStrTime);
            }


            SetDateTime(taskIntYear, taskIntMonth - 1, taskIntDate, taskIntHours, taskIntMinutes);
        }

    }

    public void SetDateTime(int year, int month, int date, int hours, int minutes) {

        DateTime.set(year, month-1, date, hours, minutes);
    }

    public Calendar GetDateTime(){
        return DateTime;
    }




    public static Comparator<TaskComparable> DateTimeComparator = new Comparator<TaskComparable>() {
        @Override
        public int compare(TaskComparable o1, TaskComparable o2) {
            Calendar calendarTask1 = o1.GetDateTime();
            Calendar calendarTask2 = o2.GetDateTime();
            return calendarTask1.compareTo(calendarTask2);
        }
    };


    public static Comparator<TaskComparable> DurationComparator = new Comparator<TaskComparable>() {
        @Override
        public int compare(TaskComparable o1, TaskComparable o2) {
            double duration1 = o1.GetDuration();
            double duration2 = o2.GetDuration();
            return duration1>duration2?1:-1;
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
