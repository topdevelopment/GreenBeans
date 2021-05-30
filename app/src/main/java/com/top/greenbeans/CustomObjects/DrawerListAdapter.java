//
//
// TOP Development
// DrawerListAdapter.java
//
//

package com.top.greenbeans.CustomObjects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.top.greenbeans.R;

import java.util.List;

public class DrawerListAdapter extends ArrayAdapter {

    /*
     *
     * Member Variables
     *
     */

    private Context mContext;
    private List<DrawerListContent.DrawerItem> mData;



    /*
     *
     * Initializer
     *
     */

    public DrawerListAdapter(Context context, int resource, List<DrawerListContent.DrawerItem> objects) {

        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_drawer_item, parent, false);
        }

        DrawerListContent.DrawerItem item = mData.get(position);
        //Set the label
        TextView label1 = convertView.findViewById(R.id.drawerItemName);
        label1.setText(item.mContent);

        ImageView iv = convertView.findViewById(R.id.drawerItemIcon);
        iv.setImageResource(item.mIconId);

        return convertView;

    }

}
