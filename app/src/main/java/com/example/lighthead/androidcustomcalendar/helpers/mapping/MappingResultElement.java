package com.example.lighthead.androidcustomcalendar.helpers.mapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class MappingResultElement {

    private ArrayList<String> Users;
    private Calendar DateTimeFrom;
    private Calendar DateTimeTo;
    private int Amount = 0;

    public void SetUserList(ArrayList<String> users) {
        Users = users;
    }

    public void SetDateTimeFrom(Calendar dateTimeFrom) {
        DateTimeFrom = dateTimeFrom;
    }

    public void SetDateTimeTo(Calendar dateTimeTo) {
        DateTimeTo = dateTimeTo;
    }

    public void SetAmount(int amount) {
        if (amount>0)
            Amount = amount;
    }

    public ArrayList<String>  GetUserList() {
        return Users;
    }

    public Calendar GetDateTimeFrom() {
        return DateTimeFrom;
    }

    public Calendar GetDateTimeTo() {
        return DateTimeTo;
    }

    public int GetAmount() {
        return Amount;
    }


    public static Comparator<MappingResultElement> DateTimeFromComparator = new Comparator<MappingResultElement>() {
        @Override
        public int compare(MappingResultElement o1, MappingResultElement o2) {
            Calendar calendarTask1 = o1.GetDateTimeFrom();
            Calendar calendarTask2 = o2.GetDateTimeFrom();
            return calendarTask1.compareTo(calendarTask2);
        }
    };

    public static Comparator<MappingResultElement> DateTimeToComparator = new Comparator<MappingResultElement>() {
        @Override
        public int compare(MappingResultElement o1, MappingResultElement o2) {
            Calendar calendarTask1 = o1.GetDateTimeTo();
            Calendar calendarTask2 = o2.GetDateTimeTo();
            return calendarTask1.compareTo(calendarTask2);
        }
    };
}
