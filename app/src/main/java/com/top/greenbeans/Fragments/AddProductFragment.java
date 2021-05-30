//
//
// TOP Development
// AddProductFragment.java
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
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Enums.ScreensID;
import com.top.greenbeans.Interfaces.AdminCallback;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.R;

public class AddProductFragment
        extends BaseFragment
        implements DialogCallback.ProductTypeDialogCallback, FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private AdminCallback mAdminCallback;
    private ProductType mSelectedType = null;
    private GBProduct mNewProduct = null;



    /*
     *
     * Constructor
     *
     */

    public static AddProductFragment newInstance() {

        Bundle args = new Bundle();

        AddProductFragment fragment = new AddProductFragment();
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
        actionBar.setTitle(R.string.title_add_product);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_add_product_layout, container, false);
        final AddProductFragment frag = this;

        final EditText productNameET = layoutView.findViewById(R.id.edittext_product_name);
        final EditText productDescET = layoutView.findViewById(R.id.edittext_product_desc);
        final EditText productPriceET = layoutView.findViewById(R.id.edittext_product_price);

        Button b = layoutView.findViewById(R.id.button_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            attemptAddProduct(productNameET.getText().toString().trim(),
                    productDescET.getText().toString().trim(), productPriceET.getText().toString().trim());

            }
        });

        b = layoutView.findViewById(R.id.button_product_type);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.createDialog(DialogType.ProductType, frag);

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

    private void attemptAddProduct(String productName, String productDesc, String productPrice) {

        if (productName.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_enter_product_name));
            return;

        }

        if (productDesc.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_enter_product_desc));
            return;

        }

        if (productPrice.isEmpty()) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_enter_product_name));
            return;

        }

        try {

            Double.valueOf(productPrice);

        } catch (Exception e) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_enter_valid_price));
            return;

        }

        if (mSelectedType == null) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_select_product_type));
            return;

        }

        mNewProduct = new GBProduct("", productName, productDesc, mSelectedType, productPrice, "");
        mFirebaseService.addProduct(mNewProduct);

    }

    @Override // ProductTypeDialogCallback Interface Method
    public void productTypeSelected(ProductType type) {

        mSelectedType = type;
        mMACallback.runOnMainThread(new Runnable() {
            @Override
            public void run() {

                Button b = getView().findViewById(R.id.button_product_type);
                b.setText("Product Type: " + mSelectedType.toString());

            }
        });

    }

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        if (method == FirebaseMethod.AddProduct && obj instanceof GBProduct) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_add_product_successful));
            mMACallback.getProductList().add((GBProduct) obj);
            mMACallback.selectScreen(ScreensID.AdminProducts);

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.AddProduct) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_add_product_error));
            mNewProduct = null;

        }

    }

}
