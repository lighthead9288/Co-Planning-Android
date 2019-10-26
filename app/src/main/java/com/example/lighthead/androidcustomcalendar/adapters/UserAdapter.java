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

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.MappingElementsManager;
import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.communication.SocketClient;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.models.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private LayoutInflater inflater;
    private int layout;
    private List<User> Users;

    private Activity currentActivity;

    //region Controls
    private ImageButton curUserSubscribeButton;
    private ImageButton addToMappingButton;
    //endregion

    private ArrayList<String> mapElements;

    private SharedPreferencesOperations sp = new SharedPreferencesOperations();
    private SocketClient socketClient = new SocketClient();
    private MappingElementsManager mappingElementsManager = new MappingElementsManager();
    private BadgesOperations badgesOperations = new BadgesOperations();

    public UserAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);

        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.Users = users;

        if (context instanceof Activity){
            currentActivity=(Activity) context;
        }

    }


    public View getView(int position, View convertView, ViewGroup parent){

        View view = inflater.inflate(layout, parent, false);

        final String curUserLogin = sp.GetLogin();

        final User selectedUser = Users.get(position);

        ArrayList<String> subscriberList = selectedUser.GetSubscriberList();

        TextView usernameTv = view.findViewById(R.id.username);
        TextView fullNameTv = view.findViewById(R.id.fullName);
        usernameTv.setText(selectedUser.GetUsername());
        fullNameTv.setText(selectedUser.GetName()+" " + selectedUser.GetSurname());

        final ImageButton subscribeButton = view.findViewById(R.id.subscribeButton);
        subscribeButton.setFocusable(false);
        subscribeButton.setFocusableInTouchMode(false);

        if (curUserLogin.equals(selectedUser.GetUsername()))
            subscribeButton.setVisibility(View.GONE);
        else {

            if (subscriberList.contains(curUserLogin)) {

                subscribeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        socketClient.SubscribeOnUser(selectedUser.GetUsername(), curUserLogin, false);
                        socketClient.SetUserSubscribeListener(onSubscribeAnswer);

                        curUserSubscribeButton = subscribeButton;
                    }
                });
                subscribeButton.setImageResource(R.drawable.subscribe_off);
            }
            else {

                subscribeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        socketClient.SubscribeOnUser(selectedUser.GetUsername(), curUserLogin, true);
                        socketClient.SetUserSubscribeListener(onSubscribeAnswer);

                        curUserSubscribeButton = subscribeButton;
                    }
                });
                subscribeButton.setImageResource(R.drawable.subscribe_on);
            }

        }

        mapElements = mappingElementsManager.GetMappingElements();

        addToMappingButton = view.findViewById(R.id.addToMappingButton);
        addToMappingButton.setFocusable(false);
        addToMappingButton.setFocusableInTouchMode(false);
        addToMappingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMapping(v, selectedUser.GetUsername());
            }
        });

        if(!mapElements.contains(selectedUser.GetUsername())) {
            addToMappingButton.setImageResource(R.drawable.add_to_mapping);

        }

        else {
            addToMappingButton.setImageResource(R.drawable.remove_from_mapping);
        }




        return view;
    }

    public void addToMapping(View view, String username) {

        addToMappingButton = view.findViewById(R.id.addToMappingButton);

        if (!mapElements.contains(username)) {
            addToMappingButton.setImageResource(R.drawable.remove_from_mapping);
            mappingElementsManager.AddMappingElement(username);
            int amount = mappingElementsManager.GetMappingElements().size();
            badgesOperations.SetMappingsAmount(amount);
        }

        else {

            addToMappingButton.setImageResource(R.drawable.add_to_mapping);
            mappingElementsManager.RemoveMappingElement(username);
            int amount = mappingElementsManager.GetMappingElements().size();
            if (amount==0)
                badgesOperations.ClearMappingsAmount();
            else
                badgesOperations.SetMappingsAmount(amount);

        }

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

                    final ImageButton btn = curUserSubscribeButton;

                    if (direction) {
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                socketClient.SubscribeOnUser(subscribeReceiver, newSubscriber, false);
                                socketClient.SetUserSubscribeListener(onSubscribeAnswer);

                                curUserSubscribeButton = btn;
                            }
                        });

                        btn.setImageResource(R.drawable.subscribe_off);

                    }

                    else {
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                socketClient.SubscribeOnUser(subscribeReceiver, newSubscriber, true);
                                socketClient.SetUserSubscribeListener(onSubscribeAnswer);

                                curUserSubscribeButton = btn;
                            }
                        });
                        btn.setImageResource(R.drawable.subscribe_on);
                    }


                }
            });
        }
    };
}
