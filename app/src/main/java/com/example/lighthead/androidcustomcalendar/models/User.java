package com.example.lighthead.androidcustomcalendar.models;

import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String name = new String();

    private String surname = new String();

    private ArrayList<ServerTask> taskList = new ArrayList<ServerTask>();

    private ArrayList<String> subscriberList = new ArrayList<>();

    private String username = new String();

    private int _v;

    private String _id = new String();
	
	
	
	public String GetName() {
		return name;
	}
	
	public void SetName(String name) {
		this.name = name;
	}
	
	public String GetSurname() {
		return surname;
	}
	
	public void SetSurname(String surname) {
		this.surname = surname;
	}

	public String GetUsername() {return username;}

	public void SetUsername(String username) {this.username = username;}
	
	public ArrayList<ServerTask> GetTaskList() {
		return taskList;
	}
	
	public void SetTaskList(ArrayList<ServerTask> taskList) {
		this.taskList = taskList;
	}
	
	public ArrayList<String> GetSubscriberList() {
		return subscriberList;
	}
	
	public void SetSubscriberList(ArrayList<String> subscriberList) {
		this.subscriberList = subscriberList;
	}

	public void AddSubscriber(String subscriber) {
		this.subscriberList.add(subscriber);
	}
	
	

}
