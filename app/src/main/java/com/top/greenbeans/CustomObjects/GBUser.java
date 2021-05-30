//
//
// TOP Development
// GBUser.java
//
//

package com.top.greenbeans.CustomObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GBUser {

    /*
     *
     * Member Variables
     *
     */

    private String mFullName;
    private String mEmail;
    private List<String> mAddresses;
    private boolean mAppAdmin;
    private ArrayList<GBProduct> mKart;



    /*
     *
     * Constructor
     *
     */

    public GBUser(String email, String fullName, String address, boolean admin) {

        mEmail = email;
        mFullName = fullName;
        mAddresses = new ArrayList<>();
        mAddresses.add(address);
        mAppAdmin = admin;
        mKart = new ArrayList<>();

    }



    /*
     *
     * Class Methods
     *
     */

    public String getEmail() {

        return mEmail;

    }

    public String getFullName() {

        return mFullName;

    }

    public List<String> getAddresses() {

        return mAddresses;

    }

    public void addAddress(String address) {

        mAddresses.add(address);

    }

    public boolean isAdmin() {

        return mAppAdmin;

    }

    public ArrayList<GBProduct> getKart() {

        return mKart;

    }

    public void addToKart(GBProduct product) {

        mKart.add(product);

    }

    public void updateKartQuantity(GBProduct product, int quantity) {

        int index = -1;
        for (int i = 0; i < mKart.size(); i++) {

            if (mKart.get(i) == product) {
                mKart.get(i).updateQuantity(quantity);
                if (mKart.get(i).getQuantity() <= 0) {
                    index = i;
                }
                break;
            }

        }

        if (index != -1) {
            mKart.remove(index);
        }

    }

}
