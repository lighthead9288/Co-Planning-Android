package com.example.lighthead.androidcustomcalendar.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.example.lighthead.androidcustomcalendar.MappingElementsManager;
import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.adapters.SavedMappingsAdapter;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.helpers.FragmentOperations;
import com.example.lighthead.androidcustomcalendar.helpers.communication.SocketClient;
import com.example.lighthead.androidcustomcalendar.helpers.mapping.SavedMapping;
import com.example.lighthead.androidcustomcalendar.interfaces.IMappingElementOperations;
import com.example.lighthead.androidcustomcalendar.R;

import com.example.lighthead.androidcustomcalendar.adapters.MappingElementAdapter;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MappingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MappingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MappingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Activity currentActivity;

    private Bundle mappingsParamsBundle;
    private JSONArray curMappingResultsData = new JSONArray();

    private Calendar dateAndTimeFrom = Calendar.getInstance();
    private Calendar dateAndTimeTo = Calendar.getInstance();

    //region Mapping intervals filling flags
    boolean isMapDateFromDefined = false;
    boolean isMapTimeFromDefined = false;
    boolean isMapDateToDefined = false;
    boolean isMapTimeToDefined = false;
    //endregion
    //region Controls
    private TextView mappingDateFrom;
    private TextView mappingTimeFrom;
    private TextView mappingDateTo;
    private TextView mappingTimeTo;
    private LinearLayout mapPanelLL;
    private ListView savedMappingsLv;
    private GridView mapElementsGv;
    //endregion

    private Global global = new Global();
    private MappingElementsManager mappingElementsManager = new MappingElementsManager();
    private BadgesOperations badgesOperations = new BadgesOperations();
    private SharedPreferencesOperations sp = new SharedPreferencesOperations();
    private SocketClient socketClient = new SocketClient();

    private View view;



    public MappingsFragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MappingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MappingsFragment newInstance(String param1, String param2) {
        MappingsFragment fragment = new MappingsFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mappings, container, false);

        //region Find and init controls
        mappingDateFrom = view.findViewById(R.id.mappingDateFrom);
        mappingDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFrom(v);
            }
        });

        mappingTimeFrom = view.findViewById(R.id.mappingTimeFrom);
        mappingTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeFrom(v);
            }
        });

        mappingDateTo = view.findViewById(R.id.mappingDateTo);
        mappingDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTo(v);
            }
        });

        mappingTimeTo = view.findViewById(R.id.mappingTimeTo);
        mappingTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeTo(v);
            }
        });


        mappingsParamsBundle = getArguments();
        if (mappingsParamsBundle!=null) {
            String strDateFrom = mappingsParamsBundle.getString("DateFrom");
            if (strDateFrom != null)
                mappingDateFrom.setText(strDateFrom);
            String strTimeFrom = mappingsParamsBundle.getString("TimeFrom");
            if (strTimeFrom != null)
                mappingTimeFrom.setText(strTimeFrom);
            String strDateTo = mappingsParamsBundle.getString("DateTo");
            if (strDateTo != null)
                mappingDateTo.setText(strDateTo);
            String strTimeTo = mappingsParamsBundle.getString("TimeTo");
            if (strTimeTo != null)
                mappingTimeTo.setText(strTimeTo);
        }

        mapElementsGv = view.findViewById(R.id.mapElementsGv);
        mapElementsGv.setNumColumns(GridView.AUTO_FIT);
        mapElementsGv.setHorizontalSpacing(5);
        mapElementsGv.setVerticalSpacing(5);

        InitMappingElements();

        savedMappingsLv = view.findViewById(R.id.savedMappings);
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
                SavedMapping curSavedMapping = (SavedMapping)parent.getItemAtPosition(position);
                ArrayList<String> participants = curSavedMapping.GetParticipants();

                SetDateFrom(curSavedMapping.GetDateTimeFrom());
                SetDateTo(curSavedMapping.GetDateTimeTo());
                SetTimeFrom(curSavedMapping.GetDateTimeFrom());
                SetTimeTo(curSavedMapping.GetDateTimeTo());

                mappingElementsManager.SetMappingsElements(participants);
                badgesOperations.SetMappingsAmount(participants.size());

                UpdateMapPanelState(view, participants);
                InitMappingElements();

            }
        };
        savedMappingsLv.setOnItemClickListener(listener);

        Button runMappingButton = view.findViewById(R.id.runMappingButton);
        runMappingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunMapping(v);
            }
        });
        //endregion

        socketClient.SetSavedMappingsListener(onSearchesAnswer);

        String username = sp.GetLogin();
        socketClient.GetSavedMappings(username);


        return view;
    }



    //region Searches results listener
    private Emitter.Listener onSearchesAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];

                    ArrayList<SavedMapping> savedMappingsResults = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonobject;

                        try {
                            jsonobject = data.getJSONObject(i);

                            String from = jsonobject.getString("dateTimeFrom");
                            Calendar dtFrom = ConvertDateAndTime.GetCalendarDateTimeFromISOString(from);
                            String to = jsonobject.getString("dateTimeTo");
                            Calendar dtTo = ConvertDateAndTime.GetCalendarDateTimeFromISOString(to);

                            JSONArray array = jsonobject.getJSONArray("participantsList");
                            int size = array.length();
                            ArrayList<String> patricipants = new ArrayList<>();
                            for (int j=0;j<size;j++) {
                                String participant = (String) array.get(j);
                                patricipants.add(participant);
                            }



                            savedMappingsResults.add(new SavedMapping(dtFrom, dtTo, patricipants));
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    SavedMappingsAdapter adapter = new SavedMappingsAdapter(getContext(), R.layout.singlesavedmappinglayout, savedMappingsResults);
                    savedMappingsLv.setAdapter(adapter);
                }

            });
        }
    };
    //endregion

    //region Listeners

    private void UpdateMapPanelState(View view, ArrayList<String> mapElements) {
        mapPanelLL = view.findViewById(R.id.mapPanel);
        int amount = mapElements.size();
        if (amount>1)
            mapPanelLL.setVisibility(View.VISIBLE);
        else
            mapPanelLL.setVisibility(View.GONE);
    }


    public void setDateFrom(View view){
        new DatePickerDialog(getContext(), curTaskDateFrom,
                dateAndTimeFrom.get(Calendar.YEAR),
                dateAndTimeFrom.get(Calendar.MONTH),
                dateAndTimeFrom.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener curTaskDateFrom=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTimeFrom.set(Calendar.YEAR, year);
            dateAndTimeFrom.set(Calendar.MONTH, monthOfYear);
            dateAndTimeFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetDateFrom(dateAndTimeFrom);



        }
    };

    public void setDateTo(View view){
        new DatePickerDialog(getContext(), curTaskDateTo,
                dateAndTimeTo.get(Calendar.YEAR),
                dateAndTimeTo.get(Calendar.MONTH),
                dateAndTimeTo.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener curTaskDateTo=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTimeTo.set(Calendar.YEAR, year);
            dateAndTimeTo.set(Calendar.MONTH, monthOfYear);
            dateAndTimeTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetDateTo(dateAndTimeTo);



        }
    };

    public void setTimeFrom(View view) {
        new TimePickerDialog(getContext(), curTaskTimeFrom, dateAndTimeFrom.get(Calendar.HOUR_OF_DAY), dateAndTimeFrom.get(Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener curTaskTimeFrom = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTimeFrom.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTimeFrom.set(Calendar.MINUTE, minute);

            SetTimeFrom(dateAndTimeFrom);
        }
    };

    public void setTimeTo(View view) {
        new TimePickerDialog(getContext(), curTaskTimeTo, dateAndTimeTo.get(Calendar.HOUR_OF_DAY), dateAndTimeTo.get(Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener curTaskTimeTo = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTimeTo.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTimeTo.set(Calendar.MINUTE, minute);

            SetTimeTo(dateAndTimeTo);
        }
    };


    public void RunMapping(View view) {

        if ((!isMapDateFromDefined)||(!isMapTimeFromDefined)||(!isMapDateToDefined)||(!isMapTimeToDefined))
            Toast.makeText(getContext(), "Date and time limits are not defined!" , Toast.LENGTH_LONG).show();

        else {
            String mapDateFrom = mappingDateFrom.getText().toString();
            String isoMapDateFrom = ConvertDateAndTime.ConvertStringDateToISO(mapDateFrom);

            String mapTimeFrom = mappingTimeFrom.getText().toString();
            String isoMapTimeFrom = ConvertDateAndTime.ConvertStringTimeToISO(mapTimeFrom);

            String mapDateTo = mappingDateTo.getText().toString();
            String isoMapDateTo = ConvertDateAndTime.ConvertStringDateToISO(mapDateTo);

            String mapTimeTo = mappingTimeTo.getText().toString();
            String isoMapTimeTo = ConvertDateAndTime.ConvertStringTimeToISO(mapTimeTo);

           // ArrayList<String> mappingElements = global.GetMappingElements();
            ArrayList<String> mappingElements = mappingElementsManager.GetMappingElements();
            JSONArray jsonMappingElements = new JSONArray();
            for (String mElement:mappingElements
            ) {
                jsonMappingElements.put(mElement);
            }

            JSONObject jsonMappingData = new JSONObject();
            try {
                jsonMappingData.put("dateFrom", isoMapDateFrom);
                jsonMappingData.put("timeFrom", isoMapTimeFrom);
                jsonMappingData.put("dateTo", isoMapDateTo);
                jsonMappingData.put("timeTo", isoMapTimeTo);
                jsonMappingData.put("users", jsonMappingElements);
            } catch (JSONException e) {
                e.printStackTrace();
            }




            MappingResultsFragment fragment = new MappingResultsFragment();
            global.SetCurMappingsFragment(fragment);

            Bundle bundle = new Bundle();
            bundle.putString("jsonMappingData", jsonMappingData.toString());
            fragment.setArguments(bundle);

            FragmentOperations fragmentOperations = new FragmentOperations(getFragmentManager());
            fragmentOperations.LoadFragment(fragment);
        }
    }

    //endregion

    //region Commands

    private void InitMappingElements() {
        final ArrayList<String> mapElements = mappingElementsManager.GetMappingElements();
        UpdateMapPanelState(view, mapElements);

        final MappingElementAdapter mapAdapter = new MappingElementAdapter(getContext(), R.layout.singlemappingelement, mapElements);
        IMappingElementOperations iMappingElementOperations = new IMappingElementOperations() {
            @Override
            public void RemoveMappingElement(String element) {

                if (mapElements.contains(element)) {

                    mapElements.remove(element);
                    int amount = mapElements.size();
                    if (amount==0)
                        badgesOperations.ClearMappingsAmount();
                    else
                        badgesOperations.SetMappingsAmount(amount);
                }
                mapAdapter.notifyDataSetChanged();
                UpdateMapPanelState(view, mapElements);

            }
        };

        mapAdapter.SetIMappingElementOperations(iMappingElementOperations);

        mapElementsGv.setAdapter(mapAdapter);
    }

    private void SetDateFrom(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        mappingDateFrom.setText(ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date));

        dateAndTimeFrom.set(year, month, date);

        isMapDateFromDefined = true;

    }

    private void SetTimeFrom(Calendar fullDate) {

        int hours = fullDate.get(Calendar.HOUR_OF_DAY);
        int minutes = fullDate.get(Calendar.MINUTE);

        mappingTimeFrom.setText(ConvertDateAndTime.ConvertToStringTime(hours, minutes));

        dateAndTimeFrom.set(Calendar.HOUR_OF_DAY, hours);
        dateAndTimeFrom.set(Calendar.MINUTE, minutes);

        isMapTimeFromDefined = true;
    }

    private void SetDateTo(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        mappingDateTo.setText(ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date));

        dateAndTimeTo.set(year, month, date);

        isMapDateToDefined = true;

    }

    private void SetTimeTo(Calendar fullDate) {

        int hours = fullDate.get(Calendar.HOUR_OF_DAY);
        int minutes = fullDate.get(Calendar.MINUTE);

        mappingTimeTo.setText(ConvertDateAndTime.ConvertToStringTime(hours, minutes));

        dateAndTimeTo.set(Calendar.HOUR_OF_DAY, hours);
        dateAndTimeTo.set(Calendar.MINUTE, minutes);

        isMapTimeToDefined = true;
    }

    //endregion


    @Override
    public void onPause() {
        super.onPause();

        mappingsParamsBundle = new Bundle();

        mappingsParamsBundle.putString("DateFrom", mappingDateFrom.getText().toString());
        mappingsParamsBundle.putString("TimeFrom", mappingTimeFrom.getText().toString());
        mappingsParamsBundle.putString("DateTo", mappingDateTo.getText().toString());
        mappingsParamsBundle.putString("TimeTo", mappingTimeTo.getText().toString());

        this.setArguments(mappingsParamsBundle);
        Fragment fragment = this;
        global.SetCurMappingsFragment(fragment);
    }


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
        mListener = null;
    }

    @Override
    public void onResume(){
        super.onResume();

        curMappingResultsData = new JSONArray();

        String username = sp.GetLogin();
        socketClient.GetSavedMappings(username);

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
