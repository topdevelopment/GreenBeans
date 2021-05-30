//
//
// TOP Development
// ViewOrdersFragment.java
//
//

package com.top.greenbeans.Fragments;

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

import com.top.greenbeans.Adapters.OrdersAdapter;
import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.R;

import java.util.List;

public class ViewOrdersFragment
        extends BaseFragment
        implements FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private GBOrder mSelectedOrder = null;
    private OrdersAdapter mOrdersAdapter;



    /*
     *
     * Constructor
     *
     */

    public static ViewOrdersFragment newInstance() {

        Bundle args = new Bundle();

        ViewOrdersFragment fragment = new ViewOrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_orders);

        if (mOrdersAdapter.getCount() == 0) {
            if (mMACallback.getCurrentUser().isAdmin()) {
                mFirebaseService.getAllAdminOrders();
            } else {
                mFirebaseService.getOrderHistory(mMACallback.getCurrentUser().getEmail());
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_view_orders_layout, container, false);

        Button b = layoutView.findViewById(R.id.button_view_details);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedOrder != null) {

                    mOrdersCallback.viewOrderDetails(mSelectedOrder);

                } else {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_selected_order));

                }

            }
        });

        ListView lv = layoutView.findViewById(R.id.listview_orders);
        mOrdersAdapter = new OrdersAdapter(mMACallback.getActivityContext(), mOrdersCallback.getOrders());
        lv.setAdapter(mOrdersAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                view.setSelected(true);
                mSelectedOrder = mOrdersAdapter.getItem(position);

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

        

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, final Object obj) {

        if (method == FirebaseMethod.GetOrders) {

            if (obj instanceof GBOrder) {

                mMACallback.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {

                        mOrdersCallback.addOrder((GBOrder) obj);
                        mOrdersAdapter.notifyDataSetChanged();

                    }
                });

            }

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        mMACallback.runOnMainThread(new Runnable() {
            @Override
            public void run() {

                mMACallback.dismissAllDialogs();
                mMACallback.postToastMessage(getString(R.string.toast_message_something_went_wrong));

            }
        });

    }

}
