//
//
// TOP Development
// OrderHistoryFragment.java
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
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.top.greenbeans.Adapters.OrdersAdapter;
import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.OrdersAdapterCallback;
import com.top.greenbeans.Interfaces.OrdersCallback;
import com.top.greenbeans.R;

import java.util.ArrayList;

public class OrderHistoryFragment
        extends BaseFragment
        implements FirebaseCallback, OrdersAdapterCallback {

    /*
     *
     * Member Variables
     *
     */

    private OrdersCallback mOrdersCallback;
    private OrdersAdapter mOrdersAdapter;



    /*
     *
     * Constructor
     *
     */

    public static OrderHistoryFragment newInstance() {

        Bundle args = new Bundle();

        OrderHistoryFragment fragment = new OrderHistoryFragment();
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
        if (context instanceof OrdersCallback) {
            mOrdersCallback = (OrdersCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_order_history);

        if (mOrdersCallback.getOrders().size() == 0) {
            mFirebaseService.getOrderHistory(mMACallback.getCurrentUser().getEmail());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_order_history_layout, container, false);
        final OrderHistoryFragment frag = this;
        setHasOptionsMenu(true);

        ListView lv = layoutView.findViewById(R.id.listview_orders);
        mOrdersAdapter = new OrdersAdapter(mMACallback.getActivityContext(), mOrdersCallback.getOrders());
        lv.setAdapter(mOrdersAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



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

        if (method == FirebaseMethod.GetOrders && obj instanceof GBOrder) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mOrdersCallback.getOrders().add((GBOrder) obj);
                    mOrdersAdapter.notifyDataSetChanged();

                }
            });

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.GetOrders) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_failed_to_retrieve_orders));

                }
            });

        }

    }

    @Override // OrdersAdapterCallback Interface Method
    public void goToOrderDetails(int position) {

        mOrdersCallback.viewOrderDetails(mOrdersCallback.getOrders().get(position));

    }

}
