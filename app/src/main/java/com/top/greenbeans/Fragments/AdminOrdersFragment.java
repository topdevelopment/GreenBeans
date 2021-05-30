//
//
// TOP Development
// AdminOrdersFragment.java
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.top.greenbeans.Adapters.OrdersAdapter;
import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.AdminCallback;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.OrdersAdapterCallback;
import com.top.greenbeans.R;

import java.util.ArrayList;

public class AdminOrdersFragment
        extends BaseFragment
        implements FirebaseCallback, OrdersAdapterCallback, DialogCallback.OrderStatusDialogCallback,
            DialogCallback.ConfirmDialogCallback {

    /*
     *
     * Member Variables
     *
     */

    private AdminCallback mAdminCallback;
    private OrdersAdapter mOrdersAdapter;
    private GBOrder mSelectedOrder = null;



    /*
     *
     * Constructor
     *
     */

    public static AdminOrdersFragment newInstance() {

        Bundle args = new Bundle();

        AdminOrdersFragment fragment = new AdminOrdersFragment();
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
        if (context instanceof AdminCallback) {
            mAdminCallback = (AdminCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_orders);

        if (mAdminCallback.getAdminOrders().size() == 0) {

            mFirebaseService.getAllAdminOrders();

        } else {

            mOrdersAdapter.notifyDataSetChanged();

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_admin_orders_layout, container, false);
        final AdminOrdersFragment frag = this;
        setHasOptionsMenu(true);

        Button b = layoutView.findViewById(R.id.button_update_status);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedOrder == null) {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_selected_order));

                } else {

                    mMACallback.createDialog(DialogType.OrderStatus, frag);

                }

            }
        });

        b = layoutView.findViewById(R.id.button_archive);
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

        ListView lv = layoutView.findViewById(R.id.listview_orders);
        mOrdersAdapter = new OrdersAdapter(mMACallback.getActivityContext(), mAdminCallback.getAdminOrders());
        lv.setAdapter(mOrdersAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItemPosition() == position) {

                    mOrdersCallback.viewOrderDetails(mAdminCallback.getAdminOrders().get(position));

                } else {

                    if (parent.getSelectedItem() != null) {
                        parent.getSelectedView().setSelected(false);
                    }
                    view.setSelected(true);
                    mSelectedOrder = mOrdersAdapter.getItem(position);

                }

            }
        });

        return layoutView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().toString().matches(getResources().getString(R.string.menu_title_menu))) {

            mMACallback.toggleNavigationDrawer();

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

        if (method == FirebaseMethod.UpdateOrderStatus) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_status_updated));

        } else if (method == FirebaseMethod.ArchiveOrder) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_order_archived));

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        if (method == FirebaseMethod.GetOrders && obj instanceof GBOrder) {

            mAdminCallback.getAdminOrders().add((GBOrder) obj);
            mOrdersAdapter.notifyDataSetChanged();

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.UpdateOrderStatus) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_failed_to_update_status));

        } else if (method == FirebaseMethod.ArchiveOrder) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_failed_to_archive));

        } else if (method == FirebaseMethod.GetOrders) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_failed_to_retrieve_orders));

        }

    }

    @Override // OrderStatusDialogCallback Interface Method
    public void orderStatusSelected(OrderStatus status) {

        mAdminCallback.getAdminOrders().get(mAdminCallback.getAdminOrders().indexOf(mSelectedOrder)).updateOrderStatus(status);
        mOrdersAdapter.notifyDataSetChanged();
        mFirebaseService.updateOrderStatus(mSelectedOrder, status);

    }

    @Override // ConfirmDialogCallback Interface Method
    public void confirmResponse(boolean confirm) {

        if (confirm) {

            mAdminCallback.getAdminOrders().remove(mSelectedOrder);
            mOrdersAdapter.notifyDataSetChanged();
            mFirebaseService.archiveStatus(mSelectedOrder);

        }

    }

    @Override // OrdersAdapterCallback Interface Method
    public void goToOrderDetails(int position) {

        mAdminCallback.viewOrderAdminDetails(mAdminCallback.getAdminOrders().get(position));

    }

}
