package com.example.lighthead.androidcustomcalendar.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public interface ITaskListFragment {

    void UpdateTaskList(Calendar dateFrom, Calendar dateTo);
    void InitButtons();
    void SetTaskListView();
    View GetTaskListView(LayoutInflater inflater, ViewGroup container);

}
