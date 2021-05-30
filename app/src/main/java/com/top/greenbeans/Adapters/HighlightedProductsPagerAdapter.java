//
//
// TOP Development
// HighlightedProductsPagerAdapter.java
//
//

package com.top.greenbeans.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.PagerFragments.HighlightedProductPageFragment;

import java.util.ArrayList;

public class HighlightedProductsPagerAdapter extends FragmentStatePagerAdapter {

    /*
     *
     * Member Variables
     *
     */

    private ArrayList<GBProduct> mHighlightedProducts;



    /*
     *
     * Constructor
     *
     */

    public HighlightedProductsPagerAdapter(ArrayList<GBProduct> highlightedProducts, FragmentManager fm) {

        super(fm);
        mHighlightedProducts = highlightedProducts;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Override
    public Fragment getItem(int position) {

        HighlightedProductPageFragment frag = HighlightedProductPageFragment.newInstance(mHighlightedProducts.get(position));
        frag.setPosition(position);
        return frag;

    }

    @Override
    public int getCount() {

        return mHighlightedProducts.size();

    }

}
