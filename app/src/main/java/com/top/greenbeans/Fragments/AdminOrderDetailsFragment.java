//
//
// TOP Development
// AdminOrderDetailsFragment.java
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.AdminCallback;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.R;

public class AdminOrderDetailsFragment
        extends BaseFragment
        implements DialogCallback.ConfirmDialogCallback, FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private GBOrder mSelectedOrder;
    private AdminCallback mAdminCallback;



    /*
     *
     * Constructor
     *
     */

    public static AdminOrderDetailsFragment newInstance(GBOrder order) {

        Bundle args = new Bundle();

        AdminOrderDetailsFragment fragment = new AdminOrderDetailsFragment();
        fragment.setArguments(args);
        fragment.mSelectedOrder = order;
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
        if (context instanceof AdminCallback) {
            mAdminCallback = (AdminCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_order_details);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_admin_order_details_layout, container, false);
        final AdminOrderDetailsFragment frag = this;
        setHasOptionsMenu(true);

        TextView tv = layoutView.findViewById(R.id.tv_order_id);
        tv.setText(mSelectedOrder.getDocumentString());

        tv = layoutView.findViewById(R.id.tv_order_status);
        tv.setText(mSelectedOrder.getOrderStatus());

        tv = layoutView.findViewById(R.id.tv_order_details);
        String orderDetails = "";
        for (int i = 0; i < mSelectedOrder.getOrderDetails().size(); i++) {
            orderDetails = orderDetails.concat(mSelectedOrder.getOrderDetails().get(i) + "\n");
        }
        tv.setText(orderDetails);

        tv = layoutView.findViewById(R.id.tv_order_total);
        tv.setText(mSelectedOrder.getOrderTotal());

        Button b = layoutView.findViewById(R.id.button_archive);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedOrder == null) {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_selected_order));

                } else {

                    mMACallback.createDialog(DialogType.Confirm, frag);

                }

            }
        });

        b = layoutView.findViewById(R.id.button_update_status);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        return layoutView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_product_details, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().toString().matches(getResources().getString(R.string.menu_title_menu))) {

            mMACallback.toggleNavigationDrawer();

        } else if (item.getTitle().toString().matches(getResources().getString(R.string.menu_title_back))) {

            mMACallback.backButtonPressed();

        }

        return super.onOptionsItemSelected(item);

    }



    /*
     *
     * Class Methods
     *
     */

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.ArchiveOrder) {

            mMACallback.backButtonPressed();

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

    }

    @Override // ConfirmDialogCallback Interface Method
    public void confirmResponse(boolean confirm) {

        if (confirm) {

            mAdminCallback.getAdminOrders().remove(mSelectedOrder);
            mFirebaseService.archiveStatus(mSelectedOrder);

        }

    }

}
