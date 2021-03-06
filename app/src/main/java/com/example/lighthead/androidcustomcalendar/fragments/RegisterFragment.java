package com.example.lighthead.androidcustomcalendar.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lighthead.androidcustomcalendar.BadgesOperations;
import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.helpers.Credentials;
import com.example.lighthead.androidcustomcalendar.FragmentTypes;
import com.example.lighthead.androidcustomcalendar.Global;
import com.example.lighthead.androidcustomcalendar.helpers.FragmentOperations;
import com.example.lighthead.androidcustomcalendar.helpers.communication.SocketClient;
import com.example.lighthead.androidcustomcalendar.interfaces.ICoPlanningAPI;
import com.example.lighthead.androidcustomcalendar.R;
import com.example.lighthead.androidcustomcalendar.helpers.communication.RetrofitClient;
import com.example.lighthead.androidcustomcalendar.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Activity currentActivity;

	private Call<User> register;

	//region Controls

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button registerButton;

	//endregion

    private SharedPreferencesOperations spa = new SharedPreferencesOperations();
    private BadgesOperations badgesOperations = new BadgesOperations();
    private Global global = new Global();
    private SocketClient socketClient;
    private FragmentOperations fragmentOperations;

    public RegisterFragment() {
        // Required empty public constructor
    }

	//region Listeners
	
	public void performRegister(View view) {
		
		RetrofitClient rClient = new RetrofitClient();
        Retrofit retrofit = rClient.GetRetrofitEntity();

        ICoPlanningAPI client = retrofit.create(ICoPlanningAPI.class);
		
		Credentials credentials = new Credentials(nameEditText.getText().toString(), surnameEditText.getText().toString(), usernameEditText.getText().toString(), passwordEditText.getText().toString());

		register = client.register(credentials);
		
		register.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
				
				if (user!=null) {

					String login = usernameEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					
                    spa.ClearSharedPreferences();
                    spa.SetSharedPreferences(login, password);

                   // socketClient.mSocket.connect();
                    socketClient.Connect();
                    socketClient.SetNotificationsListener(null);
                    socketClient.SetFullNotificationsListListener(null);
                    socketClient.SetNotificationsReceiver(login);


                    global.SetCurSchedFragment(new TaskListFragment());
                    global.SetCurSearchFragment(new SearchFragment());
					global.SetCurMappingsFragment(new MappingsFragment());
					global.SetCurNotificationsFragment(new NotificationsFragment());

                    global.ShowBottomNavigationView();

                    FragmentTypes curFragmentType = global.GetCurFragment();

                    switch(curFragmentType) {
                        case Schedule:
                            Bundle bundle = new Bundle();
                            bundle.putString("TaskListOption", "ThisWeek");
                            TaskListFragment taskListFragment = new TaskListFragment();
                            taskListFragment.setArguments(bundle);
                            global.SetCurSchedFragment(taskListFragment);
                            fragmentOperations.LoadFragment(taskListFragment);
                            break;
                        case Search:
                            SearchFragment searchFragment = new SearchFragment();
                            global.SetCurSearchFragment(searchFragment);
                            fragmentOperations.LoadFragment(searchFragment);
                            break;

                        case Mappings:
                            MappingsFragment mappingsFragment = new MappingsFragment();
                            global.SetCurMappingsFragment(mappingsFragment);
                            fragmentOperations.LoadFragment(mappingsFragment);
                            break;

                        case Notifications:
                            NotificationsFragment notifyFragment = new NotificationsFragment();
                            global.SetCurNotificationsFragment(notifyFragment);
                            fragmentOperations.LoadFragment(notifyFragment);
                            break;

                        case Settings:
                            Fragment settingsFragment = new ProfileFragment();
                            global.SetCurSettingsFragment(settingsFragment);
                            fragmentOperations.LoadFragment(settingsFragment);
                            break;
                    }
				}
				
				else {
					Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT);
				}
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Error!!!", Toast.LENGTH_SHORT);
            }
        });

        fragmentOperations = new FragmentOperations(getFragmentManager());
	}

	//endregion

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //region Find and init controls
        nameEditText = view.findViewById(R.id.nameEditText);
        surnameEditText = view.findViewById(R.id.surnameEditText);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegister(v);
            }
        });
        //endregion

        global.HideBottomNavigationView();

        return view;
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
            socketClient = new SocketClient(currentActivity, badgesOperations);
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
