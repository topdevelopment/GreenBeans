//
//
// TOP Development
// DialogType.java
//
//

package com.top.greenbeans.Enums;

public enum DialogType {

    Loading {
        @Override
        public String toString() {
            return "Loading";
        }
    },
    OrderType {
        @Override
        public String toString() {
            return "Order Type";
        }
    },
    OrderStatus {
        @Override
        public String toString() {
            return "Order Status";
        }
    },
    Confirm {
        @Override
        public String toString() {
            return "Confirm";
        }
    },
    ProductType {
        @Override
        public String toString() {
            return "Product Type";
        }
    },
    HighlightProduct {
        @Override
        public String toString() {
            return "Highlight Product";
        }
    },
    UnhighlightProduct {
        @Override
        public String toString() {
            return "Unhighlight Product";
        }
    },
    HighlightDiscount {
        @Override
        public String toString() {
            return "Highlight Discount";
        }
    },
    AddressSelection {
        @Override
        public String toString() {
            return "Address Selection";
        }
    }

}
