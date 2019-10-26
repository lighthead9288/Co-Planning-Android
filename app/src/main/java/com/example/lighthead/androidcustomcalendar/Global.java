package com.example.lighthead.androidcustomcalendar;

import android.app.Application;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.View;


public class Global extends Application {
    private static Fragment curSchedFragment;

    private static Fragment curSearchFragment;

    private static Fragment curMappingsFragment;

    private static Fragment curNotificationsFragment;

    private static Fragment curSettingsFragment;
	
	private static FragmentTypes curFragment;

	private static BottomNavigationView bottomNavigationView;

	public void SetBottomNavigationView(BottomNavigationView navigation) {
	    bottomNavigationView = navigation;
    }

    public void ShowBottomNavigationView() {
	    if (bottomNavigationView!=null) {
	        bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    public void HideBottomNavigationView() {
	    if (bottomNavigationView!=null) {
	        bottomNavigationView.setVisibility(View.GONE);
        }
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
