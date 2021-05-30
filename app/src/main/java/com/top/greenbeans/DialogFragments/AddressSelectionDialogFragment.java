//
//
// TOP Development
// AddressSelectionDialogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

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
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.Adapters.AddressesAdapter;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.MACallback;
import com.top.greenbeans.R;

public class AddressSelectionDialogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private MACallback mMACallback;
    private DialogCallback.AddressSelectionDialogCallback mDialogCallback;
    private AddressesAdapter mAddressAdapter;
    private String mSelectedAddress = "";



    /*
     *
     * Constructor
     *
     */

    public AddressSelectionDialogFragment(DialogCallback.AddressSelectionDialogCallback callback) {

        mDialogCallback = callback;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        if (context instanceof MACallback) {
            mMACallback = (MACallback) context;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.dialog_select_address_layout, container, false);

        Button b = layoutView.findViewById(R.id.button_select);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectedAddress.isEmpty()) {
                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_address_selected));
                } else {
                    mDialogCallback.addressSelected(mSelectedAddress);
                    dismiss();
                }

            }
        });

        b = layoutView.findViewById(R.id.button_cancel);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });

        ListView lv = layoutView.findViewById(R.id.listview_addresses);
        mAddressAdapter = new AddressesAdapter(mMACallback.getActivityContext(), mMACallback.getCurrentUser().getAddresses());
        lv.setAdapter(mAddressAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getSelectedItem() != null && parent.getSelectedItemPosition() >= 0) {

                    parent.getSelectedView().setSelected(false);
                    parent.setSelection(position);
                    view.setSelected(true);
                    mSelectedAddress = mAddressAdapter.getItem(position);

                }

            }
        });

        return layoutView;

    }



    /*
     *
     * Class Methods
     *
     */

}
