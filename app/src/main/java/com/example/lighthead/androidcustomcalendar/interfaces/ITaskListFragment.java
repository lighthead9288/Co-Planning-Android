package com.example.lighthead.androidcustomcalendar.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;

import java.util.Calendar;
import java.util.List;

public interface ITaskListFragment {

    //void UpdateTaskList(Calendar dateFrom, Calendar dateTo, String taskFilter);
    String GetUsername();
    void ShowTasks(List<ServerTask> tasksFromServer);
    void InitButtons();
    void SetTaskListView();
    View GetTaskListView(LayoutInflater inflater, ViewGroup container);

}
