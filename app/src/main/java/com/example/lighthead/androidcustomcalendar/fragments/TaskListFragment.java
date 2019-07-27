package com.example.lighthead.androidcustomcalendar.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.helpers.ConvertDateAndTime;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.interfaces.ICoPlanningAPI;
import com.example.lighthead.androidcustomcalendar.interfaces.ITaskListFragment;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.communication.RetrofitClient;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.ServerTask;
import com.example.lighthead.androidcustomcalendar.helpers.SetTaskListParams;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskComparable;
import com.example.lighthead.androidcustomcalendar.helpers.taskWrappers.TaskDBWrapper;
import com.example.lighthead.androidcustomcalendar.helpers.db.TaskManager;
import com.example.lighthead.androidcustomcalendar.models.User;
import com.example.lighthead.androidcustomcalendar.adapters.TaskAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


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
    Global global = new Global();

    private final String TASK_LIST_OPTION = "TaskListOption";

    private final String TASK_LIST_INTERVAL = "Interval";
    private final String TASK_LIST_SELECTED_DATE_FROM = "SelectedDateFrom";
    private final String TASK_LIST_SELECTED_DATE_TO = "SelectedDateTo";

    private final String TASK_LIST_TODAY = "Today";
    private final String TASK_LIST_THIS_WEEK = "ThisWeek";

    Bundle taskListParamsBundle;
    //choosen interval in radiogroup
    String option;
    //Date interval parameters visibility
    private boolean isParamsVisible = true;


    //region Controls
    private TextView dateFrom;
    private TextView dateTo;
    protected ListView taskList;

    private TextView taskListHeader;
    private RadioGroup readyPeriodsRadioGroup;
    private RadioButton setInterval;
    private RadioButton today;
    private RadioButton thisWeek;

    private LinearLayout dateFromLinearLayout;
    private LinearLayout dateToLinearLayout;

    private Button paramsEnableButton;

    protected ImageButton addTaskButton;
    protected ImageButton editTaskButton;
    protected ImageButton copyTaskButton;
    protected ImageButton carryTaskButton;
    protected ImageButton deleteTaskButton;
    protected ImageButton uploadTasksButton;
    protected ImageButton downloadTasksButton;
    //endregion


    //region Dates in textboxes
    Calendar dateAndTimeFrom=Calendar.getInstance();
    Calendar dateAndTimeTo=Calendar.getInstance();
    //endregion

    //region Current dates depending on the selected option
    Calendar curDateAndTimeFrom = Calendar.getInstance();
    Calendar curDateAndTimeTo = Calendar.getInstance();
    //endregion


    private TaskManager taskManager;
    protected TaskAdapter taskAdapter;

    //region To upload/download task list on server
    private Call<User> loginCall;
    private Call<User> setTaskListCall;
    //endregion

   SharedPreferencesOperations spa = new SharedPreferencesOperations();


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

        //region Find and init controls
        taskListHeader = view.findViewById(R.id.taskListHeader);

        readyPeriodsRadioGroup = view.findViewById(R.id.readyPeriodsRadioGroup);

        dateFromLinearLayout = view.findViewById(R.id.dateFromLinearLayout);
        dateToLinearLayout = view.findViewById(R.id.dateToLinearLayout);

        paramsEnableButton = view.findViewById(R.id.paramsEnableButton);
        paramsEnableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParamsEnable(v);
            }
        });
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
        copyTaskButton = view.findViewById(R.id.copyTask);
        copyTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyTask(v);
            }
        });
        carryTaskButton = view.findViewById(R.id.carryTask);
        carryTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarryTask(v);
            }
        });
        deleteTaskButton = view.findViewById(R.id.deleteTask);
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTask(v);
            }
        });
        uploadTasksButton = view.findViewById(R.id.uploadTaskList);
        uploadTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadTaskList(v);
            }
        });
        downloadTasksButton = view.findViewById(R.id.downloadTaskList);
        downloadTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTaskList(v);
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

        //endregion

        //'This week' option by default...
        option = TASK_LIST_THIS_WEEK;
        taskListParamsBundle = getArguments();

        if (taskListParamsBundle!=null){
            option = taskListParamsBundle.getString(TASK_LIST_OPTION);

            login = spa.GetLogin();
            password = spa.GetPassword();

            if ((login!="")&&(password!="")&&(login!=null)&&(password!=null)) {
                uploadTasksButton.setVisibility(View.VISIBLE);
                downloadTasksButton.setVisibility(View.VISIBLE);


            }
        }

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

        //region Fix selected tasks in shown task list
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
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

                    InitButtons();
                }
                catch(Exception e) {
                    return;
                }

            }
        };
        //endregion

        taskList.setOnItemClickListener(itemClickListener);

        InitButtons();

        return view;
    }



    //region Commands

    private void OpenTaskEditorCommand(){
        Bundle bundle = new Bundle();
        bundle.putBoolean("isNewTask", true);

        TaskEditorFragment taskEditorFragment = new TaskEditorFragment();
        taskEditorFragment.setArguments(bundle);
        loadFragment(taskEditorFragment);

    }

    private void OpenTaskEditorCommand(TaskDBWrapper task){


        Bundle bundle = new Bundle();
        bundle.putBoolean("isNewTask", false);
        bundle.putLong("Id", task.GetId());
        bundle.putString("Name", task.GetName());
        bundle.putString("Comment", task.GetComment());
        bundle.putString("DateFrom", task.GetDateFrom());
        bundle.putString("TimeFrom", task.GetTimeFrom());
        bundle.putString("DateTo", task.GetDateTo());
        bundle.putString("TimeTo", task.GetTimeTo());
        bundle.putBoolean("Visibility", task.GetVisibility());
        bundle.putBoolean("Editable", task.GetEditable());
        bundle.putBoolean("Completed", task.GetCompleted());
        bundle.putString("serverTaskNumber", task.GetServerTaskNumber());

        TaskEditorFragment taskEditorFragment = new TaskEditorFragment();
        taskEditorFragment.setArguments(bundle);
        loadFragment(taskEditorFragment);


    }


    public void CommitTaskListToServerCommand(ArrayList</*TaskDBWrapper*/ServerTask> tasksFromServer, ArrayList<TaskDBWrapper> clientTasks, String username, ICoPlanningAPI client)
            throws JSONException {
        for (TaskDBWrapper clientTask:
                clientTasks) {
            String clientTn = clientTask.GetServerTaskNumber();

            boolean isClientTnExists = false;
            for (/*TaskDBWrapper*/ServerTask serverTask:
                    tasksFromServer) {
                String serverTn = serverTask.GetServerTaskNumber();
                if (serverTn.equals(clientTn)) {

                    isClientTnExists = true;
                    /*clientTask.SetServerTaskNumber(serverTask.GetServerTaskNumber());

					clientTask.SetSubscriberList(serverTask.GetSubscriberList());*/
                    ServerTask newTask = new ServerTask(clientTask);
                    newTask.SetSubscriberList(serverTask.GetSubscriberList());
                    newTask.SetServerTaskNumber(serverTask.GetServerTaskNumber());

                    int index = tasksFromServer.indexOf(serverTask);
                    tasksFromServer.set(index, newTask);
                    break;
                }
            }
            if (!isClientTnExists) {
                String maxNumber = GetMaxServerTaskNumberCommand(tasksFromServer);
                ServerTask newTask = new ServerTask(clientTask);
                newTask.SetId(clientTask.GetId());
                newTask.SetServerTaskNumber(maxNumber);
                tasksFromServer.add(newTask);

                TaskManager tm = new TaskManager(getContext());
                tm.UpdateTask(newTask, newTask.GetId());

            }

        }

        LoadTaskListOnServerCommand(tasksFromServer, username, client);
    }


    public void LoadTaskListOnServerCommand(ArrayList</*TaskDBWrapper*/ServerTask> taskList, String username, ICoPlanningAPI client) throws JSONException {

        SetTaskListParams tlParams = new SetTaskListParams();
        tlParams.username = username;
        //tlParams.taskList = jsonTasks;
        tlParams.taskList = taskList;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Log.d("GSON", gson.toJson(tlParams));

        setTaskListCall = client.setUserTaskList(tlParams);

        setTaskListCall.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {

                                    }

                                }
        );


    }

    private String GetMaxServerTaskNumberCommand(ArrayList</*TaskDBWrapper*/ServerTask> tasksFromServer) {
        int maxNumber = 0;
        for (/*TaskDBWrapper*/ServerTask task:tasksFromServer
        ) {
            int curNumber = Integer.parseInt(task.GetServerTaskNumber());
            if (curNumber>=maxNumber)
                maxNumber = curNumber;
        }
        return String.valueOf(maxNumber+1);
    }


    public void UpdateTaskListFromServerCommand(ArrayList</*TaskDBWrapper*/ServerTask> tasksFromServer, ArrayList<TaskDBWrapper> clientTasks, TaskManager taskManager) {
        for (/*TaskDBWrapper*/ServerTask serverTask:
                tasksFromServer) {
            String serverTn = serverTask.GetServerTaskNumber();

            boolean isServerTnExists = false;
            for (TaskDBWrapper clientTask:
                    clientTasks) {
                String clientTn = clientTask.GetServerTaskNumber();
                if (serverTn.equals(clientTn)) {
                    serverTask.SetCompleted(clientTask.GetCompleted());
                    taskManager.UpdateTask(serverTask, clientTask.GetId());
                    isServerTnExists = true;
                    break;
                }
            }
            if (!isServerTnExists)
                taskManager.AddTask(serverTask);
        }
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


    private void LockDatesCommand() {
        dateFrom.setEnabled(false);
        dateTo.setEnabled(false);
    }

    private void UnlockDatesCommand() {
        dateFrom.setEnabled(true);
        dateTo.setEnabled(true);
    }




    public void ShowParamsCommand() {
        taskListHeader.setVisibility(View.VISIBLE);
        readyPeriodsRadioGroup.setVisibility(View.VISIBLE);
        dateFromLinearLayout.setVisibility(View.VISIBLE);
        dateToLinearLayout.setVisibility(View.VISIBLE);
        isParamsVisible = true;
        ViewGroup.LayoutParams params = paramsEnableButton.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsEnableButton.setLayoutParams(params);
        paramsEnableButton.setText("Hide parameters");
    }

    public void HideParamsCommand() {
        taskListHeader.setVisibility(View.GONE);
        readyPeriodsRadioGroup.setVisibility(View.GONE);
        dateFromLinearLayout.setVisibility(View.GONE);
        dateToLinearLayout.setVisibility(View.GONE);
        isParamsVisible = false;
        ViewGroup.LayoutParams params = paramsEnableButton.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsEnableButton.setLayoutParams(params);
        paramsEnableButton.setText("Show parameters");
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

        taskManager = new TaskManager(getContext());

        ArrayList<TaskDBWrapper> tasks = taskManager.GetTasks(dateFromClone, dateToClone);

        ArrayList<TaskComparable> taskComparables = new ArrayList<>();
        for (TaskDBWrapper task:tasks
        ) {
            TaskComparable taskComparable = new TaskComparable(task);
            taskComparables.add(taskComparable);
        }
        Collections.sort(taskComparables, TaskComparable.DateTimeFromComparator);

        taskList = view.findViewById(R.id.taskList);
        taskAdapter = new TaskAdapter(getContext(), R.layout.singletasklayout, taskComparables);
        taskList.setAdapter(taskAdapter);

        taskAdapter.selectedTasks.clear();


        if (taskComparables.size()>2)
            paramsEnableButton.setVisibility(View.VISIBLE);
        else
            paramsEnableButton.setVisibility(View.GONE);



    }

    public void InitButtons(){
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

            InitButtons();

        }
    };

    public void setInterval(View view) {
        SetIntervalCommand(dateAndTimeFrom, dateAndTimeTo);
        UnlockDatesCommand();
    }

    public void setToday(View view) {
        LockDatesCommand();
        SetTodayCommand();
    }

    public void setThisWeek(View view) {
        LockDatesCommand();
        SetThisWeekCommand();
    }


    public void ParamsEnable(View view) {
        if (isParamsVisible)
            HideParamsCommand();
        else
            ShowParamsCommand();

    }

    public void AddTask(View view){
        OpenTaskEditorCommand();
    }

    public void EditTask(View view){
        OpenTaskEditorCommand(taskAdapter.selectedTasks.get(0));
    }


    public void CopyTask(View view){
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
                task.SetDateTo(year, monthOfYear+1, dayOfMonth);
                String timeTo = task.GetTimeFrom();
                int timeToHours = ConvertDateAndTime.GetHourFromStringTime(timeTo);
                int timeToMinutes = ConvertDateAndTime.GetMinutesFromStringTime(timeTo);
                task.SetTimeTo(timeToHours, timeToMinutes);
                TaskManager tm = new TaskManager(getContext());
                tm.AddTask(task);
            }
            UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
            Toast.makeText(getContext(), "Copied!" , Toast.LENGTH_LONG).show();
        }
    };

    public void CarryTask(View view){
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
                task.SetDateTo(year, monthOfYear+1, dayOfMonth);
                String timeTo = task.GetTimeFrom();
                int timeToHours = ConvertDateAndTime.GetHourFromStringTime(timeTo);
                int timeToMinutes = ConvertDateAndTime.GetMinutesFromStringTime(timeTo);
                task.SetTimeTo(timeToHours, timeToMinutes);
                TaskManager tm = new TaskManager(getContext());
                tm.UpdateTask(task, task.GetId());
            }
            UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);
            Toast.makeText(getContext(), "Carried!" , Toast.LENGTH_LONG).show();
        }
    };

    public void DeleteTask(View view){

        TaskManager tm = new TaskManager(getContext());


        ArrayList<TaskComparable> selected = taskAdapter.selectedTasks;
        for (TaskDBWrapper taskForDelete:selected
        ) {
            tm.DeleteTask(taskForDelete.GetId());
        }


        UpdateTaskList(curDateAndTimeFrom, curDateAndTimeTo);

        Toast.makeText(getContext(), "Deleted!" , Toast.LENGTH_LONG).show();

    }

    public void UploadTaskList(View view) {

        RetrofitClient rClient = new RetrofitClient();
        Retrofit retrofit = rClient.GetRetrofitEntity();

        client = retrofit.create(ICoPlanningAPI.class);

        loginCall = client.login(login, password);


        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                tasksFromServer = user.taskList;
                clientTasks = taskManager.GetTasks();

                try {
                    CommitTaskListToServerCommand(tasksFromServer, clientTasks, login, client);
                    Toast.makeText(getContext(), "Uploaded!" , Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void DownloadTaskList(View view) {
        RetrofitClient rClient = new RetrofitClient();
        Retrofit retrofit = rClient.GetRetrofitEntity();

        client = retrofit.create(ICoPlanningAPI.class);

        loginCall = client.login(login, password);


        loginCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                tasksFromServer = user.taskList;
                clientTasks = taskManager.GetTasks();

                UpdateTaskListFromServerCommand(tasksFromServer, clientTasks, taskManager);


                UpdateTaskList(dateAndTimeFrom, dateAndTimeTo);

                Toast.makeText(getContext(), "Downloaded!" , Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }


    String login;
    String password;
    ICoPlanningAPI client;
    ArrayList<ServerTask> tasksFromServer;
    ArrayList<TaskDBWrapper> clientTasks;

    //endregion


    //region loadFragment

    private void loadFragment(Fragment fragment) {


        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //endregion

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
        SetFromDateCommand(dateAndTimeFrom);
        SetToDateCommand(dateAndTimeTo);
        if (!option.equals(TASK_LIST_INTERVAL))
            LockDatesCommand();
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
