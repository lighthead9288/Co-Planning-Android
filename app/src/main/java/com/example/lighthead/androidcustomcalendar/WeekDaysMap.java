package com.example.lighthead.androidcustomcalendar;

import java.util.HashMap;
import java.util.Map;

public class WeekDaysMap {

    private static Map weekDaysMap;

    public static Map createMap() {
        weekDaysMap = new HashMap<Integer, Integer>();
        weekDaysMap.put(0, R.string.sun);
        weekDaysMap.put(1, R.string.mon);
        weekDaysMap.put(2, R.string.tue);
        weekDaysMap.put(3, R.string.wed);
        weekDaysMap.put(4, R.string.thu);
        weekDaysMap.put(5, R.string.fri);
        weekDaysMap.put(6, R.string.sat);

        return weekDaysMap;
    }


}
