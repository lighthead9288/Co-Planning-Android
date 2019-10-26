package com.example.lighthead.androidcustomcalendar.helpers;

import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;

public class CurrentUserTask {

    private String username;

    private ServerTask task;

    private long taskId;
	
	public String GetUsername() {
		return username;
	}

	public void SetUsername(String username) {
		this.username = username;
	}
	
	public ServerTask GetServerTask() {
		return task;
	}

	public void SetServerTask(ServerTask task) {
		this.task = task;
	}
	
	public long GetTaskId() {
		return taskId;
	}

	public void SetTaskId(long taskId) {
		this.taskId = taskId;
	}
	
	public CurrentUserTask() {
	} 
}
