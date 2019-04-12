package com.elegion.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.elegion.myfirstapplication.model.User;
import com.google.gson.Gson;
import java.io.Serializable;


public class AuthFragment extends Fragment {
    private AutoCompleteTextView mEmail;
    private EditText mPassword;
    private Button mEnter;
    private Button mRegister;

    public static AuthFragment newInstance() {
        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmailValid() && isPasswordValid()) {
                String credentials = mEmail.getText().toString()+ ":" +mPassword.getText().toString();// concatenate username/email and password with colon for authentication
                final String authHeader= "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);//form authenfication header
                ApiUtils.getApiService().getUser(authHeader).enqueue(

                        new retrofit2.Callback<User>(){
                            //используем Handler, чтобы показывать ошибки в Main потоке, т.к. наши коллбеки возвращаются в рабочем потоке
                            Handler mainHandler = new Handler(getActivity().getMainLooper());
                            @Override
                            public void onFailure(retrofit2.Call<User> call, Throwable t) {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage(R.string.request_error);
                                    }
                                });
                            }

                            @Override
                            public void onResponse(retrofit2.Call<User> call, final retrofit2.Response<User> response) {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!response.isSuccessful()) {
                                            int errorMessage=ErrorUtils.parseError(response);
                                            Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();
                                        } else {
                                            Gson gson = new Gson();

                                            User.UserBean user = response.body().getData();

                                            Intent startProfileIntent = new Intent(getActivity(), ProfileActivity.class);
                                            startProfileIntent.putExtra(ProfileActivity.USER_KEY, (Serializable) user);
                                            startActivity(startProfileIntent);
                                            getActivity().finish();
                                        }
                                    }
                                });
                            }//

                        }); //ApiUtils.getApiService().getUser(user).enqueue

            } else {

                showMessage(R.string.input_error);
            }
        }
    };

    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
                    .addToBackStack(RegistrationFragment.class.getName())
                    .commit();
        }
    };

    private View.OnFocusChangeListener mOnEmailFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                mEmail.showDropDown();
            }
        }
    };

    private boolean isEmailValid() {
        if (TextUtils.isEmpty(mEmail.getText()))
        {
            mEmail.setError(getString(R.string.email_empty));
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches() )
        {
            mEmail.setError(getString(R.string.email_wrong));
        }
        else{
            mEmail.setError(null);
        }


        return !TextUtils.isEmpty(mEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches();
    }

    private boolean isPasswordValid() {

        return !TextUtils.isEmpty(mPassword.getText());
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mEmail = v.findViewById(R.id.etEmail);
        mPassword = v.findViewById(R.id.etPassword);
        mEnter = v.findViewById(R.id.buttonEnter);
        mRegister = v.findViewById(R.id.buttonRegister);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
        mEmail.setOnFocusChangeListener(mOnEmailFocusChangeListener);

        return v;
    }
}
