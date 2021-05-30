//
//
// TOP Development
// LoginFragment.java
//
//

package com.top.greenbeans.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.ScreensID;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.LoginCallback;
import com.top.greenbeans.MainActivity;
import com.top.greenbeans.R;

import java.util.List;

public class LoginFragment
        extends BaseFragment
        implements FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private LoginCallback mLoginCallback;
    private String mUserEmail = "";



    /*
     *
     * Constructor
     *
     */

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof LoginCallback) {
            mLoginCallback = (LoginCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_login_layout, container, false);
        final EditText etEmail = layoutView.findViewById(R.id.edittext_email);
        final EditText etPassword = layoutView.findViewById(R.id.edittext_password);

        Button b = layoutView.findViewById(R.id.button_login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password = etPassword.getText().toString().trim();
                mUserEmail = etEmail.getText().toString().trim();
                attemptLogin(mUserEmail, password);

            }
        });

        b = layoutView.findViewById(R.id.button_guest_login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            mMACallback.selectScreen(ScreensID.GuestForm);

            }
        });

        b = layoutView.findViewById(R.id.button_signup);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            mMACallback.selectScreen(ScreensID.Signup);

            }
        });

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String email = etEmail.getText().toString();
                            String password = etPassword.getText().toString();
                            attemptLogin(email, password);
                            return true;
                        default:
                            break;
                    }
                }

                return false;
            }
        });

        return layoutView;

    }



    /*
     *
     * Class Methods
     *
     */

    private void attemptLogin(String email, String password) {

        if (email.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_invalid_email));

        } else {

            if (password.isEmpty()) {

                mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_password));

            } else {

                mMACallback.createDialog(DialogType.Loading);
                mFirebaseService.attemptLogin(email, password);

            }

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.Login) {

            if (!mUserEmail.trim().isEmpty()) {

                mFirebaseService.getUserInfo(mUserEmail);

            } else {

                mMACallback.postToastMessage(getResources().getString(R.string.toast_message_something_went_wrong));

            }

        } else {

            Log.i(MainActivity.LOG_TAG, "LoginFragment | " + method.toString() + " | successResp");
            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_something_went_wrong));

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        Log.i(MainActivity.LOG_TAG, "LoginFragment | " + method.toString() + " | successObj");
        if (method == FirebaseMethod.GetUserInfo && obj instanceof GBUser) {

            mMACallback.dismissAllDialogs();
            GBUser user = (GBUser) obj;
            mLoginCallback.setCurrentUser(user);

        } else {

            errorResponse(method);

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        mMACallback.dismissAllDialogs();
        Log.i(MainActivity.LOG_TAG, "LoginFragment | " + method.toString() + " | error");
        if (method == FirebaseMethod.GetUserInfo) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_error_user_data));

                }
            });

        } else if (method == FirebaseMethod.Login) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_login_error));

                }
            });

        } else {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_something_went_wrong));

                }
            });

        }

    }

}
