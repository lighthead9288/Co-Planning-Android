package com.example.lighthead.androidcustomcalendar;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

public class SharedPreferencesOperations extends Application {

    private static SharedPreferences.Editor shPrefEditor;
    private static SharedPreferences sPref;

    private static Activity activity;


    public void SetActivity(Activity inpActivity) {
        activity = inpActivity;
    }

    public String GetLogin() {
        sPref = activity.getPreferences(MODE_PRIVATE);
        String result = sPref.getString("login", "");
        return result;
    }


    public String GetPassword() {
        sPref = activity.getPreferences(MODE_PRIVATE);
        String result = sPref.getString("password", "");
        return result;
    }


    public void SetSharedPreferences(String login, String password) {
        sPref = activity.getPreferences(MODE_PRIVATE);
        shPrefEditor = sPref.edit();
        shPrefEditor.putString("login", login);
        shPrefEditor.putString("password", password);
        shPrefEditor.commit();
    }


    public void ClearSharedPreferences() {
        sPref = activity.getPreferences(MODE_PRIVATE);
        shPrefEditor = sPref.edit();
        shPrefEditor.clear();
        shPrefEditor.commit();
    }
}
