//
//
// TOP Development
// DrawerListContent.java
//
//

package com.top.greenbeans.CustomObjects;

import android.content.Context;

import androidx.annotation.NonNull;

import com.top.greenbeans.Enums.ScreensID;
import com.top.greenbeans.R;

import java.util.ArrayList;

public class DrawerListContent {

    /*
     *
     * Member Variables
     *
     */

    private ArrayList<DrawerItem> mItems;



    /*
     *
     * Initializer
     *
     */

    public DrawerListContent(Context context, boolean admin) {
        mItems = new ArrayList<>();
        int count = 0;
        mItems.add(new DrawerItem(String.valueOf(count), "Virtual Store", R.drawable.icon_virtual_store, ScreensID.VirtualStore));
        count++;
//        mItems.add(new DrawerItem(String.valueOf(count), "Music & Podcasts", R.drawable.icon_music_podcasts, ScreensID.MusicPodcast));
//        count++;
        if (admin) {
            mItems.add(new DrawerItem(String.valueOf(count), "Admin Orders", R.drawable.orders, ScreensID.AdminOrders));
            count++;
            mItems.add(new DrawerItem(String.valueOf(count), "Admin Products", R.drawable.product_list, ScreensID.AdminProducts));
            count++;
            mItems.add(new DrawerItem(String.valueOf(count), "Admin Users", R.drawable.admin_users, ScreensID.AdminUsers));
            count++;
        }
        mItems.add(new DrawerItem(String.valueOf(count), "View Kart", R.drawable.kart, ScreensID.Kart));
        count++;
        mItems.add(new DrawerItem(String.valueOf(count), "Settings", R.drawable.settings, ScreensID.Settings));
        count++;

    }



    /*
     *
     * Class Methods
     *
     */

    public ArrayList<DrawerItem> getItems() {
        return mItems;
    }



    /*
     *
     * Inner Class:
     *
     */

    public class DrawerItem {

        /* Member Variables */

        public String mId;
        public String mContent;
        public int mIconId;
        private ScreensID mScreenId;



        /* Initializer */

        public DrawerItem(String id, String content, int icon_id, ScreensID screenId) {
            mId = id;
            mContent = content;
            mIconId = icon_id;
            mScreenId = screenId;
        }



        /* Class Methods */

        @NonNull
        @Override
        public String toString() {
            return mContent;
        }

        public ScreensID getScreenId() {
            return mScreenId;
        }

    }

}
