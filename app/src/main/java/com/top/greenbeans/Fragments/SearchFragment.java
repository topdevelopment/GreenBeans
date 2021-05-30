//
//
// TOP Development
// SearchFragment.java
//
//

package com.top.greenbeans.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.top.greenbeans.Adapters.ProductsAdapter;
import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.KartCallback;
import com.top.greenbeans.Interfaces.StoreCallback;
import com.top.greenbeans.MainActivity;
import com.top.greenbeans.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment
        extends BaseFragment
        implements FirebaseCallback, KartCallback {

    /*
     *
     * Member Variables
     *
     */

    private ProductType mPreselectedType = null;
    private List<GBProduct> mProducts = new ArrayList<>();
    private ProductsAdapter mProductAdapter;
    private KartCallback mKartCallback;
    private StoreCallback mStoreCallback;



    /*
     *
     * Constructor
     *
     */

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        fragment.mPreselectedType = null;
        return fragment;
    }

    public static SearchFragment newInstance(ProductType selectedType) {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        fragment.mPreselectedType = selectedType;
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
        if (context instanceof StoreCallback) {
            mStoreCallback = (StoreCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_virtual_store);

        setSelectedType(mPreselectedType);
        updateListView(mPreselectedType);

        if (mMACallback.getProductList().get(0).getProductImage() == null) {
            for (int i = 0; i < mMACallback.getProductList().size(); i++) {
                mFirebaseService.getProductImage(mMACallback.getProductList().get(i).getProductImageUrl());
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_search_layout, container, false);
        setHasOptionsMenu(true);

        EditText et = layoutView.findViewById(R.id.edittext_search);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mProducts.clear();
                for (int i = 0; i < mMACallback.getProductList().size(); i++) {
                    if (mMACallback.getProductList().get(i).getProductTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                        mProducts.add(mMACallback.getProductList().get(i));
                    }
                }
                mProductAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Button clothingButton = layoutView.findViewById(R.id.button_clothing);
        clothingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSelectedType(ProductType.Clothing);
                updateListView(ProductType.Clothing);

            }
        });

        final Button flowerButton = layoutView.findViewById(R.id.button_concentrate);
        flowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSelectedType(ProductType.Concentrate);
                updateListView(ProductType.Concentrate);

            }
        });

        final Button ediblesButton = layoutView.findViewById(R.id.button_edibles);
        ediblesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSelectedType(ProductType.Edibles);
                updateListView(ProductType.Edibles);

            }
        });

        final Button suppliesButton = layoutView.findViewById(R.id.button_supplies);
        suppliesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSelectedType(ProductType.Supplies);
                updateListView(ProductType.Supplies);

            }
        });

        final Button strainsButton = layoutView.findViewById(R.id.button_cbd);
        strainsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSelectedType(ProductType.CBD);
                updateListView(ProductType.CBD);

            }
        });

        final Button allProductsButton = layoutView.findViewById(R.id.button_all_products);
        allProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setSelectedType(null);
                updateListView(null);

            }
        });

        ListView lv = layoutView.findViewById(R.id.listview_products);
        mProductAdapter = new ProductsAdapter(mMACallback.getActivityContext(), mProducts, mStoreCallback);
        lv.setAdapter(mProductAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItemPosition() == position) {

                    mStoreCallback.viewProductDetails(mProducts.get(position));

                } else {

                    parent.setSelection(position);

                }

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

    @Override // KartCallback Interface Method
    public void addToKart(GBProduct product) {

        mKartCallback.addToKart(product);

    }

    @Override // KartCallback Interface Method
    public void removeFromKart(GBProduct product) {

        mKartCallback.removeFromKart(product);

    }

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

        Log.i(MainActivity.LOG_TAG, "SearchFragment | " + method.toString() + " | successfulResponse");

        if (method == FirebaseMethod.GetProductImage) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mProductAdapter.notifyDataSetChanged();

                }
            });
        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, final Object obj) {

        Log.i(MainActivity.LOG_TAG, method.toString() + " successfulObj");
        mMACallback.postToastMessage(getResources().getString(R.string.toast_message_something_went_wrong));

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        Log.i(MainActivity.LOG_TAG, method.toString() + " errorResponse");

    }

    private void setSelectedType(final ProductType type) {

        mMACallback.runOnMainThread(new Runnable() {
            @Override
            public void run() {

                View v = getView();

                if (v != null) {

                    final Button clothingButton = v.findViewById(R.id.button_clothing);
                    final Button ediblesButton = v.findViewById(R.id.button_edibles);
                    final Button suppliesButton = v.findViewById(R.id.button_supplies);
                    final Button cbdButton = v.findViewById(R.id.button_cbd);
                    final Button concButton = v.findViewById(R.id.button_concentrate);
                    final Button allProductsButton = v.findViewById(R.id.button_all_products);

                    if (type != null) {

                        if (type == ProductType.Clothing) {

                            clothingButton.setBackground(getResources().getDrawable(R.drawable.button_selected_layout));
                            ediblesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            suppliesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            cbdButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            concButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            allProductsButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));

                        } else if (type == ProductType.Edibles) {

                            clothingButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            ediblesButton.setBackground(getResources().getDrawable(R.drawable.button_selected_layout));
                            suppliesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            cbdButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            concButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            allProductsButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));

                        } else if (type == ProductType.Supplies) {

                            clothingButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            ediblesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            suppliesButton.setBackground(getResources().getDrawable(R.drawable.button_selected_layout));
                            cbdButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            concButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            allProductsButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));

                        } else if (type == ProductType.CBD) {

                            clothingButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            ediblesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            suppliesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            cbdButton.setBackground(getResources().getDrawable(R.drawable.button_selected_layout));
                            concButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            allProductsButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));

                        } else if (type == ProductType.Flower) {

                            clothingButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            ediblesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            suppliesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            cbdButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                            concButton.setBackground(getResources().getDrawable(R.drawable.button_selected_layout));
                            allProductsButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));

                        }

                    } else {

                        clothingButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                        ediblesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                        suppliesButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                        cbdButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                        concButton.setBackground(getResources().getDrawable(R.drawable.button_default_layout));
                        allProductsButton.setBackground(getResources().getDrawable(R.drawable.button_selected_layout));

                    }

                } else {

                    //mMACallback.postToastMessage(getResources().getString(R.string.toast_message_something_went_wrong));

                }

            }
        });

    }

    private void updateListView(final ProductType type) {

        mProducts.clear();
        mProductAdapter.notifyDataSetChanged();

        if (type != null) {

            for (int i = 0; i < mMACallback.getProductList().size(); i++) {

                if (mMACallback.getProductList().get(i).getProductType() == type) {

                    mProducts.add(mMACallback.getProductList().get(i));
                    mProductAdapter.notifyDataSetChanged();

                }

            }

        } else {

            mProducts.addAll(mMACallback.getProductList());
            mProductAdapter.notifyDataSetChanged();

        }

    }

}
