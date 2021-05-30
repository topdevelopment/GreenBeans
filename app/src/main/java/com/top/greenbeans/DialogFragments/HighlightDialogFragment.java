//
//
// TOP Development
// HighlightDialogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.R;

public class HighlightDialogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private DialogCallback.ConfirmDialogCallback mDialogCallback;



    /*
     *
     * Constructor
     *
     */

    public HighlightDialogFragment(DialogCallback.ConfirmDialogCallback callback) {

        mDialogCallback = callback;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.dialog_highlight_layout, container, false);

        Button b = layoutView.findViewById(R.id.button_yes);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.confirmResponse(true);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_no);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.confirmResponse(false);
                dismiss();

            }
        });

        return layoutView;

    }

}
