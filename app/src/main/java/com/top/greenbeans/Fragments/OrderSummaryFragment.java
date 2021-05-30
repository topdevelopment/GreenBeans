//
//
// TOP Development
// OrderSummaryFragment.java
//
//

package com.top.greenbeans.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.top.greenbeans.Adapters.KartAdapter;
import com.top.greenbeans.Adapters.OrderSummaryAdapter;
import com.top.greenbeans.Adapters.ProductsAdapter;
import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.KartDetailsCallback;
import com.top.greenbeans.Interfaces.OrderSummaryAdapterCallback;
import com.top.greenbeans.MainActivity;
import com.top.greenbeans.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderSummaryFragment
        extends BaseFragment
        implements OrderSummaryAdapterCallback, FirebaseCallback, DialogCallback.OrderTypeDialogCallback,
        DialogCallback.AddressSelectionDialogCallback {

    /*
     *
     * Member Variables
     *
     */

    private KartDetailsCallback mKartDetailsCallback;
    private OrderSummaryAdapter mAdapter;
    private String mOrderSubtotal = "";
    private String mOrderTax = "";
    private String mOrderTotal = "";
    private String mOrderType = "";



    /*
     *
     * Constructor
     *
     */

    public static OrderSummaryFragment newInstance() {

        Bundle args = new Bundle();

        OrderSummaryFragment fragment = new OrderSummaryFragment();
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
        if (context instanceof KartDetailsCallback) {
            mKartDetailsCallback = (KartDetailsCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_order_summary);
        updateTotals();

        if (mMACallback.getCurrentUser().getAddresses().size() == 0) {
            mFirebaseService.getAllAddresses(mMACallback.getCurrentUser().getEmail());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_order_summary_layout, container, false);
        final OrderSummaryFragment frag = this;

        Button b = layoutView.findViewById(R.id.button_checkout);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //todo: finish this
                if (mKartDetailsCallback.getKart().size() == 0) {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_empty_kart));

                } else {

                    mMACallback.createDialog(DialogType.OrderType, frag);

                }

            }
        });

        ListView lv = layoutView.findViewById(R.id.listview_products);
        mAdapter = new OrderSummaryAdapter(mMACallback.getActivityContext(), mKartDetailsCallback.getKart(), frag);
        lv.setAdapter(mAdapter);

        return layoutView;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

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

    @Override // AddressSelectionDialogCallback Interface Method
    public void addressSelected(String address) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        mFirebaseService.placeOrder(mMACallback.getCurrentUser().getEmail(), mMACallback.getCurrentUser().getFullName(),
                new GBOrder("", mMACallback.getCurrentUser().getFullName(),
                        mMACallback.getCurrentUser().getEmail(), mOrderType, OrderStatus.InProgress.toString(),
                        getOrderDetailsFromKart(), mOrderSubtotal, mOrderTax, mOrderTotal,
                        sdf.format(new Date()), address));

    }

    @Override // OrderTypeDialogCallback Interface Method
    public void orderTypeSelected(String type) {

        mOrderType = type;
        mMACallback.createDialog(DialogType.AddressSelection, this);

    }

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {



    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        if (method == FirebaseMethod.GetAllAddresses && obj instanceof String) {
            mMACallback.getCurrentUser().addAddress(String.valueOf(obj));
        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {



    }

    @Override // OrderSummaryAdapterCallback Interface Method
    public void removeProduct(final int position) {

        mMACallback.runOnMainThread(new Runnable() {
            @Override
            public void run() {

                mKartDetailsCallback.getKart().remove(position);
                mAdapter.notifyDataSetChanged();
                updateTotals();

            }
        });

    }

    private void updateDeliveryFee() {



    }

    private void updateTotals() {

        Double subtotal = 0.0;
        for (int i = 0; i < mKartDetailsCallback.getKart().size(); i++) {

            subtotal += (mKartDetailsCallback.getKart().get(i).getQuantity()
                    * Double.valueOf(mKartDetailsCallback.getKart().get(i).getProductPrice()));
            //Log.i(MainActivity.LOG_TAG, "Product Price: " + Double.valueOf(mKartDetailsCallback.getKart().get(i).getProductPrice()));
            subtotal = Math.round(subtotal * 100.00) / 100.00;
            //Log.i(MainActivity.LOG_TAG, "Subtotal: " + subtotal);

        }

        final String sSubtotal = concatZeroes(String.valueOf(subtotal));
        //Log.i(MainActivity.LOG_TAG, "Final Subtotal: " + sSubtotal);
        mOrderSubtotal = sSubtotal;

        double taxTotal = subtotal * .06;
        //Log.i(MainActivity.LOG_TAG, "Raw Tax Total: " + taxTotal);
        taxTotal = Math.round(taxTotal * 100.00) / 100.00;
        //Log.i(MainActivity.LOG_TAG, "After Round: " + Math.round(taxTotal * 100));
        final String sTaxTotal = concatZeroes(String.valueOf(taxTotal));
        mOrderTax = sTaxTotal;
        //Log.i(MainActivity.LOG_TAG, "Tax Total: " + sTaxTotal);

        final String sOrderTotal = concatZeroes(String.valueOf(taxTotal + subtotal));
        mOrderTotal = sOrderTotal;
        //Log.i(MainActivity.LOG_TAG, "Order Total: " + sOrderTotal);

        mMACallback.runOnMainThread(new Runnable() {
            @Override
            public void run() {

                TextView tv = getView().findViewById(R.id.tv_subtotal_total);
                tv.setText(sSubtotal);

                tv = getView().findViewById(R.id.tv_tax_total);
                tv.setText(sTaxTotal);

                tv = getView().findViewById(R.id.tv_order_total);
                tv.setText(sOrderTotal);

            }
        });

    }

    private String concatZeroes(String value) {

        Log.i(MainActivity.LOG_TAG, "value: " + value);
        char[] subtotalArray = String.valueOf(value).toCharArray();
        int index = -1;
        for (int j = 0; j < subtotalArray.length; j++) {
            Log.i(MainActivity.LOG_TAG, "char @ " + j + ": " + subtotalArray[j]);
            if (subtotalArray[j] == '.') {
                index = j;
                break;
            }
        }
        Log.i(MainActivity.LOG_TAG, "concatZeroes | array length: " + subtotalArray.length);
        Log.i(MainActivity.LOG_TAG, "concatZeroes | index: " + index);
        String placeholder = "";
        if (subtotalArray.length - (index + 1) == 2) {
            placeholder = value;
        } else {
            placeholder = (value + "0");
        }

        return placeholder;

    }

    private List<String> getOrderDetailsFromKart() {

        List<String> orderDetails = new ArrayList<>();

        for (int i = 0; i < mKartDetailsCallback.getKart().size(); i++) {

            orderDetails.add(mKartDetailsCallback.getKart().get(i).getProductTitle() + ";"
                    + mKartDetailsCallback.getKart().get(i).getQuantity());

        }

        return orderDetails;

    }

}
