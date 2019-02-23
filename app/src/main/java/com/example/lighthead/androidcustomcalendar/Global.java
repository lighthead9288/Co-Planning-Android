package com.example.lighthead.androidcustomcalendar;

import android.app.Application;

public class Global extends Application {
    private String curSchedFragment;

    public String GetCurSchedFragment(){
        return curSchedFragment;
    }

    public void SetCurSchedFragment(String fragment)
    {
        curSchedFragment = fragment;
    }

}
