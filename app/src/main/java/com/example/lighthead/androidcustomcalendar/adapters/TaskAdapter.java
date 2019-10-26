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
import com.example.lighthead.androidcustomcalendar.helpers.communication.ServerTaskManager;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskComparable;
import com.example.lighthead.androidcustomcalendar.models.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<TaskComparable> {

    private LayoutInflater inflater;
    private int layout;
    private List<TaskComparable> Tasks;
    private String username;
    public static ArrayList<TaskComparable> selectedTasks = new ArrayList<TaskComparable>();

    public TaskAdapter(Context context, int resource, List<TaskComparable> tasks, String user) {
        super(context, resource, tasks);
        this.Tasks = tasks;
        this.username = user;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);


    }

    public View getView(int position, View convertView, ViewGroup parent){

        View view = inflater.inflate(layout, parent, false);
        TextView tNameView = view.findViewById(R.id.taskName);
        TextView tCommentView = view.findViewById(R.id.taskComment);
        TextView tDateTimeInterval = view.findViewById(R.id.taskDateTimeInterval);

        final CheckBox tCompletedView = view.findViewById(R.id.taskCompleted);

        final int pos = position;

        tCompletedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean completedState = tCompletedView.isChecked();
                TaskComparable selectedTask = Tasks.get(pos);
                selectedTask.SetCompleted(completedState);

                ServerTaskManager stm = new ServerTaskManager(null);
                stm.UpdateTask(username, selectedTask, selectedTask.GetTaskNumber());

            }
        });

        Task task = Tasks.get(position);


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

        tCompletedView.setChecked(task.GetCompleted());

        if (selectedTasks.contains(task))
            view.setBackgroundColor(Color.GRAY);

        return view;
    }




}
