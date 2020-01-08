package com.example.lighthead.androidcustomcalendar.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.communication.SocketClient;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.models.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SelectedUserTaskAdapter extends ArrayAdapter<ServerTask> {

    private LayoutInflater inflater;
    private int layout;
    private List<ServerTask> Tasks;

    private String Username;

    //region Controls
    private ImageButton curTaskButton;
    //endregion

    private android.app.Activity currentActivity;

    private SharedPreferencesOperations sp = new SharedPreferencesOperations();

    public SelectedUserTaskAdapter(Context context, int resource, List<ServerTask> tasks, String username) {
        super(context, resource, tasks);

        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.Tasks = tasks;
        this.Username = username;

        if (context instanceof Activity){
            currentActivity=(Activity) context;
        }
    }


    SocketClient socketClient = new SocketClient();

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(layout, parent, false);

        TextView tNameView = view.findViewById(R.id.name);
        TextView tCommentView = view.findViewById(R.id.comment);
        TextView tDateTimeInterval = view.findViewById(R.id.dateTimeInterval);

        final String login = sp.GetLogin();

        final int pos = position;
        final ServerTask task = Tasks.get(pos);
        final int curTaskNumber = task.GetTaskNumber();

        tNameView.setText(task.GetName());
        tCommentView.setText(task.GetComment());

        String taskDateTimeInterval;
        String taskDateFrom = task.GetDateFrom();
        taskDateFrom = (taskDateFrom==null)?"??":taskDateFrom;
        String taskTimeFrom = task.GetTimeFrom();
        taskTimeFrom = (taskTimeFrom==null)?"??":taskTimeFrom;
        String taskDateTo = task.GetDateTo();
        taskDateTo = (taskDateTo==null)?"??":taskDateTo;
        String taskTimeTo = task.GetTimeTo();
        taskTimeTo = (taskTimeTo==null)?"??":taskTimeTo;
        taskDateTimeInterval = taskDateFrom + ", " + taskTimeFrom + " - " + taskDateTo + ", " + taskTimeTo;
        tDateTimeInterval.setText(taskDateTimeInterval);


        final ImageButton subscribeTaskButton = view.findViewById(R.id.subscribeTaskButton);


        if (login.equals(Username))
            subscribeTaskButton.setVisibility(View.GONE);
        else {

            ArrayList<String> subscribers = task.GetSubscriberList();

            if (subscribers != null) {

                if (!subscribers.contains(login)) {
                    subscribeTaskButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            socketClient.SubscribeOnUserTask(Username, login, true, curTaskNumber);
                            socketClient.SetUserTaskSubscribeListener(onSubscribeAnswer);

                            curTaskButton = subscribeTaskButton;
                        }
                    });
                    subscribeTaskButton.setImageResource(R.drawable.subscribe_on);
                } else {

                    subscribeTaskButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            socketClient.SubscribeOnUserTask(Username, login, false, curTaskNumber);
                            socketClient.SetUserTaskSubscribeListener(onSubscribeAnswer);

                            curTaskButton = subscribeTaskButton;
                        }
                    });
                    subscribeTaskButton.setImageResource(R.drawable.subscribe_off);
                }
            } else {
                subscribeTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        socketClient.SubscribeOnUserTask(Username, login, true, curTaskNumber);
                        socketClient.SetUserTaskSubscribeListener(onSubscribeAnswer);

                        curTaskButton = subscribeTaskButton;
                    }
                });
                subscribeTaskButton.setImageResource(R.drawable.subscribe_on);
            }
        }

        return view;
    }


    private Emitter.Listener onSubscribeAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final String subscribeReceiver =  (String)args[0];
                    final String newSubscriber =  (String)args[1];
                    final boolean direction = (boolean)args[2];
                    final JSONObject task = (JSONObject) args[3];
                    int taskNumber = 0;
                    try {
                        taskNumber = task.getInt("taskNumber");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    final ImageButton btn = curTaskButton;

                    if (direction) {
                        final int finalTaskNumber = taskNumber;
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                socketClient.SubscribeOnUserTask(subscribeReceiver, newSubscriber, false, finalTaskNumber);
                                socketClient.SetUserTaskSubscribeListener(onSubscribeAnswer);

                                curTaskButton = btn;
                            }
                        });

                        btn.setImageResource(R.drawable.subscribe_off);

                    }

                    else {
                        final int finalTaskNumber = taskNumber;
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                socketClient.SubscribeOnUserTask(subscribeReceiver, newSubscriber, true, finalTaskNumber);
                                socketClient.SetUserTaskSubscribeListener(onSubscribeAnswer);

                                curTaskButton = btn;
                            }
                        });
                        btn.setImageResource(R.drawable.subscribe_on);
                    }


                }
            });
        }
    };
}
