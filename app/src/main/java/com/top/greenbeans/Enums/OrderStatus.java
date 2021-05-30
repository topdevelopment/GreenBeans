//
//
// TOP Development
// OrderStatus.java
//
//

package com.top.greenbeans.Enums;

public enum OrderStatus {

    InProgress {
        @Override
        public String toString() {
            return "IN PROGRESS";
        }
    },
    ReadyForPickup {
        @Override
        public String toString() {
            return "READY FOR PICKUP";
        }
    },
    EnRoute {
        @Override
        public String toString() {
            return "EN ROUTE";
        }
    },
    Completed {
        @Override
        public String toString() {
            return "COMPLETED";
        }
    }

}
