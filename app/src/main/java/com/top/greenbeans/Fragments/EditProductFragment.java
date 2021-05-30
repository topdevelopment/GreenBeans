//
//
// TOP Development
// EditProductFragment.java
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
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.R;

public class EditProductFragment
        extends BaseFragment
        implements DialogCallback.ProductTypeDialogCallback, FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private GBProduct mSelectedProduct;
    private ProductType mSelectedProductType = null;



    /*
     *
     * Constructor
     *
     */

    public static EditProductFragment newInstance(GBProduct product) {

        Bundle args = new Bundle();

        EditProductFragment fragment = new EditProductFragment();
        fragment.setArguments(args);
        fragment.mSelectedProduct = product;
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

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_edit_product);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_add_product_layout, container, false);
        final EditProductFragment frag = this;
        setHasOptionsMenu(true);

        Button b = layoutView.findViewById(R.id.button_product_type);
        b.setText("Product Type: " + mSelectedProduct.getProductType().toString());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.createDialog(DialogType.ProductType, frag);

            }
        });

        final EditText productNameET = layoutView.findViewById(R.id.edittext_product_name);
        productNameET.setText(mSelectedProduct.getProductTitle());

        final EditText productDescET = layoutView.findViewById(R.id.edittext_product_desc);
        productDescET.setText(mSelectedProduct.getProductDescription());

        final EditText proudctPriceET = layoutView.findViewById(R.id.edittext_product_price);
        proudctPriceET.setText(mSelectedProduct.getProductPrice());

        b = layoutView.findViewById(R.id.button_add);
        b.setText(getResources().getString(R.string.b_edit_product));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptEditProduct(productNameET.getText().toString().trim(),
                        productDescET.getText().toString().trim(), proudctPriceET.getText().toString().trim());

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

    private void attemptEditProduct(String productName, String productDesc, String productPrice) {

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

        mFirebaseService.editProduct(mSelectedProduct);

    }

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.EditProduct) {



        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        // method not invoked

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.EditProduct) {



        }

    }

    @Override // ProductTypeDialogCallback Interface Method
    public void productTypeSelected(ProductType type) {

        mSelectedProduct.updateProductType(type);

    }

}
