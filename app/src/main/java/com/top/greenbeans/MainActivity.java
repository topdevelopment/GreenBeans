//
//
// TOP Development
// MainActivity.java
//
//

package com.top.greenbeans;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.top.greenbeans.CustomObjects.DrawerListAdapter;
import com.top.greenbeans.CustomObjects.DrawerListContent;
import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.DialogFragments.AddAdminUserDialogFragment;
import com.top.greenbeans.DialogFragments.AddressSelectionDialogFragment;
import com.top.greenbeans.DialogFragments.ConfirmDialogFragment;
import com.top.greenbeans.DialogFragments.HighlightDialogFragment;
import com.top.greenbeans.DialogFragments.HighlightDiscountDialogFragment;
import com.top.greenbeans.DialogFragments.LoadingDialogFragment;
import com.top.greenbeans.DialogFragments.OrderStatusDailogFragment;
import com.top.greenbeans.DialogFragments.OrderTypeDialogFragment;
import com.top.greenbeans.DialogFragments.ProductTypeDialogFragment;
import com.top.greenbeans.DialogFragments.UnhighlightDialogFragment;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Enums.ScreensID;
import com.top.greenbeans.Fragments.AddProductFragment;
import com.top.greenbeans.Fragments.AdminOrderDetailsFragment;
import com.top.greenbeans.Fragments.AdminOrdersFragment;
import com.top.greenbeans.Fragments.AdminProductsFragment;
import com.top.greenbeans.Fragments.AdminUsersFragment;
import com.top.greenbeans.Fragments.EditProductFragment;
import com.top.greenbeans.Fragments.GuestFormFragment;
import com.top.greenbeans.Fragments.KartFragment;
import com.top.greenbeans.Fragments.LoginFragment;
import com.top.greenbeans.Fragments.MusicPodcastFragment;
import com.top.greenbeans.Fragments.OrderDetailsFragment;
import com.top.greenbeans.Fragments.OrderHistoryFragment;
import com.top.greenbeans.Fragments.OrderSummaryFragment;
import com.top.greenbeans.Fragments.ProductDetailsFragment;
import com.top.greenbeans.Fragments.SearchFragment;
import com.top.greenbeans.Fragments.SettingsFragment;
import com.top.greenbeans.Fragments.SignUpFragment;
import com.top.greenbeans.Fragments.ViewOrdersFragment;
import com.top.greenbeans.Fragments.VirtualStoreFragment;
import com.top.greenbeans.Interfaces.AdminCallback;
import com.top.greenbeans.Interfaces.AdminDialogCallback;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.FirebaseDirectCallback;
import com.top.greenbeans.Interfaces.GMCallback;
import com.top.greenbeans.Interfaces.KartCallback;
import com.top.greenbeans.Interfaces.KartDetailsCallback;
import com.top.greenbeans.Interfaces.LoginCallback;
import com.top.greenbeans.Interfaces.MACallback;
import com.top.greenbeans.Interfaces.OrdersCallback;
import com.top.greenbeans.Interfaces.StoreCallback;
import com.top.greenbeans.Services.FirebaseCommService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class MainActivity
        extends AppCompatActivity
        implements MACallback, LoginCallback, OrdersCallback,
        KartDetailsCallback, StoreCallback, AdminCallback, FirebaseDirectCallback {

    /*
     *
     * Member Variables
     *
     */

    public static String LOG_TAG = "GreenBeans";
    public static String STORE_ADDRESS = "101 South Mason Road, Saginaw, MI";
    private Toast mToast = null;
    private DialogFragment mDialog = null;
    private GBUser mCurrentUser = null;
    private DrawerLayout mDrawerLayout = null;
    private boolean mIsDrawerOpen = false;
    private ArrayList<GBOrder> mOrdersList = new ArrayList<>();
    private ArrayList<GBProduct> mProductsList = new ArrayList<>();
    private ArrayList<GBProduct> mKart = new ArrayList<>();
    private ArrayList<GBOrder> mAdminOrderList = new ArrayList<>();
    private GMCallback mGMCallback = null;



    /*
     *
     * Lifecycle Methods
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        selectScreen(ScreensID.Login);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
            PlacesClient placesClient = Places.createClient(this);
        }

    }

    @Override
    public void onBackPressed() {

        getSupportFragmentManager().popBackStack();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Log.i(LOG_TAG, "onActivityResult");
        if (requestCode == 1) {
            //Log.i(LOG_TAG, "requestCode == 1");
            if (resultCode == RESULT_OK) {
                //Log.i(LOG_TAG, "resultCode == RESULT_OK");
                Place place = Autocomplete.getPlaceFromIntent(data);
                mGMCallback.returnedPlace(place);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                //Log.i(LOG_TAG, "resultCode == RESULT_ERROR");
                Status status = Autocomplete.getStatusFromIntent(data);

            } else if (resultCode == RESULT_CANCELED) {
                //Log.i(LOG_TAG, "resultCode == RESULT_CANCELED");

            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    /*
     *
     * Class Methods
     *
     */

    @Override // MACallback Interface Method
    public Context getActivityContext() {

        return this;

    }

    @Override
    public GBUser getCurrentUser() {

        return mCurrentUser;

    }

    @Override // MACallback Interface Method
    public void selectScreen(ScreensID screenId) {

        Fragment frag = null;

        if (screenId == ScreensID.Login) {

            frag = LoginFragment.newInstance();

        } else if (screenId == ScreensID.VirtualStore) {

            frag = VirtualStoreFragment.newInstance();

        } else if (screenId == ScreensID.MusicPodcast) {

            frag = MusicPodcastFragment.newInstance();

        } else if (screenId == ScreensID.AdminOrders) {

            frag = AdminOrdersFragment.newInstance();

        } else if (screenId == ScreensID.AdminProducts) {

            frag = AdminProductsFragment.newInstance();

        } else if (screenId == ScreensID.Kart) {

            frag = OrderSummaryFragment.newInstance();

        } else if (screenId == ScreensID.Settings) {

            frag = SettingsFragment.newInstance();

        } else if (screenId == ScreensID.AddProduct) {

            frag = AddProductFragment.newInstance();

        } else if (screenId == ScreensID.GuestForm) {

            frag = GuestFormFragment.newInstance();

        } else if (screenId == ScreensID.Signup) {

            frag = SignUpFragment.newInstance();

        } else if (screenId == ScreensID.OrderHistory) {

            frag = OrderHistoryFragment.newInstance();

        } else if (screenId == ScreensID.AdminUsers) {

            frag = AdminUsersFragment.newInstance();

        }

        if (frag != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, frag)
                    .addToBackStack(screenId.toString()).commit();

        }

    }

    @Override // MACallback Interface Method
    public void searchVirtualStore(@Nullable ProductType type) {

        Fragment frag;

        if (type != null) {

            frag = SearchFragment.newInstance(type);

        } else {

            frag = SearchFragment.newInstance();

        }

        if (frag != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, frag)
                    .commit();

        }

    }

    @Override // MACallback Interface Method
    public void postToastMessage(String toastMessage) {

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(getActivityContext(), toastMessage, Toast.LENGTH_SHORT);
        mToast.show();

    }

    @Override // MACallback Interface Method
    public void toggleNavigationDrawer() {

        if (mIsDrawerOpen) {
            mDrawerLayout.closeDrawers();
            mIsDrawerOpen = false;
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mIsDrawerOpen = true;
        }

    }

    @Override // MACallback Interface Method
    public void backButtonPressed() {

        onBackPressed();

    }

    @Override // MACallback Interface Method
    public void runOnMainThread(Runnable runnable) {

        runOnUiThread(runnable);

    }

    @Override // MACallback Interface Method
    public void createDialog(DialogType type) {

        dismissAllDialogs();
        if (type == DialogType.Loading) {

            mDialog = new LoadingDialogFragment();
            mDialog.show(getSupportFragmentManager(), type.toString());

        }

    }

    @Override // MACallback Interface Method
    public void createDialog(DialogType type, DialogCallback callback) {

        dismissAllDialogs();
        if (type == DialogType.OrderType) {

            if (callback instanceof DialogCallback.OrderTypeDialogCallback) {
                mDialog = new OrderTypeDialogFragment((DialogCallback.OrderTypeDialogCallback) callback);
            }

        } else if (type == DialogType.OrderStatus) {

            if (callback instanceof DialogCallback.OrderStatusDialogCallback) {
                mDialog = new OrderStatusDailogFragment((DialogCallback.OrderStatusDialogCallback) callback);
            }

        } else if (type == DialogType.Confirm) {

            if (callback instanceof DialogCallback.ConfirmDialogCallback) {
                mDialog = new ConfirmDialogFragment((DialogCallback.ConfirmDialogCallback) callback);
            }

        } else if (type == DialogType.ProductType) {

            if (callback instanceof DialogCallback.ProductTypeDialogCallback) {
                mDialog = new ProductTypeDialogFragment((DialogCallback.ProductTypeDialogCallback) callback);
            }

        } else if (type == DialogType.HighlightProduct) {

            if (callback instanceof DialogCallback.ConfirmDialogCallback) {
                mDialog = new HighlightDialogFragment((DialogCallback.ConfirmDialogCallback) callback);
            }

        } else if (type == DialogType.UnhighlightProduct) {

            if (callback instanceof DialogCallback.ConfirmDialogCallback) {
                mDialog = new UnhighlightDialogFragment((DialogCallback.ConfirmDialogCallback) callback);
            }

        } else if (type == DialogType.HighlightDiscount) {

            if (callback instanceof DialogCallback.HighlightDiscountDialogCallback) {
                mDialog = new HighlightDiscountDialogFragment(this, (DialogCallback.HighlightDiscountDialogCallback) callback);
            }

        } else if (type == DialogType.AddressSelection) {

            if (callback instanceof DialogCallback.AddressSelectionDialogCallback) {
                mDialog = new AddressSelectionDialogFragment((DialogCallback.AddressSelectionDialogCallback) callback);
            }

        } else {

            mDialog = null;

        }

        if (mDialog != null) {

            mDialog.show(getSupportFragmentManager(), type.toString());

        }

    }

    @Override // MACallback Interface Method
    public void createDialog(DialogType type, DialogCallback callback, String title) {

        dismissAllDialogs();
        if (type == DialogType.Confirm) {

            if (callback instanceof DialogCallback.ConfirmDialogCallback) {
                mDialog = new ConfirmDialogFragment((DialogCallback.ConfirmDialogCallback) callback, title);
                mDialog.show(getSupportFragmentManager(), type.toString());
            }

        }

    }

    @Override // MACallback Interface Method
    public void dismissAllDialogs() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mDialog != null && mDialog.getDialog() != null && mDialog.getDialog().isShowing()) {

                    mDialog.dismiss();

                }

                if (mToast != null) {

                    mToast.cancel();

                }

                hideKeyboard(MainActivity.this);

            }
        });

    }

    @Override // MACallback Interface Method
    public ArrayList<GBProduct> getProductList() {

        return mProductsList;

    }

    @Override // MACallback Interface Method
    public void openGMIntent(GMCallback callback) {

        mGMCallback = callback;
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
        startActivityForResult(intent, 1);

    }

    @Override // LoginCallback Interface Method
    public void setCurrentUser(GBUser user) {

        mCurrentUser = user;
        selectScreen(ScreensID.VirtualStore);
        ListView drawerList = findViewById(R.id.left_drawer);
        final DrawerListContent content = new DrawerListContent(this, true);
        drawerList.setAdapter(new DrawerListAdapter(this, R.layout.menu_drawer_item, content.getItems()));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toggleNavigationDrawer();
                selectScreen(content.getItems().get(position).getScreenId());
            }
        });

        postToastMessage(getResources().getString(R.string.toast_message_login_successful));

    }

    @Override // OrdersCallback Interface Method
    public ArrayList<GBOrder> getOrders() {

        return mOrdersList;

    }

    @Override // OrdersCallback Interface Method
    public void addOrder(GBOrder order) {

        mOrdersList.add(order);

    }

    @Override // OrdersCallback Interface Method
    public void viewOrderDetails(GBOrder order) {

        if (order != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, OrderDetailsFragment.newInstance(order))
                    .commit();

        }

    }

    @Override
    public void updateEditedOrder(GBOrder order) {

        //todo: go through the list of orders and update the data within the app and the data in the database

    }

    @Override // KartCallback Interface Method
    public void addToKart(GBProduct product) {

        mKart.add(product);
        postToastMessage(getResources().getString(R.string.toast_message_added_to_kart));

    }

    @Override // KartCallback Interface Method
    public void removeFromKart(GBProduct product) {

        int index = -1;
        for(int i = 0; i < mKart.size(); i++) {

            if (mKart.get(i).getProductTitle().matches(product.getProductTitle())) {

                mKart.get(i).updateQuantity(product.getQuantity());
                if (mKart.get(i).getQuantity() <= 0) {
                    index = i;
                }
                break;

            }

        }

        if (index != -1) {

            mKart.remove(index);
            postToastMessage(getResources().getString(R.string.toast_message_removed_from_kart));

        }

    }

    @Override // KartDetailsCallback Interface Method
    public ArrayList<GBProduct> getKart() {

        return mKart;

    }

    @Override // StoreCallback Interface Method
    public void viewProductDetails(GBProduct product) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ProductDetailsFragment.newInstance(product))
                .addToBackStack(ScreensID.ProductDetails.toString()).commit();

    }

    @Override // AdminCallback Interface Method
    public ArrayList<GBOrder> getAdminOrders() {

        return mAdminOrderList;

    }

    @Override // AdminCallback Interface Method
    public void editProduct(GBProduct product) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, EditProductFragment.newInstance(product))
                .addToBackStack(ScreensID.EditProduct.toString()).commit();

    }

    @Override // AdminCallback Interface Method
    public void addUser(AdminDialogCallback callback) {

        dismissAllDialogs();

    }

    @Override // AdminCallback Interface Method
    public void editUser(AdminDialogCallback callback, GBUser user) {

        dismissAllDialogs();
        mDialog = new AddAdminUserDialogFragment(callback);
        mDialog.show(getSupportFragmentManager(), "");

    }

    @Override // AdminCallback Interface Method
    public void viewOrderAdminDetails(GBOrder order) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, AdminOrderDetailsFragment.newInstance(order))
                .addToBackStack(ScreensID.AdminOrderDetails.toString()).commit();

    }

    public static void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}
