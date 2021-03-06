package com.elegion.myfirstapplication;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.elegion.myfirstapplication.model.User;
import okhttp3.MediaType;


public class RegistrationFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final int MIN_PASSWORD_LENGTH = 8;//

    private EditText mEmail;
    private EditText mName;
    private EditText mPassword;
    private EditText mPasswordAgain;
    private Button mRegistration;
    private TextInputLayout mTextInputLayout;
    private TextInputLayout mPasswordInputLayout;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    private View.OnClickListener mOnRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isInputValid()) {
                User.UserBean user = new User.UserBean(
                        mEmail.getText().toString(),
                        mName.getText().toString(),
                        mPassword.getText().toString());

                ApiUtils.getApiService().registration(user).enqueue(
                        new retrofit2.Callback<Void>() {
                            //используем Handler, чтобы показывать ошибки в Main потоке, т.к. наши коллбеки возвращаются в рабочем потоке
                            Handler mainHandler = new Handler(getActivity().getMainLooper());

                            @Override
                            public void onResponse(retrofit2.Call<Void> call, final retrofit2.Response<Void> response) {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!response.isSuccessful()) {

                                            int errorMessage=ErrorUtils.parseError(response);
                                            Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();

                                        } else {
                                            // showMessage(R.string.registration_success);
                                            int errorMessage=ErrorUtils.parseError(response);
                                            Toast.makeText(getActivity(),errorMessage,Toast.LENGTH_SHORT).show();
                                            getFragmentManager().popBackStack();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showMessage(R.string.request_error);

                                    }
                                });
                            }



                        });


            } else {
                showMessage(R.string.input_error);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        mTextInputLayout =  view.findViewById(R.id.textInputLayout);
        mPasswordInputLayout = view.findViewById(R.id.passwordInputLayout);

        mEmail = view.findViewById(R.id.etEmail);
        mName = view.findViewById(R.id.etName);
        mPassword = view.findViewById(R.id.etPassword);
        mPasswordAgain = view.findViewById(R.id.tvPasswordAgain);
        mRegistration = view.findViewById(R.id.btnRegistration);

        mRegistration.setOnClickListener(mOnRegistrationClickListener);

        return view;
    }

    private boolean isInputValid() {
        return isEmailValid(mEmail.getText().toString())
                && !TextUtils.isEmpty(mName.getText())
                && isPasswordsValid();
    }

    private boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email))
        {
            mEmail.setError(getString(R.string.email_empty));
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() )
        {
            // showMessage(R.string.email_wrong);
            mEmail.setError(getString(R.string.email_wrong));
        }
        else{
            mEmail.setError(null);
//            showMessage(R.string.email);
        }

        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean isPasswordsValid() {
        String password = mPassword.getText().toString();
        String passwordAgain = mPasswordAgain.getText().toString();

        if (TextUtils.getTrimmedLength(password)<MIN_PASSWORD_LENGTH)
        {
            mPassword.setError(getString(R.string.password_length_error));
            return false;
        }
        else{mPassword.setError(null);}
        if (!password.equals(passwordAgain))
        {
            mPassword.setError(getString(R.string.password_not_equal));
            return false;
        }
        else{mPassword.setError(null);}

        return password.equals(passwordAgain)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordAgain);
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}
