//
//
// TOP Development
// ProductDetailsFragment.java
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.ProductSize;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Enums.ScreensID;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.KartCallback;
import com.top.greenbeans.R;

public class ProductDetailsFragment
        extends BaseFragment
        implements FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private KartCallback mKartCallback;
    private GBProduct mSelectedProduct;
    private int mCurrentQuantity = 1;
    private ProductSize mSelectedSize = null;



    /*
     *
     * Constructor
     *
     */

    public static ProductDetailsFragment newInstance(GBProduct product) {

        Bundle args = new Bundle();

        ProductDetailsFragment fragment = new ProductDetailsFragment();
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
        if (context instanceof KartCallback) {
            mKartCallback = (KartCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_product_details);

        if (mSelectedProduct.getProductImage() == null) {
            mFirebaseService.getProductImage(mSelectedProduct.getProductTitle());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_product_details_layout, container, false);
        final EditText etQuanity = layoutView.findViewById(R.id.et_current_quantity_value);
        setHasOptionsMenu(true);

        ImageView iv = layoutView.findViewById(R.id.imageview_produt_image);
        if (mSelectedProduct.getProductImage() == null) {
            iv.setImageResource(R.drawable.no_product_image);
        } else {
            iv.setImageBitmap(mSelectedProduct.getProductImage());
        }

        TextView tv = layoutView.findViewById(R.id.tv_product_name);
        tv.setText(mSelectedProduct.getProductTitle());

        tv = layoutView.findViewById(R.id.tv_product_desc);
        tv.setText(mSelectedProduct.getProductDescription());

        tv = layoutView.findViewById(R.id.tv_product_price);
        tv.setText(mSelectedProduct.getProductPrice());

        Button b = layoutView.findViewById(R.id.button_minus);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrentQuantity > 1) {

                    mCurrentQuantity -= 1;
                    etQuanity.setText(String.valueOf(mCurrentQuantity));
                    mSelectedProduct.updateQuantity(-1);

                } else {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_cannot_be_lower_than_zero));

                }

            }
        });

        b = layoutView.findViewById(R.id.button_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentQuantity += 1;
                etQuanity.setText(String.valueOf(mCurrentQuantity));
                mSelectedProduct.updateQuantity(1);

            }
        });

        b = layoutView.findViewById(R.id.button_add_to_kart);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedProduct.getProductType() == ProductType.Clothing) {
                    if (mSelectedSize != null) {

                    }
                }
                mKartCallback.addToKart(mSelectedProduct);
                mMACallback.searchVirtualStore(null);

            }
        });

        final Button smallButton = layoutView.findViewById(R.id.button_small);
        final Button mediumButton = layoutView.findViewById(R.id.button_medium);
        final Button largeButton = layoutView.findViewById(R.id.button_large);
        if (mSelectedProduct.getProductType() == ProductType.Clothing) {

            smallButton.setVisibility(View.VISIBLE);
            mediumButton.setVisibility(View.VISIBLE);
            largeButton.setVisibility(View.VISIBLE);

            smallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    smallButton.setSelected(true);
                    mediumButton.setSelected(false);
                    largeButton.setSelected(false);

                }
            });

            mediumButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    smallButton.setSelected(false);
                    mediumButton.setSelected(true);
                    largeButton.setSelected(false);

                }
            });

            largeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    smallButton.setSelected(false);
                    mediumButton.setSelected(false);
                    largeButton.setSelected(true);

                }
            });

        }

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

        if (method == FirebaseMethod.GetProductImage) {

            ImageView iv = getView().findViewById(R.id.imageview_produt_image);
            iv.setImageBitmap(mSelectedProduct.getProductImage());

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        // method not invoked

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        // method not invoked

    }

}
