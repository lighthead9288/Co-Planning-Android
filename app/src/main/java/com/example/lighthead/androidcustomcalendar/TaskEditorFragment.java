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
    TextView taskDateFrom;
    TextView taskTimeFrom;
    TextView taskDateTo;
    TextView taskTimeTo;
    EditText taskDuration;
    CheckBox taskVisibility;
    CheckBox taskEditable;

    CheckBox dateFromConfirm;
    CheckBox timeFromConfirm;
    CheckBox dateToConfirm;
    CheckBox timeToConfirm;

    Button saveButton;

    boolean isNewTask=true;
    private long TaskId;
    boolean EditedTaskCompleted = false;

    Calendar dateAndTimeFrom=Calendar.getInstance();
    Calendar dateAndTimeTo = Calendar.getInstance();

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

        taskDateFrom = view.findViewById(R.id.taskDateFrom);
        taskDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFrom(v);
            }
        });

        taskTimeFrom = view.findViewById(R.id.taskTimeFrom);
        taskTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeFrom(v);
            }
        });


        taskDateTo = view.findViewById(R.id.taskDateTo);
        taskDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTo(v);
            }
        });

        taskTimeTo = view.findViewById(R.id.taskTimeTo);
        taskTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimeTo(v);
            }
        });

        //taskDuration = view.findViewById(R.id.taskDuration);
        taskVisibility = view.findViewById(R.id.taskVisibility);
        taskEditable = view.findViewById(R.id.taskEditable);

        dateFromConfirm = view.findViewById(R.id.dateFromConfirm);
        dateFromConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDateFromConfirmSet(v);
            }
        });

        timeFromConfirm = view.findViewById(R.id.timeFromConfirm);
        timeFromConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskTimeFromConfirmSet(v);
            }
        });

        dateToConfirm = view.findViewById(R.id.dateToConfirm);
        dateToConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDateToConfirmSet(v);
            }
        });

        timeToConfirm = view.findViewById(R.id.timeToConfirm);
        timeToConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskTimeToConfirmSet(v);
            }
        });


        saveButton = view.findViewById(R.id.taskConfirm);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveTask(v);
            }
        });

        taskEditorParamsBundle = getArguments();

        isNewTask = taskEditorParamsBundle.getBoolean("isNewTask");

        if (!isNewTask) {
            TaskId = taskEditorParamsBundle.getLong("Id");
            EditedTaskCompleted = taskEditorParamsBundle.getBoolean("Completed");
        }

        taskName.setText(taskEditorParamsBundle.getString("Name"));
        taskComment.setText(taskEditorParamsBundle.getString("Comment"));

        int year, month, date, hours, minutes;


        String strDateFrom = taskEditorParamsBundle.getString("DateFrom");
        if (!(strDateFrom==null)) {
            year = ConvertDateAndTime.GetYearFromStringDate(strDateFrom);
            dateAndTimeFrom.set(Calendar.YEAR, year);
            month = ConvertDateAndTime.GetMonthFromStringDate(strDateFrom);
            dateAndTimeFrom.set(Calendar.MONTH, month-1);
            date = ConvertDateAndTime.GetDayFromStringDate(strDateFrom);
            dateAndTimeFrom.set(Calendar.DAY_OF_MONTH, date);
            taskDateFrom.setText(strDateFrom);
        }
        else {
            dateFromConfirm.setChecked(true);
            DisableDateFromSet();
            taskDateFrom.setText("Press to set");
        }



        String strTimeFrom = taskEditorParamsBundle.getString("TimeFrom");
        if (!(strTimeFrom==null)) {
            hours = ConvertDateAndTime.GetHourFromStringTime(strTimeFrom);
            dateAndTimeFrom.set(Calendar.HOUR_OF_DAY, hours);
            minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTimeFrom);
            dateAndTimeFrom.set(Calendar.MINUTE, minutes);
            taskTimeFrom.setText(strTimeFrom);
        }
        else {
            timeFromConfirm.setChecked(true);
            DisableTimeFromSet();
            taskTimeFrom.setText("Press to set");
        }


        String strDateTo = taskEditorParamsBundle.getString("DateTo");
        if (!(strDateTo==null)) {
            year = ConvertDateAndTime.GetYearFromStringDate(strDateTo);
            dateAndTimeTo.set(Calendar.YEAR, year);
            month = ConvertDateAndTime.GetMonthFromStringDate(strDateTo);
            dateAndTimeTo.set(Calendar.MONTH, month-1);
            date = ConvertDateAndTime.GetDayFromStringDate(strDateTo);
            dateAndTimeTo.set(Calendar.DAY_OF_MONTH, date);
            taskDateTo.setText(strDateTo);
        }
        else {
            dateToConfirm.setChecked(true);
            DisableDateToSet();
            taskDateTo.setText("Press to set");
        }



        String strTimeTo = taskEditorParamsBundle.getString("TimeTo");
        if (!(strTimeTo==null)) {
            hours = ConvertDateAndTime.GetHourFromStringTime(strTimeTo);
            dateAndTimeTo.set(Calendar.HOUR_OF_DAY, hours);
            minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTimeTo);
            dateAndTimeTo.set(Calendar.MINUTE, minutes);
            taskTimeTo.setText(strTimeTo);
        }
        else {
            timeToConfirm.setChecked(true);
            DisableTimeToSet();
            taskTimeTo.setText("Press to set");
        }


      //  taskDuration.setText(String.valueOf(taskEditorParamsBundle.getDouble("Duration")));
        taskVisibility.setChecked(taskEditorParamsBundle.getBoolean("Visibility"));
        taskEditable.setChecked(taskEditorParamsBundle.getBoolean("Editable"));


        // Inflate the layout for this fragment
        return view;
    }

    public void TaskDateFromConfirmSet(View view) {
        if (dateFromConfirm.isChecked())
            DisableDateFromSet();
        else EnableDateFromSet();

    }

    public void TaskTimeFromConfirmSet(View view) {
        if (timeFromConfirm.isChecked())
            DisableTimeFromSet();
        else EnableTimeFromSet();

    }

    public void TaskDateToConfirmSet(View view) {
        if (dateToConfirm.isChecked())
            DisableDateToSet();
        else EnableDateToSet();

    }

    public void TaskTimeToConfirmSet(View view) {
        if (timeToConfirm.isChecked())
            DisableTimeToSet();
        else EnableTimeToSet();

    }

    private void DisableDateFromSet() {

        taskDateFrom.setEnabled(false);
    }

    private void DisableTimeFromSet() {
        timeFromConfirm.setChecked(true);
        taskTimeFrom.setEnabled(false);
    }

    private void DisableDateToSet() {

        taskDateTo.setEnabled(false);
    }

    private void DisableTimeToSet() {
        timeToConfirm.setChecked(true);
        taskTimeTo.setEnabled(false);
    }


    private void EnableDateFromSet() {
        taskDateFrom.setEnabled(true);
    }

    private void EnableTimeFromSet() {
        taskTimeFrom.setEnabled(true);
    }

    private void EnableDateToSet() {
        taskDateTo.setEnabled(true);
    }

    private void EnableTimeToSet() {
        taskTimeTo.setEnabled(true);
    }



    private void SetDateFrom(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        taskDateFrom.setText(ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date));

        dateAndTimeFrom.set(year, month, date);

    }

    private void SetTimeFrom(Calendar fullDate) {

        int hours = fullDate.get(Calendar.HOUR_OF_DAY);
        int minutes = fullDate.get(Calendar.MINUTE);

        taskTimeFrom.setText(ConvertDateAndTime.ConvertToStringTime(hours, minutes));

        dateAndTimeFrom.set(Calendar.HOUR_OF_DAY, hours);
        dateAndTimeFrom.set(Calendar.MINUTE, minutes);
    }

    private void SetDateTo(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        taskDateTo.setText(ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date));

        dateAndTimeTo.set(year, month, date);

    }

    private void SetTimeTo(Calendar fullDate) {

        int hours = fullDate.get(Calendar.HOUR_OF_DAY);
        int minutes = fullDate.get(Calendar.MINUTE);

        taskTimeTo.setText(ConvertDateAndTime.ConvertToStringTime(hours, minutes));

        dateAndTimeTo.set(Calendar.HOUR_OF_DAY, hours);
        dateAndTimeTo.set(Calendar.MINUTE, minutes);
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

    public void SaveTask(View view) {

        Task task = GetTaskFromView();
        TaskManager tm = new TaskManager(getContext());

        if (isNewTask) {
            tm.AddTask(task);
            getFragmentManager().popBackStack();


        }
        else {
            tm.UpdateTask(task, TaskId);
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
        task.SetCompleted(EditedTaskCompleted);

       /* String strDuration = taskDuration.getText().toString();
        if (!strDuration.isEmpty()&&strDuration!=null) {
            double duration = Double.parseDouble(strDuration);
            task.SetDuration(duration);
        }*/

        if (!dateFromConfirm.isChecked()&&taskDateFrom.getText()!="Press to set") {
            String strDateFrom = taskDateFrom.getText().toString();
            int year = ConvertDateAndTime.GetYearFromStringDate(strDateFrom);
            int month = ConvertDateAndTime.GetMonthFromStringDate(strDateFrom);
            int date = ConvertDateAndTime.GetDayFromStringDate(strDateFrom);
            task.SetDateFrom(year, month, date);
        }

        if (!timeFromConfirm.isChecked()&&taskTimeFrom.getText()!="Press to set") {
            String strTimeFrom = taskTimeFrom.getText().toString();
            int hour = ConvertDateAndTime.GetHourFromStringTime(strTimeFrom);
            int minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTimeFrom);
            task.SetTimeFrom(hour, minutes);
        }

        if (!dateToConfirm.isChecked()&&taskDateTo.getText()!="Press to set") {
            String strDateTo = taskDateTo.getText().toString();
            int year = ConvertDateAndTime.GetYearFromStringDate(strDateTo);
            int month = ConvertDateAndTime.GetMonthFromStringDate(strDateTo);
            int date = ConvertDateAndTime.GetDayFromStringDate(strDateTo);
            task.SetDateTo(year, month, date);
        }

        if (!timeToConfirm.isChecked()&&taskTimeTo.getText()!="Press to set") {
            String strTimeTo = taskTimeTo.getText().toString();
            int hour = ConvertDateAndTime.GetHourFromStringTime(strTimeTo);
            int minutes = ConvertDateAndTime.GetMinutesFromStringTime(strTimeTo);
            task.SetTimeTo(hour, minutes);
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
        taskEditorParamsBundle.putString("DateFrom", curTask.GetDateFrom());
        taskEditorParamsBundle.putString("TimeFrom", curTask.GetTimeFrom());
        //taskEditorParamsBundle.putDouble("Duration", curTask.GetDuration());
        taskEditorParamsBundle.putBoolean("Visibility", curTask.GetVisibility());
        taskEditorParamsBundle.putBoolean("Editable", curTask.GetEditable());
        taskEditorParamsBundle.putString("DateTo", curTask.GetDateTo());
        taskEditorParamsBundle.putString("TimeTo", curTask.GetTimeTo());


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
