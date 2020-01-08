package com.example.lighthead.androidcustomcalendar;

import android.app.Application;
import android.content.Context;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class BadgesOperations extends Application {

    private static BottomNavigationView navigation;
    private static Context MainActivityContext;
    private static View mappingBadge;
    private static TextView mappingText;
    private static View notificationBadge;
    private static TextView notificationText;

    public void Init(BottomNavigationView view, Context context) {
        navigation = view;
        MainActivityContext = context;



        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);

        BottomNavigationItemView mappingItemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(2);
        mappingBadge = LayoutInflater.from(MainActivityContext)
                .inflate(R.layout.notificationbadge, mappingItemView, true);

        mappingText = mappingBadge.findViewById(R.id.notifications_badge);
        mappingText.setVisibility(View.GONE);

        BottomNavigationItemView notificationItemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(3);
        notificationBadge = LayoutInflater.from(MainActivityContext)
                .inflate(R.layout.notificationbadge, notificationItemView, true);
        notificationText = notificationBadge.findViewById(R.id.notifications_badge);

        notificationText.setVisibility(View.GONE);


    }



    public void SetMappingsAmount(int amount) {
        if (amount==0)
            mappingText.setVisibility(View.GONE);
        else
            mappingText.setVisibility(View.VISIBLE);

        mappingText.setText(String.valueOf(amount));

    }

    public void ClearMappingsAmount() {
        mappingText = mappingBadge.findViewById(R.id.notifications_badge);
        mappingText.setText("0");
        mappingText.setVisibility(View.GONE);
    }

    public void SetNotificationsAmount(int amount) {
        if (amount==0)
            notificationText.setVisibility(View.GONE);
        else
            notificationText.setVisibility(View.VISIBLE);

        notificationText.setText(String.valueOf(amount));
    }

    public void ClearNotificationsAmount() {
        notificationText = notificationBadge.findViewById(R.id.notifications_badge);
        notificationText.setText("0");
        notificationText.setVisibility(View.GONE);
    }

    public int GetNotificationsAmount() {
        String str = notificationText.getText().toString();

        if (str.equals(""))
            return 0;

        int result = Integer.valueOf(notificationText.getText().toString());


        return result;
    }

}
