package com.example.lighthead.androidcustomcalendar.helpers.communication;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.helpers.CurrentUserTask;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.interfaces.ICoPlanningAPI;
import com.example.lighthead.androidcustomcalendar.interfaces.ITaskListOperations;
import com.example.lighthead.androidcustomcalendar.models.User;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServerTaskManager {

    private ITaskListOperations iTaskListOperations;

    private Call<User> getUserTaskListCall;
    private Call<String> addTaskCall;
    private Call<String> deleteTaskCall;
    private Call<String> editTaskCall;

    private ICoPlanningAPI client;


    public ServerTaskManager(ITaskListOperations ops) {
        iTaskListOperations = ops;
        ClientInit();
    }

    private void ClientInit() {
        RetrofitClient rClient = new RetrofitClient();
        Retrofit retrofit = rClient.GetRetrofitEntity();

        client = retrofit.create(ICoPlanningAPI.class);

    }

    public void GetTasksFromServer(String user, Calendar dateTimeFrom, Calendar dateTimeTo) {

        String dateFrom = ConvertDateAndTime.GetISOStringDateFromCalendar(dateTimeFrom);
        String timeFrom = ConvertDateAndTime.GetISOStringTimeFromCalendar(dateTimeFrom);
        String dateTo = ConvertDateAndTime.GetISOStringDateFromCalendar(dateTimeTo);
        String timeTo = ConvertDateAndTime.GetISOStringTimeFromCalendar(dateTimeTo);

        getUserTaskListCall = client.getUserTaskList(user, dateFrom, timeFrom, dateTo, timeTo);

        getUserTaskListCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                ArrayList<ServerTask> tasksFromServer = user.GetTaskList();

                if (iTaskListOperations!=null)
                    iTaskListOperations.OnGetTasks(tasksFromServer);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }


    public void AddTask(String username, ServerTask task) {
        CurrentUserTask curUserTask = new CurrentUserTask();
        curUserTask.SetUsername(username);
        curUserTask.SetServerTask(task);

        addTaskCall = client.addTask(curUserTask);

        addTaskCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void DeleteTask(String username, int taskId, final Calendar dateTimeFrom, final Calendar dateTimeTo) {
        deleteTaskCall = client.deleteTask(username, taskId);

        deleteTaskCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    public void UpdateTask(String username, ServerTask task, long taskId) {
        CurrentUserTask curUserTask = new CurrentUserTask();
        curUserTask.SetUsername(username);
        curUserTask.SetServerTask(task);
        curUserTask.SetTaskId(taskId);

        editTaskCall = client.editTask(curUserTask);

        editTaskCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }






}
