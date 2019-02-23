package com.example.lighthead.androidcustomcalendar;

public class TaskDBWrapper extends Task {

    private long Id;


    public TaskDBWrapper(String name) {
        super(name);
    }

    public TaskDBWrapper(Task task) {
        super(task.GetName());
        SetComment(task.GetComment());

        String strDate = task.GetDate();
        if (strDate!=null)
            SetDate(ConvertDateAndTime.GetYearFromStringDate(strDate), ConvertDateAndTime.GetMonthFromStringDate(strDate), ConvertDateAndTime.GetDayFromStringDate(strDate));

        String strTime = task.GetTime();
        if (strTime!=null)
            SetTime(ConvertDateAndTime.GetHourFromStringTime(strTime), ConvertDateAndTime.GetMinutesFromStringTime(strTime));

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
