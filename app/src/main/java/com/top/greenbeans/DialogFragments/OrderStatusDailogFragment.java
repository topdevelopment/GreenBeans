//
//
// TOP Development
// OrderStatusDialogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.MACallback;
import com.top.greenbeans.R;

public class OrderStatusDailogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private MACallback mMACallback;
    private DialogCallback.OrderStatusDialogCallback mDialogCallback;



    /*
     *
     * Constructor
     *
     */

    public OrderStatusDailogFragment(DialogCallback.OrderStatusDialogCallback callback) {

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.dialog_order_status_layout, container, false);

        Button b = layoutView.findViewById(R.id.button_completed);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.orderStatusSelected(OrderStatus.Completed);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_en_route);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.orderStatusSelected(OrderStatus.EnRoute);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_ready_for_pickup);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.orderStatusSelected(OrderStatus.ReadyForPickup);
                dismiss();

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

}
