package com.example.lighthead.androidcustomcalendar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.GroupedTasksCollection;
import com.example.lighthead.androidcustomcalendar.helpers.communication.ServerTaskManager;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskComparable;
import com.example.lighthead.androidcustomcalendar.models.Task;

import java.util.ArrayList;

public class ExpandableTaskListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> GroupsList;
    private ArrayList<GroupedTasksCollection> TaskCollection;
    private int GroupLayout;
    private int TaskLayout;
    private String User;

    public static ArrayList<TaskComparable> selectedTasks = new ArrayList<TaskComparable>();

    public ExpandableTaskListAdapter(Context context, ArrayList<String> groupsList, ArrayList<GroupedTasksCollection> taskCollection, int groupLayout, int taskLayout, String user) {
        this.context = context;
        this.GroupsList = groupsList;
        this.TaskCollection = taskCollection;
        this.GroupLayout = groupLayout;
        this.TaskLayout = taskLayout;
        this.User = user;

    }

    @Override
    public int getGroupCount() {
        return GroupsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return TaskCollection.get(groupPosition).GetTaskList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return GroupsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return TaskCollection.get(groupPosition).GetTaskList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String groupTitle = (String) getGroup(groupPosition);

        ExpandableListView expListView = (ExpandableListView) parent;
        expListView.expandGroup(groupPosition);

        if (convertView==null) {

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.GroupLayout, null);
        }

        TextView groupTitleTv = convertView.findViewById(R.id.taskGroupName);
        groupTitleTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        groupTitleTv.setText(groupTitle);

        return convertView;
    }



    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final TaskComparable curTask = (TaskComparable) getChild(groupPosition, childPosition);

        if (convertView==null) {

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.TaskLayout, null);
        }


        TextView tNameView = convertView.findViewById(R.id.taskName);
        TextView tCommentView = convertView.findViewById(R.id.taskComment);
        TextView tDateTimeInterval = convertView.findViewById(R.id.taskDateTimeInterval);

        final CheckBox tCompletedView = convertView.findViewById(R.id.taskCompleted);

        tCompletedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean completedState = tCompletedView.isChecked();
                TaskComparable selectedTask = curTask;
                selectedTask.SetCompleted(completedState);

                ServerTaskManager stm = new ServerTaskManager(null);
                stm.UpdateTask(User, selectedTask, selectedTask.GetTaskNumber());
            }
        });

        Task task = curTask;


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
            convertView.setBackgroundColor(Color.GRAY);


        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
