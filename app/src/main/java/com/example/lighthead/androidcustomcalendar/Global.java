package com.example.lighthead.androidcustomcalendar;

import android.app.Application;
import android.support.v4.app.Fragment;

public class Global extends Application {
    private static Fragment curSchedFragment;

    public Fragment GetCurSchedFragment(){

        return curSchedFragment;
    }
    public void SetCurSchedFragment(Fragment fragment)
    {
        curSchedFragment = fragment;
    }

    public Global(){

    }

}
