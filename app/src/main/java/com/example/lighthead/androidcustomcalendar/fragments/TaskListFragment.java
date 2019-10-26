package com.example.lighthead.androidcustomcalendar.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.adapters.ExpandableTaskListAdapter;
import com.example.lighthead.androidcustomcalendar.calendar.CalendarCustomView;
import com.example.lighthead.androidcustomcalendar.calendar.ICalendarCellClick;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.helpers.FragmentOperations;
import com.example.lighthead.androidcustomcalendar.helpers.GroupedTasksCollection;
import com.example.lighthead.androidcustomcalendar.helpers.communication.ServerTaskManager;
import com.example.lighthead.androidcustomcalendar.interfaces.ITaskListFragment;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskComparable;
import com.example.lighthead.androidcustomcalendar.interfaces.ITaskListOperations;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListFragment extends Fragment implements ITaskListFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public View view;

    private final String TASK_LIST_OPTION = "TaskListOption";

    private final String TASK_LIST_INTERVAL = "Interval";
    private final String TASK_LIST_SELECTED_DATE_FROM = "SelectedDateFrom";
    private final String TASK_LIST_SELECTED_DATE_TO = "SelectedDateTo";

    private final String TASK_LIST_TODAY = "Today";
    private final String TASK_LIST_THIS_WEEK = "ThisWeek";

    Bundle taskListParamsBundle = new Bundle();
    //choosen interval in radiogroup
    String option;
    //Date interval parameters visibility
    private boolean isParamsVisible = true;


    //region Controls
    private TextView dateFrom;
    private TextView dateTo;
    protected ListView taskList;
    protected ExpandableListView expandableTaskList;

  //  private TextView taskListHeader;
    private RadioGroup readyPeriodsRadioGroup;
    private RadioButton setInterval;
    private RadioButton today;
    private RadioButton thisWeek;

    private LinearLayout dateFromLinearLayout;
    private LinearLayout dateToLinearLayout;


    protected ImageButton addTaskButton;
    protected ImageButton editTaskButton;
   // protected ImageButton copyTaskButton;
   // protected ImageButton carryTaskButton;
    protected ImageButton deleteTaskButton;
  //  protected ImageButton uploadTasksButton;
  //  protected ImageButton downloadTasksButton;
    protected ImageButton filterButton;
    protected ImageButton calendarButton;

    private CalendarCustomView calendarCustomView;
    //endregion

    //region Dates in textboxes
    Calendar dateAndTimeFrom=Calendar.getInstance();
    Calendar dateAndTimeTo=Calendar.getInstance();
    //endregion

    //region Current dates depending on the selected option
    Calendar curDateAndTimeFrom = Calendar.getInstance();
    Calendar curDateAndTimeTo = Calendar.getInstance();
    //endregion

    private ServerTaskManager serverTaskManager;
    protected ExpandableTaskListAdapter expandableTaskAdapter;


    private String login;
    private String password;

    private SharedPreferencesOperations spa = new SharedPreferencesOperations();
    public Global global = new Global();
    private FragmentOperations fragmentOperations;


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
    public View GetTaskListView(LayoutInflater inflater, ViewGroup container) {
        View resView = inflater.inflate(R.layout.fragment_task_list, container, false);
        return resView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = GetTaskListView(inflater, container);

        //region Find and init controls

        readyPeriodsRadioGroup = view.findViewById(R.id.readyPeriodsRadioGroup);

        dateFromLinearLayout = view.findViewById(R.id.dateFromLinearLayout);
        dateToLinearLayout = view.findViewById(R.id.dateToLinearLayout);


        addTaskButton = view.findViewById(R.id.addTask);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTask(v);
            }
        });
        editTaskButton = view.findViewById(R.id.editTask);
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTask(v);
            }
        });

        deleteTaskButton = view.findViewById(R.id.deleteTask);
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTask(v);
            }
        });

        filterButton = view.findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterSelect(v);
            }
        });

        calendarButton = view.findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarSelect(v);
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

        calendarCustomView = view.findViewById(R.id.calendarView);

        ICalendarCellClick iCalendarCellClick = new ICalendarCellClick() {
            @Override
            public void OnClick(Calendar calendar) {

                SetIntervalCommand(calendar, calendar);
                HideParamsCommand();
                HideCalendarCommand();


            }
        };

        calendarCustomView.SetICalendarCellClick(iCalendarCellClick);

        //endregion

        //'This week' option by default...
        option = TASK_LIST_THIS_WEEK;
        Bundle inputArguments = getArguments();

        if (inputArguments!=null){
            taskListParamsBundle = inputArguments;
            option = taskListParamsBundle.getString(TASK_LIST_OPTION);
        }

        login = spa.GetLogin();
        password = spa.GetPassword();

        ITaskListOperations iTaskListOperations = new ITaskListOperations() {
            @Override
            public void OnGetTasks(ArrayList<ServerTask> tasksFromServer) {
                ArrayList<TaskComparable> taskComparables = new ArrayList<>();
                for (ServerTask task:tasksFromServer
                ) {
                    TaskComparable taskComparable = new TaskComparable(task);
                    taskComparables.add(taskComparable);
                }
                Collections.sort(taskComparables, TaskComparable.DateTimeFromComparator);

               SetExpandableListAdapter(getContext(), R.layout.singletasklayout, taskComparables, login);

            }

            @Override
            public void OnDeleteTasks() {
                UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
                HideParamsCommand();
                HideCalendarCommand();
            }
        };

        serverTaskManager = new ServerTaskManager(iTaskListOperations);


        boolean isInterval = (option.equals(TASK_LIST_INTERVAL));
        boolean isToday = (option.equals(TASK_LIST_TODAY));
        boolean isThisWeek = (option.equals(TASK_LIST_THIS_WEEK));

        if (isInterval) {
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
        SetTaskListView();
        InitButtons();

        fragmentOperations = new FragmentOperations(getFragmentManager());

        filterButton.setImageResource(R.drawable.filter);
        calendarButton.setImageResource(R.drawable.calendar);

        HideParamsCommand();
        HideCalendarCommand();

        return view;
    }

    //region Commands
    @Override
    public void SetTaskListView() {
        expandableTaskList = view.findViewById(R.id.expandableTaskList);
        expandableTaskList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expandableTaskList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int groupPosition, int childPosition, long l) {
                try {
                    ExpandableListAdapter adapter = expandableListView.getExpandableListAdapter();

                    TaskComparable selectedTask = (TaskComparable)adapter.getChild(groupPosition,childPosition);

                    if (!expandableTaskAdapter.selectedTasks.contains(selectedTask))
                    {
                        expandableTaskAdapter.selectedTasks.add(selectedTask);
                        view.setBackgroundColor(Color.GRAY);

                    }
                    else
                    {
                        expandableTaskAdapter.selectedTasks.remove(selectedTask);
                        view.setBackgroundColor(Color.WHITE);

                    }

                    InitButtons();
                }
                catch(Exception e) {
                    return false;
                }


                return false;
            }
        });
    }


    private void SetExpandableListAdapter(Context context, int resource, List<TaskComparable> tasks, String user) {

        ArrayList<String> groupsArrayList = new ArrayList<>();

        ArrayList<GroupedTasksCollection> сhildDataList = new ArrayList<>();

        for (TaskComparable task: tasks) {

            String dateFrom = task.GetDateFrom();

            Calendar curTaskDateTimeFrom = task.GetDateTimeFrom();
            int dayOfWeek = curTaskDateTimeFrom.get(Calendar.DAY_OF_WEEK);
            String strDayOfWeek = GetStrWeekDay(dayOfWeek);
            String fullDayView = strDayOfWeek + ", " + dateFrom;


            if (!groupsArrayList.contains(fullDayView)) {
                groupsArrayList.add(fullDayView);

                ArrayList<TaskComparable> curDateTasks = GetCurDateTasks(tasks, dateFrom);
                сhildDataList.add(new GroupedTasksCollection(fullDayView, curDateTasks));

            }
        }

        ExpandableTaskListAdapter adapter = new ExpandableTaskListAdapter(context, groupsArrayList, сhildDataList, R.layout.singletaskgrouplayout, R.layout.singletasklayout, login);
        expandableTaskList.setAdapter(adapter);
        ExpandableTaskListAdapter.selectedTasks.clear();

    }

    public String GetStrWeekDay(int dayNumber) {

        switch (dayNumber) {
            case 6: return "SUN";
            case 7: return "MON";
            case 1: return "TUE";
            case 2: return "WED";
            case 3: return "THU";
            case 4: return "FRI";
            case 5: return "SAT";
            default: return "";
        }
    }

    private ArrayList<TaskComparable> GetCurDateTasks(List<TaskComparable> tasks, String dateFrom) {

        ArrayList<TaskComparable> resultList = new ArrayList<>();
        for(TaskComparable task: tasks) {
            String curDate = task.GetDateFrom();
            if (curDate.equals(dateFrom)) {
                resultList.add(task);
            }
        }


        return resultList;
    }


    private void OpenTaskEditorCommand(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("isNewTask", true);

        TaskEditorFragment taskEditorFragment = new TaskEditorFragment();
        taskEditorFragment.setArguments(bundle);
        fragmentOperations.LoadFragment(taskEditorFragment);

    }

    private void OpenTaskEditorCommand(TaskComparable task){


        Bundle bundle = new Bundle();
        bundle.putBoolean("isNewTask", false);
       // bundle.putLong("Id", task.GetId());
        bundle.putString("Name", task.GetName());
        bundle.putString("Comment", task.GetComment());
        bundle.putString("DateFrom", task.GetDateFrom());
        bundle.putString("TimeFrom", task.GetTimeFrom());
        bundle.putString("DateTo", task.GetDateTo());
        bundle.putString("TimeTo", task.GetTimeTo());
        bundle.putBoolean("Visibility", task.GetVisibility());
        bundle.putBoolean("Editable", task.GetEditable());
        bundle.putBoolean("Completed", task.GetCompleted());
        bundle.putInt("serverTaskNumber", task.GetTaskNumber());

        TaskEditorFragment taskEditorFragment = new TaskEditorFragment();
        taskEditorFragment.setArguments(bundle);
        fragmentOperations.LoadFragment(taskEditorFragment);


    }


    private void SetFromDateCommand(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        dateFrom.setText(ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date));

        dateAndTimeFrom.set(year, month, date);

    }

    private void SetToDateCommand(Calendar fullDate) {

        int date = fullDate.get(Calendar.DAY_OF_MONTH);
        int month = fullDate.get(Calendar.MONTH);
        int year = fullDate.get(Calendar.YEAR);

        int dispMonth = month+1;

        dateTo.setText(ConvertDateAndTime.ConvertToStringDate(year, dispMonth, date));

        dateAndTimeTo.set(year, month, date);

    }

    private void SetIntervalCommand(Calendar dateFrom, Calendar dateTo) {
        UnlockDatesCommand();
        SetFromDateCommand(dateFrom);
        SetToDateCommand(dateTo);

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
        LockDatesCommand();

        Calendar now = new GregorianCalendar();

        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        curDateAndTimeFrom = now;

        Calendar nowClone = (Calendar) now.clone();

        nowClone.set(Calendar.HOUR_OF_DAY, 23);
        nowClone.set(Calendar.MINUTE, 59);
        nowClone.set(Calendar.SECOND, 59);
        curDateAndTimeTo = nowClone;

        taskListParamsBundle.clear();
        taskListParamsBundle.putString(TASK_LIST_OPTION, TASK_LIST_TODAY);

        today.setChecked(true);
        UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
        HideParamsCommand();
        HideCalendarCommand();
    }

    private void SetThisWeekCommand() {
        LockDatesCommand();

        Calendar now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_WEEK, -(now.get(Calendar.DAY_OF_WEEK))+1);
        curDateAndTimeFrom = now;

        now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_WEEK, 7 - now.get(Calendar.DAY_OF_WEEK) + 1);
        curDateAndTimeTo = now;

        taskListParamsBundle.clear();
        taskListParamsBundle.putString(TASK_LIST_OPTION, TASK_LIST_THIS_WEEK);

        thisWeek.setChecked(true);
        UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
        HideParamsCommand();
        HideCalendarCommand();
    }


    private void LockDatesCommand() {
        dateFrom.setEnabled(false);
        dateTo.setEnabled(false);
    }

    private void UnlockDatesCommand() {
        dateFrom.setEnabled(true);
        dateTo.setEnabled(true);
    }




    public void ShowParamsCommand() {
        filterButton.setImageResource(R.drawable.filter_selected);
        readyPeriodsRadioGroup.setVisibility(View.VISIBLE);
        dateFromLinearLayout.setVisibility(View.VISIBLE);
        dateToLinearLayout.setVisibility(View.VISIBLE);
        isParamsVisible = true;

        filterStates = FiltersStates.Filter;
    }

    public void HideParamsCommand() {
        filterButton.setImageResource(R.drawable.filter);
        readyPeriodsRadioGroup.setVisibility(View.GONE);
        dateFromLinearLayout.setVisibility(View.GONE);
        dateToLinearLayout.setVisibility(View.GONE);
        isParamsVisible = false;

        filterStates = FiltersStates.AllHidden;
    }

    public void ShowCalendarCommand() {
        calendarButton.setImageResource(R.drawable.calendar_selected);
        calendarCustomView.setVisibility(View.VISIBLE);

        filterStates = FiltersStates.Calendar;
    }

    public void HideCalendarCommand() {
        calendarButton.setImageResource(R.drawable.calendar);
        calendarCustomView.setVisibility(View.GONE);

        filterStates = FiltersStates.AllHidden;
    }

    //endregion


    //region ITaskLIstFragment implementation

    public void UpdateTaskList(Calendar dateFrom, Calendar dateTo) {

        Calendar dateFromClone = (Calendar) dateFrom.clone();
        dateFromClone.set(Calendar.HOUR_OF_DAY, 0);
        dateFromClone.set(Calendar.MINUTE, 0);
        dateFromClone.set(Calendar.SECOND, 0);

        Calendar dateToClone = (Calendar) dateTo.clone();
        dateToClone.set(Calendar.HOUR_OF_DAY, 23);
        dateToClone.set(Calendar.MINUTE, 59);
        dateToClone.set(Calendar.SECOND, 59);

        serverTaskManager.GetTasksFromServer(login, dateFromClone, dateToClone);

    }



    public void InitButtons(){
        int tasksCount = expandableTaskAdapter.selectedTasks.size();

        switch (tasksCount){
            case 0:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(false);
                deleteTaskButton.setEnabled(false);
                break;
            }
            case 1:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(true);
                deleteTaskButton.setEnabled(true);
                break;
            }
            default:
            {
                addTaskButton.setEnabled(true);
                editTaskButton.setEnabled(false);
                deleteTaskButton.setEnabled(true);
                break;
            }
        }
    }



    //endregion


    //region Event listeners
    public void setDateFrom(View view){
        new DatePickerDialog(getContext(), dFrom,
                dateAndTimeFrom.get(Calendar.YEAR),
                dateAndTimeFrom.get(Calendar.MONTH),
                dateAndTimeFrom.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener dFrom=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTimeFrom.set(Calendar.YEAR, year);
            dateAndTimeFrom.set(Calendar.MONTH, monthOfYear);
            dateAndTimeFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetFromDateCommand(dateAndTimeFrom);

            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
            HideParamsCommand();
            HideCalendarCommand();

            InitButtons();

        }
    };

    public void setDateTo(View view){

        new DatePickerDialog(getContext(), dTo,
                dateAndTimeTo.get(Calendar.YEAR),
                dateAndTimeTo.get(Calendar.MONTH),
                dateAndTimeTo.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    DatePickerDialog.OnDateSetListener dTo=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTimeTo.set(Calendar.YEAR, year);
            dateAndTimeTo.set(Calendar.MONTH, monthOfYear);
            dateAndTimeTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SetToDateCommand(dateAndTimeTo);

            UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
            HideParamsCommand();
            HideCalendarCommand();

            InitButtons();

        }
    };

    public void setInterval(View view) {
        SetIntervalCommand(dateAndTimeFrom, dateAndTimeTo);

    }

    public void setToday(View view) {
        SetTodayCommand();
    }

    public void setThisWeek(View view) {
        SetThisWeekCommand();
    }

    public void AddTask(View view){
        OpenTaskEditorCommand();
    }

    public void EditTask(View view){
        OpenTaskEditorCommand(expandableTaskAdapter.selectedTasks.get(0));
    }




    public void DeleteTask(View view){

        ArrayList<TaskComparable> selected = expandableTaskAdapter.selectedTasks;
        for (ServerTask taskForDelete:selected
        ) {
            serverTaskManager.DeleteTask(login, taskForDelete.GetTaskNumber(), curDateAndTimeFrom, curDateAndTimeTo);
        }

        UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
        HideParamsCommand();
        HideCalendarCommand();

    }


    private FiltersStates filterStates = FiltersStates.AllHidden;

    public void FilterSelect(View view) {

       if ((filterStates==FiltersStates.AllHidden)||(filterStates== FiltersStates.Calendar)) {
           ShowParamsCommand();
           HideCalendarCommand();

           filterStates = FiltersStates.Filter;
       }
       else {
           HideParamsCommand();

           filterStates = FiltersStates.AllHidden;
       }

    }

    public void CalendarSelect(View view) {

       if ((filterStates==FiltersStates.AllHidden)||(filterStates== FiltersStates.Filter)) {

           HideParamsCommand();
           ShowCalendarCommand();

           filterStates = FiltersStates.Calendar;
       }

       else {

           HideCalendarCommand();

           filterStates = FiltersStates.AllHidden;
       }
    }

    private enum FiltersStates {
        AllHidden, Filter, Calendar
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
    public void onResume(){
        super.onResume();
        SetFromDateCommand(dateAndTimeFrom);
        SetToDateCommand(dateAndTimeTo);
        if (!option.equals(TASK_LIST_INTERVAL))
            LockDatesCommand();
       // UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);
        UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
        HideParamsCommand();
        HideCalendarCommand();
    }

    @Override
    public void onPause(){
        super.onPause();

        this.setArguments(taskListParamsBundle);

        String simpleName = this.getClass().getSimpleName();

        if (simpleName.equals("TaskListFragment"))
            global.SetCurSchedFragment(this);
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
