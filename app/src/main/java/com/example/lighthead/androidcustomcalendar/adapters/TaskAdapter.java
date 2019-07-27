package com.example.lighthead.androidcustomcalendar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskComparable;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskDBWrapper;
import com.example.lighthead.androidcustomcalendar.helpers.db.TaskManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<TaskComparable> {

    private LayoutInflater inflater;
    private int layout;
    private List<TaskComparable> Tasks;
    public static ArrayList<TaskComparable> selectedTasks = new ArrayList<TaskComparable>();

    public TaskAdapter(Context context, int resource, List<TaskComparable> tasks) {
        super(context, resource, tasks);
        this.Tasks = tasks;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);


    }

    public View getView(int position, View convertView, ViewGroup parent){

        View view = inflater.inflate(layout, parent, false);
        TextView tNameView = view.findViewById(R.id.taskName);
        TextView tCommentView = view.findViewById(R.id.taskComment);
        TextView tDateTimeInterval = view.findViewById(R.id.taskDateTimeInterval);
        TextView tDayOfWeekView = view.findViewById(R.id.taskDayOfWeek);

        final CheckBox tCompletedView = view.findViewById(R.id.taskCompleted);

        final int pos = position;

        tCompletedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean completedState = tCompletedView.isChecked();
                TaskDBWrapper selectedTask = Tasks.get(pos);
                selectedTask.SetCompleted(completedState);

                TaskManager tm = new TaskManager(getContext());
                tm.UpdateTask(selectedTask, selectedTask.GetId());
            }
        });

        TaskDBWrapper task = Tasks.get(position);

        Calendar curTaskDateTimeFrom = ((TaskComparable) task).GetDateTimeFrom();
        int dayOfWeek = curTaskDateTimeFrom.get(Calendar.DAY_OF_WEEK);

        String taskDateTimeInterval;
        String taskDateFrom = task.GetDateFrom();
        taskDateFrom = (taskDateFrom==null)?"??":taskDateFrom;
        String taskTimeFrom = task.GetTimeFrom();
        taskTimeFrom = (taskTimeFrom==null)?"??":taskTimeFrom;
        String taskDateTo = task.GetDateTo();
        taskDateTo = (taskDateTo==null)?"??":taskDateTo;
        String taskTimeTo = task.GetTimeTo();
        taskTimeTo = (taskTimeTo==null)?"??":taskTimeTo;
        taskDateTimeInterval = taskDateFrom + ", " + taskTimeFrom + " - " + taskDateTo + ", " + taskTimeTo;


        tNameView.setText(task.GetName());
        tCommentView.setText(task.GetComment());
        tDateTimeInterval.setText(taskDateTimeInterval);

        tDayOfWeekView.setText(GetStrWeekDay(dayOfWeek));

        tCompletedView.setChecked(task.GetCompleted());

        if (selectedTasks.contains(task))
            view.setBackgroundColor(Color.GRAY);

        return view;
    }

    public String GetStrWeekDay(int dayNumber) {

        switch (dayNumber) {
            case 1: return "WED";
            case 2: return "THU";
            case 3: return "FRI";
            case 4: return "SAT";
            case 5: return "SUN";
            case 6: return "MON";
            case 7: return "TUE";
            default: return "";
        }
    }







}
