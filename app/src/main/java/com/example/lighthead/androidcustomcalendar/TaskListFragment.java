package com.example.lighthead.androidcustomcalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View view;

    private final String TASK_LIST_OPTION = "TaskListOption";

    private final String TASK_LIST_INTERVAL = "Interval";
    private final String TASK_LIST_SELECTED_DATE_FROM = "SelectedDateFrom";
    private final String TASK_LIST_SELECTED_DATE_TO = "SelectedDateTo";

    private final String TASK_LIST_TODAY = "Today";
    private final String TASK_LIST_THIS_WEEK = "ThisWeek";

    private TextView dateFrom;
    private TextView dateTo;
    private ListView taskList;
    private RadioButton setInterval;
    private RadioButton today;
    private RadioButton thisWeek;

    private ImageButton addTaskButton;
    private ImageButton editTaskButton;
    private ImageButton copyTaskButton;
    private ImageButton carryTaskButton;
    private ImageButton deleteTaskButton;

    Global global = new Global();
    Bundle taskListParamsBundle;

    //Даты, указанные в текстбоксах
    Calendar dateAndTimeFrom=Calendar.getInstance();
    Calendar dateAndTimeTo=Calendar.getInstance();

    //Текущие даты в зависимости от выбранной опции
    Calendar curDateAndTimeFrom = Calendar.getInstance();
    Calendar curDateAndTimeTo = Calendar.getInstance();

    String option;

    private TaskManager taskManager;

    private TaskAdapter taskAdapter;


    public TaskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskListFragment newInstance(String param1, String param2) {
        TaskListFragment fragment = new TaskListFragment();
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

        view = inflater.inflate(R.layout.fragment_task_list, container, false);


        addTaskButton = view.findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskCommand(v);
            }
        });
        editTaskButton = view.findViewById(R.id.editTask);
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTaskCommand(v);
            }
        });
        copyTaskButton = view.findViewById(R.id.copyTask);
        copyTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyTaskCommand(v);
            }
        });
        carryTaskButton = view.findViewById(R.id.carryTask);
        carryTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarryTaskCommand(v);
            }
        });
        deleteTaskButton = view.findViewById(R.id.deleteTask);
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTaskCommand(v);
            }
        });


        dateFrom = view.findViewById(R.id.dateFrom);
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFrom(v);
            }
        });
        dateTo = view.findViewById(R.id.dateTo);
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTo(v);
            }
        });

        setInterval = view.findViewById(R.id.setInterval);
        setInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInterval(v);
            }
        });
        today = view.findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToday(v);
            }
        });
        thisWeek = view.findViewById(R.id.thisWeek);
        thisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setThisWeek(v);
            }
        });

        option = TASK_LIST_THIS_WEEK;
        //Intent intent = getIntent();
        taskListParamsBundle = getArguments();

        if (taskListParamsBundle!=null)
            option = taskListParamsBundle.getString(TASK_LIST_OPTION);

        boolean isInterval = (option.equals(TASK_LIST_INTERVAL));
        boolean isToday = (option.equals(TASK_LIST_TODAY));
        boolean isThisWeek = (option.equals(TASK_LIST_THIS_WEEK));

        if (isInterval) {

           // Calendar dateFrom =(Calendar)intent.getSerializableExtra(TASK_LIST_SELECTED_DATE_FROM);
           // Calendar dateTo = (Calendar)intent.getSerializableExtra(TASK_LIST_SELECTED_DATE_TO);
            Calendar dateFrom = (Calendar)taskListParamsBundle.getSerializable(TASK_LIST_SELECTED_DATE_FROM);
            Calendar dateTo = (Calendar)taskListParamsBundle.getSerializable(TASK_LIST_SELECTED_DATE_TO);

            SetIntervalCommand(dateFrom, dateTo);

        }

        if (isToday) {

            SetTodayCommand();

        }

        if (isThisWeek) {
            SetThisWeekCommand();

        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TaskComparable selectedTask = (TaskComparable)parent.getItemAtPosition(position);

                if (!taskAdapter.selectedTasks.contains(selectedTask))
                {
                    taskAdapter.selectedTasks.add(selectedTask);
                    view.setBackgroundColor(Color.GRAY);

                }
                else
                {
                    taskAdapter.selectedTasks.remove(selectedTask);
                    view.setBackgroundColor(Color.WHITE);

                }

                // Toast.makeText(getApplicationContext(), " Position " + String.valueOf(position) +" id " + GetTaskIdFromMap(position), Toast.LENGTH_LONG).show();
                EnableButtons();

            }
        };

        taskList.setOnItemClickListener(itemClickListener);



        //  UpdateTaskList();

        EnableButtons();




       // global.SetCurSchedFragment("taskList");


        // Inflate the layout for this fragment
        return view;
    }



    private void UpdateTaskList(Calendar dateFrom, Calendar dateTo) {

        Calendar dateFromClone = (Calendar) dateFrom.clone();
        dateFromClone.set(Calendar.HOUR_OF_DAY, 0);
        dateFromClone.set(Calendar.MINUTE, 0);
        dateFromClone.set(Calendar.SECOND, 0);

        Calendar dateToClone = (Calendar) dateTo.clone();
        dateToClone.set(Calendar.HOUR_OF_DAY, 23);
        dateToClone.set(Calendar.MINUTE, 59);
        dateToClone.set(Calendar.SECOND, 59);

        taskManager = new TaskManager(getContext());
        ArrayList<TaskDBWrapper> tasks = taskManager.GetTasks(dateFromClone, dateToClone);

        ArrayList<TaskComparable> taskComparables = new ArrayList<>();
        for (TaskDBWrapper task:tasks
        ) {
            TaskComparable taskComparable = new TaskComparable(task);
            taskComparables.add(taskComparable);
        }
        Collections.sort(taskComparables, TaskComparable.DateTimeFromComparator);

        // UpdateIdsMap(tasks);

        taskList = view.findViewById(R.id.taskList);
        taskAdapter = new TaskAdapter(getContext(), R.layout.singletasklayout, taskComparables);
        taskList.setAdapter(taskAdapter);

        taskAdapter.selectedTasks.clear();



    }
    private void SetFromDate(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        dateFrom.setText(date + "." + dispMonth + "." + year);

        dateAndTimeFrom.set(year, month, date);

    }

    private void SetToDate(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        dateTo.setText(date + "." + dispMonth + "." + year);

        dateAndTimeTo.set(year, month, date);

    }

    private void SetIntervalCommand(Calendar dateFrom, Calendar dateTo) {
        SetFromDate(dateFrom);
        SetToDate(dateTo);

        curDateAndTimeFrom = dateFrom;
        curDateAndTimeTo = dateTo;

        taskListParamsBundle.clear();
        taskListParamsBundle.putString(TASK_LIST_OPTION, TASK_LIST_INTERVAL);
        taskListParamsBundle.putSerializable(TASK_LIST_SELECTED_DATE_FROM, dateFrom);
        taskListParamsBundle.putSerializable(TASK_LIST_SELECTED_DATE_TO, dateTo);


        setInterval.setChecked(true);
        UpdateTaskList(dateFrom, dateTo);
    }

    private void SetTodayCommand() {
        Calendar now = new GregorianCalendar();
      /*  now.add(Calendar.DAY_OF_WEEK, 7 - now.get(Calendar.DAY_OF_WEEK) + 1);
        now.add(Calendar.DAY_OF_WEEK, -6);*/

        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        //Calendar dateFrom = now;
        curDateAndTimeFrom = now;

        Calendar nowClone = (Calendar) now.clone();

        nowClone.set(Calendar.HOUR_OF_DAY, 23);
        nowClone.set(Calendar.MINUTE, 59);
        nowClone.set(Calendar.SECOND, 59);
       // Calendar dateTo = nowClone;
        curDateAndTimeTo = nowClone;

        taskListParamsBundle.clear();
        taskListParamsBundle.putString(TASK_LIST_OPTION, TASK_LIST_TODAY);

        today.setChecked(true);
        //UpdateTaskList(dateFrom, dateTo);
        UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
    }

    private void SetThisWeekCommand() {
        Calendar now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_WEEK, -(now.get(Calendar.DAY_OF_WEEK))+1);
        //Calendar dateFrom = now;
        curDateAndTimeFrom = now;

        now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_WEEK, 7 - now.get(Calendar.DAY_OF_WEEK) + 1);
        //Calendar dateTo = now;
        curDateAndTimeTo = now;

        taskListParamsBundle.clear();
        taskListParamsBundle.putString(TASK_LIST_OPTION, TASK_LIST_THIS_WEEK);

        thisWeek.setChecked(true);
        //UpdateTaskList(dateFrom, dateTo);
        UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
    }




    public void setDateFrom(View view){
        new DatePickerDialog(getContext(), dFrom,
                dateAndTimeFrom.get(Calendar.YEAR),
                dateAndTimeFrom.get(Calendar.MONTH),
                dateAndTimeFrom.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener dFrom=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTimeFrom.set(Calendar.YEAR, year);
            dateAndTimeFrom.set(Calendar.MONTH, monthOfYear);
            dateAndTimeFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetFromDate(dateAndTimeFrom);

            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);

            EnableButtons();

        }
    };

    public void setDateTo(View view){

        new DatePickerDialog(getContext(), dTo,
                dateAndTimeTo.get(Calendar.YEAR),
                dateAndTimeTo.get(Calendar.MONTH),
                dateAndTimeTo.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener dTo=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTimeTo.set(Calendar.YEAR, year);
            dateAndTimeTo.set(Calendar.MONTH, monthOfYear);
            dateAndTimeTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetToDate(dateAndTimeTo);

            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);

            EnableButtons();

        }
    };






    public void setInterval(View view) {
        SetIntervalCommand(dateAndTimeFrom, dateAndTimeTo);
        UnlockDates();
    }

    public void setToday(View view) {
        LockDates();
        SetTodayCommand();
    }

    public void setThisWeek(View view) {
        LockDates();
        SetThisWeekCommand();
    }

    private void LockDates() {
        dateFrom.setEnabled(false);
        dateTo.setEnabled(false);
    }

    private void UnlockDates() {
        dateFrom.setEnabled(true);
        dateTo.setEnabled(true);
    }

    private void EnableButtons(){
        int tasksCount = taskAdapter.selectedTasks.size();

        switch (tasksCount){
            case 0:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(false);
                copyTaskButton.setEnabled(false);
                carryTaskButton.setEnabled(false);
                deleteTaskButton.setEnabled(false);
                break;
            }
            case 1:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(true);
                copyTaskButton.setEnabled(true);
                carryTaskButton.setEnabled(true);
                deleteTaskButton.setEnabled(true);
                break;
            }
            default:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(false);
                copyTaskButton.setEnabled(true);
                carryTaskButton.setEnabled(true);
                deleteTaskButton.setEnabled(true);
                break;
            }
        }
    }

    public void AddTaskCommand(View view){
        OpenTaskEditor();
    }

    public void EditTaskCommand(View view){
        OpenTaskEditor(taskAdapter.selectedTasks.get(0));
    }


    public void CopyTaskCommand(View view){
        //Toast.makeText(getApplicationContext(), "Copy" , Toast.LENGTH_LONG).show();
        Calendar calendarCopy = new GregorianCalendar();

        new DatePickerDialog(getContext(), dCopy,
                calendarCopy.get(Calendar.YEAR),
                calendarCopy.get(Calendar.MONTH),
                calendarCopy.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    DatePickerDialog.OnDateSetListener dCopy=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            ArrayList<TaskComparable> selected = taskAdapter.selectedTasks;
            for (TaskDBWrapper task:selected
            ) {
                task.SetDateFrom(year, monthOfYear+1, dayOfMonth);
                TaskManager tm = new TaskManager(getContext());
                tm.AddTask(task);
            }
            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
            Toast.makeText(getContext(), "Copied!" , Toast.LENGTH_LONG).show();
        }
    };

    public void CarryTaskCommand(View view){
        //Toast.makeText(getApplicationContext(), "Carry" , Toast.LENGTH_LONG).show();
        Calendar calendarCarry = new GregorianCalendar();

        new DatePickerDialog(getContext(), dCarry,
                calendarCarry.get(Calendar.YEAR),
                calendarCarry.get(Calendar.MONTH),
                calendarCarry.get(Calendar.DAY_OF_MONTH))
                .show();


    }

    DatePickerDialog.OnDateSetListener dCarry=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            ArrayList<TaskComparable> selected = taskAdapter.selectedTasks;
            for (TaskDBWrapper task:selected
            ) {
                task.SetDateFrom(year, monthOfYear+1, dayOfMonth);
                TaskManager tm = new TaskManager(getContext());
                tm.UpdateTask(task, task.GetId());
            }
            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
            Toast.makeText(getContext(), "Carried!" , Toast.LENGTH_LONG).show();
        }
    };

    public void DeleteTaskCommand(View view){

        TaskManager tm = new TaskManager(getContext());


        ArrayList<TaskComparable> selected = taskAdapter.selectedTasks;
        for (TaskDBWrapper taskForDelete:selected
        ) {
            tm.DeleteTask(taskForDelete.GetId());
        }


        UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);

        Toast.makeText(getContext(), "Deleted!" , Toast.LENGTH_LONG).show();

    }

    private void OpenTaskEditor(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("isNewTask", true);

        TaskEditorFragment taskEditorFragment = new TaskEditorFragment();
        taskEditorFragment.setArguments(bundle);
        loadFragment(taskEditorFragment);

        // startActivityForResult(intent, 1);
    }



    private void OpenTaskEditor(TaskDBWrapper task){


        Bundle bundle = new Bundle();
        bundle.putBoolean("isNewTask", false);
        bundle.putLong("Id", task.GetId());
        bundle.putString("Name", task.GetName());
        bundle.putString("Comment", task.GetComment());
        bundle.putString("DateFrom", task.GetDateFrom());
        bundle.putString("TimeFrom", task.GetTimeFrom());
        bundle.putString("DateTo", task.GetDateTo());
        bundle.putString("TimeTo", task.GetTimeTo());
     //   bundle.putDouble("Duration", task.GetDuration());
        bundle.putBoolean("Visibility", task.GetVisibility());
        bundle.putBoolean("Editable", task.GetEditable());
        bundle.putBoolean("Completed", task.GetCompleted());

        TaskEditorFragment taskEditorFragment = new TaskEditorFragment();
        taskEditorFragment.setArguments(bundle);
        loadFragment(taskEditorFragment);


    }

    private void loadFragment(Fragment fragment) {


        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
        UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);

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
        SetFromDate(dateAndTimeFrom);
        SetToDate(dateAndTimeTo);
        if (!option.equals(TASK_LIST_INTERVAL))
            LockDates();
       // UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
        UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
    }

    @Override
    public void onPause(){
        super.onPause();

        this.setArguments(taskListParamsBundle);
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
