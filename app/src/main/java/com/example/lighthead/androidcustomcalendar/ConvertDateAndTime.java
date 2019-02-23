package com.example.lighthead.androidcustomcalendar;

public class ConvertDateAndTime {

    public static String ConvertToStringDate(int year, int month, int date) {
        String result = String.valueOf(date) + "." + String.valueOf(month) + "." +  String.valueOf(year);
        return result;
    }

    public static String ConvertToStringTime(int hours, int minutes) {
        String result = String.valueOf(hours) + "-"+  String.valueOf(minutes);
        return result;
    }




    private static String [] GetDateValuesFromStringDate(String date) {
        String [] dateVals = date.split("\\.");

        return dateVals;
    }

    public static int GetYearFromStringDate(String date) {
        String [] dateVals = GetDateValuesFromStringDate(date);
        int intYear = Integer.parseInt(dateVals[2]);
        return intYear;
    }

    public static int GetMonthFromStringDate(String date) {
        String [] dateVals = GetDateValuesFromStringDate(date);
        int intMonth = Integer.parseInt(dateVals[1]);
        return intMonth;
    }

    public static int GetDayFromStringDate(String date) {
        String [] dateVals = GetDateValuesFromStringDate(date);
        int intDate = Integer.parseInt(dateVals[0]);
        return intDate;
    }



    private static String [] GetTimeValuesFromStringTime(String time) {
        String [] timeVals = time.split("\\-");

        return timeVals;
    }

    public static int GetHourFromStringTime(String time) {
        String [] timeVals = GetTimeValuesFromStringTime(time);
        int intHours = Integer.parseInt(timeVals[0]);
        return intHours;
    }

    public static int GetMinutesFromStringTime(String time) {
        String [] timeVals = GetTimeValuesFromStringTime(time);
        int intMinutes = Integer.parseInt(timeVals[1]);
        return intMinutes;
    }




}
