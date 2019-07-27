package com.example.lighthead.androidcustomcalendar.models;

import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    public String name = new String();

    public String surname = new String();

   // public String Password = new String();
  //  public String About=new String();
    public ArrayList</*TaskDBWrapper*/ServerTask> taskList = new ArrayList</*TaskDBWrapper*/ServerTask>();

    public ArrayList<String> subscriberList = new ArrayList<>();

    public String username = new String();

    public int _v;

    public String _id = new String();

}
