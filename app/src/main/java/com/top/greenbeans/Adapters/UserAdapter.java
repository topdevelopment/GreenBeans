//
//
// TOP Development
// UserAdapter.java
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

import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.R;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<GBUser> {

    /*
     *
     * Member Variables
     *
     */

    private Context mContext;
    private ArrayList<GBUser> mUsers;



    /*
     *
     * Constructor
     *
     */

    public UserAdapter(Context context, ArrayList<GBUser> users) {

        super(context, R.layout.listview_user_item_layout, users);
        mContext = context;
        mUsers = users;

    }



    /*
     *
     * Class Methods
     *
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.listview_user_item_layout, parent, false);

        }

        TextView tv = convertView.findViewById(R.id.tv_user_fullname);
        tv.setText(mUsers.get(position).getFullName());

        tv = convertView.findViewById(R.id.tv_user_email);
        tv.setText(mUsers.get(position).getEmail());

        return convertView;

    }

}
