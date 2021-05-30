//
//
// TOP Development
// AdminProductsAdapter.java
//
//

package com.top.greenbeans.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.R;

import java.net.ContentHandler;
import java.util.ArrayList;

public class AdminProductsAdapter extends ArrayAdapter<GBProduct> {

    /*
     *
     * Member Variables
     *
     */

    private Context mContext;
    private ArrayList<GBProduct> mProducts;



    /*
     *
     * Constructor
     *
     */

    public AdminProductsAdapter(Context context, ArrayList<GBProduct> products) {

        super(context, R.layout.listview_admin_product_item_layout, products);
        mProducts = products;
        mContext = context;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.listview_admin_product_item_layout, parent, false);

        }

        TextView tv = convertView.findViewById(R.id.tv_product_name);
        tv.setText(mProducts.get(position).getProductTitle());

        tv = convertView.findViewById(R.id.tv_product_desc);
        tv.setText(mProducts.get(position).getProductDescription());

        return convertView;

    }

}
