package com.example.lighthead.androidcustomcalendar.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;

import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.adapters.ExpandableMappingIntervalsAdapter;

import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.helpers.GroupedMappingResultsCollection;
import com.example.lighthead.androidcustomcalendar.helpers.communication.SocketClient;
import com.example.lighthead.androidcustomcalendar.helpers.mapping.MappingResultElement;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MappingResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MappingResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MappingResultsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //region Controls
    private ExpandableListView expandableMappingLv;

    private CheckBox allParticipantsCb;
    private CheckBox allParticipantsWithoutOneCb;
    private CheckBox otherResultsCb;
    //endregion

    private Activity currentActivity;
    private ArrayList<MappingResultElement> allMappingResultElements;

    private JSONArray curMappingResultsData = new JSONArray();

    private SocketClient socketClient = new SocketClient();
    private SharedPreferencesOperations sp = new SharedPreferencesOperations();

    public MappingResultsFragment() {

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MappingResultsFragment.
     */
    public static MappingResultsFragment newInstance(String param1, String param2) {
        MappingResultsFragment fragment = new MappingResultsFragment();
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

        View view = inflater.inflate(R.layout.fragment_mapping_results, container, false);

        //region Find and init controls
        allParticipantsCb = view.findViewById(R.id.allParticapants);
        allParticipantsCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UpdateResultsList();
            }
        });

        allParticipantsWithoutOneCb = view.findViewById(R.id.allParticapantsWithoutOne);
        allParticipantsWithoutOneCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UpdateResultsList();
            }
        });

        otherResultsCb = view.findViewById(R.id.other);
        otherResultsCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UpdateResultsList();
            }
        });

        expandableMappingLv = view.findViewById(R.id.mappingIntervals);
        //endregion

        //region Get mapping arguments
        Bundle mappingArguments = getArguments();
        JSONObject jsonMappingData = null;
        try {
            jsonMappingData = new JSONObject(mappingArguments.getString("jsonMappingData"));

            String username = sp.GetLogin();
            socketClient.SetMappingAnswerListener(onMappingAnswer);
            socketClient.GetMappingsResult(jsonMappingData, username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion

        return view;
    }


    //region Mapping results listener

    private Emitter.Listener onMappingAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];

                    if (data.toString().equals(curMappingResultsData.toString())) return;
                    else
                        curMappingResultsData = data;

                    allMappingResultElements = new ArrayList<>();
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonobject;
                        try {
                            MappingResultElement mappingResultElement = new MappingResultElement();

                            jsonobject = data.getJSONObject(i);
                            String from = jsonobject.getString("from");
                            Calendar dtFrom = ConvertDateAndTime.GetCalendarDateTimeFromISOString(from);
                            String to = jsonobject.getString("to");
                            Calendar dtTo = ConvertDateAndTime.GetCalendarDateTimeFromISOString(to);
                            mappingResultElement.SetDateTimeFrom(dtFrom);
                            mappingResultElement.SetDateTimeTo(dtTo);


                            int amount = jsonobject.getInt("amount");
                            mappingResultElement.SetAmount(amount);

                            JSONArray array = jsonobject.getJSONArray("freeUsers");
                            int size = array.length();
                            ArrayList<String> users = new ArrayList<>();
                            for (int j=0;j<size;j++) {
                                String user = (String) array.get(j);
                                users.add(user);
                            }
                            mappingResultElement.SetUserList(users);

                            allMappingResultElements.add(mappingResultElement);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }


                    int maxParticipants = allMappingResultElements.get(0).GetAmount();
                    String maxParticipantsStr = Integer.toString(maxParticipants);
                    String maxParticipantsWithoutOneStr = Integer.toString(maxParticipants - 1);

                    allParticipantsCb.setText("All(" + maxParticipantsStr + ") persons");
                    allParticipantsWithoutOneCb.setText(maxParticipantsWithoutOneStr + " person(s)");
                    otherResultsCb.setText("Other");


                    allParticipantsCb.setChecked(true);
                    allParticipantsWithoutOneCb.setChecked(true);
                    otherResultsCb.setChecked(true);

                    ShowMappingVisibleResultsElements(allMappingResultElements);



                }
            });
        }
    };



    //endregion

    // region Commands

    private void UpdateResultsList() {
        ArrayList<MappingResultElement> filteredResults = new ArrayList<>();

        boolean isAll = allParticipantsCb.isChecked();
        boolean isAllWithoutOne = allParticipantsWithoutOneCb.isChecked();
        boolean isOther = otherResultsCb.isChecked();

        int maxParticipants = allMappingResultElements.get(0).GetAmount();


        for (MappingResultElement element: allMappingResultElements) {
                if ((element.GetAmount()==maxParticipants)&&(isAll))
                    filteredResults.add(element);
                if ((element.GetAmount()==maxParticipants-1)&&(isAllWithoutOne))
                    filteredResults.add(element);
                if ((element.GetAmount()<maxParticipants-1)&&(isOther))
                    filteredResults.add(element);
        }

        ShowMappingVisibleResultsElements(filteredResults);


    }

    private void ShowMappingVisibleResultsElements(ArrayList<MappingResultElement> mappingResultElements) {

        ArrayList<String> intervalsGroupsList = GetGroupsList(mappingResultElements);
        ArrayList<GroupedMappingResultsCollection> groupedByDateMappingResultsList = new ArrayList<>();

        for (String group: intervalsGroupsList) {

            ArrayList<MappingResultElement> curDateElements = GetCurDateMappingResultsList(mappingResultElements, group);
            groupedByDateMappingResultsList.add(new GroupedMappingResultsCollection(group, curDateElements));
        }


        ExpandableMappingIntervalsAdapter adapter = new ExpandableMappingIntervalsAdapter(getContext(), intervalsGroupsList, groupedByDateMappingResultsList,
                R.layout.mappingresultgrouplayout, R.layout.singlemappingintervallayout);
        expandableMappingLv.setAdapter(adapter);

    }


    private ArrayList<String> GetGroupsList(ArrayList<MappingResultElement> mappingVisibleResultElements) {

        ArrayList<String> resultsList = new ArrayList<>();

        Collections.sort(mappingVisibleResultElements, MappingResultElement.DateTimeFromComparator);

        for(MappingResultElement mappingResultElement:mappingVisibleResultElements) {


                Calendar dateTimeFrom = mappingResultElement.GetDateTimeFrom();
                String dateFrom  = ConvertDateAndTime.GetStringDateFromCalendar(dateTimeFrom);
                Calendar dateTimeTo = mappingResultElement.GetDateTimeTo();
                String dateTo = ConvertDateAndTime.GetStringDateFromCalendar(dateTimeTo);

                if (!resultsList.contains(dateFrom))
                    resultsList.add(dateFrom);

        }

        return resultsList;
    }


    private ArrayList<MappingResultElement> GetCurDateMappingResultsList(ArrayList<MappingResultElement> mappingResultElements, String date) {

        ArrayList<MappingResultElement> resultsList = new ArrayList<>();


        for(MappingResultElement mappingVisibleResultElement:mappingResultElements) {

                Calendar dateTimeFrom = mappingVisibleResultElement.GetDateTimeFrom();
                String dateFrom  = ConvertDateAndTime.GetStringDateFromCalendar(dateTimeFrom);
                Calendar dateTimeTo = mappingVisibleResultElement.GetDateTimeTo();
                String dateTo = ConvertDateAndTime.GetStringDateFromCalendar(dateTimeTo);

                if (dateFrom.equals(date)&&(dateTo.equals(date)))
                    resultsList.add(mappingVisibleResultElement);
        }

        return resultsList;
    }

    //endregion

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
        void onFragmentInteraction(Uri uri);
    }
}
