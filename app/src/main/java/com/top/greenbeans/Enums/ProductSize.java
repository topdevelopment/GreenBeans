//
//
// TOP Development
// ProductSize.java
//
//

package com.top.greenbeans.Enums;

public enum ProductSize {

    Small {
        @Override
        public String toString() {
            return "Small";
        }
    },
    Medium {
        @Override
        public String toString() {
            return "Medium";
        }
    },
    Large {
        @Override
        public String toString() {
            return "Large";
        }
    }

}
