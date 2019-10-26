package com.example.lighthead.androidcustomcalendar.helpers.communication;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketClient extends Application {


    public SocketClient() {

    }

    public SocketClient(Activity act, BadgesOperations ops) {
        activity = act;
        badgesOperations = ops;
    }

    private static Activity activity;

    private static BadgesOperations badgesOperations;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000/");
        } catch (URISyntaxException e) {
            Log.d("MyLog", e.getMessage());
            String err = e.getMessage();
        }
    }


    private static Emitter.Listener SearchAnswerListener;
    private static Emitter.Listener SavedMappingsListener;
    private static Emitter.Listener UnavailableTimeAnswerListener;
    private static Emitter.Listener MappingAnswerListener;
    private static Emitter.Listener UserSubscribeListener;
    private static Emitter.Listener UserTaskSubscribeListener;

    private static Emitter.Listener NotificationsListener;
    private static Emitter.Listener FullNotificationsListListener;
    private static String CurrentUser;

    private Emitter.Listener DefaultNotificationsListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String sender = (String)args[0];
                    String description = (String)args[1];

                    int notifAmount = badgesOperations.GetNotificationsAmount() + 1;
                    badgesOperations.SetNotificationsAmount(notifAmount);

                }
            });
        }
    };

    private Emitter.Listener DefaultFullNotificationsListListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray notifications = (JSONArray) args[0];

                    int unreadNotificationsAmount = 0;

                    for (int i=0; i<notifications.length(); i++) {
                        try {
                            JSONObject notification = notifications.getJSONObject(i);
                            Boolean status = notification.getBoolean("status");

                            if (!status)
                                unreadNotificationsAmount++;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    badgesOperations.SetNotificationsAmount(unreadNotificationsAmount);

                }
            });
        }
    };

    public void Connect() {
        mSocket.connect();
    }

    public void Disconnect() {
        mSocket.disconnect();
    }

    public void OffListeners() {
        mSocket.off();
    }

    public void SetSearchAnswerListener(Emitter.Listener listener) {
        SearchAnswerListener = listener;
        mSocket.on("search", SearchAnswerListener);

    }

    public void Search(String text) {
        mSocket.emit("search", text);
    }

    public void SetSavedMappingsListener(Emitter.Listener listener) {
        SavedMappingsListener = listener;
        mSocket.on("getUserSearchesList", SavedMappingsListener);
    }

    public void GetSavedMappings(String username) {
        mSocket.emit("getUserSearchesList", username);
    }

    public void GetFullNotificationsList(String username) {
        mSocket.emit("notifications", username);
    }

    public void ChangeNotificationsStatus(String username, JSONArray notifications) {
        mSocket.emit("changeNotificationStatus", username, notifications);
    }

    public void SetUnavailableTimeAnswer(Emitter.Listener listener) {
        UnavailableTimeAnswerListener = listener;
        mSocket.on("unavailableTime", UnavailableTimeAnswerListener);
    }

    public void GetUserUnavailableTime(String username) {
        mSocket.emit("unavailableTime", username);
    }

    public void SetUserUnavailableTime(String username, JSONObject unavailableTime) {
        mSocket.emit("unavailableTime", username, unavailableTime);
    }

    public void SetMappingAnswerListener(Emitter.Listener listener) {
        MappingAnswerListener = listener;
        mSocket.on("mapping", MappingAnswerListener);
    }

    public void GetMappingsResult(JSONObject data, String username) {
        mSocket.emit("mapping", data, username);
    }

    public void SetUserSubscribeListener(Emitter.Listener listener) {
        UserSubscribeListener = listener;
        mSocket.on("subscribe", UserSubscribeListener);
    }

    public void SubscribeOnUser(String receiver, String subscriber, Boolean direction) {
        mSocket.emit("subscribe", receiver, subscriber, direction);
    }

    public void SetUserTaskSubscribeListener(Emitter.Listener listener) {
        UserTaskSubscribeListener = listener;
        mSocket.on("subscribe", UserTaskSubscribeListener);
    }

    public void SubscribeOnUserTask(String receiver, String subscriber, Boolean direction, int taskId) {
        mSocket.emit("subscribe", receiver, subscriber, direction, taskId);
    }


    public void SetNotificationsReceiver(String username) {
        CurrentUser = username;
        mSocket.on("changes_" + CurrentUser, NotificationsListener);

    }

    public void SetNotificationsListener(Emitter.Listener listener) {

        if (listener==null) {
            mSocket.off("changes_" + CurrentUser, NotificationsListener);
            NotificationsListener = DefaultNotificationsListener;
            mSocket.on("changes_" + CurrentUser, NotificationsListener);
        }
        else {
            mSocket.off("changes_" + CurrentUser, NotificationsListener);
            NotificationsListener = listener;
            mSocket.on("changes_" + CurrentUser, NotificationsListener);
        }
    }


    public void SetFullNotificationsListListener(Emitter.Listener listener) {

        if (listener==null) {
            mSocket.off("notifications", FullNotificationsListListener);
            FullNotificationsListListener = DefaultFullNotificationsListListener;
            mSocket.on("notifications", FullNotificationsListListener);
        }

        else {
            mSocket.off("notifications", FullNotificationsListListener);
            FullNotificationsListListener = listener;
            mSocket.on("notifications", FullNotificationsListListener);
        }
    }




}
