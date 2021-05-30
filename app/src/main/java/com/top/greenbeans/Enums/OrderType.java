//
//
// TOP Development
// OrderType.java
//
//

package com.top.greenbeans.Enums;

public enum OrderType {

    Delivery {
        @Override
        public String toString() {
            return "Delivery";
        }
    },
    PickUp {
        @Override
        public String toString() {
            return "PickUp";
        }
    }

}
