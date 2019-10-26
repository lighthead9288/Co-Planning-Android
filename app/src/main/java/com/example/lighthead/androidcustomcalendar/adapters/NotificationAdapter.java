package com.example.lighthead.androidcustomcalendar.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.helpers.Notification;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private LayoutInflater inflater;
    private int layout;
    private List<Notification> Results;


    public NotificationAdapter(Context context, int resource, List<Notification> results) {
        super(context, resource, results);

        this.Results = results;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);


        final Notification currentNotification = Results.get(position);

        TextView dateTimeTv = view.findViewById(R.id.dateTime);

        String date = ConvertDateAndTime.GetISOStringDateFromISOString(currentNotification.GetDateTime());
        String time = ConvertDateAndTime.GetISOStringTimeFromISOString(currentNotification.GetDateTime());

        dateTimeTv.setText(date + ", " + time);

        TextView senderTv = view.findViewById(R.id.sender);
        senderTv.setText(currentNotification.GetSender());
        senderTv.setPaintFlags(senderTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        TextView descriptionTv = view.findViewById(R.id.description);
        descriptionTv.setText(currentNotification.GetDescription());

        TextView additionalInfoTv = view.findViewById(R.id.additionalInfo);

        String additionalInfoText = "";

        String nameChanges = (currentNotification.GetNameChanges()==null)?"":"Name: " + currentNotification.GetNameChanges() + "\n";
        String commentChanges = (currentNotification.GetCommentChanges()==null)?"":" "+ "Comment: " + currentNotification.GetCommentChanges() + "\n";
        String dateTimeFromChanges = (currentNotification.GetDateTimeFromChanges()==null)?"":" "+ "Start date: " + currentNotification.GetDateTimeFromChanges() + "\n";
        String dateTimeToChanges = (currentNotification.GetDateTimeToChanges()==null)?"":" "+ "Finish date: " + currentNotification.GetDateTimeToChanges() + "\n";

        additionalInfoText = nameChanges + commentChanges + dateTimeFromChanges + dateTimeToChanges;

        additionalInfoTv.setText(additionalInfoText);

        return view;

    }


}
