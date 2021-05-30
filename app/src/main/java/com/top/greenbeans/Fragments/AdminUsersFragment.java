//
//
// TOP Development
// AdminUsersFragment.java
//
//

package com.top.greenbeans.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.top.greenbeans.Adapters.UserAdapter;
import com.top.greenbeans.CustomObjects.GBUser;
import com.top.greenbeans.Enums.DialogType;
import com.top.greenbeans.Enums.FirebaseMethod;
import com.top.greenbeans.Enums.OrderStatus;
import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.AdminCallback;
import com.top.greenbeans.Interfaces.AdminDialogCallback;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.FirebaseCallback;
import com.top.greenbeans.Interfaces.MACallback;
import com.top.greenbeans.R;

import java.util.ArrayList;

public class AdminUsersFragment
        extends BaseFragment
        implements AdminDialogCallback, FirebaseCallback, DialogCallback.ConfirmDialogCallback {

    /*
     *
     * Member Variables
     *
     */

    private AdminCallback mAdminCallback;
    private UserAdapter mUserAdapter;
    private ArrayList<GBUser> mUsersList = new ArrayList<>();
    private GBUser mSelectedUser = null;
    private GBUser mSlotUser = null;
    private int mResponseCount = 0;



    /*
     *
     * Constructor
     *
     */

    public static AdminUsersFragment newInstance() {

        Bundle args = new Bundle();

        AdminUsersFragment fragment = new AdminUsersFragment();
        fragment.setArguments(args);
        return fragment;
    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof AdminCallback) {
            mAdminCallback = (AdminCallback) context;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.title_users);

        mFirebaseService.getAllAdminUsers();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.fragment_admin_users_layout, container, false);
        final AdminUsersFragment frag = this;

        Button b = layoutView.findViewById(R.id.button_add);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAdminCallback.addUser(frag);

            }
        });

        b = layoutView.findViewById(R.id.button_edit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedUser == null) {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_selected_user));

                } else {

                    mAdminCallback.editUser(frag, mSelectedUser);

                }

            }
        });

        b = layoutView.findViewById(R.id.button_delete);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedUser == null) {

                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_selected_user));

                } else {

                    mMACallback.createDialog(DialogType.Confirm, frag);

                }

            }
        });

        ListView lv = layoutView.findViewById(R.id.listview_users);
        mUserAdapter = new UserAdapter(mMACallback.getActivityContext(), mUsersList);
        lv.setAdapter(mUserAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                parent.getSelectedView().setSelected(false);
                view.setSelected(true);
                mSelectedUser = mUsersList.get(position);

            }
        });

        return layoutView;

    }



    /*
     *
     * Class Methods
     *
     */

    @Override // AdminDialogCallback Interface Method
    public void addUserResponse(GBUser user) {

        mSlotUser = user;
        mResponseCount = 0;
        mFirebaseService.createAccount(user.getEmail(), "password", user.getFullName(), user.getAddresses().get(0), true);

    }

    @Override // AdminDialogCallback Interface Method
    public void editUserResponse(GBUser user) {

        mSlotUser = user;
        mFirebaseService.editAccount(user.getEmail(), user.getFullName(), user.getAddresses().get(0));

    }

    @Override // ConfirmDialogCallback Interface Method
    public void confirmResponse(boolean confirm) {

        if (confirm) {

            mResponseCount = 0;
            mFirebaseService.deleteAccount(mMACallback.getCurrentUser().getEmail());

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.DeleteAccount) {

            mResponseCount++;
            if (mResponseCount == 2) {

                mMACallback.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mUsersList.remove(mSelectedUser);
                        mUserAdapter.notifyDataSetChanged();
                        mMACallback.postToastMessage(getResources().getString(R.string.toast_message_account_deleted_success));
                    }
                });

            }

        } else if (method == FirebaseMethod.CreateAccount) {

            mResponseCount++;
            if (mResponseCount == 2) {

                mMACallback.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        mUsersList.add(mSlotUser);
                        mUserAdapter.notifyDataSetChanged();
                        mMACallback.postToastMessage(getResources().getString(R.string.toast_message_account_created_success));
                    }
                });

            }

        } else if (method == FirebaseMethod.EditAccount) {

            mMACallback.runOnMainThread(new Runnable() {
                @Override
                public void run() {

                    mUserAdapter.notifyDataSetChanged();
                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_account_edit_success));
                }
            });

        }

    }

    @Override // FirebaseCallback Interface Method
    public void successfulObjectResponse(FirebaseMethod method, Object obj) {

        if (method == FirebaseMethod.GetAllAdminUsers && obj instanceof GBUser) {

            mUsersList.add((GBUser) obj);
            mUserAdapter.notifyDataSetChanged();

        }

    }

    @Override // FirebaseCallback Interface Method
    public void errorResponse(FirebaseMethod method) {

        if (method == FirebaseMethod.DeleteAccount) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_account_deleted_error));

        } else if (method == FirebaseMethod.CreateAccount) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_account_created_error));

        } else if (method == FirebaseMethod.EditAccount) {

            mMACallback.postToastMessage(getResources().getString(R.string.toast_message_account_edit_failed));

        }

    }

}
