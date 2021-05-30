//
//
// TOP Development
// AdminCallback.java
//
//

package com.top.greenbeans.Interfaces;

import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.CustomObjects.GBUser;

import java.util.ArrayList;

public interface AdminCallback {

    ArrayList<GBOrder> getAdminOrders();
    void editProduct(GBProduct product);
    void addUser(AdminDialogCallback callback);
    void editUser(AdminDialogCallback callback, GBUser user);
    void viewOrderAdminDetails(GBOrder order);

}
