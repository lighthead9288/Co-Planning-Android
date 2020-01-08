package com.example.lighthead.androidcustomcalendar;

import android.app.Application;

import java.util.ArrayList;

public class MappingElementsManager extends Application {

    private static ArrayList<String> MappingElements = new ArrayList<>();

    public ArrayList<String> GetMappingElements(){
        return MappingElements;
    }

    public void SetMappingsElements(ArrayList<String> elements) {
        MappingElements = elements;
    }

    public void AddMappingElement(String element) {
        MappingElements.add(element);
    }

    public void RemoveMappingElement(String element) {
        MappingElements.remove(element);
    }

    public void ClearMappingElements() {
        MappingElements.clear();
    }
}
