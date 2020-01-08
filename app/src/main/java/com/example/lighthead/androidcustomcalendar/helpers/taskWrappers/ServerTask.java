package com.example.lighthead.androidcustomcalendar.helpers.taskWrappers;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.models.Task;

import java.util.ArrayList;

public class ServerTask extends Task {

    private ArrayList<String> subscriberList;

    private int taskNumber;
	
	public ServerTask(String name) {
		super(name);
	}
	
	public ServerTask(Task task) {
		super(task);

		if (task.GetDateFrom()==null)
			dateFrom = "";
		if (task.GetTimeFrom()==null)
			timeFrom = "";
		if (task.GetDateTo()==null)
			dateTo = "";
		if (task.GetTimeTo()==null)
			timeTo = "";


		
	}
	
	public void SetSubscriberList(ArrayList<String> list) {
		subscriberList = list;
	}

	public ArrayList<String> GetSubscriberList() {return subscriberList;}

	public void SetTaskNumber(int number) {

		taskNumber = number;
	}

	public int GetTaskNumber() {
		return taskNumber;
	}

	public void SetDateFrom(int year, int month, int date)
	{
		dateFrom = ConvertDateAndTime.ConvertToISOStringDate(year, month, date);

	}

	public void SetTimeFrom(int hours, int minutes)
	{
		timeFrom = ConvertDateAndTime.ConvertToISOStringTime(hours, minutes);
	}

	public void SetDateTo(int year, int month, int date)
	{
		dateTo = ConvertDateAndTime.ConvertToISOStringDate(year, month, date);
	}

	public void SetTimeTo(int hours, int minutes)
	{
		timeTo = ConvertDateAndTime.ConvertToISOStringTime(hours, minutes);
	}



}
