//
//
// TOP Development
// MACallback.java
//
//

package com.top.greenbeans.Interfaces;

import android.content.Context;

import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Enums.ScreensID;
import com.top.greenbeans.Services.FirebaseCommService;

import java.util.ArrayList;

import javax.annotation.Nullable;

public interface MACallback {

    Context getActivityContext();
    GBUser getCurrentUser();
    void selectScreen(ScreensID screenId);
    void searchVirtualStore(@Nullable ProductType type);
    void postToastMessage(String toastMessage);
    void toggleNavigationDrawer();
    void backButtonPressed();
    void runOnMainThread(Runnable runnable);
    void createDialog(DialogType type);
    void createDialog(DialogType type, DialogCallback callback);
    void createDialog(DialogType type, DialogCallback callback, String title);
    void dismissAllDialogs();
    ArrayList<GBProduct> getProductList();
    void openGMIntent(GMCallback callback);

}
