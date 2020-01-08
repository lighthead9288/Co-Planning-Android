package com.example.lighthead.androidcustomcalendar;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lighthead.androidcustomcalendar.fragments.LoginFragment;
import com.example.lighthead.androidcustomcalendar.fragments.MappingResultsFragment;
import com.example.lighthead.androidcustomcalendar.fragments.MappingsFragment;
import com.example.lighthead.androidcustomcalendar.fragments.NotificationsFragment;
import com.example.lighthead.androidcustomcalendar.fragments.ProfileFragment;
import com.example.lighthead.androidcustomcalendar.fragments.RegisterFragment;
import com.example.lighthead.androidcustomcalendar.fragments.SearchFragment;
import com.example.lighthead.androidcustomcalendar.fragments.TaskEditorFragment;
import com.example.lighthead.androidcustomcalendar.fragments.TaskListFragment;

import com.example.lighthead.androidcustomcalendar.helpers.BottomNavigationBehaviour;
import com.example.lighthead.androidcustomcalendar.helpers.FragmentOperations;
import com.example.lighthead.androidcustomcalendar.helpers.communication.RetrofitClient;
import com.example.lighthead.androidcustomcalendar.helpers.communication.SocketClient;
import com.example.lighthead.androidcustomcalendar.interfaces.ICoPlanningAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements
        SearchFragment.OnFragmentInteractionListener, MappingsFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener,
        TaskListFragment.OnFragmentInteractionListener, TaskEditorFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        MappingResultsFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Activity currentActivity;
    private BottomNavigationView navigation;
    private Context MainActivityContext;

    private SharedPreferencesOperations spa = new SharedPreferencesOperations();
    private FragmentOperations fragmentOperations = new FragmentOperations(getSupportFragmentManager());
    private BadgesOperations badgesOps = new BadgesOperations();
    private Global global = new Global();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehaviour(this, null));

        global.SetBottomNavigationView(navigation);


        spa.SetActivity(this);
        currentActivity = this;

        MainActivityContext = this;
        badgesOps.Init(navigation, MainActivityContext);

        final String login = spa.GetLogin();
        String password = spa.GetPassword();

        if ((login=="")||(password=="")) {

            Fragment fragment = new LoginFragment();

            global.SetCurSchedFragment(fragment);

            global.SetCurSearchFragment(fragment);
            global.SetCurMappingsFragment(fragment);
            global.SetCurNotificationsFragment(fragment);
            global.SetCurSettingsFragment(fragment);

            Fragment curSchedFrag = global.GetCurSchedFragment();
            global.SetCurFragment(FragmentTypes.Schedule);
            fragmentOperations.LoadFragment(curSchedFrag);
        }

        else {

            RetrofitClient rClient = new RetrofitClient();
            Retrofit retrofit = rClient.GetRetrofitEntity();

            ICoPlanningAPI client = retrofit.create(ICoPlanningAPI.class);

            Call loginCall = client.login(login, password);
            loginCall.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    Bundle bundle = new Bundle();
                    bundle.putString("TaskListOption", "ThisWeek");
                    TaskListFragment taskListFragment = new TaskListFragment();
                    taskListFragment.setArguments(bundle);
                    global.SetCurSchedFragment(taskListFragment);


                    Fragment searchFragment = new SearchFragment();
                    global.SetCurSearchFragment(searchFragment);
                    Fragment mappingsFragment = new MappingsFragment();
                    global.SetCurMappingsFragment(mappingsFragment);
                    Fragment notifyFragment = new NotificationsFragment();
                    global.SetCurNotificationsFragment(notifyFragment);
                    Fragment settingsFragment = new ProfileFragment();
                    global.SetCurSettingsFragment(settingsFragment);


                    Fragment curSchedFrag = global.GetCurSchedFragment();
                    global.SetCurFragment(FragmentTypes.Schedule);
                    fragmentOperations.LoadFragment(curSchedFrag);


                    SocketClient socketClient = new SocketClient(currentActivity, badgesOps);
                    socketClient.SetNotificationsListener(null);
                    socketClient.Connect();
                    socketClient.SetNotificationsReceiver(login);
                    socketClient.SetFullNotificationsListListener(null);
                    socketClient.GetFullNotificationsList(login);


                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });

        }

       /* Bundle arguments = getIntent().getExtras();

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
        }*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count==0)
            finish();

    }

    //region Navigation item selected listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            String login = spa.GetLogin();
            String password = spa.GetPassword();

            boolean clearBackStack =  ((login!="")&&(password!=""));


            switch (item.getItemId()) {
                case R.id.mySchedule:
                    if (clearBackStack)
                        fragmentOperations.ClearBackStack();
                    Fragment curSchedFrag = global.GetCurSchedFragment();
				    global.SetCurFragment(FragmentTypes.Schedule);
                    fragmentOperations.LoadFragment(curSchedFrag);

                    return true;

                case R.id.search:
                    if (clearBackStack)
                        fragmentOperations.ClearBackStack();
                    fragment = global.GetCurSearchFragment();
					global.SetCurFragment(FragmentTypes.Search);
                    fragmentOperations.LoadFragment(fragment);

                    return true;

                case R.id.mappings:
                    if (clearBackStack)
                        fragmentOperations.ClearBackStack();
                    fragment = global.GetCurMappingsFragment();
					global.SetCurFragment(FragmentTypes.Mappings);
                    fragmentOperations.LoadFragment(fragment);

                    return true;


                case R.id.notifications:
                    if (clearBackStack)
                        fragmentOperations.ClearBackStack();
                    fragment = global.GetCurNotificationsFragment();
					global.SetCurFragment(FragmentTypes.Notifications);
                    fragmentOperations.LoadFragment(fragment);

                    return true;

                case R.id.settings:
                    if (clearBackStack)
                        fragmentOperations.ClearBackStack();
                    fragment = global.GetCurSettingsFragment();
                    global.SetCurFragment(FragmentTypes.Settings);
                    fragmentOperations.LoadFragment(fragment);

                    return true;

            }
            return true;
        }
    };
    //endregion


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
