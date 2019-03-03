package com.example.lighthead.androidcustomcalendar;

public class TaskDBWrapper extends Task {

    private long Id;


    public TaskDBWrapper(String name) {
        super(name);
    }

    public TaskDBWrapper(Task task) {
        super(task.GetName());
        SetComment(task.GetComment());

        String strDateFrom = task.GetDateFrom();
        if (strDateFrom!=null)
            SetDateFrom(ConvertDateAndTime.GetYearFromStringDate(strDateFrom), ConvertDateAndTime.GetMonthFromStringDate(strDateFrom), ConvertDateAndTime.GetDayFromStringDate(strDateFrom));

        String strTimeFrom = task.GetTimeFrom();
        if (strTimeFrom!=null)
            SetTimeFrom(ConvertDateAndTime.GetHourFromStringTime(strTimeFrom), ConvertDateAndTime.GetMinutesFromStringTime(strTimeFrom));

        String strDateTo = task.GetDateTo();
        if (strDateTo!=null)
            SetDateTo(ConvertDateAndTime.GetYearFromStringDate(strDateTo), ConvertDateAndTime.GetMonthFromStringDate(strDateTo), ConvertDateAndTime.GetDayFromStringDate(strDateTo));

        String strTimeTo = task.GetTimeTo();
        if (strTimeTo!=null)
            SetTimeTo(ConvertDateAndTime.GetHourFromStringTime(strTimeTo), ConvertDateAndTime.GetMinutesFromStringTime(strTimeTo));

        SetDuration(task.GetDuration());

        SetVisibility(task.GetVisibility());

        SetEditable(task.GetEditable());

        SetCompleted(task.GetCompleted());

    }

    public void SetId(long id) {

        Id = id;
    }

    public long GetId() {
        return Id;
    }
}
