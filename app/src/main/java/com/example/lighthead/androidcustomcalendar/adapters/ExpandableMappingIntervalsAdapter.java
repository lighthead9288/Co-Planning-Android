package com.example.lighthead.androidcustomcalendar.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.helpers.GroupedMappingResultsCollection;
import com.example.lighthead.androidcustomcalendar.helpers.mapping.MappingResultElement;


import java.util.ArrayList;
import java.util.Calendar;

public class ExpandableMappingIntervalsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> GroupsList;
    private ArrayList<GroupedMappingResultsCollection> MappingResultsCollection;
    private int GroupLayout;
    private int MappingIntervalLayout;


    public ExpandableMappingIntervalsAdapter(Context context, ArrayList<String> groupsList, ArrayList<GroupedMappingResultsCollection> mappingResultsCollection, int groupLayout, int mappingIntervalLayout) {
        this.context = context;
        this.GroupsList = groupsList;
        this.MappingResultsCollection = mappingResultsCollection;
        this.GroupLayout = groupLayout;
        this.MappingIntervalLayout = mappingIntervalLayout;
    }

    @Override
    public int getGroupCount() {
        return GroupsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return MappingResultsCollection.get(groupPosition).GetResultElements().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return GroupsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return MappingResultsCollection.get(groupPosition).GetResultElements().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);

        ExpandableListView expListView = (ExpandableListView) parent;
        expListView.expandGroup(groupPosition);

        if (convertView==null) {

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.GroupLayout, null);
        }

        TextView groupTitleTv = convertView.findViewById(R.id.groupName);
        groupTitleTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        groupTitleTv.setText(groupTitle);

        return convertView;
    }




    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final MappingResultElement curMappingResultElement = (MappingResultElement) getChild(groupPosition, childPosition);

        if (convertView==null) {

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.MappingIntervalLayout, null);
        }


        Calendar dateTimeFrom = curMappingResultElement.GetDateTimeFrom();
        Calendar dateTimeTo = curMappingResultElement.GetDateTimeTo();
        String dateFrom  = ConvertDateAndTime.GetStringDateFromCalendar(dateTimeFrom);
        String timeFrom = ConvertDateAndTime.GetISOStringTimeFromCalendar(dateTimeFrom);
        String dateTo = ConvertDateAndTime.GetStringDateFromCalendar(dateTimeTo);
        String timeTo = ConvertDateAndTime.GetISOStringTimeFromCalendar(dateTimeTo);


        TextView timeFromTv = convertView.findViewById(R.id.timeFrom);
        TextView timeToTv = convertView.findViewById(R.id.timeTo);

        if (dateFrom.equals(dateTo)) {
            timeFromTv.setText(timeFrom);
            timeToTv.setText(timeTo);

        }

        else {
            timeFromTv.setText(dateFrom + ", " + timeFrom);
            timeToTv.setText(dateTo + ", " + timeTo);
        }

        TextView usersListTv = convertView.findViewById(R.id.usersList);
        ArrayList<String> users = curMappingResultElement.GetUserList();

        String usersView = "";
        for (String user : users
        ) {
            usersView += user + " ";
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(usersView);

        for (final String user : users
        ) {
            ClickableSpan curSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {

                }
            };
            int indexOfCurUser = usersView.indexOf(user);
            spannableStringBuilder.setSpan(curSpan, indexOfCurUser, indexOfCurUser + user.length(), Spanned.SPAN_COMPOSING);
        }

        usersListTv.setText(spannableStringBuilder);
        usersListTv.setMovementMethod(LinkMovementMethod.getInstance());

        return convertView;

    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
