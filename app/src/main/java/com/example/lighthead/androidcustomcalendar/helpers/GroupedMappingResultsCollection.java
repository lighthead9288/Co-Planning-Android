package com.example.lighthead.androidcustomcalendar.helpers;

import com.example.lighthead.androidcustomcalendar.helpers.mapping.MappingResultElement;

import java.util.ArrayList;

public class GroupedMappingResultsCollection {

    private String GroupName;

    private ArrayList<MappingResultElement> ResultElements;

    public String GetGroupName() {
        return GroupName;
    }

    public ArrayList<MappingResultElement> GetResultElements() {
        return ResultElements;
    }


    public GroupedMappingResultsCollection(String groupName, ArrayList<MappingResultElement> elements) {
        GroupName = groupName;
        ResultElements = elements;

    }


}
