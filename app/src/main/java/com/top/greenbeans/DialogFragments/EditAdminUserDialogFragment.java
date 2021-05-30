//
//
// TOP Development
// EditAdminUserDialogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.Interfaces.AdminDialogCallback;
import com.top.greenbeans.Interfaces.MACallback;
import com.top.greenbeans.R;

public class EditAdminUserDialogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private AdminDialogCallback mDialogCallback;
    private MACallback mMACallback;



    /*
     *
     * Constructor
     *
     */

    public EditAdminUserDialogFragment(AdminDialogCallback callback) {

        mDialogCallback = callback;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof MACallback) {
            mMACallback = (MACallback) context;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.dialog_add_admin_user_layout, container, false);

        TextView tv = layoutView.findViewById(R.id.tv_title);
        tv.setText(getResources().getString(R.string.tv_edit_user));

        final EditText fullnameET = layoutView.findViewById(R.id.edittext_fullname);
        final EditText emailET = layoutView.findViewById(R.id.edittext_email);
        emailET.setEnabled(false);

        Button b = layoutView.findViewById(R.id.button_add);
        b.setText(getResources().getString(R.string.b_edit_product));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptEditUser(fullnameET.getText().toString().trim(), emailET.getText().toString().trim());

            }
        });

        b = layoutView.findViewById(R.id.button_cancel);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });

        return layoutView;

    }



    /*
     *
     * Class Methods
     *
     */

    private void attemptEditUser(String fullname, String email) {

        if (fullname.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_fullname));
            return;

        }

        if (email.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_email));
            return;

        }

        mDialogCallback.editUserResponse(new GBUser(email, fullname, "", true));
        dismiss();

    }

}
