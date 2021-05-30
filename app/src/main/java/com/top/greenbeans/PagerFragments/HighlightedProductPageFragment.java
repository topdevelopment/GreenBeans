//
//
// TOP Development
// HighlightedProductPageFragment.java
//
//

package com.top.greenbeans.PagerFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.R;

public class HighlightedProductPageFragment extends Fragment {

    /*
     *
     * Member Variables
     *
     */

    private GBProduct mHighlightedProduct;
    private int mPosition = -1;



    /*
     *
     * Constructor
     *
     */

    public static HighlightedProductPageFragment newInstance(GBProduct highlightedProduct) {
        
        Bundle args = new Bundle();
        
        HighlightedProductPageFragment fragment = new HighlightedProductPageFragment();
        fragment.setArguments(args);
        fragment.mHighlightedProduct = highlightedProduct;
        return fragment;
    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.page_frag_highlight_product_layout, container, false);

        TextView tv = layoutView.findViewById(R.id.tv_product_name);
        tv.setText(mHighlightedProduct.getProductTitle());

        tv = layoutView.findViewById(R.id.tv_product_discount);
        tv.setText(mHighlightedProduct.getHighlightedDiscount() + " OFF");

        tv = layoutView.findViewById(R.id.tv_product_price);
        tv.setText(mHighlightedProduct.getProductPrice());

        return layoutView;

    }



    /*
     *
     * Class Methods
     *
     */

    public void setPosition(int position) {

        mPosition = position;

    }

}
