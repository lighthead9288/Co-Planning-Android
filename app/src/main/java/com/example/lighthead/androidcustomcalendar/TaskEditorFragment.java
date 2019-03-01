package com.example.lighthead.androidcustomcalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskEditorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskEditorFragment extends Fragment {

    View view;

    EditText taskName;
    EditText taskComment;
    TextView taskDate;
    TextView taskTime;
    EditText taskDuration;
    CheckBox taskVisibility;
    CheckBox taskEditable;

    CheckBox dateConfirm;
    CheckBox timeConfirm;

    Button saveButton;

    boolean isNewTask=true;
    private long TaskId;

    Calendar dateAndTime=Calendar.getInstance();

    Bundle taskEditorParamsBundle;

    Global global = new Global();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TaskEditorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskEditorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskEditorFragment newInstance(String param1, String param2) {
        TaskEditorFragment fragment = new TaskEditorFragment();
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
        view = inflater.inflate(R.layout.fragment_task_editor, container, false);

        taskName = view.findViewById(R.id.taskName);
        taskComment = view.findViewById(R.id.taskComment);

        taskDate = view.findViewById(R.id.taskDate);
        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });

        taskTime = view.findViewById(R.id.taskTime);
        taskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(v);
            }
        });

        taskDuration = view.findViewById(R.id.taskDuration);
        taskVisibility = view.findViewById(R.id.taskVisibility);
        taskEditable = view.findViewById(R.id.taskEditable);

        dateConfirm = view.findViewById(R.id.dateConfirm);
        dateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDateConfirmSet(v);
            }
        });

        timeConfirm = view.findViewById(R.id.timeConfirm);
        timeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskTimeConfirmSet(v);
            }
        });


        saveButton = view.findViewById(R.id.taskConfirm);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTask(v);
            }
        });

       // Intent intent = getIntent();
        taskEditorParamsBundle = getArguments();

        //isNewTask = intent.getBooleanExtra("isNewTask", true);
        isNewTask = taskEditorParamsBundle.getBoolean("isNewTask");

    //    if (!isNewTask) {
            //TaskId = intent.getLongExtra("Id", 0);
        if (!isNewTask) TaskId = taskEditorParamsBundle.getLong("Id");

            taskName.setText(/*intent.getStringExtra("Name")*/taskEditorParamsBundle.getString("Name"));
            taskComment.setText(/*intent.getStringExtra("Comment")*/taskEditorParamsBundle.getString("Comment"));

            int year, month, date, hours, minutes;


            //String strDate = intent.getStringExtra("Date");
            String strDate = taskEditorParamsBundle.getString("Date");
            if (!(strDate==null)) {
                year = ConvertDateAndTime.GetYearFromStringDate(strDate);
                dateAndTime.set(Calendar.YEAR, year);
                month = ConvertDateAndTime.GetMonthFromStringDate(strDate);
                dateAndTime.set(Calendar.MONTH, month-1);
                date = ConvertDateAndTime.GetDayFromStringDate(strDate);
                dateAndTime.set(Calendar.DAY_OF_MONTH, date);
                taskDate.setText(strDate);
            }
            else {
                dateConfirm.setChecked(true);
                DisableDateSet();
                taskDate.setText("Press to set");
            }



           // String strTime = intent.getStringExtra("Time");
            String strTime = taskEditorParamsBundle.getString("Time");
            if (!(strTime==null)) {
                hours = ConvertDateAndTime.GetHourFromStringTime(strTime);
                dateAndTime.set(Calendar.HOUR_OF_DAY, hours);
                minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTime);
                dateAndTime.set(Calendar.MINUTE, minutes);
                taskTime.setText(strTime);
            }
            else {
                timeConfirm.setChecked(true);
                DisableTimeSet();
                taskTime.setText("Press to set");
            }

            //dateAndTime.set(year, month-1, date, hours, minutes);

            //double duration = intent.getIntExtra("Duration", 0);
            //double duration = bundle.getDouble("Duration");

            taskDuration.setText(String.valueOf(/*intent.getDoubleExtra("Duration", 0))*/taskEditorParamsBundle.getDouble("Duration")));
            taskVisibility.setChecked(/*intent.getBooleanExtra("Visibility", false)*/taskEditorParamsBundle.getBoolean("Visibility"));
            taskEditable.setChecked(/*intent.getBooleanExtra("Editable", false)*/taskEditorParamsBundle.getBoolean("Editable"));
       // }

      /*  else {
            dateAndTime = new GregorianCalendar();
            int year = dateAndTime.get(Calendar.YEAR);
            int month = dateAndTime.get(Calendar.MONTH)+1;
            int date = dateAndTime.get(Calendar.DAY_OF_MONTH);
            int hours = dateAndTime.get(Calendar.HOUR_OF_DAY);
            int minutes = dateAndTime.get(Calendar.MINUTE);
            // int seconds = dateAndTime.get(Calendar.SECOND);

            String strDate = ConvertDateAndTime.ConvertToStringDate(year, month, date);
            String strTime = ConvertDateAndTime.ConvertToStringTime(hours, minutes);

            taskDate.setText("Press to set");
            taskTime.setText("Press to set");


        }*/


        // Inflate the layout for this fragment
        return view;
    }

    public void TaskDateConfirmSet(View view) {
        if (dateConfirm.isChecked())
            DisableDateSet();
        else EnableDateSet();

    }

    public void TaskTimeConfirmSet(View view) {
        if (timeConfirm.isChecked())
            DisableTimeSet();
        else EnableTimeSet();

    }

    private void DisableDateSet() {

        taskDate.setEnabled(false);
    }

    private void DisableTimeSet() {
        timeConfirm.setChecked(true);
        taskTime.setEnabled(false);
    }


    private void EnableDateSet() {
        taskDate.setEnabled(true);
    }

    private void EnableTimeSet() {
        taskTime.setEnabled(true);
    }



    private void SetDate(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        taskDate.setText(ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date));

        dateAndTime.set(year, month, date);

    }

    private void SetTime(Calendar fullDate) {

        int hours = fullDate.get(Calendar.HOUR_OF_DAY);
        int minutes = fullDate.get(Calendar.MINUTE);

        taskTime.setText(ConvertDateAndTime.ConvertToStringTime(hours, minutes));

        dateAndTime.set(Calendar.HOUR_OF_DAY, hours);
        dateAndTime.set(Calendar.MINUTE, minutes);
    }


    public void setDate(View view){
        new DatePickerDialog(getContext(), curTaskDate,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener curTaskDate=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetDate(dateAndTime);



        }
    };

    public void setTime(View view) {
        new TimePickerDialog(getContext(), curTaskTime, dateAndTime.get(Calendar.HOUR_OF_DAY), dateAndTime.get(Calendar.MINUTE), true).show();
    }

    TimePickerDialog.OnTimeSetListener curTaskTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);

            SetTime(dateAndTime);
        }
    };

    public void SaveTask(View view) {

        Task task = GetTaskFromView();
        TaskManager tm = new TaskManager(getContext());

        if (isNewTask) {
            tm.AddTask(task);

           /* Intent intent = new Intent();
            intent.putExtra("name", taskName.getText().toString());
            setResult(RESULT_OK, intent);*/
            //finish();
            getFragmentManager().popBackStack();


        }
        else {
            tm.UpdateTask(task, TaskId);
            //finish();
            getFragmentManager().popBackStack();
        }

    }

    public Task GetTaskFromView() {

        String name = taskName.getText().toString();
        String comment = taskComment.getText().toString();


        boolean visibility = taskVisibility.isChecked();
        boolean editable = taskEditable.isChecked();

        Task task = new Task(name);
        task.SetComment(comment);

        task.SetVisibility(visibility);
        task.SetEditable(editable);

        String strDuration = taskDuration.getText().toString();
        if (!strDuration.isEmpty()&&strDuration!=null) {
            double duration = Double.parseDouble(strDuration);
            task.SetDuration(duration);
        }

        if (!dateConfirm.isChecked()&&taskDate.getText()!="Press to set") {
            String strDate = taskDate.getText().toString();
            int year = ConvertDateAndTime.GetYearFromStringDate(strDate);
            int month = ConvertDateAndTime.GetMonthFromStringDate(strDate);
            int date = ConvertDateAndTime.GetDayFromStringDate(strDate);
            task.SetDate(year, month, date);
        }

        if (!timeConfirm.isChecked()&&taskTime.getText()!="Press to set") {
            String strTime = taskTime.getText().toString();
            int hour = ConvertDateAndTime.GetHourFromStringTime(strTime);
            int minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTime);
            task.SetTime(hour, minutes);
        }

        return task;

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

    @Override
    public void onPause(){
        super.onPause();

        Task curTask = GetTaskFromView();

        if (!isNewTask)
            taskEditorParamsBundle.putLong("Id", TaskId);
        taskEditorParamsBundle.putString("Name", curTask.GetName());
        taskEditorParamsBundle.putString("Comment", curTask.GetComment());
        taskEditorParamsBundle.putString("Date", curTask.GetDate());
        taskEditorParamsBundle.putString("Time", curTask.GetTime());
        taskEditorParamsBundle.putDouble("Duration", curTask.GetDuration());
        taskEditorParamsBundle.putBoolean("Visibility", curTask.GetVisibility());
        taskEditorParamsBundle.putBoolean("Editable", curTask.GetEditable());


        this.setArguments(taskEditorParamsBundle);
        Fragment fragment = this;
        global.SetCurSchedFragment(fragment);
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
