//
//
// TOP Development
// OrderTypeDailogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.R;

public class OrderTypeDialogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private DialogCallback.OrderTypeDialogCallback mDialogCallback;



    /*
     *
     * Constructor
     *
     */

    public OrderTypeDialogFragment(DialogCallback.OrderTypeDialogCallback callback) {

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

        View layoutView = inflater.inflate(R.layout.dialog_order_type_layout, container, false);

        Button b = layoutView.findViewById(R.id.button_delivery);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.orderTypeSelected("Delivery");
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_pick_up);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.orderTypeSelected("Pick-Up");
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



}
