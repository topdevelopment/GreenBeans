//
//
// TOP Development
// SignUpFragment.java
//
//

package com.top.greenbeans.Fragments;

import android.content.Context;
import android.os.Bundle;
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

import com.google.android.libraries.places.api.model.Place;
import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.GMCallback;
import com.top.greenbeans.Interfaces.LoginCallback;
import com.top.greenbeans.R;

public class SignUpFragment
        extends BaseFragment
        implements FirebaseCallback, DialogCallback.ConfirmDialogCallback, GMCallback {

    /*
     *
     * Member Variables
     *
     */

    private LoginCallback mLoginCallback;
    private int mCreateAccountSuccessCount = 0;
    private int mCreateAccountErrorCount = 0;
    private String mEmail = "";
    private String mAddress = "";



    /*
     *
     * Constructor
     *
     */

    public static SignUpFragment newInstance() {

        Bundle args = new Bundle();

        SignUpFragment fragment = new SignUpFragment();
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
        actionBar.setTitle(R.string.title_sign_up);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_signup_layout, container, false);
        final SignUpFragment frag = this;

        final EditText emailET = layoutView.findViewById(R.id.edittext_email);
        final EditText confirmEmailET = layoutView.findViewById(R.id.edittext_confirm_email);
        final EditText passwordET = layoutView.findViewById(R.id.edittext_password);
        final EditText confirmPasswordET = layoutView.findViewById(R.id.edittext_confirm_password);
        final EditText fullNameET = layoutView.findViewById(R.id.edittext_fullname);

        Button b = layoutView.findViewById(R.id.button_create_account);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptCreateAccount(emailET.getText().toString().trim(), confirmEmailET.getText().toString().trim(),
                        passwordET.getText().toString().trim(), confirmPasswordET.getText().toString().trim(),
                        fullNameET.getText().toString().trim(), mAddress);

            }
        });

        b = layoutView.findViewById(R.id.button_find_address);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAddress.isEmpty()) {

                    mMACallback.openGMIntent(frag);

                } else {

                    mMACallback.createDialog(DialogType.Confirm, frag, getResources().getString(R.string.dialog_title_update_address));

                }

            }
        });

        return layoutView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_signup, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().toString().matches(getResources().getString(R.string.menu_title_back))) {

            mMACallback.backButtonPressed();

        }

        return super.onOptionsItemSelected(item);

    }



    /*
     *
     * Class Methods
     *
     */

    @Override // DialogCallback Interface Method
    public void confirmResponse(boolean confirm) {

        if (confirm) {

            mMACallback.openGMIntent(this);

        }

    }

    @Override // GMCallback Interface Method
    public void returnedPlace(Place place) {

        mAddress = place.getAddress();
        mMACallback.runOnMainThread(new Runnable() {
            @Override
            public void run() {

                Button b = getView().findViewById(R.id.button_find_address);
                b.setBackground(getResources().getDrawable(R.drawable.button_secondary_layout));

            }
        });

    }

    private void attemptCreateAccount(String email, String confirmEmail, String password,
                                      String confirmPassword, String fullname, String address) {

        if (email.isEmpty() || confirmEmail.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_email));
            return;

        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_password));
            return;

        }

        if (fullname.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_fullname));
            return;

        }

        if (address.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_address));
            return;

        }

        if (!email.matches(confirmEmail)) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_email_mismatch));
            return;

        }

        if (!password.matches(confirmPassword)) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_password_mismatch));
            return;

        }

        mCreateAccountSuccessCount = 0;
        mCreateAccountErrorCount = 0;
        mEmail = email;
        mFirebaseService.createAccount(email, password, fullname, address, false);

    }



    /*
     *
     * Class Methods
     *
     */

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.CreateAccount) {

            mCreateAccountSuccessCount++;
            if (mCreateAccountSuccessCount == 2) {

                mFirebaseService.getUserInfo(mEmail);

            } else if (mCreateAccountErrorCount == 1) {

                mMACallback.postToastMessage(getResources().getString(R.string.toast_message_failed_to_create_account));

            }

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        if (method == FirebaseMethod.GetUserInfo && obj instanceof GBUser) {

            mLoginCallback.setCurrentUser((GBUser) obj);

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (mCreateAccountSuccessCount == 1) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_failed_to_create_account));

        } else {

            mCreateAccountErrorCount++;

        }

    }

}
