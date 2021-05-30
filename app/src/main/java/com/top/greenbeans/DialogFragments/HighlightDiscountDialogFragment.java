//
//
// TOP Development
// HighlightDiscountDialogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.Interfaces.MACallback;
import com.top.greenbeans.R;

public class HighlightDiscountDialogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private MACallback mMACallback;
    private DialogCallback.HighlightDiscountDialogCallback mDialogCallback;



    /*
     *
     * Constructor
     *
     */

    public HighlightDiscountDialogFragment(MACallback maCallback, DialogCallback.HighlightDiscountDialogCallback callback) {

        mMACallback = maCallback;
        mDialogCallback = callback;

    }



    /*
     *
     * Lifecycle Methods
     *
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layoutView = inflater.inflate(R.layout.dialog_highlight_discount_layout, container, false);

        final EditText et = layoutView.findViewById(R.id.edittext_discount);

        Button b = layoutView.findViewById(R.id.button_confirm);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String discount = et.getText().toString();

                if (discount.isEmpty()) {
                    mMACallback.postToastMessage(getResources().getString(R.string.toast_message_no_discount));
                    return;
                }

                if (!discount.contains("%")) {
                    discount = discount.concat("%");
                }

                mDialogCallback.highlightDiscountConfirmed(discount);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_cancel);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });

        return layoutView;

    }
}
