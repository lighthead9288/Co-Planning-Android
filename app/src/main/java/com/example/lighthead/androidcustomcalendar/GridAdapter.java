package com.example.lighthead.androidcustomcalendar;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
public class GridAdapter extends ArrayAdapter {
    private static final String TAG = GridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<EventObjects> allEvents;


    public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate
    //        , List<EventObjects> allEvents
    )
    {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
       // this.allEvents = allEvents;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        final Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        final int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        final int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        final int displayYear = dateCal.get(Calendar.YEAR);
        final int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(Color.parseColor("#FF5733"));

        } else {
            view.setBackgroundColor(Color.parseColor("#cccccc"));

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               // Toast.makeText(getContext(), String.valueOf(displayMonth) + " " + String.valueOf(dayValue) + ", " + String.valueOf(displayYear) , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext().getApplicationContext(), TaskListView.class);
              /*  Date SelectedDate = new Date();

                SelectedDate.setMonth(displayMonth-1);
                SelectedDate.setDate(dayValue);

                SelectedDate.setYear(displayYear);*/

                Calendar calendar = new GregorianCalendar();
                calendar.set(displayYear, displayMonth-1, dayValue);


                intent.putExtra("TaskListOption", "Interval");
                intent.putExtra("SelectedDateFrom", calendar);
                intent.putExtra("SelectedDateTo", calendar);
                getContext().startActivity(intent);
            }
        });

        //Add day to calendar
        TextView cellNumber = (TextView) view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));
        //Add events to the calendar
        TextView eventIndicator = (TextView) view.findViewById(R.id.event_id);
        Calendar eventCalendar = Calendar.getInstance();
     //   for (int i = 0; i < allEvents.size(); i++) {
     //       eventCalendar.setTime(allEvents.get(i).getDate());
     //       if (dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
      //              && displayYear == eventCalendar.get(Calendar.YEAR)) {
      //          eventIndicator.setBackgroundColor(Color.parseColor("#FF4081"));
     //       }
     //   }
        return view;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }
}