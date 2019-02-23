package com.example.lighthead.androidcustomcalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObservable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class TaskListView  extends AppCompatActivity  {

    private final String TASK_LIST_OPTION = "TaskListOption";

    private final String TASK_LIST_INTERVAL = "Interval";
    private final String TASK_LIST_SELECTED_DATE_FROM = "SelectedDateFrom";
    private final String TASK_LIST_SELECTED_DATE_TO = "SelectedDateTo";

    private final String TASK_LIST_TODAY = "Today";
    private final String TASK_LIST_THIS_WEEK = "ThisWeek";

    private TextView dateFrom;
    private TextView dateTo;
    private ListView taskList;
    private RadioButton setInterval;
    private RadioButton today;
    private RadioButton thisWeek;

    private ImageButton addTaskButton;
    private ImageButton editTaskButton;
    private ImageButton copyTaskButton;
    private ImageButton carryTaskButton;
    private ImageButton deleteTaskButton;

    Global global = new Global();

    Calendar dateAndTimeFrom=Calendar.getInstance();
    Calendar dateAndTimeTo=Calendar.getInstance();

    private TaskManager taskManager;

    private TaskAdapter taskAdapter;

    //private Map IdsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_layout);

        addTaskButton = findViewById(R.id.addTask);
        editTaskButton = findViewById(R.id.editTask);
        copyTaskButton = findViewById(R.id.copyTask);
        carryTaskButton = findViewById(R.id.carryTask);
        deleteTaskButton = findViewById(R.id.deleteTask);


        dateFrom = findViewById(R.id.dateFrom);
        dateTo = findViewById(R.id.dateTo);

        setInterval = findViewById(R.id.setInterval);
        today = findViewById(R.id.today);
        thisWeek = findViewById(R.id.thisWeek);

        Intent intent = getIntent();
        String option = intent.getStringExtra(TASK_LIST_OPTION);

        boolean isInterval = (option.equals(TASK_LIST_INTERVAL));
        boolean isToday = (option.equals(TASK_LIST_TODAY));
        boolean isThisWeek = (option.equals(TASK_LIST_THIS_WEEK));

        if (isInterval) {

            Calendar dateFrom =(Calendar)intent.getSerializableExtra(TASK_LIST_SELECTED_DATE_FROM);
            Calendar dateTo = (Calendar)intent.getSerializableExtra(TASK_LIST_SELECTED_DATE_TO);

            SetIntervalCommand(dateFrom, dateTo);

        }

        if (isToday) {

            SetTodayCommand();

        }

        if (isThisWeek) {
            SetThisWeekCommand();

        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TaskComparable selectedTask = (TaskComparable)parent.getItemAtPosition(position);

                if (!taskAdapter.selectedTasks.contains(selectedTask))
                {
                    taskAdapter.selectedTasks.add(selectedTask);
                    view.setBackgroundColor(Color.GRAY);

                }
                else
                {
                    taskAdapter.selectedTasks.remove(selectedTask);
                    view.setBackgroundColor(Color.WHITE);

                }

               // Toast.makeText(getApplicationContext(), " Position " + String.valueOf(position) +" id " + GetTaskIdFromMap(position), Toast.LENGTH_LONG).show();
                EnableButtons();

            }
        };

        taskList.setOnItemClickListener(itemClickListener);

      //  UpdateTaskList();

        EnableButtons();




        global.SetCurSchedFragment("taskList");

    }





    private void UpdateTaskList(Calendar dateFrom, Calendar dateTo) {

        dateFrom.set(Calendar.HOUR_OF_DAY, 0);
        dateFrom.set(Calendar.MINUTE, 0);
        dateFrom.set(Calendar.SECOND, 0);

        dateTo.set(Calendar.HOUR_OF_DAY, 23);
        dateTo.set(Calendar.MINUTE, 59);
        dateTo.set(Calendar.SECOND, 59);

        taskManager = new TaskManager(getApplicationContext());
        ArrayList<TaskDBWrapper> tasks = taskManager.GetTasks(dateFrom, dateTo);

        ArrayList<TaskComparable> taskComparables = new ArrayList<>();
        for (TaskDBWrapper task:tasks
             ) {
            TaskComparable taskComparable = new TaskComparable(task);
            taskComparables.add(taskComparable);
        }
        Collections.sort(taskComparables, TaskComparable.DateTimeComparator);

       // UpdateIdsMap(tasks);

        taskList = findViewById(R.id.taskList);
        taskAdapter = new TaskAdapter(getApplicationContext(), R.layout.singletasklayout, taskComparables);
        taskList.setAdapter(taskAdapter);

        taskAdapter.selectedTasks.clear();



}
    private void SetFromDate(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        dateFrom.setText(date + "." + dispMonth + "." + year);

        dateAndTimeFrom.set(year, month, date);

    }

    private void SetToDate(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        dateTo.setText(date + "." + dispMonth + "." + year);

        dateAndTimeTo.set(year, month, date);

    }

    private void SetIntervalCommand(Calendar dateFrom, Calendar dateTo) {
        SetFromDate(dateFrom);
        SetToDate(dateTo);

        setInterval.setChecked(true);
        UpdateTaskList(dateFrom, dateTo);
    }

    private void SetTodayCommand() {
        Calendar now = new GregorianCalendar();
      /*  now.add(Calendar.DAY_OF_WEEK, 7 - now.get(Calendar.DAY_OF_WEEK) + 1);
        now.add(Calendar.DAY_OF_WEEK, -6);*/

        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        Calendar dateFrom = now;

        Calendar nowClone = (Calendar) now.clone();

        nowClone.set(Calendar.HOUR_OF_DAY, 23);
        nowClone.set(Calendar.MINUTE, 59);
        nowClone.set(Calendar.SECOND, 59);
        Calendar dateTo = nowClone;

        today.setChecked(true);
        UpdateTaskList(dateFrom, dateTo);
    }

    private void SetThisWeekCommand() {
        Calendar now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_WEEK, -(now.get(Calendar.DAY_OF_WEEK))+1);
        Calendar dateFrom = now;

        now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_WEEK, 7 - now.get(Calendar.DAY_OF_WEEK) + 1);
        Calendar dateTo = now;

        thisWeek.setChecked(true);
        UpdateTaskList(dateFrom, dateTo);
    }




    public void setDateFrom(View view){
           new DatePickerDialog(TaskListView.this, dFrom,
                dateAndTimeFrom.get(Calendar.YEAR),
                dateAndTimeFrom.get(Calendar.MONTH),
                dateAndTimeFrom.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener dFrom=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTimeFrom.set(Calendar.YEAR, year);
            dateAndTimeFrom.set(Calendar.MONTH, monthOfYear);
            dateAndTimeFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetFromDate(dateAndTimeFrom);

            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);

            EnableButtons();

        }
    };

    public void setDateTo(View view){

        new DatePickerDialog(TaskListView.this, dTo,
                dateAndTimeTo.get(Calendar.YEAR),
                dateAndTimeTo.get(Calendar.MONTH),
                dateAndTimeTo.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener dTo=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTimeTo.set(Calendar.YEAR, year);
            dateAndTimeTo.set(Calendar.MONTH, monthOfYear);
            dateAndTimeTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetToDate(dateAndTimeTo);

            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);

            EnableButtons();

        }
    };






    public void setInterval(View view) {
        SetIntervalCommand(dateAndTimeFrom, dateAndTimeTo);
        UnlockDates();
    }

    public void setToday(View view) {
        LockDates();
        SetTodayCommand();
    }

    public void setThisWeek(View view) {
        LockDates();
        SetThisWeekCommand();
    }

    private void LockDates() {
        dateFrom.setEnabled(false);
        dateTo.setEnabled(false);
    }

    private void UnlockDates() {
        dateFrom.setEnabled(true);
        dateTo.setEnabled(true);
    }

    private void EnableButtons(){
        int tasksCount = taskAdapter.selectedTasks.size();

        switch (tasksCount){
            case 0:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(false);
                copyTaskButton.setEnabled(false);
                carryTaskButton.setEnabled(false);
                deleteTaskButton.setEnabled(false);
                break;
            }
            case 1:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(true);
                copyTaskButton.setEnabled(true);
                carryTaskButton.setEnabled(true);
                deleteTaskButton.setEnabled(true);
                break;
            }
            default:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(false);
                copyTaskButton.setEnabled(true);
                carryTaskButton.setEnabled(true);
                deleteTaskButton.setEnabled(true);
                break;
            }
        }
    }

    public void AddTaskCommand(View view){
        OpenTaskEditor();
    }

    public void EditTaskCommand(View view){
        OpenTaskEditor(taskAdapter.selectedTasks.get(0));
    }


    public void CopyTaskCommand(View view){
        //Toast.makeText(getApplicationContext(), "Copy" , Toast.LENGTH_LONG).show();
        Calendar calendarCopy = new GregorianCalendar();

        new DatePickerDialog(TaskListView.this, dCopy,
                calendarCopy.get(Calendar.YEAR),
                calendarCopy.get(Calendar.MONTH),
                calendarCopy.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    DatePickerDialog.OnDateSetListener dCopy=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            ArrayList<TaskComparable> selected = taskAdapter.selectedTasks;
            for (TaskDBWrapper task:selected
                 ) {
                task.SetDate(year, monthOfYear+1, dayOfMonth);
                TaskManager tm = new TaskManager(getApplicationContext());
                tm.AddTask(task);
            }
            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
            Toast.makeText(getApplicationContext(), "Copied!" , Toast.LENGTH_LONG).show();
        }
    };

    public void CarryTaskCommand(View view){
        //Toast.makeText(getApplicationContext(), "Carry" , Toast.LENGTH_LONG).show();
        Calendar calendarCarry = new GregorianCalendar();

        new DatePickerDialog(TaskListView.this, dCarry,
                calendarCarry.get(Calendar.YEAR),
                calendarCarry.get(Calendar.MONTH),
                calendarCarry.get(Calendar.DAY_OF_MONTH))
                .show();


    }

    DatePickerDialog.OnDateSetListener dCarry=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            ArrayList<TaskComparable> selected = taskAdapter.selectedTasks;
            for (TaskDBWrapper task:selected
            ) {
                task.SetDate(year, monthOfYear+1, dayOfMonth);
                TaskManager tm = new TaskManager(getApplicationContext());
                tm.UpdateTask(task, task.GetId());
            }
            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
            Toast.makeText(getApplicationContext(), "Carried!" , Toast.LENGTH_LONG).show();
        }
    };

    public void DeleteTaskCommand(View view){

        TaskManager tm = new TaskManager(getApplicationContext());


        ArrayList<TaskComparable> selected = taskAdapter.selectedTasks;
        for (TaskDBWrapper taskForDelete:selected
        ) {
            tm.DeleteTask(taskForDelete.GetId());
        }


        UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);

        Toast.makeText(getApplicationContext(), "Deleted!" , Toast.LENGTH_LONG).show();

    }

    private void OpenTaskEditor(){
        Intent intent = new Intent(getApplicationContext(), TaskEditor.class);
        intent.putExtra("isNewTask", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);

       // startActivityForResult(intent, 1);
    }

    private void OpenTaskEditor(TaskDBWrapper task){
        Intent intent = new Intent(getApplicationContext(), TaskEditor.class);


        intent.putExtra("isNewTask", false);
        intent.putExtra("Id", task.GetId());
        intent.putExtra("Name", task.GetName());
        intent.putExtra("Comment", task.GetComment());
        intent.putExtra("Date", task.GetDate());
        intent.putExtra("Time", task.GetTime());
        intent.putExtra("Duration", task.GetDuration());
        intent.putExtra("Visibility", task.GetVisibility());
        intent.putExtra("Editable", task.GetEditable());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
    }




}
