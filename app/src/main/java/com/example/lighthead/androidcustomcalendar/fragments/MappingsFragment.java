package com.example.lighthead.androidcustomcalendar.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.interfaces.IMappingElementOperations;
import com.example.lighthead.androidcustomcalendar.helpers.mapping.MappingResultElement;
import com.example.lighthead.androidcustomcalendar.helpers.mapping.MappingVisibleResultElement;
import com.example.lighthead.androidcustomcalendar.R;

import com.example.lighthead.androidcustomcalendar.adapters.MappingElementAdapter;
import com.example.lighthead.androidcustomcalendar.adapters.MappingResultAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
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

    private Global global = new Global();

    Bundle mappingsParamsBundle;

    Calendar dateAndTimeFrom = Calendar.getInstance();
    Calendar dateAndTimeTo = Calendar.getInstance();

    //region Mapping intervals filling flags
    boolean isMapDateFromDefined = false;
    boolean isMapTimeFromDefined = false;
    boolean isMapDateToDefined = false;
    boolean isMapTimeToDefined = false;
    //endregion

    //region Controls
    TextView mappingDateFrom;
    TextView mappingTimeFrom;
    TextView mappingDateTo;
    TextView mappingTimeTo;
    LinearLayout mapPanelLL;
    ListView mapElementsLv;
    Button showMappingPanelButton;
    //endregion

    public MappingsFragment() {
        // Required empty public constructor
    }


    //region iBadgesOperations
   BadgesOperations badgesOperations = new BadgesOperations();
    //endregion

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
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_mappings, container, false);

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


        final ArrayList<String> mapElements = global.GetMappingElements();
        UpdateMapPanelState(view, mapElements);

        mapElementsLv = view.findViewById(R.id.mapElements);
        final MappingElementAdapter mapAdapter = new MappingElementAdapter(getContext(), R.layout.singlemappingelement, mapElements);
        IMappingElementOperations iMappingElementOperations = new IMappingElementOperations() {
            @Override
            public void RemoveMappingElement(String element) {
                ArrayList<String> mappingElements = global.GetMappingElements();

                if (mappingElements.contains(element)) {

                    mappingElements.remove(element);
                    int amount = mappingElements.size();
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


        mapElementsLv.setAdapter(mapAdapter);


        Button runMappingButton = view.findViewById(R.id.runMappingButton);
        runMappingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunMapping(v);
            }
        });

        showMappingPanelButton = view.findViewById(R.id.showMappingPanelButton);
        showMappingPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMappingPanel(v);
            }
        });
        showMappingPanelButton.setVisibility(View.GONE);

        //endregion

        mSocket.on("mapping", onMappingAnswer);
        mSocket.connect();


        return view;
    }

    //region Mapping results listener

    private Emitter.Listener onMappingAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];

                    ArrayList<MappingResultElement> mappingResults = new ArrayList<>();
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

                            mappingResults.add(mappingResultElement);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }


                    ArrayList<MappingVisibleResultElement> mappingVisibleResultElements = new ArrayList<>();

                    for(int k=global.GetMappingElements().size();k>0;k--) {
                        ArrayList<MappingResultElement> list = new ArrayList<MappingResultElement>();
                        for (MappingResultElement element:
                             mappingResults) {
                            if (element.GetAmount()==k){
                                list.add(element);
                            }

                        }

                        if (list.size()>0) {

                            MappingVisibleResultElement mappingVisibleResultElement = new MappingVisibleResultElement();
                            mappingVisibleResultElement.SetAmount(k);
                            mappingVisibleResultElement.SetIntervalsAndUsers(list);

                            mappingVisibleResultElements.add(mappingVisibleResultElement);
                        }
                    }

                    ListView mappingResultsLv = getView().findViewById(R.id.mappingResults);
                    MappingResultAdapter adapter = new MappingResultAdapter(getContext(), R.layout.singlemappingresultlayout, mappingVisibleResultElements);
                    mappingResultsLv.setAdapter(adapter);
                   // Collections.sort(mappingResults, MappingResultElement.DateTimeFromComparator);
                    HideMappingPanelCommand();
                }
            });
        }
    };

    //endregion

    //region Listeners

    private void ShowMappingPanel(View view) {
        ShowMappingPanelCommand();
    }


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

            ArrayList<String> mappingElements = global.GetMappingElements();
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


            mSocket.emit("mapping", jsonMappingData);
        }
    }

    //endregion

    //region Commands

    private void HideMappingPanelCommand() {
        mapPanelLL.setVisibility(View.GONE);
        mapElementsLv.setVisibility(View.GONE);
        showMappingPanelButton.setVisibility(View.VISIBLE);
    }

    private void ShowMappingPanelCommand() {
        mapPanelLL.setVisibility(View.VISIBLE);
        mapElementsLv.setVisibility(View.VISIBLE);
        showMappingPanelButton.setVisibility(View.GONE);

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
