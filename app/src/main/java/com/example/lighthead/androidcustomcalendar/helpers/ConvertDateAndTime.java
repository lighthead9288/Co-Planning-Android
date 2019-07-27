package com.example.lighthead.androidcustomcalendar.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ConvertDateAndTime {

    public static String ConvertToStringDate(int year, int month, int date) {
        String strDate = String.valueOf(date);
        strDate = (strDate.length()>1)?strDate:"0"+strDate;
        String strMonth = String.valueOf(month);
        strMonth = (strMonth.length()>1)?strMonth:"0"+strMonth;
        String strYear = String.valueOf(year);

        String result = strDate + "." + strMonth + "." + strYear;
        return result;
    }

    public static String ConvertToStringTime(int hours, int minutes) {
        String strHours = String.valueOf(hours);
        strHours = (strHours.length()>1)?strHours:"0"+strHours;
        String strMinutes = String.valueOf(minutes);
        strMinutes = (strMinutes.length()>1)?strMinutes:"0"+strMinutes;


        String result = strHours + "-"+  strMinutes;
        return result;
    }

    public static String ConvertToISOStringDate(int year, int month, int date) {
        String strDate = String.valueOf(date);
        strDate = (strDate.length()>1)?strDate:"0"+strDate;
        String strMonth = String.valueOf(month);
        strMonth = (strMonth.length()>1)?strMonth:"0"+strMonth;
        String strYear = String.valueOf(year);

        String result = strYear + "-" + strMonth + "-" + strDate;
        return result;
    }

    public static String ConvertToISOStringTime(int hours, int minutes) {
        String strHours = String.valueOf(hours);
        strHours = (strHours.length()>1)?strHours:"0"+strHours;
        String strMinutes = String.valueOf(minutes);
        strMinutes = (strMinutes.length()>1)?strMinutes:"0"+strMinutes;


        String result = strHours + ":"+  strMinutes;
        return result;
    }
	
	
	public static String ConvertStringDateToISO(String date) {
		String [] dateVals = GetDateValuesFromStringDate(date);
		int intYear = Integer.parseInt(dateVals[2]);
		int intMonth = Integer.parseInt(dateVals[1]);
		int intDate = Integer.parseInt(dateVals[0]);
		
		String isoDate = ConvertToISOStringDate(intYear, intMonth, intDate);
		
		return isoDate;
	}
	
	public static String ConvertStringTimeToISO(String time) {
		String [] timeVals = time.split("\\-");
		
		int intHours = Integer.parseInt(timeVals[0]);
		int intMinutes = Integer.parseInt(timeVals[1]);
		
		String isoTime = ConvertToISOStringTime(intHours, intMinutes);

		return isoTime;
	}	

	
	private static String[] GetValuesFromISODateTimeString(String dateTime) {
		String [] vals = dateTime.split("\\'T'");
		return vals;
	}

    private static String [] GetDateValuesFromStringDate(String date) {
        String [] dateVals = date.split("\\.");

        return dateVals;
    }
	
	private static String[] GetDateValuesFromISOStringDate(String date) {
		String [] dateVals = date.split("\\-");

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
	
	public static int GetYearFromISOStringDate(String date) {
        String [] dateVals = GetDateValuesFromISOStringDate(date);
        int intYear = Integer.parseInt(dateVals[0]);
        return intYear;
    }

    public static int GetMonthFromISOStringDate(String date) {
        String [] dateVals = GetDateValuesFromISOStringDate(date);
        int intMonth = Integer.parseInt(dateVals[1]);
        return intMonth;
    }

    public static int GetDayFromISOStringDate(String date) {
        String [] dateVals = GetDateValuesFromISOStringDate(date);
        int intDate = Integer.parseInt(dateVals[2]);
        return intDate;
    }



    private static String [] GetTimeValuesFromStringTime(String time) {
        String [] timeVals = time.split("\\-");

        return timeVals;
    }

    private static String [] GetTimeValuesFromISOStringTime(String time) {
        String [] timeVals = time.split("\\:");

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

    public static int GetHourFromISOStringTime(String time) {
        String [] timeVals = GetTimeValuesFromISOStringTime(time);
        int intHours = Integer.parseInt(timeVals[0]);
        return intHours;
    }

    public static int GetMinutesFromISOStringTime(String time) {
        String [] timeVals = GetTimeValuesFromISOStringTime(time);
        int intMinutes = Integer.parseInt(timeVals[1]);
        return intMinutes;
    }


    public static Calendar GetCalendarDateTimeFromISOString(String isoDateTime) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dateFrom = null;
        try {
            dateFrom = df.parse(isoDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(dateFrom);
        return cal;

    }

    public static String GetStringDateFromCalendar(Calendar calendar) {
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int dispMonth = month+1;

        String result = ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date);

        return result;
    }

    public static String GetStringTimeFromCalendar(Calendar calendar) {
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        String result = ConvertDateAndTime.ConvertToStringTime(hours, minutes);

        return result;
    }




}
