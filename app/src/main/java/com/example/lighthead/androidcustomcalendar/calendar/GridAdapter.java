package com.example.lighthead.androidcustomcalendar.calendar;

import android.content.Context;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import java.util.List;
public class GridAdapter extends ArrayAdapter implements TaskListFragment.OnFragmentInteractionListener {
    private static final String TAG = GridAdapter.class.getSimpleName();
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
        int currentYear = currentDate.get(Calendar.YEAR);        View view = convertView;
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

               /* Intent intent = new Intent(getContext().getApplicationContext(), TaskListView.class);

                Calendar calendar = new GregorianCalendar();
                calendar.set(displayYear, displayMonth-1, dayValue);

                intent.putExtra("TaskListOption", "Interval");
                intent.putExtra("SelectedDateFrom", calendar);
                intent.putExtra("SelectedDateTo", calendar);
                getContext().startActivity(intent);*/


                Calendar calendar = new GregorianCalendar();
                calendar.set(displayYear, displayMonth-1, dayValue);

                Bundle bundle = new Bundle();
                bundle.putString("TaskListOption", "Interval");
                bundle.putSerializable("SelectedDateFrom", calendar);
                bundle.putSerializable("SelectedDateTo", calendar);

                ICalendar.OnClick(bundle);

            }
        });

        //Add day to calendar
        TextView cellNumber = (TextView) view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));

        return view;
    }



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