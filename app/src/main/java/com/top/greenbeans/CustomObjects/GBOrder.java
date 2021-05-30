//
//
// TOP Development
// GBOrder.java
//
//

package com.top.greenbeans.CustomObjects;

import com.top.greenbeans.Enums.OrderStatus;

import java.util.List;

public abstract class GBOrder {

    /*
     *
     * Member Variables
     *
     */

    private String mDocumentString;
    private String mUserFullName;
    private List<String> mOrderDetails;
    private String mOrderStatus;
    private String mOrderTotal;
    private String mOrderTax;
    private String mOrderSubtotal;
    private String mUserEmail;
    private String mOrderTime;
    private String mSpecialInstructions;



    /*
     *
     * Constructor
     *
     */

    public GBOrder(String documentString, String userFullName, String userEmail, String orderStatus,
                   List<String> orderDetails, String orderSubtotal, String orderTax,
                   String orderTotal, String orderTime) {

        mDocumentString = documentString;
        mUserFullName = userFullName;
        mUserEmail = userEmail;
        mOrderStatus = orderStatus;
        mOrderDetails = orderDetails;
        mOrderSubtotal = orderSubtotal;
        mOrderTax = orderTax;
        mOrderTotal = orderTotal;
        mOrderTime = orderTime;
        mSpecialInstructions = "";

    }

    public GBOrder(String documentString, String userFullName, String userEmail,
                   String orderStatus, List<String> orderDetails, String orderSubtotal,
                   String orderTax, String orderTotal, String orderTime, String specialInstructions) {

        mDocumentString = documentString;
        mUserFullName = userFullName;
        mUserEmail = userEmail;
        mOrderStatus = orderStatus;
        mOrderDetails = orderDetails;
        mOrderSubtotal = orderSubtotal;
        mOrderTax = orderTax;
        mOrderTotal = orderTotal;
        mOrderTime = orderTime;
        mSpecialInstructions = specialInstructions;

    }



    /*
     *
     * Class Methods
     *
     */

    public String getDocumentString() {

        return mDocumentString;

    }

    public String getUserFullName() {

        return mUserFullName;

    }

    public String getUserEmail() {

        return mUserEmail;

    }

    public String getOrderStatus() {

        return mOrderStatus;

    }

    public void updateOrderStatus(OrderStatus status) {

        mOrderStatus = status.toString();

    }

    public List<String> getOrderDetails() {

        return mOrderDetails;

    }

    public String getOrderSubtotal() {

        return mOrderSubtotal;

    }

    public String getOrderTax() {

        return mOrderTax;

    }

    public String getOrderTotal() {

        return mOrderTotal;

    }

    public String getOrderTime() {

        return mOrderTime;

    }

    public String getSpecialInstructions() {

        return mSpecialInstructions;

    }

}
