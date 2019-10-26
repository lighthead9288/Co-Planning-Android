package com.example.lighthead.androidcustomcalendar.helpers;


public class Notification {

    private String DateTime;

    private String Sender;

    private String Description;

    private boolean Status;

    private NotificationChanges AdditionalInfo;

    public String GetDateTime() {
        return DateTime;
    }

    public String GetSender() {
        return Sender;
    }

    public String GetDescription() {
        return Description;
    }

    public boolean GetStatus() {
        return Status;
    }

    public String GetNameChanges() {
        return AdditionalInfo.GetName();
    }

    public String GetCommentChanges() {
        return AdditionalInfo.GetComment();
    }

    public String GetDateTimeFromChanges() {
        return AdditionalInfo.GetDateTimeFrom();
    }

    public String GetDateTimeToChanges() {
        return AdditionalInfo.GetDateTimeTo();
    }



    public Notification(String dateTime, String sender, String description, boolean status, NotificationChanges additionalInfo) {
        DateTime = dateTime;
        Sender = sender;
        Description = description;
        Status = status;
        AdditionalInfo = additionalInfo;
    }


}
