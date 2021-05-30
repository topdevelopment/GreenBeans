//
//
// TOP Development
// GBProduct.java
//
//

package com.top.greenbeans.CustomObjects;

import android.graphics.Bitmap;

import com.top.greenbeans.Enums.ProductType;

public class GBProduct {

    /*
     *
     * Member Variables
     *
     */

    private String mDocumentId;
    private String mProductTitle;
    private String mProductDesc;
    private ProductType mProductType;
    private String mProductPrice;
    private int mProductQuantity;
    private boolean mHighlightedProduct;
    private String mHighlightedDiscount;
    private Bitmap mProductImage = null;
    private String mProductImageUrl;



    /*
     *
     * Constructor
     *
     */

    public GBProduct(String documentId, String title, String desc, ProductType type, String price, String productImageUrl) {

        mProductTitle = title;
        mProductDesc = desc;
        mProductType = type;
        mProductPrice = price;
        mProductQuantity = 1;
        mHighlightedProduct = false;
        mHighlightedDiscount = "";
        mDocumentId = documentId;
        mProductImageUrl = productImageUrl;

    }

    public GBProduct(String documentId, String title, String desc, ProductType type, String price, String highlightedDiscount, String productImageUrl) {

        mProductTitle = title;
        mProductDesc = desc;
        mProductType = type;
        mProductPrice = price;
        mProductQuantity = 1;
        mHighlightedProduct = true;
        mHighlightedDiscount = highlightedDiscount;
        mDocumentId = documentId;
        mProductImageUrl = productImageUrl;

    }

    public GBProduct(GBProduct productShell, String documentId) {

        mProductTitle = productShell.getProductTitle();
        mProductDesc = productShell.getProductDescription();
        mProductType = productShell.getProductType();
        mProductPrice = productShell.getProductPrice();
        mProductQuantity = productShell.getQuantity();
        mHighlightedProduct = productShell.isHighlightedProduct();
        mHighlightedDiscount = productShell.getHighlightedDiscount();
        mDocumentId = documentId;
        mProductImageUrl = productShell.getProductImageUrl();

    }



    /*
     *
     * Class Methods
     *
     */

    public String getProductTitle() {

        return mProductTitle;

    }

    public String getProductDescription() {

        return mProductDesc;

    }

    public ProductType getProductType() {

        return mProductType;

    }

    public void updateProductType(ProductType type) {

        mProductType = type;

    }

    public String getProductPrice() {

        return mProductPrice;

    }

    public void updateQuantity(int amount) {

        mProductQuantity += amount;

    }

    public int getQuantity() {

        return mProductQuantity;

    }

    public boolean isHighlightedProduct() {

        return mHighlightedProduct;

    }

    public void toggleHighlightProduct() {

        mHighlightedProduct = !mHighlightedProduct;

    }

    public String getHighlightedDiscount() {

        return mHighlightedDiscount;

    }

    public String getDocumentId() {

        return mDocumentId;

    }

    public void setProductImage(Bitmap productImage) {

        mProductImage = productImage;

    }

    public Bitmap getProductImage() {

        return mProductImage;

    }

    public String getProductImageUrl() {

        return mProductImageUrl;

    }

}
