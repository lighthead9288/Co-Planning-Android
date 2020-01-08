package com.example.lighthead.androidcustomcalendar.helpers;

import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskComparable;

import java.util.ArrayList;

public class GroupedTasksCollection {

    private String GroupName;
    private ArrayList<TaskComparable> TaskList;

    public ArrayList<TaskComparable> GetTaskList() {
        return TaskList;
    }

    public GroupedTasksCollection(String groupName, ArrayList<TaskComparable> taskList) {
        GroupName = groupName;
        TaskList = taskList;
    }
}
