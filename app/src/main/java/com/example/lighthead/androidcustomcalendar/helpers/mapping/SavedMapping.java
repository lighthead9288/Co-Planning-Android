package com.example.lighthead.androidcustomcalendar.helpers.mapping;

import java.util.ArrayList;
import java.util.Calendar;

public class SavedMapping {

    private Calendar DateTimeFrom;
    private Calendar DateTimeTo;
    private ArrayList<String> Participants;

    public Calendar GetDateTimeFrom() {
        return DateTimeFrom;
    }

    public Calendar GetDateTimeTo() {
        return DateTimeTo;
    }

    public ArrayList<String> GetParticipants() {
        return Participants;
    }

    public SavedMapping(Calendar dateTimeFrom, Calendar dateTimeTo, ArrayList<String> participants) {
        DateTimeFrom = dateTimeFrom;
        DateTimeTo = dateTimeTo;
        Participants = participants;
    }
}
