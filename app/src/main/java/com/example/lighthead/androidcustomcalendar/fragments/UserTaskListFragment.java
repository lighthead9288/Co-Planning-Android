package com.example.lighthead.androidcustomcalendar.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.helpers.communication.ServerTaskManager;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskComparable;
import com.example.lighthead.androidcustomcalendar.interfaces.ITaskListFragment;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.adapters.SelectedUserTaskAdapter;
import com.example.lighthead.androidcustomcalendar.interfaces.ITaskListOperations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class UserTaskListFragment extends TaskListFragment implements ITaskListFragment {

    public UserTaskListFragment() {

        super();

        ITaskListOperations iTaskListOperations = new ITaskListOperations() {
            @Override
            public void OnGetTasks(ArrayList<ServerTask> tasksFromServer) {
                ShowTasks(tasksFromServer);

            }

            @Override
            public void OnDeleteTasks() { }
        };

        serverTaskManager = new ServerTaskManager(iTaskListOperations);
    }

    public void SetUsername(String username) {
        this.username = username;
    }

    public String GetUsername() {
        return this.username;
    }


    public SelectedUserTaskAdapter selectedUserTaskAdapter;


    @Override
    public View GetTaskListView(LayoutInflater inflater, ViewGroup container) {
        View resView = inflater.inflate(R.layout.fragment_usertasklist, container, false);
        return resView;
    }


    @Override
    public void ShowTasks(List<ServerTask> tasksFromServer) {
        SetTaskListView();

        selectedUserTaskAdapter = new SelectedUserTaskAdapter(getContext(), R.layout.singlefoundedusertasklistlayout, tasksFromServer, username);
        taskList.setAdapter(selectedUserTaskAdapter);
    }

    @Override
    public void SetTaskListView() {
        taskList = view.findViewById(R.id.taskList);
    }


    @Override
    public void onPause(){
        super.onPause();
        global.SetCurSearchFragment(this);
    }



    @Override
    public void InitButtons(){

        addTaskButton.setVisibility(View.GONE);
        editTaskButton.setVisibility(View.GONE);
        deleteTaskButton.setVisibility(View.GONE);

    }


}
