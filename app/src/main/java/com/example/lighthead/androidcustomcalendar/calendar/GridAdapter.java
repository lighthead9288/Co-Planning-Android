package com.example.lighthead.androidcustomcalendar.calendar;

import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.fragments.TaskListFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GridAdapter extends ArrayAdapter implements TaskListFragment.OnFragmentInteractionListener {
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private ICalendarCellClick ICalendar;


    public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, ICalendarCellClick iCalendarCellClick)
    {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.ICalendar = iCalendarCellClick;
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

        Calendar today = new GregorianCalendar();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH) + 1;
        int todayDate = today.get(Calendar.DAY_OF_MONTH);

        View view = convertView;


        if (view == null) {
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }

        //Add day to calendar
        TextView cellNumber = (TextView) view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));


        if (displayMonth == todayMonth && displayYear == todayYear && dayValue == todayDate) {
           // view.setBackgroundColor(Color.parseColor("#a61b03"));
            cellNumber.setTextColor(Color.parseColor("#a61b03"));
        }

        else if (displayMonth == currentMonth && displayYear == currentYear) {
           // view.setBackgroundColor(Color.parseColor("#FF5733"));
            cellNumber.setTextColor(Color.parseColor("#FF5733"));


        } else {
           // view.setBackgroundColor(Color.parseColor("#cccccc"));
            cellNumber.setTextColor(Color.parseColor("#cccccc"));

        }



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Iterator iterator = selectedDays.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry) iterator.next();
                    View curView = (View) pair.getKey();
                    int curColor = (int) pair.getValue();

                    //curView.setBackgroundColor(curColor);
                    TextView cellNumber = (TextView) curView.findViewById(R.id.calendar_date_id);
                    cellNumber.setTextColor(curColor);
                }

             //   ColorDrawable viewColor = (ColorDrawable) v.getBackground();
             //   int prevBackgroundColor = viewColor.getColor();

                //v.setBackgroundColor(Color.parseColor("#8c4c22"));

                TextView cellNumber = (TextView) v.findViewById(R.id.calendar_date_id);
                int prevBackgroundColor = cellNumber.getCurrentTextColor();
                cellNumber.setTextColor(Color.parseColor("#8c4c22"));


                Calendar calendar = new GregorianCalendar();
                calendar.set(displayYear, displayMonth-1, dayValue);

                if (ICalendar!=null)
                    ICalendar.OnClick(calendar);

                selectedDays.put(v, prevBackgroundColor);

            }
        });



        return view;
    }

    private static HashMap<View, Integer> selectedDays = new HashMap<>();



    @Override
    public void onFragmentInteraction(Uri uri) {

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

