package com.example.lighthead.androidcustomcalendar;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

public class Global extends Application {
    private static Fragment curSchedFragment;

    private static Fragment curSearchFragment;

    private static Fragment curMappingsFragment;

    private static Fragment curNotificationsFragment;

    private static Fragment curSettingsFragment;
	
	private static FragmentTypes curFragment;

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


	public FragmentTypes GetCurFragment(){

        return curFragment;
    }
    public void SetCurFragment(FragmentTypes fragment)
    {
        curFragment = fragment;
    }

    public Fragment GetCurSchedFragment(){

        return curSchedFragment;
    }
    public void SetCurSchedFragment(Fragment fragment)
    {
        curSchedFragment = fragment;
    }

    public Fragment GetCurSearchFragment(){

        return curSearchFragment;
    }

    public void SetCurSearchFragment(Fragment fragment) {
	    curSearchFragment = fragment;}

    public Fragment GetCurMappingsFragment(){

        return curMappingsFragment;
    }

    public void SetCurMappingsFragment(Fragment fragment) { curMappingsFragment = fragment;}

    public Fragment GetCurNotificationsFragment(){

        return curNotificationsFragment;
    }

    public void SetCurNotificationsFragment(Fragment fragment) { curNotificationsFragment = fragment;}

    public Fragment GetCurSettingsFragment(){

        return curSettingsFragment;
    }

    public void SetCurSettingsFragment(Fragment fragment) { curSettingsFragment = fragment;}
	



    public Global(){

    }

}
