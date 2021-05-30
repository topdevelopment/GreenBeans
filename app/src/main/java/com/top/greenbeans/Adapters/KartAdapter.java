//
//
// TOP Development
// KartAdapter.java
//
//

package com.top.greenbeans.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.top.greenbeans.CustomObjects.GBProduct;
import com.top.greenbeans.Interfaces.KartDetailsCallback;
import com.top.greenbeans.R;

import java.util.ArrayList;

public class KartAdapter extends ArrayAdapter<GBProduct> {

    /*
     *
     * Member Variables
     *
     */

    private Context mContext;
    private KartDetailsCallback mKartDetailsCallback;



    /*
     *
     * Constructor
     *
     */

    public KartAdapter(Context context, KartDetailsCallback callback) {

        super(context, R.layout.listview_kart_item_layout, callback.getKart());
        mContext = context;
        mKartDetailsCallback = callback;

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
                    .inflate(R.layout.listview_kart_item_layout, parent, false);

        }

        TextView tv = convertView.findViewById(R.id.tv_product_name);
        tv.setText(mKartDetailsCallback.getKart().get(position).getProductTitle());

        final EditText et = convertView.findViewById(R.id.et_current_quantity_value);
        et.setText(String.valueOf(mKartDetailsCallback.getKart().get(position).getQuantity()));

        Button b = convertView.findViewById(R.id.button_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mKartDetailsCallback.getKart().get(position).updateQuantity(1);
                et.setText(String.valueOf(mKartDetailsCallback.getKart().get(position).getQuantity()));

            }
        });

        b = convertView.findViewById(R.id.button_minus);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mKartDetailsCallback.getKart().get(position).updateQuantity(-1);
                et.setText(String.valueOf(mKartDetailsCallback.getKart().get(position).getQuantity()));

            }
        });

        return convertView;

    }

}
