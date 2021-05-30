//
//
// TOP Development
// AddressesAdapter.java
//
//

package com.top.greenbeans.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.top.greenbeans.R;

import java.util.ArrayList;
import java.util.List;

public class AddressesAdapter extends ArrayAdapter<String> {

    /*
     *
     * Member Variables
     *
     */

    private Context mContext;
    private List<String> mAddresses;



    /*
     *
     * Constructor
     *
     */

    public AddressesAdapter(Context context, List<String> addresses) {

        super(context, R.layout.listview_address_layout,addresses);
        mContext = context;
        mAddresses = addresses;

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
                    .inflate(R.layout.listview_address_layout, parent, false);

        }

        TextView tv = convertView.findViewById(R.id.tv_address);
        tv.setText(mAddresses.get(position));

        return convertView;

    }

}
