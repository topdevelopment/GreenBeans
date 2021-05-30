//
//
// TOP Development
// FirebaseCommService.java
//
//

package com.top.greenbeans.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.top.greenbeans.CustomObjects.GBDeliveryOrder;
import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.CustomObjects.GBPickUpOrder;
import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.OrderType;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.FirebaseDirectCallback;
import com.top.greenbeans.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseCommService {

    /*
     *
     * Database Identifiers
     *
     */

    private static String USERS_IDENTIFIER = "Users";
    private static String PRODUCTS_IDENTIFIER = "Products";
    private static String ORDERS_IDENTIFIER = "Orders";
    private static String ORDER_HISTORY_IDENTIFIER = "OrderHistory";
    private static String ADDRESSES_IDENTIFIER = "Address";



    /*
     *
     * Database Fields Identifiers
     *
     */

    private static String FULL_NAME_ID = "fullName";
    private static String PRODUCT_TITLE_ID = "productTitle";
    private static String PRODUCT_DESCRIPTION_ID = "productDescription";
    private static String PRODUCT_PRICE_ID = "productPrice";
    private static String PRODUCT_TYPE_ID = "productType";
    private static String ARCHIVED_ID = "archived";
    private static String ORDER_DETAILS_ID = "orderDetails";
    private static String ORDER_STATUS_ID = "orderStatus";
    private static String ORDER_TOTAL_ID = "orderTotal";
    private static String ORDER_TYPE_ID = "orderType";
    private static String ORDER_TIME_ID = "orderTime";
    private static String USER_EMAIL_ID = "userEmail";
    private static String APP_MANAGER_ID = "appManager";
    private static String HIGHLIGHTED_PRODUCT_ID = "highlightedProduct";
    private static String HIGHLIGHTED_DISCOUNT_ID = "highlightedDiscount";
    private static String ADDRESS_ID = "address";
    private static String CUSTOMER_ADDRESS_ID = "customerAddress";
    private static String PICK_UP_ADDRESS_ID = "pickUpAddress";
    private static String TAX_ID = "tax";
    private static String SUBTOTAL_ID = "subtotal";
    private static String DELIVERY_FEE_ID = "deliveryFee";
    private static String PRODUCT_IMAGE_URL = "productImageUrl";
    private static String SPECIAL_INSTRUCTIONS_ID = "specialInstructions";



    /*
     *
     * Member Variables
     *
     */

    private int ONE_MB = (1024 * 1024);
    private FirebaseCallback mCallback;
    private FirebaseDirectCallback mMACallback;



    /*
     *
     * Constructor
     *
     */

    public FirebaseCommService(Context context, FirebaseCallback callback) {

        mCallback = callback;
        if (context instanceof FirebaseDirectCallback) {
            mMACallback = (FirebaseDirectCallback) context;
        }

    }



    /*
     *
     * Class Methods
     *
     */

    public void attemptLogin(final String email, final String password) {

        Log.i(MainActivity.LOG_TAG, "Login Attempt | Email: " + email + " | Password: " + password);
        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //Log.i(MainActivity.LOG_TAG, "Login Attempt Successful");
                            getUserInfo(email);

                        } else {

                            //Log.i(MainActivity.LOG_TAG, "Login Attempt Failed");
                            mCallback.errorResponse(FirebaseMethod.Login);

                        }

                    }
                });

            }
        }).start();

    }

    public void getUserInfo(final String email) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);
                Log.i(MainActivity.LOG_TAG, "Start Get User Info");

                database.collection(USERS_IDENTIFIER).document(email).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()) {
                                    Log.i(MainActivity.LOG_TAG, "Get User Info Successful");

                                    DocumentSnapshot snapshot = task.getResult();

                                    if (snapshot != null) {

                                        String address = "";
                                        if (snapshot.contains(ADDRESS_ID)) {

                                            address = snapshot.getString(ADDRESS_ID);

                                        }

                                        String fullName = "";
                                        if (snapshot.contains(FULL_NAME_ID)) {

                                            fullName = snapshot.getString(FULL_NAME_ID);
                                            GBUser user = new GBUser(email, fullName, address, snapshot.contains(APP_MANAGER_ID));
                                            if (mCallback != null) {
                                                mCallback.successfulObjectResponse(FirebaseMethod.GetUserInfo, user);
                                            }

                                        } else {

                                            if (mCallback != null) {
                                                mCallback.errorResponse(FirebaseMethod.GetUserInfo);
                                            }

                                        }

                                    } else {

                                        if (mCallback != null) {
                                            mCallback.errorResponse(FirebaseMethod.GetUserInfo);
                                        }

                                    }

                                } else {

                                    Log.i(MainActivity.LOG_TAG, "Get User Info Failed");
                                    if (mCallback != null) {
                                        mCallback.errorResponse(FirebaseMethod.GetUserInfo);
                                    }

                                }

                            }
                        });

            }
        }).start();

    }

    public void getAllProducts() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                database.collection(PRODUCTS_IDENTIFIER).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    if (task.getResult() == null) {

                                        mCallback.errorResponse(FirebaseMethod.GetAllProducts);
                                        return;

                                    }

                                    List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                                    if (snapshots.size() > 0) {

                                        for (int i = 0; i < snapshots.size(); i++) {

                                            DocumentSnapshot snapshot = snapshots.get(i);
                                            String title = "";
                                            String desc = "";
                                            String price = "";
                                            ProductType type = null;
                                            String imageUrl = "";

                                            if (snapshot.contains(PRODUCT_TITLE_ID)) {
                                                title = snapshot.getString(PRODUCT_TITLE_ID);
                                            }
                                            if (snapshot.contains(PRODUCT_DESCRIPTION_ID)) {
                                                desc = snapshot.getString(PRODUCT_DESCRIPTION_ID);
                                            }
                                            if (snapshot.contains(PRODUCT_PRICE_ID)) {
                                                price = snapshot.getString(PRODUCT_PRICE_ID);
                                            }
                                            if (snapshot.contains(PRODUCT_IMAGE_URL)) {
                                                imageUrl = snapshot.getString(PRODUCT_IMAGE_URL);
                                            }
                                            if (snapshot.contains(PRODUCT_TYPE_ID)) {

                                                String pType = snapshot.getString(PRODUCT_TYPE_ID);
                                                if (!pType.trim().isEmpty()) {

                                                    if (pType.matches(ProductType.Edibles.toString())) {
                                                        type = ProductType.Edibles;
                                                    } else if (pType.matches(ProductType.CBD.toString())) {
                                                        type = ProductType.CBD;
                                                    } else if (pType.matches(ProductType.Supplies.toString())) {
                                                        type = ProductType.Supplies;
                                                    } else if (pType.matches(ProductType.Partner.toString())) {
                                                        type = ProductType.Partner;
                                                    } else if (pType.matches(ProductType.Flower.toString())) {
                                                        type = ProductType.Flower;
                                                    } else if (pType.matches(ProductType.Concentrate.toString())) {
                                                        type = ProductType.Concentrate;
                                                    } else if (pType.matches(ProductType.Clothing.toString())) {
                                                        type = ProductType.Clothing;
                                                    }

                                                }

                                            }

                                            Log.i(MainActivity.LOG_TAG, "title: " + title);
                                            Log.i(MainActivity.LOG_TAG, "desc: " + desc);
                                            Log.i(MainActivity.LOG_TAG, "price: " + price);
                                            Log.i(MainActivity.LOG_TAG, "type: " + type);
                                            Log.i(MainActivity.LOG_TAG, "imageUrl: " + imageUrl);
                                            Log.i(MainActivity.LOG_TAG, "highlightedProduct: " + snapshot.getString(HIGHLIGHTED_PRODUCT_ID));

                                            if (title != null && !title.isEmpty()
                                                    && desc != null && !desc.isEmpty()
                                                    && price != null && !price.isEmpty()
                                                    && type != null) {

                                                if (snapshot.getString(HIGHLIGHTED_PRODUCT_ID) != null && Boolean.valueOf(snapshot.getString(HIGHLIGHTED_PRODUCT_ID))) {

                                                    mCallback.successfulObjectResponse(FirebaseMethod.GetAllProducts,
                                                            new GBProduct(snapshot.getId(), title, desc,
                                                                    type, price, snapshot.getString(HIGHLIGHTED_DISCOUNT_ID), imageUrl));

                                                } else {

                                                    mCallback.successfulObjectResponse(FirebaseMethod.GetAllProducts,
                                                            new GBProduct(snapshot.getId(), title, desc, type, price, imageUrl));

                                                }

                                            } else {
                                                Log.i(MainActivity.LOG_TAG, "not sending successful obj response");
                                            }

                                            if (i == snapshots.size()) {
                                                mCallback.successfulResponse(FirebaseMethod.GetAllProducts);
                                            }

                                        }

                                    } else {

                                        mCallback.successfulResponse(FirebaseMethod.GetAllProducts);

                                    }

                                } else {

                                    if (mCallback != null) {

                                        mCallback.errorResponse(FirebaseMethod.GetAllProducts);

                                    }

                                }

                            }
                        });

            }
        }).start();

    }

    public void getAllAdminOrders() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                database.collection(ORDERS_IDENTIFIER).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    if (task.getResult() != null) {

                                        if (task.getResult().getDocuments().size() > 0) {

                                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {

                                                String fullName = "";
                                                if (task.getResult().getDocuments().get(i).contains(FULL_NAME_ID)) {
                                                    fullName = task.getResult().getDocuments().get(i).getString(FULL_NAME_ID);
                                                }

                                                String email = "";
                                                if (task.getResult().getDocuments().get(i).contains(USER_EMAIL_ID)) {
                                                    email = task.getResult().getDocuments().get(i).getString(USER_EMAIL_ID);
                                                }

                                                List<String> orderDetails = new ArrayList<>();
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_DETAILS_ID)) {
                                                    orderDetails = (List<String>) task.getResult().getDocuments().get(i).get(ORDER_DETAILS_ID);
                                                }

                                                String orderStatus = "";
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_STATUS_ID)) {
                                                    orderStatus = task.getResult().getDocuments().get(i).getString(ORDER_STATUS_ID);
                                                }

                                                String deliveryFee = "";
                                                if (task.getResult().getDocuments().get(i).contains(DELIVERY_FEE_ID)) {
                                                    deliveryFee = task.getResult().getDocuments().get(i).getString(DELIVERY_FEE_ID);
                                                }

                                                String orderSubtotal = "";
                                                if (task.getResult().getDocuments().get(i).contains(SUBTOTAL_ID)) {
                                                    orderSubtotal = task.getResult().getDocuments().get(i).getString(SUBTOTAL_ID);
                                                }

                                                String orderTax = "";
                                                if (task.getResult().getDocuments().get(i).contains(TAX_ID)) {
                                                    orderTax = task.getResult().getDocuments().get(i).getString(TAX_ID);
                                                }

                                                String orderTotal = "";
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_TOTAL_ID)) {
                                                    orderTotal = task.getResult().getDocuments().get(i).getString(ORDER_TOTAL_ID);
                                                }

                                                String orderType = "";
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_TYPE_ID)) {
                                                    orderType = task.getResult().getDocuments().get(i).getString(ORDER_TYPE_ID);
                                                }

                                                String orderTime = "";
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_TIME_ID)) {
                                                    orderTime = task.getResult().getDocuments().get(i).getString(ORDER_TIME_ID);
                                                }

                                                String specialInstructions = "";
                                                if (task.getResult().getDocuments().get(i).contains(SPECIAL_INSTRUCTIONS_ID)) {
                                                    specialInstructions = task.getResult().getDocuments().get(i).getString(SPECIAL_INSTRUCTIONS_ID);
                                                }

                                                String documentString = task.getResult().getDocuments().get(i).getId();

                                                GBOrder order = null;
                                                if (orderType.matches(OrderType.Delivery.toString())) {

                                                    order = new GBDeliveryOrder(documentString, fullName, email,
                                                            orderStatus, orderDetails, deliveryFee, orderSubtotal, orderTax,
                                                            orderTotal, orderTime, specialInstructions);

                                                } else if (orderType.matches(OrderType.PickUp.toString())) {

                                                    order = new GBPickUpOrder(documentString, fullName, email,
                                                            orderStatus, orderDetails, deliveryFee, orderSubtotal, orderTax,
                                                            orderTotal, orderTime, specialInstructions);

                                                }

                                                if (order != null) {

                                                    mCallback.successfulObjectResponse(FirebaseMethod.GetOrders, order);

                                                } else {

                                                    mCallback.errorResponse(FirebaseMethod.GetOrders);

                                                }

                                            }

                                        } else {

                                            mCallback.successfulResponse(FirebaseMethod.GetOrders);

                                        }

                                    } else {

                                        mCallback.errorResponse(FirebaseMethod.GetOrders);

                                    }

                                } else {

                                    mCallback.errorResponse(FirebaseMethod.GetOrders);

                                }

                            }
                        });

            }
        }).start();

    }

    public void getOrderHistory(final String email) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                database.collection(USERS_IDENTIFIER).document(email).collection(ORDER_HISTORY_IDENTIFIER).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {

                                    if (task.getResult() != null) {

                                        if (task.getResult().getDocuments().size() > 0) {

                                            for (int i = 0; i < task.getResult().getDocuments().size(); i++) {

                                                String fullName = "";
                                                if (task.getResult().getDocuments().get(i).contains(FULL_NAME_ID)) {
                                                    fullName = task.getResult().getDocuments().get(i).getString(FULL_NAME_ID);
                                                }

                                                String email = "";
                                                if (task.getResult().getDocuments().get(i).contains(USER_EMAIL_ID)) {
                                                    email = task.getResult().getDocuments().get(i).getString(USER_EMAIL_ID);
                                                }

                                                List<String> orderDetails = new ArrayList<>();
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_DETAILS_ID)) {
                                                    orderDetails = (List<String>) task.getResult().getDocuments().get(i).get(ORDER_DETAILS_ID);
                                                }

                                                String orderStatus = "";
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_STATUS_ID)) {
                                                    orderStatus = task.getResult().getDocuments().get(i).getString(ORDER_STATUS_ID);
                                                }

                                                String deliveryFee = "";
                                                if (task.getResult().getDocuments().get(i).contains(DELIVERY_FEE_ID)) {
                                                    deliveryFee = task.getResult().getDocuments().get(i).getString(DELIVERY_FEE_ID);
                                                }

                                                String orderSubtotal = "";
                                                if (task.getResult().getDocuments().get(i).contains(SUBTOTAL_ID)) {
                                                    orderSubtotal = task.getResult().getDocuments().get(i).getString(SUBTOTAL_ID);
                                                }

                                                String orderTax = "";
                                                if (task.getResult().getDocuments().get(i).contains(TAX_ID)) {
                                                    orderTax = task.getResult().getDocuments().get(i).getString(TAX_ID);
                                                }

                                                String orderTotal = "";
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_TOTAL_ID)) {
                                                    orderTotal = task.getResult().getDocuments().get(i).getString(ORDER_TOTAL_ID);
                                                }

                                                String orderType = "";
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_TYPE_ID)) {
                                                    orderType = task.getResult().getDocuments().get(i).getString(ORDER_TYPE_ID);
                                                }

                                                String orderTime = "";
                                                if (task.getResult().getDocuments().get(i).contains(ORDER_TIME_ID)) {
                                                    orderTime = task.getResult().getDocuments().get(i).getString(ORDER_TIME_ID);
                                                }

                                                String specialInstructions = "";
                                                if (task.getResult().getDocuments().get(i).contains(SPECIAL_INSTRUCTIONS_ID)) {
                                                    specialInstructions = task.getResult().getDocuments().get(i).getString(SPECIAL_INSTRUCTIONS_ID);
                                                }

                                                String documentString = task.getResult().getDocuments().get(i).getId();

                                                GBOrder order = null;
                                                if (orderType.matches(OrderType.Delivery.toString())) {

                                                    order = new GBDeliveryOrder(documentString, fullName, email,
                                                            orderStatus, orderDetails, deliveryFee, orderSubtotal, orderTax,
                                                            orderTotal, orderTime, specialInstructions);

                                                } else if (orderType.matches(OrderType.PickUp.toString())) {

                                                    order = new GBPickUpOrder(documentString, fullName, email,
                                                            orderStatus, orderDetails, deliveryFee, orderSubtotal, orderTax,
                                                            orderTotal, orderTime, specialInstructions);

                                                }

                                                if (order != null) {

                                                    mCallback.successfulObjectResponse(FirebaseMethod.GetOrders, order);

                                                } else {

                                                    mCallback.errorResponse(FirebaseMethod.GetOrders);

                                                }

                                            }

                                        } else {

                                            mCallback.successfulResponse(FirebaseMethod.GetOrders);

                                        }

                                    } else {

                                        mCallback.errorResponse(FirebaseMethod.GetOrders);

                                    }

                                } else {

                                    mCallback.errorResponse(FirebaseMethod.GetOrders);

                                }

                            }
                        });

            }
        }).start();

    }

    public void placeOrder(final String email, final String userFullname, final GBOrder order) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                final Map<String, Object> newOrder = new HashMap<>();
                newOrder.put(ARCHIVED_ID, false);
                newOrder.put(USER_EMAIL_ID, email);
                newOrder.put(FULL_NAME_ID, userFullname);
                newOrder.put(ORDER_DETAILS_ID, order.getOrderDetails());
                newOrder.put(ORDER_STATUS_ID, order.getOrderStatus());
                newOrder.put(SUBTOTAL_ID, order.getOrderSubtotal());
                newOrder.put(TAX_ID, order.getOrderTax());
                newOrder.put(ORDER_TOTAL_ID, order.getOrderTotal());
                newOrder.put(ORDER_TIME_ID, order.getOrderTime());

                if (order instanceof GBDeliveryOrder) {

                    GBDeliveryOrder delivery = (GBDeliveryOrder) order;
                    newOrder.put(CUSTOMER_ADDRESS_ID, delivery.getDeliveryLocation());
                    newOrder.put(DELIVERY_FEE_ID, delivery.getDeliveryFee());

                } else if (order instanceof GBPickUpOrder) {

                    GBPickUpOrder pickup = (GBPickUpOrder) order;
                    newOrder.put(PICK_UP_ADDRESS_ID, MainActivity.STORE_ADDRESS);

                }

                database.collection(ORDERS_IDENTIFIER).add(newOrder).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulObjectResponse(FirebaseMethod.PlaceOrder, task.getResult().getId());

                            if (order instanceof GBDeliveryOrder) {

                                GBDeliveryOrder delivery = (GBDeliveryOrder) order;
                                addToOrderHistory(email, userFullname, new GBDeliveryOrder(task.getResult().getId(), delivery));

                            } else if (order instanceof GBPickUpOrder) {

                                GBPickUpOrder pickup = (GBPickUpOrder) order;
                                addToOrderHistory(email, userFullname, new GBPickUpOrder(task.getResult().getId(), pickup));

                            }

                        } else {

                            mCallback.errorResponse(FirebaseMethod.PlaceOrder);

                        }

                    }
                });

            }
        }).start();

    }

    public void addToOrderHistory(final String email, final String userFullname, final GBOrder order) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                Map<String, Object> newOrder = new HashMap<>();
                newOrder.put(ARCHIVED_ID, false);
                newOrder.put(USER_EMAIL_ID, email);
                newOrder.put(FULL_NAME_ID, userFullname);
                newOrder.put(ORDER_DETAILS_ID, order.getOrderDetails());
                newOrder.put(ORDER_STATUS_ID, order.getOrderStatus());
                newOrder.put(SUBTOTAL_ID, order.getOrderSubtotal());
                newOrder.put(TAX_ID, order.getOrderTax());
                newOrder.put(ORDER_TOTAL_ID, order.getOrderTotal());
                newOrder.put(ORDER_TIME_ID, order.getOrderTime());

                if (order instanceof GBDeliveryOrder) {

                    GBDeliveryOrder delivery = (GBDeliveryOrder) order;
                    newOrder.put(CUSTOMER_ADDRESS_ID, delivery.getDeliveryLocation());
                    newOrder.put(DELIVERY_FEE_ID, delivery.getDeliveryFee());

                } else if (order instanceof GBPickUpOrder) {

                    GBPickUpOrder pickup = (GBPickUpOrder) order;
                    newOrder.put(PICK_UP_ADDRESS_ID, MainActivity.STORE_ADDRESS);

                }

                database.collection(USERS_IDENTIFIER).document(email).collection(ORDER_HISTORY_IDENTIFIER)
                        .document(order.getDocumentString()).set(newOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.AddToOrderHistory);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.AddToOrderHistory);

                        }

                    }
                });

            }
        }).start();

    }

    public void updateOrderStatus(final GBOrder order, final OrderStatus newStatus) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                Map<String, Object> updatedOrder = new HashMap<>();
                updatedOrder.put(ORDER_STATUS_ID, newStatus.toString());

                database.collection(ORDERS_IDENTIFIER).document(order.getDocumentString())
                        .update(updatedOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.UpdateOrderStatus);

                        } else {

                            Log.i(MainActivity.LOG_TAG, task.getException().getLocalizedMessage());
                            mCallback.errorResponse(FirebaseMethod.UpdateOrderStatus);

                        }

                    }
                });

                database.collection(USERS_IDENTIFIER).document(order.getUserEmail()).collection(ORDER_HISTORY_IDENTIFIER)
                        .document(order.getDocumentString()).update(updatedOrder)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.UpdateOrderStatus);

                        } else {

                            Log.i(MainActivity.LOG_TAG, task.getException().getLocalizedMessage());
                            mCallback.errorResponse(FirebaseMethod.UpdateOrderStatus);

                        }

                    }
                });

            }
        }).start();

    }

    public void archiveStatus(final GBOrder order) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                Map<String, Object> updatedOrder = new HashMap<>();
                updatedOrder.put(ARCHIVED_ID, true);

                database.collection(ORDERS_IDENTIFIER).document(order.getDocumentString()).update(updatedOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.ArchiveOrder);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.ArchiveOrder);

                        }

                    }
                });

            }
        }).start();

    }

    public void addProduct(final GBProduct product) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                Map<String, Object> newProduct = new HashMap<>();
                newProduct.put(PRODUCT_TITLE_ID, product.getProductTitle());
                newProduct.put(PRODUCT_DESCRIPTION_ID, product.getProductDescription());
                newProduct.put(PRODUCT_PRICE_ID, ("$" + product.getProductPrice()));
                newProduct.put(PRODUCT_TYPE_ID, product.getProductType().toString());
                newProduct.put(HIGHLIGHTED_PRODUCT_ID, false);

                database.collection(PRODUCTS_IDENTIFIER).add(newProduct).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulObjectResponse(FirebaseMethod.AddProduct,
                                    new GBProduct(product, task.getResult().getId()));

                        } else {

                            mCallback.errorResponse(FirebaseMethod.AddProduct);

                        }

                    }
                });

            }
        }).start();

    }

    public void editProduct(final GBProduct product) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                Map<String, Object> editProduct = new HashMap<>();
                editProduct.put(PRODUCT_TITLE_ID, product.getProductTitle());
                editProduct.put(PRODUCT_DESCRIPTION_ID, product.getProductDescription());
                editProduct.put(PRODUCT_PRICE_ID, (product.getProductPrice()));
                editProduct.put(PRODUCT_TYPE_ID, product.getProductType().toString());
                editProduct.put(HIGHLIGHTED_PRODUCT_ID, product.isHighlightedProduct());

                database.collection(PRODUCTS_IDENTIFIER).document(product.getDocumentId()).update(editProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.EditProduct);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.EditProduct);

                        }

                    }
                });

            }
        }).start();

    }

    public void deleteProduct(final GBProduct product) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                database.collection(PRODUCTS_IDENTIFIER).document(product.getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.DeleteProduct);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.DeleteProduct);

                        }

                    }
                });

            }
        }).start();

    }

    public void highlightProduct(final GBProduct product, final String discount) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                Map<String, Object> editProduct = new HashMap<>();
                editProduct.put(PRODUCT_TITLE_ID, product.getProductTitle());
                editProduct.put(PRODUCT_DESCRIPTION_ID, product.getProductDescription());
                editProduct.put(PRODUCT_PRICE_ID, (product.getProductPrice()));
                editProduct.put(PRODUCT_TYPE_ID, product.getProductType().toString());
                editProduct.put(HIGHLIGHTED_PRODUCT_ID, product.isHighlightedProduct());
                editProduct.put(HIGHLIGHTED_DISCOUNT_ID, discount);

                database.collection(PRODUCTS_IDENTIFIER).document(product.getDocumentId()).update(editProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.HighlightDiscount);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.HighlightDiscount);

                        }

                    }
                });

            }
        }).start();

    }

    public void createAccount(final String email, final String password, final String fullname, final String address, final boolean isAdmin) {

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    auth.signInWithEmailAndPassword(email, password);
                                    mCallback.successfulResponse(FirebaseMethod.CreateAccount);

                                } else {

                                    mCallback.errorResponse(FirebaseMethod.CreateAccount);

                                }

                            }
                        });

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, Object> newUserData = new HashMap<>();
                newUserData.put(ADDRESS_ID, address);
                newUserData.put(FULL_NAME_ID, fullname);
                newUserData.put(APP_MANAGER_ID, isAdmin);

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                database.collection(USERS_IDENTIFIER).document(email).set(newUserData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    mCallback.successfulResponse(FirebaseMethod.CreateAccount);

                                } else {

                                    mCallback.errorResponse(FirebaseMethod.CreateAccount);

                                }


                            }
                        });

            }
        }).start();

    }

    public void deleteAccount(final String email) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                final FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.DeleteAccount);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.DeleteAccount);

                        }

                    }
                });

                database.collection(USERS_IDENTIFIER).document(email).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.DeleteAccount);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.DeleteAccount);

                        }

                    }
                });

            }
        }).start();

    }

    public void editAccount(final String email, final String fullname, final String address) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                Map<String, Object> newUserData = new HashMap<>();
                newUserData.put(ADDRESS_ID, address);
                newUserData.put(FULL_NAME_ID, fullname);

                database.collection(USERS_IDENTIFIER).document(email).update(newUserData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            mCallback.successfulResponse(FirebaseMethod.EditAccount);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.EditAccount);

                        }

                    }
                });

            }
        }).start();

    }

    public void getProductImage(final String productUrl) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.i(MainActivity.LOG_TAG, productUrl);

                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(productUrl);

                storageRef.getBytes(ONE_MB * 2).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {

                        if (task.isSuccessful()) {

                            Bitmap bmp = BitmapFactory.decodeByteArray(task.getResult(),0, task.getResult().length);
                            for (int i = 0; i < mMACallback.getProductList().size(); i++) {
                                if (mMACallback.getProductList().get(i).getProductImageUrl().matches(productUrl)) {
                                    mMACallback.getProductList().get(i).setProductImage(bmp);
                                    mCallback.successfulResponse(FirebaseMethod.GetProductImage);
                                }
                            }

                        } else {

                            mCallback.errorResponse(FirebaseMethod.GetProductImage);

                        }

                    }
                });

            }
        }).start();

    }

    public void getAllAdminUsers() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                database.collection(USERS_IDENTIFIER).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                            for (int i = 0; i < snapshots.size(); i++) {

                                DocumentSnapshot snapshot = snapshots.get(i);
                                if (snapshot.contains(APP_MANAGER_ID)) {

                                    String address = "";
                                    if (snapshot.contains(ADDRESS_ID)) {

                                        address = snapshot.getString(ADDRESS_ID);

                                    }

                                    String fullName = "";
                                    if (snapshot.contains(FULL_NAME_ID)) {

                                        fullName = snapshot.getString(FULL_NAME_ID);
                                        GBUser user = new GBUser(snapshot.getId(), fullName, address, true);
                                        if (mCallback != null) {
                                            mCallback.successfulObjectResponse(FirebaseMethod.GetAllAdminUsers, user);
                                        }
                                    }



                                }

                            }

                            mCallback.successfulResponse(FirebaseMethod.GetAllAdminUsers);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.GetAllAdminUsers);

                        }

                    }
                });

            }
        }).start();

    }

    public void getAllAddresses(final String email) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setTimestampsInSnapshotsEnabled(true)
                        .build();
                database.setFirestoreSettings(settings);

                database.collection(USERS_IDENTIFIER).document(email).collection(ADDRESSES_IDENTIFIER).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            List<DocumentSnapshot> snapshots = task.getResult().getDocuments();
                            for (int i = 0; i < snapshots.size(); i++) {
                                mCallback.successfulObjectResponse(FirebaseMethod.GetAllAddresses, snapshots.get(i).getString(ADDRESS_ID));
                            }
                            mCallback.successfulResponse(FirebaseMethod.GetAllAddresses);

                        } else {

                            mCallback.errorResponse(FirebaseMethod.GetAllAddresses);

                        }

                    }
                });

            }
        }).start();

    }

}
