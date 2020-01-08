package com.example.lighthead.androidcustomcalendar.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.helpers.mapping.SavedMapping;

import java.util.ArrayList;

public class SavedMappingsAdapter extends ArrayAdapter<SavedMapping> {
    public SavedMappingsAdapter(@NonNull Context context, int resource, ArrayList<SavedMapping> savedMappings) {
        super(context, resource, savedMappings);

        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.SavedMappings = savedMappings;



    }

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<SavedMapping> SavedMappings;


    public View getView(int position, View convertView, ViewGroup parent){

        View view = inflater.inflate(layout, parent, false);

        SavedMapping curElement = SavedMappings.get(position);

        TextView dateTimeFromTv = view.findViewById(R.id.dateTimeFrom);
        String dateFrom = ConvertDateAndTime.GetISOStringDateFromCalendar(curElement.GetDateTimeFrom());
        String timeFrom = ConvertDateAndTime.GetISOStringTimeFromCalendar(curElement.GetDateTimeFrom());
        dateTimeFromTv.setText(dateFrom + ", " + timeFrom);

        TextView dateTimeToTv = view.findViewById(R.id.dateTimeTo);
        String dateTo = ConvertDateAndTime.GetISOStringDateFromCalendar(curElement.GetDateTimeTo());
        String timeTo = ConvertDateAndTime.GetISOStringTimeFromCalendar(curElement.GetDateTimeTo());
        dateTimeToTv.setText(dateTo + ", " + timeTo);


        TextView participantsTv = view.findViewById(R.id.participants);
        ArrayList<String> participants = curElement.GetParticipants();

        String participantsListStr = "";
        for(String participant: participants) {
            participantsListStr +=participant + "; ";
        }

        participantsTv.setText(participantsListStr);

        return view;

    }
}
