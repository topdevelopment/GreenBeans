//
//
// TOP Development
// ConfirmDialogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.MACallback;
import com.top.greenbeans.R;

public class ConfirmDialogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private DialogCallback.ConfirmDialogCallback mDialogCallback;
    private String mTitle = "";



    /*
     *
     * Constructor
     *
     */

    public ConfirmDialogFragment(DialogCallback.ConfirmDialogCallback callback) {

        mDialogCallback = callback;

    }

    public ConfirmDialogFragment(DialogCallback.ConfirmDialogCallback callback, String title) {

        mDialogCallback = callback;
        mTitle = title;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.dialog_confirm_layout, container, false);

        Button b = layoutView.findViewById(R.id.button_confirm);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.confirmResponse(true);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_cancel);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.confirmResponse(false);
                dismiss();

            }
        });

        if (!mTitle.isEmpty()) {
            TextView tv = layoutView.findViewById(R.id.tv_title);
            tv.setText(mTitle);
        }

        return layoutView;

    }

}
