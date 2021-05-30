//
//
// TOP Development
// VirtualStoreFragment.java
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
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.top.greenbeans.Adapters.HighlightedProductsPagerAdapter;
import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.KartCallback;
import com.top.greenbeans.Interfaces.StoreCallback;
import com.top.greenbeans.MainActivity;
import com.top.greenbeans.R;

import java.util.ArrayList;

public class VirtualStoreFragment
        extends BaseFragment
        implements FirebaseCallback {

    /*
     *
     * Member Variables
     *
     */

    private ArrayList<GBProduct> mProducts = new ArrayList<>();
    private ViewPager mHighlightedProductsPager;
    private HighlightedProductsPagerAdapter mHighlightedProductAdapter;
    private StoreCallback mStoreCallback;



    /*
     *
     * Constructor
     *
     */

    public static VirtualStoreFragment newInstance() {

        Bundle args = new Bundle();

        VirtualStoreFragment fragment = new VirtualStoreFragment();
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
        if (context instanceof StoreCallback) {
            mStoreCallback = (StoreCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_virtual_store);

        if(mMACallback.getProductList().size() == 0) {
            mFirebaseService.getAllProducts();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_virtual_store_layout, container, false);
        setHasOptionsMenu(true);

        mHighlightedProductsPager = layoutView.findViewById(R.id.pager_products_of_the_day);
        mHighlightedProductAdapter = new HighlightedProductsPagerAdapter(mProducts, getFragmentManager());
        mHighlightedProductsPager.setAdapter(mHighlightedProductAdapter);

        View v = layoutView.findViewById(R.id.button_pager_button);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mStoreCallback.viewProductDetails(mProducts.get(mHighlightedProductsPager.getCurrentItem()));

            }
        });

        Button b = layoutView.findViewById(R.id.button_clothing);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.searchVirtualStore(ProductType.Clothing);

            }
        });

        b = layoutView.findViewById(R.id.button_concentrate);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.searchVirtualStore(ProductType.Concentrate);

            }
        });

        b = layoutView.findViewById(R.id.button_edibles);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.searchVirtualStore(ProductType.Edibles);

            }
        });

        b = layoutView.findViewById(R.id.button_cbd);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.searchVirtualStore(ProductType.CBD);

            }
        });

        b = layoutView.findViewById(R.id.button_supplies);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.searchVirtualStore(ProductType.Supplies);

            }
        });

        b = layoutView.findViewById(R.id.button_all_products);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMACallback.searchVirtualStore(null);

            }
        });

        ImageButton ib = layoutView.findViewById(R.id.button_left);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mHighlightedProductsPager.getCurrentItem() == 0) {

                    mHighlightedProductsPager.setCurrentItem(mProducts.size());

                } else {

                    mHighlightedProductsPager.setCurrentItem(mHighlightedProductsPager.getCurrentItem() - 1);

                }
                mHighlightedProductAdapter.notifyDataSetChanged();

            }
        });

        ib = layoutView.findViewById(R.id.button_right);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mHighlightedProductsPager.getCurrentItem() == mProducts.size()) {

                    mHighlightedProductsPager.setCurrentItem(0);

                } else {

                    mHighlightedProductsPager.setCurrentItem(mHighlightedProductsPager.getCurrentItem() + 1);

                }
                mHighlightedProductAdapter.notifyDataSetChanged();

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

        // method not invoked

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        if (method == FirebaseMethod.GetAllProducts && obj instanceof GBProduct) {

            Log.i(MainActivity.LOG_TAG, "successful object response for products");
            GBProduct product = (GBProduct) obj;
            mMACallback.getProductList().add(product);
            if (product.isHighlightedProduct()) {
                Log.i(MainActivity.LOG_TAG, "successful object response for highlighted products");
                mProducts.add(product);
                mHighlightedProductAdapter.notifyDataSetChanged();
            }

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.GetAllProducts) {



        }

    }

}
