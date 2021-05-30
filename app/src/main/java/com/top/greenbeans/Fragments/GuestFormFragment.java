//
//
// TOP Development
// GuestFormFragment.java
//
//

package com.top.greenbeans.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.GMCallback;
import com.top.greenbeans.Interfaces.LoginCallback;
import com.top.greenbeans.MainActivity;
import com.top.greenbeans.R;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Arrays;
import java.util.List;

public class GuestFormFragment
        extends BaseFragment
        implements GMCallback, DialogCallback.ConfirmDialogCallback {

    /*
     *
     * Member Variables
     *
     */

    private LoginCallback mLoginCallback;
    private String mAddress = "";



    /*
     *
     * Constructor
     *
     */

    public static GuestFormFragment newInstance() {

        Bundle args = new Bundle();

        GuestFormFragment fragment = new GuestFormFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_guest_form_layout, container, false);
        final GuestFormFragment frag = this;

        final EditText emailET = layoutView.findViewById(R.id.edittext_email);
        final EditText confirmEmailET = layoutView.findViewById(R.id.edittext_confirm_email);
        final EditText fullNameET = layoutView.findViewById(R.id.edittext_fullname);
        final EditText phoneNumberET = layoutView.findViewById(R.id.edittext_phone_number);

        Button b = layoutView.findViewById(R.id.button_login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptGuestLogin(emailET.getText().toString().trim(), confirmEmailET.getText().toString().trim(),
                        mAddress, fullNameET.getText().toString().trim(), phoneNumberET.getText().toString());

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



    /*
     *
     * Member Variables
     *
     */

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

    @Override // ConfirmDialogCallback Interface Method
    public void confirmResponse(boolean confirm) {

        if (confirm) {

            mMACallback.openGMIntent(this);

        }

    }

    private void attemptGuestLogin(String email, String confirmEmail, String address, String fullName, String phonenumber) {

        if (email.isEmpty() || confirmEmail.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_email));
            return;

        }

        if (!EmailValidator.getInstance().isValid(email)) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_invalid_email));
            return;

        }

        if (address.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_address));
            return;

        }

        if (fullName.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_fullname));
            return;

        }

        if (phonenumber.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_phone_number));
            return;

        }

        if (phonenumber.replace("-", "").length() != 9) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_invalid_phone_number));
            return;

        }

        mLoginCallback.setCurrentUser(new GBUser(email, fullName, address, false));

    }

}
