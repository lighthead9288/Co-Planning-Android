package com.example.lighthead.androidcustomcalendar.interfaces;

import android.view.View;

import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.adapters.TaskAdapter;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.helpers.communication.RetrofitClient;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskComparable;
import com.example.lighthead.androidcustomcalendar.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public interface ITaskListOperations {

    void OnGetTasks(ArrayList<ServerTask> tasksFromServer);
    void OnDeleteTasks();


}
