package com.example.lighthead.androidcustomcalendar.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.adapters.NotificationAdapter;
import com.example.lighthead.androidcustomcalendar.helpers.Notification;
import com.example.lighthead.androidcustomcalendar.helpers.NotificationChanges;
import com.example.lighthead.androidcustomcalendar.helpers.communication.SocketClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Activity currentActivity;

    private ListView notificationsListLv;

    private String login;

    private ArrayList<Notification> notificationsList = new ArrayList<>();

    private NotificationAdapter notificationAdapter;

    private SocketClient socketClient = new SocketClient();
    private SharedPreferencesOperations sp = new SharedPreferencesOperations();
    private BadgesOperations badgesOperations = new BadgesOperations();


    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        //region Find and init controls
        notificationsListLv = view.findViewById(R.id.notificationsList);
        //endregion

        login  = sp.GetLogin();

        badgesOperations.ClearNotificationsAmount();

        socketClient.SetNotificationsListener(onGetNotifications);

        socketClient.SetFullNotificationsListListener(onGetFullNotificationsList);
        socketClient.GetFullNotificationsList(login);

        // Inflate the layout for this fragment
        return view;
    }

    //region Get notifications listener
    private Emitter.Listener onGetNotifications = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String sender = (String) args[0];
                    String description = (String) args[1];
                    String dateTime = (String) args[2];
                    JSONObject additionalInfo = (JSONObject) args[3];

                    NotificationChanges notificationChanges = GetNotificationAdditionalInfoFromJSONObject(additionalInfo);


                    JSONObject jsonNotification = new JSONObject();
                    try {
                        jsonNotification.put("dateTime", dateTime);
                        jsonNotification.put("receiver", login);
                        jsonNotification.put("sender", sender);
                        jsonNotification.put("description", description);
                        jsonNotification.put("status", false);


                        NotifyAboutReadNotifications(new JSONArray().put(jsonNotification));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Notification newNotification = new Notification(dateTime, sender, description, false, notificationChanges);
                    notificationsList.add(0, newNotification);
                    UpdateNotificationsList();


                }
            });

        }
    };
    //endregion

    //region Get full notifications list listener
    private Emitter.Listener onGetFullNotificationsList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray notifications = (JSONArray) args[0];

                    JSONArray unreadNotifications = new JSONArray();

                    notificationsList.clear();

                    for (int i=0; i<notifications.length(); i++) {
                        try {
                            JSONObject notification = notifications.getJSONObject(i);
                            String dateTime = notification.getString("dateTime");
                            String sender = notification.getString("sender");
                            String description = notification.getString("description");

                            Boolean status = notification.getBoolean("status");

                            if (!status)
                                unreadNotifications.put(notification);

                            JSONObject additionalInfo = notification.getJSONObject("additionalInfo");

                            NotificationChanges notificationChanges = GetNotificationAdditionalInfoFromJSONObject(additionalInfo);


                            notificationsList.add(new Notification(dateTime,  sender, description, status, notificationChanges));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    UpdateNotificationsList();

                    NotifyAboutReadNotifications(unreadNotifications);

                }
            });
        }
    };
    //endregion

    //region Commands
    private NotificationChanges GetNotificationAdditionalInfoFromJSONObject(JSONObject additionalInfo) {
        NotificationChanges notificationChanges = new NotificationChanges();

        String nameChanges = null;
        try {
            nameChanges = additionalInfo.getString("name");
            notificationChanges.SetName(nameChanges);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String commentChanges = null;
        try {
            commentChanges = additionalInfo.getString("comment");
            notificationChanges.SetComment(commentChanges);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String dateTimeFromChanges = null;
        try {
            dateTimeFromChanges = additionalInfo.getString("dateTimeFrom");
            notificationChanges.SetDateTimeFrom(dateTimeFromChanges);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String dateTimeToChanges = null;
        try {
            dateTimeToChanges = additionalInfo.getString("dateTimeTo");
            notificationChanges.SetDateTimeTo(dateTimeToChanges);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return notificationChanges;
    }

    private void UpdateNotificationsList() {
        notificationAdapter = new NotificationAdapter(getContext(), R.layout.singlenotificationlayout, notificationsList);
        notificationsListLv.setAdapter(notificationAdapter);
    }

    private void NotifyAboutReadNotifications(JSONArray notifications) {
        socketClient.ChangeNotificationsStatus(login, notifications);
    }
    //endregion


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            currentActivity=(Activity) context;
        }

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
        socketClient.SetFullNotificationsListListener(null);
        socketClient.SetNotificationsListener(null);
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
