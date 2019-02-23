package com.example.lighthead.androidcustomcalendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarCustomView extends LinearLayout{
    private static final String TAG = CalendarCustomView.class.getSimpleName();
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

    public CalendarCustomView(Context context) {
        super(context);
    }
    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
       // setGridCellClickEvents();

        setDaysInWeekItemSelectEvent();



    }
    public CalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initializeUILayout(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);

        createWeekDaysTextViews();
        previousButton = (ImageView)view.findViewById(R.id.previous_month);
        nextButton = (ImageView)view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.display_current_date);
        addEventButton = (Button)view.findViewById(R.id.add_calendar_event);
        calendarGridView = (GridView)view.findViewById(R.id.calendar_grid);
        firstWeekDaySelect = (Spinner)view.findViewById(R.id.firstWeekDaySelect);

        ArrayAdapter<String> weekDaysAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, weekDaysList);
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
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }
    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
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
        LinearLayout layout = findViewById(R.id.weekDays);
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

        TextView tv = new TextView(context);
        LayoutParams lp = new LayoutParams(
                0, // Width of TextView
                LayoutParams.WRAP_CONTENT);
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
        mAdapter = new GridAdapter(context, dayValueInCells, cal
             //   , mEvents
        );
        calendarGridView.setAdapter(mAdapter);
    }
}



