package com.example.lighthead.androidcustomcalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskEditor extends AppCompatActivity {

    EditText taskName;
    EditText taskComment;
    TextView taskDate;
    TextView taskTime;
    EditText taskDuration;
    CheckBox taskVisibility;
    CheckBox taskEditable;

    CheckBox dateConfirm;
    CheckBox timeConfirm;

    boolean isNewTask=true;
    private long TaskId;

    Calendar dateAndTime=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskeditor_layout);

        taskName = findViewById(R.id.taskName);
        taskComment = findViewById(R.id.taskComment);
        taskDate = findViewById(R.id.taskDate);
        taskTime = findViewById(R.id.taskTime);
        taskDuration = findViewById(R.id.taskDuration);
        taskVisibility = findViewById(R.id.taskVisibility);
        taskEditable = findViewById(R.id.taskEditable);

        dateConfirm = findViewById(R.id.dateConfirm);
        timeConfirm = findViewById(R.id.timeConfirm);

        Intent intent = getIntent();

        isNewTask = intent.getBooleanExtra("isNewTask", true);

        if (!isNewTask) {
            TaskId = intent.getLongExtra("Id", 0);

            taskName.setText(intent.getStringExtra("Name"));
            taskComment.setText(intent.getStringExtra("Comment"));

            int year, month, date, hours, minutes;


            String strDate = intent.getStringExtra("Date");
            if (!(strDate==null)) {
                year = ConvertDateAndTime.GetYearFromStringDate(strDate);
                dateAndTime.set(Calendar.YEAR, year);
                month = ConvertDateAndTime.GetMonthFromStringDate(strDate);
                dateAndTime.set(Calendar.MONTH, month-1);
                date = ConvertDateAndTime.GetDayFromStringDate(strDate);
                dateAndTime.set(Calendar.DAY_OF_MONTH, date);
                taskDate.setText(strDate);
            }
            else {
                dateConfirm.setChecked(true);
                DisableDateSet();
            }



            String strTime = intent.getStringExtra("Time");
            if (!(strTime==null)) {
                hours = ConvertDateAndTime.GetHourFromStringTime(strTime);
                dateAndTime.set(Calendar.HOUR_OF_DAY, hours);
                minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTime);
                dateAndTime.set(Calendar.MINUTE, minutes);
                taskTime.setText(strTime);
            }
            else {
                timeConfirm.setChecked(true);
                DisableTimeSet();
            }

            //dateAndTime.set(year, month-1, date, hours, minutes);



            taskDuration.setText(String.valueOf(intent.getIntExtra("Duration", 0)));
            taskVisibility.setChecked(intent.getBooleanExtra("Visibility", false));
            taskEditable.setChecked(intent.getBooleanExtra("Editable", false));
        }

        else {
            dateAndTime = new GregorianCalendar();
            int year = dateAndTime.get(Calendar.YEAR);
            int month = dateAndTime.get(Calendar.MONTH)+1;
            int date = dateAndTime.get(Calendar.DAY_OF_MONTH);
            int hours = dateAndTime.get(Calendar.HOUR_OF_DAY);
            int minutes = dateAndTime.get(Calendar.MINUTE);
           // int seconds = dateAndTime.get(Calendar.SECOND);

            String strDate = ConvertDateAndTime.ConvertToStringDate(year, month, date);
            String strTime = ConvertDateAndTime.ConvertToStringTime(hours, minutes);

            taskDate.setText("Press to set");
            taskTime.setText("Press to set");

           /* year = ConvertDateAndTime.GetYearFromStringDate(strDate);
            month = ConvertDateAndTime.GetMonthFromStringDate(strDate);
            date = ConvertDateAndTime.GetDayFromStringDate(strDate);

            hours = ConvertDateAndTime.GetHourFromStringTime(strTime);
            minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTime);*/


        }

    }


    public void TaskDateConfirmSet(View view) {
        if (dateConfirm.isChecked())
            DisableDateSet();
        else EnableDateSet();

    }

    public void TaskTimeConfirmSet(View view) {
        if (timeConfirm.isChecked())
            DisableTimeSet();
        else EnableTimeSet();

    }

    private void DisableDateSet() {

        taskDate.setEnabled(false);
    }

    private void DisableTimeSet() {
        timeConfirm.setChecked(true);
        taskTime.setEnabled(false);
    }


    private void EnableDateSet() {
        taskDate.setEnabled(true);
    }

    private void EnableTimeSet() {
        taskTime.setEnabled(true);
    }



    private void SetDate(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        taskDate.setText(ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date));

        dateAndTime.set(year, month, date);

    }

    private void SetTime(Calendar fullDate) {

        int hours = fullDate.get(Calendar.HOUR_OF_DAY);
        int minutes = fullDate.get(Calendar.MINUTE);

        taskTime.setText(ConvertDateAndTime.ConvertToStringTime(hours, minutes));

        dateAndTime.set(Calendar.HOUR_OF_DAY, hours);
        dateAndTime.set(Calendar.MINUTE, minutes);
    }


    public void setDate(View view){
        new DatePickerDialog(TaskEditor.this, curTaskDate,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener curTaskDate=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetDate(dateAndTime);



        }
    };

    public void setTime(View view) {
        new TimePickerDialog(TaskEditor.this, curTaskTime, dateAndTime.get(Calendar.HOUR_OF_DAY), dateAndTime.get(Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener curTaskTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);

            SetTime(dateAndTime);
        }
    };

    public void SaveTask(View view) {

        Task task = GetTaskFromView();
        TaskManager tm = new TaskManager(getApplicationContext());

        if (isNewTask) {
            tm.AddTask(task);

           /* Intent intent = new Intent();
            intent.putExtra("name", taskName.getText().toString());
            setResult(RESULT_OK, intent);*/
            finish();


        }
        else {
            tm.UpdateTask(task, TaskId);
            finish();
        }

    }

    public Task GetTaskFromView() {

        String name = taskName.getText().toString();
        String comment = taskComment.getText().toString();


        boolean visibility = taskVisibility.isChecked();
        boolean editable = taskEditable.isChecked();

        Task task = new Task(name);
        task.SetComment(comment);

        task.SetVisibility(visibility);
        task.SetEditable(editable);

        String strDuration = taskDuration.getText().toString();
        if (!strDuration.isEmpty()&&strDuration!=null) {
            double duration = Double.parseDouble(strDuration);
            task.SetDuration(duration);
        }

        if (!dateConfirm.isChecked()&&taskDate.getText()!="Press to set") {
            String strDate = taskDate.getText().toString();
            int year = ConvertDateAndTime.GetYearFromStringDate(strDate);
            int month = ConvertDateAndTime.GetMonthFromStringDate(strDate);
            int date = ConvertDateAndTime.GetDayFromStringDate(strDate);
            task.SetDate(year, month, date);
        }

        if (!timeConfirm.isChecked()&&taskTime.getText()!="Press to set") {
            String strTime = taskTime.getText().toString();
            int hour = ConvertDateAndTime.GetHourFromStringTime(strTime);
            int minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTime);
            task.SetTime(hour, minutes);
        }

        return task;

    }
}
