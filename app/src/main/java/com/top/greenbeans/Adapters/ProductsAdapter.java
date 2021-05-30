//
//
// TOP Development
// ProductsAdapter.java
//
//

package com.top.greenbeans.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.Interfaces.KartCallback;
import com.top.greenbeans.Interfaces.StoreCallback;
import com.top.greenbeans.R;

import java.util.List;

public class ProductsAdapter extends ArrayAdapter<GBProduct> {

    /*
     *
     * Member Variable
     *
     */

    private Context mContext;
    private List<GBProduct> mProducts;
    private StoreCallback mCallback;



    /*
     *
     * Constructor
     *
     */

    public ProductsAdapter(Context context, List<GBProduct> products, StoreCallback callback) {

        super(context, R.layout.listview_product_item_layout, products);
        mProducts = products;
        mContext = context;
        mCallback = callback;

    }



    /*
     *
     * Class Methods
     *
     */

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_product_item_layout, parent, false);

        }

        TextView tv = convertView.findViewById(R.id.tv_product_name);
        tv.setText(mProducts.get(position).getProductTitle());

        tv = convertView.findViewById(R.id.tv_price);
        tv.setText(mProducts.get(position).getProductPrice());

        Button b = convertView.findViewById(R.id.button_product_details);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.viewProductDetails(mProducts.get(position));

            }
        });

        ImageView iv = convertView.findViewById(R.id.imageview_produt_image);
        if (mProducts.get(position).getProductImage() != null) {
            iv.setImageBitmap(mProducts.get(position).getProductImage());
        } else {
            iv.setImageResource(R.drawable.no_product_image);
        }

        return convertView;

    }

}
