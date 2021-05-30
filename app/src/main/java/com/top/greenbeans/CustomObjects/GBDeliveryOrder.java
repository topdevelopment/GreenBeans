//
//
// TOP Development
// GBDeliveryOrder.java
//
//

package com.top.greenbeans.CustomObjects;

import java.util.List;

public class GBDeliveryOrder extends GBOrder {

    /*
     *
     * Member Variables
     *
     */

    private String mDeliveryFee;
    private String mDeliveryLocation;



    /*
     *
     * Constructor
     *
     */

    public GBDeliveryOrder(String documentString, String userFullName, String userEmail, String orderStatus,
                   List<String> orderDetails, String orderSubtotal, String orderTax,
                   String orderTotal, String orderTime, String deliveryFee, String deliveryLocation) {

        super(documentString, userFullName, userEmail, orderStatus, orderDetails, orderSubtotal,
                orderTax, orderTotal, orderTime);
        mDeliveryFee = deliveryFee;
        mDeliveryLocation = deliveryLocation;

    }

    public GBDeliveryOrder(String documentString, String userFullName, String userEmail, String orderStatus,
                           List<String> orderDetails, String orderSubtotal, String orderTax,
                           String orderTotal, String orderTime, String specialInstructions,
                           String deliveryFee, String deliveryLocation) {

        super(documentString, userFullName, userEmail, orderStatus, orderDetails, orderSubtotal,
                orderTax, orderTotal, orderTime, specialInstructions);
        mDeliveryFee = deliveryFee;
        mDeliveryLocation = deliveryLocation;

    }

    public GBDeliveryOrder(String documentString, GBDeliveryOrder order) {

        super(documentString, order.getUserFullName(), order.getUserEmail(), order.getOrderStatus(),
                order.getOrderDetails(), order.getOrderSubtotal(), order.getOrderTax(),
                order.getOrderTotal(), order.getOrderTime(), order.getSpecialInstructions());
        mDeliveryLocation = order.getDeliveryLocation();
        mDeliveryFee = order.getDeliveryFee();

    }



    /*
     *
     * Class Methods
     *
     */

    public String getDeliveryFee() {

        return mDeliveryFee;

    }

    public String getDeliveryLocation() {

        return mDeliveryLocation;

    }

}
