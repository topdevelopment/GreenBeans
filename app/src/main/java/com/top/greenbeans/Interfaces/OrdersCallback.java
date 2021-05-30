//
//
// TOP Development
// OrdersCallback.java
//
//

package com.top.greenbeans.Interfaces;

import com.top.greenbeans.CustomObjects.GBOrder;

import java.util.ArrayList;

public interface OrdersCallback {

    ArrayList<GBOrder> getOrders();
    void addOrder(GBOrder order);
    void viewOrderDetails(GBOrder order);
    void updateEditedOrder(GBOrder order);

}
