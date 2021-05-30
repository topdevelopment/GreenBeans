//
//
// TOP Development
// AdminProductsFragment.java
//
//

package com.top.greenbeans.Fragments;

import android.content.Context;
import android.media.midi.MidiManager;
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

import com.top.greenbeans.Adapters.AdminProductsAdapter;
import com.top.greenbeans.Adapters.ProductsAdapter;
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

public class AdminProductsFragment
        extends BaseFragment
        implements DialogCallback.ConfirmDialogCallback, DialogCallback.HighlightDiscountDialogCallback, FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private AdminCallback mAdminCallback;
    private AdminProductsAdapter mProductsAdapter;
    private GBProduct mSelectedProduct = null;
    private boolean mDeleteProduct = false;
    private boolean mHighlightProduct = false;
    private boolean mUnhighlightProduct = false;



    /*
     *
     * Constructor
     *
     */

    public static AdminProductsFragment newInstance() {

        Bundle args = new Bundle();

        AdminProductsFragment fragment = new AdminProductsFragment();
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
        actionBar.setTitle(R.string.title_products);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_admin_products_layout, container, false);
        final AdminProductsFragment frag = this;
        setHasOptionsMenu(true);

        Button b =layoutView.findViewById(R.id.button_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.selectScreen(ScreensID.AddProduct);

            }
        });

        b = layoutView.findViewById(R.id.button_edit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedProduct == null) {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_product_selected));

                } else {

                    mAdminCallback.editProduct(mSelectedProduct);

                }

            }
        });

        b = layoutView.findViewById(R.id.button_delete);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedProduct == null) {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_product_selected));

                } else {

                    mDeleteProduct = true;
                    mMACallback.createDialog(DialogType.Confirm, frag);

                }

            }
        });

        b = layoutView.findViewById(R.id.button_highlight);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedProduct == null) {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_product_selected));

                } else {

                    mDeleteProduct = false;
                    if (mSelectedProduct.isHighlightedProduct()) {

                        mUnhighlightProduct = true;
                        mMACallback.createDialog(DialogType.UnhighlightProduct, frag);

                    } else {

                        mHighlightProduct = true;
                        mMACallback.createDialog(DialogType.HighlightProduct, frag);

                    }

                }

            }
        });

        ListView lv = layoutView.findViewById(R.id.listview_products);
        mProductsAdapter = new AdminProductsAdapter(mMACallback.getActivityContext(), mMACallback.getProductList());
        lv.setAdapter(mProductsAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem() != null) {
                    parent.getSelectedView().setSelected(false);
                }
                view.setSelected(true);
                mSelectedProduct = mProductsAdapter.getItem(position);

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

    @Override // ConfirmDialogCallback Interface Method
    public void confirmResponse(boolean confirm) {

        if (confirm) {

            if (mDeleteProduct) {

                mDeleteProduct = false;
                mFirebaseService.deleteProduct(mSelectedProduct);

            } else if (mHighlightProduct) {

                mHighlightProduct = false;
                mSelectedProduct.toggleHighlightProduct();
                mMACallback.createDialog(DialogType.HighlightDiscount, this);

            } else {

                mUnhighlightProduct = false;
                mSelectedProduct.toggleHighlightProduct();
                mFirebaseService.editProduct(mSelectedProduct);

            }

        }

    }

    @Override // HighlightDiscountDialogCallback Interface Method
    public void highlightDiscountConfirmed(String discount) {

        mFirebaseService.highlightProduct(mSelectedProduct, discount);

    }

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.DeleteProduct) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.getProductList().remove(mSelectedProduct);
                    mProductsAdapter.notifyDataSetChanged();
                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_product_deleted_successful));

                }
            });

        } else if (method == FirebaseMethod.HighlightDiscount) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_product_highlighted_success));

                }
            });

        } else if (method == FirebaseMethod.EditProduct) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_product_edit_success));

                }
            });

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {



    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.DeleteProduct) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_product_deleted_error));

                }
            });

        } else if (method == FirebaseMethod.HighlightDiscount) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_product_highlighted_error));

                }
            });

        } else if (method == FirebaseMethod.EditProduct) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_product_edit_error));

                }
            });

        }

    }

}
