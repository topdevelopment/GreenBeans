//
//
// TOP Development
// ProductTypeDialogFragment.java
//
//

package com.top.greenbeans.DialogFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.top.greenbeans.Enums.ProductType;
import com.top.greenbeans.Interfaces.DialogCallback;
import com.top.greenbeans.R;

public class ProductTypeDialogFragment extends DialogFragment {

    /*
     *
     * Member Variables
     *
     */

    private DialogCallback.ProductTypeDialogCallback mDialogCallback;



    /*
     *
     * Constructor
     *
     */

    public ProductTypeDialogFragment(DialogCallback.ProductTypeDialogCallback callback) {

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

        View layoutView = inflater.inflate(R.layout.dialog_product_type_layout, container, false);

        Button b =layoutView.findViewById(R.id.button_merchandise);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.productTypeSelected(ProductType.Clothing);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_flower);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.productTypeSelected(ProductType.Flower);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_cbd);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.productTypeSelected(ProductType.CBD);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_edibles);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.productTypeSelected(ProductType.Edibles);
                dismiss();

            }
        });

        b = layoutView.findViewById(R.id.button_supplies);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialogCallback.productTypeSelected(ProductType.Supplies);
                dismiss();

            }
        });

        return layoutView;

    }

}
