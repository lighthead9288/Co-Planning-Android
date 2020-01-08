package com.example.lighthead.androidcustomcalendar.models;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;

/**
 * Created by u0227 on 31.07.2018.
 */
public class Task /*<TNotifyMethod>*/ {
    private String name;

    //_______________________________________________

    private String comment;

    protected String dateFrom;

    protected String timeFrom;

    protected String dateTo;

    protected String timeTo;

    private boolean visibility;

    private boolean editable;

    private boolean completed;

    public String GetName() {
        return name;
    }

    public String GetComment() {
        return comment;
    }

    public String GetDateFrom() {
        return dateFrom;
    }

    public String GetTimeFrom() {
        return timeFrom;
    }

    public String GetDateTo() {
        return dateTo;
    }

    public String GetTimeTo() {
        return timeTo;
    }


    public Boolean GetVisibility() {
        return visibility;
    }

    public Boolean GetEditable() {
        return editable;
    }

    public Boolean GetCompleted() {
        return completed;
    }

    public void SetName(String inpName) {
        name = inpName;
    }

    public void SetComment(String inpComment) {
        comment = inpComment;
    }

    public void SetDateFrom(int year, int month, int date) {
        dateFrom = ConvertDateAndTime.ConvertToISOStringDate(year, month, date);
    }

    public void SetTimeFrom(int hours, int minutes) {
        timeFrom = ConvertDateAndTime.ConvertToISOStringTime(hours, minutes);
    }

    public void SetDateTo(int year, int month, int date) {
        dateTo = ConvertDateAndTime.ConvertToISOStringDate(year, month, date);
    }

    public void SetTimeTo(int hours, int minutes) {
        timeTo = ConvertDateAndTime.ConvertToISOStringTime(hours, minutes);
    }


    public void SetVisibility(boolean inpVisibility) {
        visibility = inpVisibility;
    }

    public void SetEditable(boolean inpEditable) {
        editable = inpEditable;
    }

    public void SetCompleted(boolean inpCompleted) {
        completed = inpCompleted;
    }


    public Task(String name) {
        SetName(name);
    }

    public Task(Task task) {
        SetName(task.GetName());
        SetComment(task.GetComment());

        String strDateFrom = task.GetDateFrom();
        if ((strDateFrom != null) && (strDateFrom != ""))
            SetDateFrom(ConvertDateAndTime.GetYearFromISOStringDate(strDateFrom), ConvertDateAndTime.GetMonthFromISOStringDate(strDateFrom), ConvertDateAndTime.GetDayFromISOStringDate(strDateFrom));

        String strTimeFrom = task.GetTimeFrom();
        if ((strTimeFrom != null) && (strTimeFrom != ""))
            SetTimeFrom(ConvertDateAndTime.GetHourFromISOStringTime(strTimeFrom), ConvertDateAndTime.GetMinutesFromISOStringTime(strTimeFrom));

        String strDateTo = task.GetDateTo();
        if ((strDateTo != null) && (strDateTo != ""))
            SetDateTo(ConvertDateAndTime.GetYearFromISOStringDate(strDateTo), ConvertDateAndTime.GetMonthFromISOStringDate(strDateTo), ConvertDateAndTime.GetDayFromISOStringDate(strDateTo));

        String strTimeTo = task.GetTimeTo();
        if ((strTimeTo != null) && (strTimeTo != ""))
            SetTimeTo(ConvertDateAndTime.GetHourFromISOStringTime(strTimeTo), ConvertDateAndTime.GetMinutesFromISOStringTime(strTimeTo));


        SetVisibility(task.GetVisibility());

        SetEditable(task.GetEditable());

        SetCompleted(task.GetCompleted());


    }

}



