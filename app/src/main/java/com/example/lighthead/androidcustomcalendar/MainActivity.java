package com.example.lighthead.androidcustomcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.calendar.CalendarFragment;
import com.example.lighthead.androidcustomcalendar.fragments.LoginFragment;
import com.example.lighthead.androidcustomcalendar.fragments.MappingsFragment;
import com.example.lighthead.androidcustomcalendar.fragments.NotificationsFragment;
import com.example.lighthead.androidcustomcalendar.fragments.ProfileFragment;
import com.example.lighthead.androidcustomcalendar.fragments.RegisterFragment;
import com.example.lighthead.androidcustomcalendar.fragments.SearchFragment;
import com.example.lighthead.androidcustomcalendar.fragments.TaskEditorFragment;
import com.example.lighthead.androidcustomcalendar.fragments.TaskListFragment;

import com.example.lighthead.androidcustomcalendar.models.User;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements
        SearchFragment.OnFragmentInteractionListener, MappingsFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener, TaskListFragment.OnFragmentInteractionListener, TaskEditorFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    Global global = new Global();

    private BottomNavigationView navigation;
    private Context MainActivityContext;
    private View badge;
    private TextView txt;


    SharedPreferencesOperations spa = new SharedPreferencesOperations();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        spa.SetActivity(this);

        MainActivityContext = this;

        BadgesOperations badgesOps = new BadgesOperations();
        badgesOps.SetBottomNavigationView(navigation);
        badgesOps.SetContext(MainActivityContext);
        badgesOps.SetBadge(badge);
        badgesOps.SetTextView(txt);


        final String login = spa.GetLogin();
        String password = spa.GetPassword();

        if ((login=="")||(password=="")) {

            Fragment fragment = new LoginFragment();
            global.SetCurSearchFragment(fragment);
            global.SetCurMappingsFragment(fragment);
            global.SetCurNotificationsFragment(fragment);
            global.SetCurSettingsFragment(fragment);
        }
        else {
            Fragment searchFragment = new SearchFragment();
            global.SetCurSearchFragment(searchFragment);
            Fragment mappingsFragment = new MappingsFragment();
            global.SetCurMappingsFragment(mappingsFragment);
            Fragment notifyFragment = new NotificationsFragment();
            global.SetCurNotificationsFragment(notifyFragment);
            Fragment settingsFragment = new ProfileFragment();
            global.SetCurSettingsFragment(settingsFragment);
			


        }

        Bundle arguments = getIntent().getExtras();

        if (arguments!=null) {

            if (arguments.containsKey("notificationText"))
            {
                String message = arguments.getString("notificationText");
                String str = "";
            }
        }

        else {

            Bundle bundle = new Bundle();
            bundle.putString("TaskListOption", "ThisWeek");

            TaskListFragment taskListFragment = new TaskListFragment();
            taskListFragment.setArguments(bundle);
            global.SetCurSchedFragment(taskListFragment);
            loadFragment(taskListFragment);
            global.SetCurFragment(FragmentTypes.Schedule);
        }




    }










    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.mySchedule:
                    Fragment curSchedFrag = global.GetCurSchedFragment();
				    global.SetCurFragment(FragmentTypes.Schedule);
					loadFragment(curSchedFrag);

                    return true;

                case R.id.search:
                    fragment = global.GetCurSearchFragment();
					global.SetCurFragment(FragmentTypes.Search);
                    loadFragment(fragment);

                    return true;

                case R.id.mappings:
                    fragment = global.GetCurMappingsFragment();
					global.SetCurFragment(FragmentTypes.Mappings);
                    loadFragment(fragment);

                    return true;


                case R.id.notifications:
                    fragment = global.GetCurNotificationsFragment();
					global.SetCurFragment(FragmentTypes.Notifications);
                    loadFragment(fragment);

                    return true;

                case R.id.settings:
                    fragment = global.GetCurSettingsFragment();
                    global.SetCurFragment(FragmentTypes.Settings);
                    loadFragment(fragment);

                    return true;

            }
            return true;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
