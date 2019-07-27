package com.example.lighthead.androidcustomcalendar.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lighthead.androidcustomcalendar.SharedPreferencesOperations;
import com.example.lighthead.androidcustomcalendar.helpers.Credentials;
import com.example.lighthead.androidcustomcalendar.FragmentTypes;
import com.example.lighthead.androidcustomcalendar.Global;
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

    Global global = new Global();

	private Call<User> register;

	//region Controls

	EditText nameEditText;
	EditText surnameEditText;
	EditText usernameEditText;
	EditText passwordEditText;
	Button registerButton;

	//endregion

    //region iSharedPrefsOperations
    SharedPreferencesOperations spa = new SharedPreferencesOperations();
    //endregion

    public RegisterFragment() {
        // Required empty public constructor
    }

	//region Listeners
	
	public void performRegister(View view) {
		
		RetrofitClient rClient = new RetrofitClient();
        Retrofit retrofit = rClient.GetRetrofitEntity();

        ICoPlanningAPI client = retrofit.create(ICoPlanningAPI.class);
		
		Credentials credentials = new Credentials();
		credentials.name = nameEditText.getText().toString();
		credentials.surname = surnameEditText.getText().toString();
		credentials.username = usernameEditText.getText().toString();
		credentials.password = passwordEditText.getText().toString();
		
		register = client.register(credentials);
		
		register.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
				
				if (user!=null) {

                    Log.d("MyLog", "Что-то прилетело");
                    Log.d("MyLog", user.username);

					String login = usernameEditText.getText().toString();
					String password = passwordEditText.getText().toString();
					
					//ISharedPrefsOperations.ClearSharedPreferences();
                    spa.ClearSharedPreferences();
					//ISharedPrefsOperations.SetSharedPreferences(login, password);
                    spa.SetSharedPreferences(login, password);
					
					global.SetCurSearchFragment(new SearchFragment());
					global.SetCurMappingsFragment(new MappingsFragment());
					global.SetCurNotificationsFragment(new NotificationsFragment());

                    FragmentTypes curFragmentType = global.GetCurFragment();

                    switch(curFragmentType) {
                        case Search:
                            SearchFragment searchFragment = new SearchFragment();
                            global.SetCurSearchFragment(searchFragment);
                            loadFragment(searchFragment);
                            break;

                        case Mappings:
                            MappingsFragment mappingsFragment = new MappingsFragment();
                            global.SetCurMappingsFragment(mappingsFragment);
                            loadFragment(mappingsFragment);
                            break;

                        case Notifications:
                            NotificationsFragment notifyFragment = new NotificationsFragment();
                            global.SetCurNotificationsFragment(notifyFragment);
                            loadFragment(notifyFragment);
                            break;

                        case Settings:
                            Fragment settingsFragment = new ProfileFragment();
                           // ((ProfileFragment) settingsFragment).SetISharedPrefsOperations(ISharedPrefsOperations);
                            global.SetCurSettingsFragment(settingsFragment);
                            loadFragment(settingsFragment);
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
	}

	//endregion

    //region LoadFragment
	
	private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        // Inflate the layout for this fragment
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

       // Fragment fragment = this;
       // global.SetCurSearchFragment(fragment);
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
