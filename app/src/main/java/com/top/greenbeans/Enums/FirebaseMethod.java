//
//
// TOP Development
// FirebaseMethod.java
//
//

package com.top.greenbeans.Enums;

public enum FirebaseMethod {

    Login {
        @Override
        public String toString() {
            return "Login";
        }
    },
    GetUserInfo {
        @Override
        public String toString() {
            return "Get User Info";
        }
    },
    GetAllProducts {
        @Override
        public String toString() {
            return "Get All Products";
        }
    },
    GetOrders {
        @Override
        public String toString() {
            return "Get Orders";
        }
    },
    PlaceOrder {
        @Override
        public String toString() {
            return "Place Order";
        }
    },
    AddToOrderHistory {
        @Override
        public String toString() {
            return "Add To Order History";
        }
    },
    UpdateOrderStatus {
        @Override
        public String toString() {
            return "Update Order Status";
        }
    },
    ArchiveOrder {
        @Override
        public String toString() {
            return "Archive Order";
        }
    },
    AddProduct {
        @Override
        public String toString() {
            return "Add Product";
        }
    },
    EditProduct {
        @Override
        public String toString() {
            return "Edit Product";
        }
    },
    DeleteProduct {
        @Override
        public String toString() {
            return "Delete Product";
        }
    },
    CreateAccount {
        @Override
        public String toString() {
            return "Create Account";
        }
    },
    DeleteAccount {
        @Override
        public String toString() {
            return "Delete Account";
        }
    },
    EditAccount {
        @Override
        public String toString() {
            return "Edit Account";
        }
    },
    GetProductImage {
        @Override
        public String toString() {
            return "Get Product Image";
        }
    },
    HighlightDiscount {
        @Override
        public String toString() {
            return "Highlight Discount";
        }
    },
    GetAllAdminUsers {
        @Override
        public String toString() {
            return "Get All Admin Users";
        }
    },
    GetAllAddresses {
        @Override
        public String toString() {
            return "Get All Addresses";
        }
    }

}
