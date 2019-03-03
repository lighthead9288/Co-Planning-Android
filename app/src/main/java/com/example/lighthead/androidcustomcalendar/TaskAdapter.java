package com.example.lighthead.androidcustomcalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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
     //   TextView tDateFromView = view.findViewById(R.id.taskDateFrom);
     //   TextView tTimeFromView = view.findViewById(R.id.taskTimeFrom);
       // TextView tDurationView = view.findViewById(R.id.taskDuration);
        TextView tDateTimeInterval = view.findViewById(R.id.taskDateTimeInterval);
        TextView tDayOfWeekView = view.findViewById(R.id.taskDayOfWeek);
      //  TextView tVisibilityView = view.findViewById(R.id.taskVisibility);
      //  TextView tEditableView = view.findViewById(R.id.taskEditable);
        final CheckBox tCompletedView = view.findViewById(R.id.taskCompleted);

        final int pos = position;

        tCompletedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean completedState = tCompletedView.isChecked();
              //  Toast.makeText(getContext(), completedState , Toast.LENGTH_LONG).show();
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
     //   tDateFromView.setText(task.GetDateFrom());
    //    tTimeFromView.setText(task.GetTimeFrom());
       // tDurationView.setText(String.valueOf(task.GetDuration()+"(min.)"));
        tDayOfWeekView.setText(GetStrWeekDay(dayOfWeek));
      //  tVisibilityView.setText(task.GetVisibility()?"Yes":"No");
      //  tEditableView.setText(task.GetEditable()?"Yes":"No");
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
