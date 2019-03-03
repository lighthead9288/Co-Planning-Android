package com.example.lighthead.androidcustomcalendar;

import android.app.ActionBar;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

public class MainActivity extends AppCompatActivity implements ScheduleFragment.OnFragmentInteractionListener ,
        SearchFragment.OnFragmentInteractionListener, MappingsFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener,
        CalendarFragment.OnFragmentInteractionListener, TaskListFragment.OnFragmentInteractionListener, TaskEditorFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    Global global = new Global();

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





      //  CalendarCustomView mView = (CalendarCustomView)findViewById(R.id.calendar_layout);
        //mView.setVisibility(View.VISIBLE);
       /* XmlPullParser parser = getResources().getXml(R.xml.calendarcustomxml);
        try {
            parser.next();
            parser.nextTag();
        } catch (Exception e) {
            e.printStackTrace();
        }

        AttributeSet attr = Xml.asAttributeSet(parser);

        CalendarCustomView mView = new CalendarCustomView(getApplicationContext(), attr);

        LinearLayout layout = findViewById(R.id.activity_custom_calendar);
        layout.addView(mView);*/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Fragment fragment = new CalendarFragment();
        global.SetCurSchedFragment(fragment);
        loadFragment(fragment);
       /* intent = new Intent(getApplicationContext(), ScheduleActivity.class);
        startActivity(intent);*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.mySchedule:
                    Fragment curSchedFrag = global.GetCurSchedFragment();
                   // if (curSchedFrag.equals("calendar")) {
                   //     fragment = new CalendarFragment();
                        loadFragment(curSchedFrag);


                   // }


                    return true;
                case R.id.search:

                    fragment = new SearchFragment();
                   // Bundle bundle = new Bundle();
                  //  bundle.putString("CurSearchFragment", "StartSearch");
                  //  fragment.setArguments(bundle);
                    loadFragment(fragment);

                    return true;

                case R.id.mappings:
                    fragment = new MappingsFragment();
                    loadFragment(fragment);

                    return true;


                case R.id.notifications:
                    fragment = new NotificationsFragment();
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
