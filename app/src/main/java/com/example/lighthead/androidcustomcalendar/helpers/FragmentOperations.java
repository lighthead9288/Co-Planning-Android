package com.example.lighthead.androidcustomcalendar.helpers;

import android.app.Application;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.lighthead.androidcustomcalendar.R;

public class FragmentOperations extends Application {

    private FragmentManager SupportFragmentManager;

    public FragmentOperations(FragmentManager supportFragmentManager) {
        SupportFragmentManager = supportFragmentManager;
    }

    public void LoadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = SupportFragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void ClearBackStack() {
        if (SupportFragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = SupportFragmentManager.getBackStackEntryAt(0);
            SupportFragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}
