package com.example.lighthead.androidcustomcalendar.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.models.User;
import com.example.lighthead.androidcustomcalendar.adapters.UserAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //region Controls
    private SearchView searchStringSearchView;
    private ListView usersListLv;
    //endregion

    private UserAdapter userAdapter;

    Global global = new Global();

    //region iBadgesOperations
    BadgesOperations badgesOperations = new BadgesOperations();
    //endregion

    //region iSharedPrefsOperations
    SharedPreferencesOperations spa = new SharedPreferencesOperations();
    //endregion

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //region Socket init
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000/");
        } catch (URISyntaxException e) {
            Log.d("MyLog", e.getMessage());
            String err = e.getMessage();
        }
    }
    //endregion



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

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //region Find and init controls

        searchStringSearchView = view.findViewById(R.id.searchString);
        searchStringSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchCommand(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchCommand(newText);
                return false;
            }
        });

        usersListLv = view.findViewById(R.id.usersList);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User selectedUser = (User)parent.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putString("TaskListOption", "ThisWeek");

                UserTaskListFragment userTaskListFragment = new UserTaskListFragment();
                userTaskListFragment.setArguments(bundle);
                //userTaskListFragment.SetISharedPrefsOperations(ISharedPrefsOperations);
                userTaskListFragment.SetTasks(selectedUser.taskList);
                userTaskListFragment.SetUsername(selectedUser.username);
                global.SetCurSearchFragment(userTaskListFragment);
                loadFragment(userTaskListFragment);

            }
        };
        usersListLv.setOnItemClickListener(itemClickListener);

        //endregion


        mSocket.on("search", onSearchAnswer);
        mSocket.connect();

        global.SetCurSearchFragment(this);

        return view;
    }

    //region Search results listener

    private Emitter.Listener onSearchAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];
                    ArrayList<User> usersList = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject jsonobject;
                        try {
                            jsonobject = data.getJSONObject(i);

                            User user = new User();
                            user.username = jsonobject.getString("username");
                            user.name = jsonobject.getString("name");
                            user.surname = jsonobject.getString("surname");

                            JSONArray subscriberArray = jsonobject.getJSONArray("subscriberList");
                            for(int k=0; k<subscriberArray.length();k++) {
                                String subscriber = (String)subscriberArray.get(k);
                                user.subscriberList.add(subscriber);


                            }



                            JSONArray taskArray = jsonobject.getJSONArray("taskList");

                            ArrayList<ServerTask> serverTasks = new ArrayList<>();
                            for (int j=0;j<taskArray.length();j++) {
                                JSONObject jsonObject = taskArray.getJSONObject(j);

                                Gson gson=new Gson();

                                ServerTask sTask = gson.fromJson(jsonObject.toString(), ServerTask.class);

                                serverTasks.add(sTask);

                            }

                            user.taskList = serverTasks;

                            usersList.add(user);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    userAdapter = new UserAdapter(getContext(), R.layout.singleuserlayout, usersList, getActivity());
                    usersListLv.setAdapter(userAdapter);


                }
            });
        }
    };

    //endregion

    //region Commands

    public void SearchCommand(String text) {
        if (text!="") {
            mSocket.emit("search", text);
        }
    }
    //endregion

    //region LoadFragment

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();


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
