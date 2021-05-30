//
//
// TOP Development
// ProductType.java
//
//

package com.top.greenbeans.Enums;

public enum ProductType {

    Flower {
        @Override
        public String toString() {
            return "Flower";
        }
    },
    Edibles {
        @Override
        public String toString() {
            return "Edible";
        }
    },
    CBD {
        @Override
        public String toString() {
            return "CBD";
        }
    },
    Supplies {
        @Override
        public String toString() {
            return "Supplies";
        }
    },
    Partner {
        @Override
        public String toString() {
            return "Partner Product";
        }
    },
    Clothing {
        @Override
        public String toString() {
            return "Clothing";
        }
    },
    Concentrate {
        @Override
        public String toString() {
            return "Concentrate";
        }
    }

}
