package com.top.greenbeans.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.top.greenbeans.CustomObjects.GBOrder;
import com.top.greenbeans.Interfaces.OrdersAdapterCallback;
import com.top.greenbeans.R;

import java.util.List;

public class OrdersAdapter extends ArrayAdapter<GBOrder> {

    /*
     *
     * Member Variables
     *
     */

    private Context mContext;
    private List<GBOrder> mOrdersList;



    /*
     *
     * Constructor
     *
     */

    public OrdersAdapter(Context context, List<GBOrder> orders) {

        super(context, R.layout.listview_order_item_layout, orders);
        mContext = context;
        mOrdersList = orders;

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

            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.listview_order_item_layout, parent, false);

        }

        TextView tv = convertView.findViewById(R.id.tv_order_id);
        tv.setText(mOrdersList.get(position).getOrderTime());

        tv = convertView.findViewById(R.id.tv_order_status);
        tv.setText(mOrdersList.get(position).getOrderStatus());

        return convertView;
    }

}
