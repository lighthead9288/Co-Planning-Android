package com.example.lighthead.androidcustomcalendar.helpers;

import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;

import java.util.ArrayList;

public class SetTaskListParams {

	private String username;

	private ArrayList<ServerTask> taskList;
	
	public String GetUsername() {
		return username;
	}
	
	public ArrayList<ServerTask> GetTaskList() {
		return taskList;
	}
	
	public SetTaskListParams(String username, ArrayList<ServerTask> taskList) {
		this.username = username;
		this.taskList = taskList;
	}
	
}