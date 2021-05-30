//
//
// TOP Development
// LoadingDialogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.R;

public class LoadingDialogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private String mTitle = "";



    /*
     *
     * Constructor
     *
     */

    public LoadingDialogFragment() { }

    public LoadingDialogFragment(String title) {
        mTitle = title;
    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setCancelable(false);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());

        if (mTitle.isEmpty()) {
            dialog.setTitle(getResources().getString(R.string.dialog_title_loading));
        } else {
            dialog.setTitle(mTitle);
        }

        dialog.setIndeterminate(true);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;

    }

}
