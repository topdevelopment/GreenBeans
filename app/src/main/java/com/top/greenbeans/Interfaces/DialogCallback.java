//
//
// TOP Development
// DialogCallback.java
//
//

package com.top.greenbeans.Interfaces;

import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.ProductType;

public interface DialogCallback {

    public interface OrderTypeDialogCallback extends DialogCallback {
        void orderTypeSelected(String type);
    }

    public interface OrderStatusDialogCallback extends DialogCallback {
        void orderStatusSelected(OrderStatus status);
    }

    public interface ConfirmDialogCallback extends DialogCallback {
        void confirmResponse(boolean confirm);
    }

    public interface ProductTypeDialogCallback extends DialogCallback {
        void productTypeSelected(ProductType type);
    }

    public interface HighlightDiscountDialogCallback extends DialogCallback {
        void highlightDiscountConfirmed(String discount);
    }

    public interface AddressSelectionDialogCallback extends DialogCallback {
        void addressSelected(String address);
    }

}
