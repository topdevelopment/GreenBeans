//
//
// TOP Development
// BaseFragment.java
//
//

package com.top.greenbeans.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.MACallback;
import com.top.greenbeans.Interfaces.OrdersCallback;
import com.top.greenbeans.R;
import com.top.greenbeans.Services.FirebaseCommService;

public class BaseFragment extends Fragment {

    /*
     *
     * Member Variables
     *
     */

    MACallback mMACallback;
    FirebaseCommService mFirebaseService;
    OrdersCallback mOrdersCallback;



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
        if (this instanceof FirebaseCallback) {
            mFirebaseService = new FirebaseCommService(context, (FirebaseCallback) this);
        }
        if (this instanceof ViewOrdersFragment) {
            mOrdersCallback = (OrdersCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.app_name);

    }

}
