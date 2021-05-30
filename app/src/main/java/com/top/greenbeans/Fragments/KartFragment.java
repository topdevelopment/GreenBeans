//
//
// TOP Development
// KartFragment.java
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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.top.greenbeans.Adapters.KartAdapter;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Enums.ScreensID;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.KartDetailsCallback;
import com.top.greenbeans.R;

public class KartFragment
        extends BaseFragment
        implements DialogCallback.OrderTypeDialogCallback, FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private KartDetailsCallback mKartDetailsCallback;
    private KartAdapter mKartAdapter;
    private boolean mPlaceOrderCheck = false;
    private boolean mAddToHistoryCheck = false;



    /*
     *
     * Constructor
     *
     */

    public static KartFragment newInstance() {

        Bundle args = new Bundle();

        KartFragment fragment = new KartFragment();
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
        actionBar.setTitle(R.string.title_kart);

        checkKartContent();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_kart_layout, container, false);
        final KartFragment frag = this;
        setHasOptionsMenu(true);

        ListView lv = layoutView.findViewById(R.id.lv_kart);
        mKartAdapter = new KartAdapter(mMACallback.getActivityContext(), mKartDetailsCallback);
        lv.setAdapter(mKartAdapter);

        Button b = layoutView.findViewById(R.id.button_place_order);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.createDialog(DialogType.OrderType, frag);

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

    private void checkKartContent() {

        mMACallback.runOnMainThread(new Runnable() {
            @Override
            public void run() {

                if (mKartDetailsCallback.getKart().size() == 0) {

                    ListView lv = getView().findViewById(R.id.lv_kart);
                    lv.setVisibility(View.INVISIBLE);
                    TextView tv = getView().findViewById(R.id.tv_message);
                    tv.setVisibility(View.VISIBLE);

                } else {

                    ListView lv = getView().findViewById(R.id.lv_kart);
                    lv.setVisibility(View.VISIBLE);
                    TextView tv = getView().findViewById(R.id.tv_message);
                    tv.setVisibility(View.INVISIBLE);

                }

            }
        });

    }

    private String getOrderDetailsFromKart() {

        String orderDetails = "";

        for (int i = 0; i < mKartDetailsCallback.getKart().size(); i++) {

            if (i != 0) { orderDetails += ";"; }
            orderDetails += (mKartDetailsCallback.getKart().get(i).getProductTitle() + ":"
                    + mKartDetailsCallback.getKart().get(i).getQuantity());

        }

        return orderDetails;

    }

    private String getOrderTotal() {

        double total = 0.0;

        for (int i = 0; i < mKartDetailsCallback.getKart().size(); i++) {

            total += Double.valueOf(mKartDetailsCallback.getKart().get(i).getProductPrice().replace("$",""));

        }

        return String.valueOf(total);

    }

    @Override // OrderTypeDialogCallback Interface Method
    public void orderTypeSelected(String type) {

        //todo: fix this
        //mFirebaseService.placeOrder(mMACallback.getCurrentUser().getEmail(),
        // mMACallback.getCurrentUser().getFullName(), getOrderDetailsFromKart(),
        // OrderStatus.InProgress.toString(), getOrderTotal(), type);

    }

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.AddToOrderHistory) {

            mAddToHistoryCheck = true;
            if (mPlaceOrderCheck) {
                mKartDetailsCallback.getKart().clear();
                mMACallback.selectScreen(ScreensID.VirtualStore);
            }

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        if (method == FirebaseMethod.PlaceOrder) {

            mPlaceOrderCheck = true;
            if (mAddToHistoryCheck) {
                mKartDetailsCallback.getKart().clear();
                mMACallback.selectScreen(ScreensID.VirtualStore);
            }

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.PlaceOrder) {

            mPlaceOrderCheck = false;
            mAddToHistoryCheck = false;

        } else if (method == FirebaseMethod.AddToOrderHistory) {

            mPlaceOrderCheck = false;
            mAddToHistoryCheck = false;

        }

    }

}
