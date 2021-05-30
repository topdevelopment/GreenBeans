//
//
// TOP Development
// OrderSummaryAdapter.java
//
//

package com.top.greenbeans.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.Interfaces.OrderSummaryAdapterCallback;
import com.top.greenbeans.R;

import java.util.ArrayList;

public class OrderSummaryAdapter extends ArrayAdapter<GBProduct> {

    /*
     *
     * Member Variables
     *
     */

    private Context mContext;
    private ArrayList<GBProduct> mProducts;
    private OrderSummaryAdapterCallback mCallback;



    /*
     *
     * Constructor
     *
     */

    public OrderSummaryAdapter(Context context, ArrayList<GBProduct> products, OrderSummaryAdapterCallback callback) {

        super(context, R.layout.listview_order_summary_item_layout, products);
        mProducts = products;
        mContext = context;
        mCallback = callback;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.listview_order_summary_item_layout, parent, false);

        }

        TextView tv = convertView.findViewById(R.id.tv_product_title);
        tv.setText(mProducts.get(position).getProductTitle());

        tv = convertView.findViewById(R.id.tv_quantity);
        tv.setText(String.valueOf(mProducts.get(position).getQuantity()));

        Button b = convertView.findViewById(R.id.button_minus);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProducts.get(position).updateQuantity(-1);

            }
        });

        b = convertView.findViewById(R.id.button_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProducts.get(position).updateQuantity(1);

            }
        });

        b = convertView.findViewById(R.id.button_remove);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.removeProduct(position);

            }
        });

        return convertView;

    }

}
