package com.example.lighthead.androidcustomcalendar;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View view;

    private static final int daysInWeek = 7;
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Button addEventButton;
    private Spinner firstWeekDaySelect;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private GridAdapter mAdapter;
    private Map weekDaysMap;
    private int firstWeekDayNumber = 1;
    private final String[] weekDaysList={"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);



        return fragment;
    }


    private void initializeUILayout(){

       /* LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_schedule, this);*/

        createWeekDaysTextViews();
        previousButton = (ImageView)view.findViewById(R.id.previous_month);
        nextButton = (ImageView)view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.display_current_date);
        addEventButton = (Button)view.findViewById(R.id.add_calendar_event);
        calendarGridView = (GridView)view.findViewById(R.id.calendar_grid);
        firstWeekDaySelect = (Spinner)view.findViewById(R.id.firstWeekDaySelect);

        ArrayAdapter<String> weekDaysAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, weekDaysList);
        weekDaysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstWeekDaySelect.setAdapter(weekDaysAdapter);

    }

    private void setDaysInWeekItemSelectEvent() {
        firstWeekDaySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //  String item = firstWeekDaySelect.getSelectedItem().toString();
                int pos = (position<6)?position+1:0;
                //Toast.makeText(context, String.valueOf(pos), Toast.LENGTH_LONG).show();
                firstWeekDayNumber = pos;
                createWeekDaysTextViews();
                setUpCalendarAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }
    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
            }
        });
    }
  /*  private void setGridCellClickEvents(){
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tvItem = ((TextView)view.findViewById(R.id.calendar_date_id));
                String strItem = tvItem.getText().toString();

                Toast.makeText(context, "Clicked: " + currentDate.getText() + ", " +  strItem + " " , Toast.LENGTH_LONG).show();
            }


        });
    }*/






    private void createWeekDaysTextViews(){

        weekDaysMap = WeekDaysMap.createMap();

        /*
        *  <TextView
            android:id="@+id/mon"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:textColor="@color/colorWhite"
            android:text="@string/mon"
            android:layout_marginTop="4dp"
            android:textStyle="bold"
            android:layout_weight="1"/>


            .....

        * */
        LinearLayout layout = view.findViewById(R.id.weekDays);
        fillWeekDaysByFirstWeekDay(layout);
    }

    private LinearLayout fillWeekDaysByFirstWeekDay(LinearLayout layout) {

        if (firstWeekDayNumber>daysInWeek) return layout;
        layout.removeAllViews();

        for(int i=firstWeekDayNumber; i<daysInWeek; i++) {

            createSingleWeekDayTextView(getResources().getString((int)weekDaysMap.get(i)), layout);
        }

        for(int i=0;i<firstWeekDayNumber;i++) {
            createSingleWeekDayTextView(getResources().getString((int)weekDaysMap.get(i)), layout);
        }


        return layout;

    }

    private LinearLayout createSingleWeekDayTextView(String day, LinearLayout layout){

        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                0, // Width of TextView
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.topMargin = 4;

        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setText(day);

        layout.addView(tv);
        return layout;
    }

    private void setUpCalendarAdapter(){
        List<Date> dayValueInCells = new ArrayList<Date>();
        //   mQuery = new DatabaseQuery(context);
        //    List<EventObjects> mEvents = mQuery.getAllFutureEvents();
        Calendar mCal = (Calendar)cal.clone();

        // mCal.setFirstDayOfWeek(Calendar.MONDAY);

        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1 - firstWeekDayNumber;

        Log.d("MyLog", String.valueOf(firstDayOfTheMonth));

        if (firstDayOfTheMonth>=0)
            mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        else
            mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth - daysInWeek);

        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            dayValueInCells.add(mCal.getTime());

            // Log.d("MyLog", mCal.getTime().toString());

            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        for (Date date:dayValueInCells
        ) {
            Log.d("MyLog", date.toString());
        }

        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);



        ICalendarCellClick iCalendarCellClick = new ICalendarCellClick() {
            @Override
            public void OnClick(Bundle bundle) {
                //Toast.makeText(getContext(), "Clicked: " + message , Toast.LENGTH_LONG).show();
                TaskListFragment taskListFragment = new TaskListFragment();
                taskListFragment.setArguments(bundle);
                loadFragment(taskListFragment);


            }
        };


        mAdapter = new GridAdapter(getContext(), dayValueInCells, cal, iCalendarCellClick
                //   , mEvents
        );
        calendarGridView.setAdapter(mAdapter);
    }

    private void loadFragment(Fragment fragment) {


        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        // setGridCellClickEvents();


        setDaysInWeekItemSelectEvent();
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
