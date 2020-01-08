package com.example.lighthead.androidcustomcalendar.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.example.lighthead.androidcustomcalendar.MappingElementsManager;
import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.FragmentOperations;
import com.example.lighthead.androidcustomcalendar.helpers.communication.SocketClient;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //region Controls

    private TextView usernameTv;
    private ImageButton logout;

    private TextView defaultUnavailableTimeFromTv;
    private TextView defaultUnavailableTimeToTv;
	
	//private LinearLayout customUnavailableTimesList;

	private ImageButton addCustomUnavailableInterval;
	private ImageButton saveUnavailableIntervals;
	
	private TextView curDateTv;
	private TextView curTimeTv;

	//endregion

    private Activity currentActivity;
    private OnFragmentInteractionListener mListener;
    private JSONObject curUnavailableTimeData;

    //region Default unavailable time interval
    private String defaultUnavailableTimeFrom;
    private String defaultUnavailableTimeTo;
    //endregion

    private FragmentOperations fragmentOperations;

    private Global global = new Global();
    private MappingElementsManager mappingElementsManager = new MappingElementsManager();
    private SharedPreferencesOperations sp = new SharedPreferencesOperations();
    private BadgesOperations badgesOperations = new BadgesOperations();
    private SocketClient socketClient = new SocketClient();



    public ProfileFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTv = view.findViewById(R.id.username);
        usernameTv.setPaintFlags(usernameTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String login  = sp.GetLogin();

        usernameTv.setText(login);

        curUnavailableTimeData = new JSONObject();


        //region Find and init controls

        defaultUnavailableTimeFromTv = view.findViewById(R.id.defaultUnavailableTimeFrom);
        defaultUnavailableTimeFromTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultUnavailableTimeFrom(v);
            }
        });


        defaultUnavailableTimeToTv = view.findViewById(R.id.defaultUnavailableTimeTo);
        defaultUnavailableTimeToTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultUnavailableTimeTo(v);
            }
        });


       /* customUnavailableTimesList = view.findViewById(R.id.customUnavailableTimesList);
        addCustomUnavailableInterval = view.findViewById(R.id.addCustomUnavailableInterval);
        addCustomUnavailableInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomUnavailableInterval(v);
            }
        });*/
        saveUnavailableIntervals = view.findViewById(R.id.saveUnavailableIntervals);
        saveUnavailableIntervals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveIntervals(v);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(v);
            }
        });

        //endregion

        //region Get user unavailable time with a socket
        socketClient.GetUserUnavailableTime(login);
        socketClient.SetUnavailableTimeAnswer(onUnavailableTimeAnswer);
        //endregion

        fragmentOperations = new FragmentOperations(getFragmentManager());

        // Inflate the layout for this fragment
        return view;
    }



    //region Unavailable intervals(default and custom) listener
    private Emitter.Listener onUnavailableTimeAnswer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    if (data.toString().equals(curUnavailableTimeData.toString())) return;
                    else
                        curUnavailableTimeData = data;

                    try {
                        JSONObject defaultUnavailableTime = data.getJSONObject("default");
                        defaultUnavailableTimeFrom = defaultUnavailableTime.getString("from");
                        defaultUnavailableTimeTo = defaultUnavailableTime.getString("to");
						
						try {
							ConvertDateAndTime.GetHourFromISOStringTime(defaultUnavailableTimeFrom);
							ConvertDateAndTime.GetMinutesFromISOStringTime(defaultUnavailableTimeFrom);  
							ConvertDateAndTime.GetHourFromISOStringTime(defaultUnavailableTimeTo);
							ConvertDateAndTime.GetMinutesFromISOStringTime(defaultUnavailableTimeTo);

							defaultUnavailableTimeFromTv.setText(defaultUnavailableTimeFrom);
							defaultUnavailableTimeToTv.setText(defaultUnavailableTimeTo);
						}
						
						catch(Exception e) {
							defaultUnavailableTimeFromTv.setText("Press to set");
                            defaultUnavailableTimeFrom = "00:00";
							
							defaultUnavailableTimeToTv.setText("Press to set");
                            defaultUnavailableTimeTo = "00:00";
						}


                        /*JSONArray customIntervals = (JSONArray)data.getJSONArray("custom");
                        for (int i = 0; i < customIntervals.length(); i++) {
                            JSONObject jsonobject;
                            try {
                                jsonobject = customIntervals.getJSONObject(i);
                                String from = jsonobject.getString("from");
                                String to = jsonobject.getString("to");
                                addCustomUnavailableIntervalCommand(from, to);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }*/

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    //endregion

    //region Listeners(controls)

    public void saveIntervals(View view) throws JSONException {
        JSONObject unavailableTime = new JSONObject();
        JSONObject defaultUnavailableTime = new JSONObject();

        if ((defaultUnavailableTimeFromTv.getText().equals("Press to set"))||(defaultUnavailableTimeToTv.getText().equals("Press to set"))) {
            Toast.makeText(getContext(), "Interval cannot be saved!" , Toast.LENGTH_LONG).show();
            return;

        }

        defaultUnavailableTime.put("from", defaultUnavailableTimeFrom);
        defaultUnavailableTime.put("to", defaultUnavailableTimeTo);

        JSONArray customIntervals = new JSONArray();

       /* int customIntervalsAmount = customUnavailableTimesList.getChildCount();

        for (int i=0;i<customIntervalsAmount;i++) {
            View curChild = customUnavailableTimesList.getChildAt(i);

            TextView dFrom = curChild.findViewById(R.id.customUnavailableDateFrom);
            String dFromText = dFrom.getText().toString();
            TextView tFrom = curChild.findViewById(R.id.customUnavailableTimeFrom);
            String tFromText = tFrom.getText().toString();
            TextView dTo = curChild.findViewById(R.id.customUnavailableDateTo);
            String dToText = dTo.getText().toString();
            TextView tTo = curChild.findViewById(R.id.customUnavailableTimeTo);
            String tToText = tTo.getText().toString();

            if ((!dFromText.equals("Set date"))&&(!tFromText.equals("Set time"))&&(!dToText.equals("Set date"))&&(!tToText.equals("Set time"))) {

                int year = ConvertDateAndTime.GetYearFromStringDate(dFromText);
                int month = ConvertDateAndTime.GetMonthFromStringDate(dFromText);
                int day = ConvertDateAndTime.GetDayFromStringDate(dFromText);
                String isoDateFrom = ConvertDateAndTime.ConvertToISOStringDate(year, month, day);

                year = ConvertDateAndTime.GetYearFromStringDate(dToText);
                month = ConvertDateAndTime.GetMonthFromStringDate(dToText);
                day = ConvertDateAndTime.GetDayFromStringDate(dToText);
                String isoDateTo = ConvertDateAndTime.ConvertToISOStringDate(year, month, day);


                JSONObject customUnavailableTimeInterval = new JSONObject();
                customUnavailableTimeInterval.put("dateFrom", isoDateFrom);
                customUnavailableTimeInterval.put("timeFrom", tFromText);
                customUnavailableTimeInterval.put("dateTo", isoDateTo);
                customUnavailableTimeInterval.put("timeTo", tToText);

                customIntervals.put(customUnavailableTimeInterval);
            }

        }*/

        unavailableTime.put("default", defaultUnavailableTime);
        unavailableTime.put("custom", customIntervals);

        String login = sp.GetLogin();
        socketClient.SetUserUnavailableTime(login, unavailableTime);

        Toast.makeText(getContext(), "Interval has been saved!" , Toast.LENGTH_LONG).show();


    }


	/*public void addCustomUnavailableInterval(View view) {

        addCustomUnavailableIntervalCommand(null, null);
	}*/
	

	
	public void setUnavailableDate(View view){
		curDateTv = (TextView) view;
		String curDate = (String) curDateTv.getText();
		int year;
		int month;
		int day;
				
		if (curDate.equals("Set date")) {
			Calendar calendar = new GregorianCalendar();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);
		}
		else {
			year = ConvertDateAndTime.GetYearFromStringDate(curDate);
			month = ConvertDateAndTime.GetMonthFromStringDate(curDate)-1;
			day = ConvertDateAndTime.GetDayFromStringDate(curDate);
		}
        new DatePickerDialog(getContext(), unavailableDateListener, year, month, day).show();
    }

    private DatePickerDialog.OnDateSetListener unavailableDateListener=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            curDateTv.setText(ConvertDateAndTime.ConvertToStringDate(year, monthOfYear+1, dayOfMonth));

        }
    };	
	
	public void setUnavailableTime(View view) {
		curTimeTv = (TextView) view;
		String curTime = (String) curTimeTv.getText();
		int hours;
		int minutes;
		
		if (curTime.equals("Set time")) {
			Calendar calendar = new GregorianCalendar();
			hours = calendar.get(Calendar.HOUR_OF_DAY);
			minutes = calendar.get(Calendar.MINUTE);
		}
		else {
			hours = ConvertDateAndTime.GetHourFromISOStringTime(curTime);
			minutes = ConvertDateAndTime.GetMinutesFromISOStringTime(curTime);
		}
		
		new TimePickerDialog(getContext(), unavailableTimeListener, hours, minutes, true).show();
    }

    private TimePickerDialog.OnTimeSetListener unavailableTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            curTimeTv.setText(ConvertDateAndTime.ConvertToISOStringTime(hourOfDay, minute));
        }
    };	
	

	
	
	
	
	
    public void setDefaultUnavailableTimeFrom(View view) {
        new TimePickerDialog(getContext(), defaultUnavailableTimeFromListener, ConvertDateAndTime.GetHourFromISOStringTime(defaultUnavailableTimeFrom),
                ConvertDateAndTime.GetMinutesFromISOStringTime(defaultUnavailableTimeFrom), true).show();
    }

    TimePickerDialog.OnTimeSetListener defaultUnavailableTimeFromListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            defaultUnavailableTimeFrom = ConvertDateAndTime.ConvertToISOStringTime(hourOfDay, minute);
            defaultUnavailableTimeFromTv.setText(defaultUnavailableTimeFrom);
        }
    };

    public void setDefaultUnavailableTimeTo(View view) {
        new TimePickerDialog(getContext(), defaultUnavailableTimeToListener, ConvertDateAndTime.GetHourFromISOStringTime(defaultUnavailableTimeTo),
                ConvertDateAndTime.GetMinutesFromISOStringTime(defaultUnavailableTimeTo), true).show();
    }

    TimePickerDialog.OnTimeSetListener defaultUnavailableTimeToListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            defaultUnavailableTimeTo = ConvertDateAndTime.ConvertToISOStringTime(hourOfDay, minute);
            defaultUnavailableTimeToTv.setText(defaultUnavailableTimeTo);
        }
    };
	
	
	
	

    public void logout(View view) {
        badgesOperations.ClearMappingsAmount();
        sp.ClearSharedPreferences();

       // global.ClearMappingElements();
        mappingElementsManager.ClearMappingElements();

        String login  = sp.GetLogin();
       // socketClient.mSocket.off();
        socketClient.OffListeners();
      //  socketClient.mSocket.disconnect();
        socketClient.Disconnect();

        Fragment fragment = new LoginFragment();
        global.SetCurSchedFragment(fragment);
        global.SetCurSearchFragment(fragment);
        global.SetCurMappingsFragment(fragment);
        global.SetCurNotificationsFragment(fragment);
        global.SetCurSettingsFragment(fragment);

        fragmentOperations.LoadFragment(fragment);
    }

    //endregion

    //region Commands

  /*  public void addCustomUnavailableIntervalCommand(String dateTimeFrom, String dateTimeTo) {

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customIntervalView = layoutInflater.inflate(R.layout.singleunavailablecustomtimelayout, null);

        TextView dateFromTv = customIntervalView.findViewById(R.id.customUnavailableDateFrom);
        dateFromTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnavailableDate(v);
            }
        });

        TextView timeFromTv = customIntervalView.findViewById(R.id.customUnavailableTimeFrom);
        timeFromTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnavailableTime(v);
            }
        });

        TextView dateToTv = customIntervalView.findViewById(R.id.customUnavailableDateTo);
        dateToTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnavailableDate(v);
            }
        });

        TextView timeToTv = customIntervalView.findViewById(R.id.customUnavailableTimeTo);
        timeToTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnavailableTime(v);
            }
        });


        TextView removeCustomUnavailableInterval = customIntervalView.findViewById(R.id.removeCustomUnavailableInterval);
        removeCustomUnavailableInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout)customIntervalView.getParent()).removeView(customIntervalView);
            }
        });


        if (dateTimeFrom!=null) {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            df.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                Date dateFrom = df.parse(dateTimeFrom);
                cal.setTime(dateFrom);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH)+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);

                String date = ConvertDateAndTime.ConvertToStringDate(year, month, day);
                String time = ConvertDateAndTime.ConvertToISOStringTime(hours, minutes);
                dateFromTv.setText(date);
                timeFromTv.setText(time);


            }
            catch (ParseException e) {
                e.printStackTrace();
            }

        }

        if (dateTimeTo!=null) {

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            df.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                Date dateTo = df.parse(dateTimeTo);
                cal.setTime(dateTo);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH)+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hours = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);

                String date = ConvertDateAndTime.ConvertToStringDate(year, month, day);
                String time = ConvertDateAndTime.ConvertToISOStringTime(hours, minutes);
                dateToTv.setText(date);
                timeToTv.setText(time);

            }
            catch (ParseException e) {
                e.printStackTrace();
            }

        }

        customUnavailableTimesList.addView(customIntervalView);

    }*/

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
