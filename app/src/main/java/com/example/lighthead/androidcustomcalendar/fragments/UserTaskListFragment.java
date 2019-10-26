package com.example.lighthead.androidcustomcalendar.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.interfaces.ITaskListFragment;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.adapters.SelectedUserTaskAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UserTaskListFragment extends TaskListFragment implements ITaskListFragment {

    public UserTaskListFragment() {
        super();
    }

    private ArrayList<ServerTask> Tasks;
    private String Username;

    public void SetUsername(String username) {
        Username = username;
    }

    public void SetTasks(ArrayList<ServerTask> tasks) {
        Tasks = tasks;
    }

    public SelectedUserTaskAdapter selectedUserTaskAdapter;

    @Override
    public View GetTaskListView(LayoutInflater inflater, ViewGroup container) {
        View resView = inflater.inflate(R.layout.fragment_usertasklist, container, false);
        return resView;
    }

    @Override
    public void UpdateTaskList(Calendar dateFrom, Calendar dateTo) {
        Calendar dateFromClone = (Calendar) dateFrom.clone();
        dateFromClone.set(Calendar.HOUR_OF_DAY, 0);
        dateFromClone.set(Calendar.MINUTE, 0);
        dateFromClone.set(Calendar.SECOND, 0);

        Calendar dateToClone = (Calendar) dateTo.clone();
        dateToClone.set(Calendar.HOUR_OF_DAY, 23);
        dateToClone.set(Calendar.MINUTE, 59);
        dateToClone.set(Calendar.SECOND, 59);

        ArrayList<ServerTask> list = FilterTasks(Tasks, dateFromClone, dateToClone);

        SetTaskListView();

        selectedUserTaskAdapter = new SelectedUserTaskAdapter(getContext(), R.layout.singlefoundedusertasklistlayout, list, Username);
        taskList.setAdapter(selectedUserTaskAdapter);

    }

    @Override
    public void SetTaskListView() {
        taskList = view.findViewById(R.id.taskList);
    }

    private ArrayList<ServerTask> FilterTasks(ArrayList<ServerTask> tasks, Calendar dateTimeFrom, Calendar dateTimeTo) {
        ArrayList<ServerTask> resultList = new ArrayList<>();

        for (ServerTask task:tasks
        ) {

            String dateFrom = task.GetDateFrom();
            String timeFrom = task.GetTimeFrom();
            Calendar curDateTimeFrom = new GregorianCalendar();

            if (dateFrom.equals("")||timeFrom.equals(""))
                curDateTimeFrom = null;
            else
                curDateTimeFrom.set(ConvertDateAndTime.GetYearFromISOStringDate(dateFrom),
                        ConvertDateAndTime.GetMonthFromISOStringDate(dateFrom)-1,
                        ConvertDateAndTime.GetDayFromISOStringDate(dateFrom),
                        ConvertDateAndTime.GetHourFromISOStringTime(timeFrom),
                        ConvertDateAndTime.GetMinutesFromISOStringTime(timeFrom), dateTimeTo.getTime().getSeconds());

            String dateTo = task.GetDateTo();
            String timeTo = task.GetTimeTo();
            Calendar curDateTimeTo = new GregorianCalendar();

            if (dateTo.equals("")||timeTo.equals(""))
                curDateTimeTo = null;
            else
                curDateTimeTo.set(ConvertDateAndTime.GetYearFromISOStringDate(dateTo),
                        ConvertDateAndTime.GetMonthFromISOStringDate(dateTo)-1,
                        ConvertDateAndTime.GetDayFromISOStringDate(dateTo),
                        ConvertDateAndTime.GetHourFromISOStringTime(timeTo),
                        ConvertDateAndTime.GetMinutesFromISOStringTime(timeTo), dateTimeTo.getTime().getSeconds());



            boolean condition = false;
            if (curDateTimeFrom!=null) {
                condition = ((curDateTimeFrom.after(dateTimeFrom))&&(curDateTimeFrom.before(dateTimeTo)));
            }
            if (curDateTimeTo!=null) {
                condition = condition||(((curDateTimeTo.after(dateTimeFrom))&&(curDateTimeTo.before(dateTimeTo))));
            }

            if (condition)
                resultList.add(task);
        }
        return resultList;
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
