//
//
// TOP Development
// GBPickUpOrder.java
//
//

package com.top.greenbeans.CustomObjects;

import java.util.List;

public class GBPickUpOrder extends GBOrder {

    /*
     *
     * Member Variables
     *
     */

    private String mPickupTime;
    private String mPickupLocation;



    /*
     *
     * Constructor
     *
     */

    public GBPickUpOrder(String documentString, String userFullName, String userEmail, String orderStatus,
                   List<String> orderDetails, String orderSubtotal, String orderTax,
                   String orderTotal, String orderTime, String pickupTime, String pickupLocation) {

        super(documentString, userFullName, userEmail, orderStatus, orderDetails, orderSubtotal,
                orderTax, orderTotal, orderTime);
        mPickupTime = pickupTime;
        mPickupLocation = pickupLocation;

    }

    public GBPickUpOrder(String documentString, String userFullName, String userEmail, String orderStatus,
                           List<String> orderDetails, String orderSubtotal, String orderTax,
                           String orderTotal, String orderTime, String specialInstructions,
                         String pickupTime, String pickupLocation) {

        super(documentString, userFullName, userEmail, orderStatus, orderDetails, orderSubtotal,
                orderTax, orderTotal, orderTime, specialInstructions);
        mPickupTime = pickupTime;
        mPickupLocation = pickupLocation;

    }

    public GBPickUpOrder(String documentString, GBPickUpOrder order) {

        super(documentString, order.getUserFullName(), order.getUserEmail(), order.getOrderStatus(),
                order.getOrderDetails(), order.getOrderSubtotal(), order.getOrderTax(),
                order.getOrderTotal(), order.getOrderTime(), order.getSpecialInstructions());
        mPickupTime = order.getPickupTime();
        mPickupLocation = order.getPickupLocation();

    }



    /*
     *
     * Class Methods
     *
     */

    public String getPickupTime() {

        return mPickupTime;

    }

    public String getPickupLocation() {

        return mPickupLocation;

    }

}
