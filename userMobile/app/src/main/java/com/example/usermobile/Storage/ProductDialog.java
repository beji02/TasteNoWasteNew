package com.example.usermobile.Storage;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.usermobile.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductDialog extends DialogFragment {
    Button buttonDelete, buttonCancel;
    Product product;
    StorageListView storageParent;

    private static class ViewHolder {
        TextView txtName;
        TextView txtQuantity;
        TextView txtDate;
        TextView txtCategory;
    }

    public ProductDialog (final Product product, StorageListView storageParent) {
        this.product = product;
        this.storageParent = storageParent;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_product, container);
        setInformation(view);

        buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageParent.deleteProduct(product);
                getDialog().dismiss();
            }
        });

        return view;
    }

    private void setInformation (View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtName = (TextView) view.findViewById(R.id.nameDialog);
        viewHolder.txtQuantity = (TextView) view.findViewById(R.id.quantityDialog);
        viewHolder.txtDate = (TextView) view.findViewById(R.id.expirationDateDialog);

        viewHolder.txtName.setText(product.getName());
        viewHolder.txtDate.setText("Expiration date: " + product.getExpirationDate().toString());

        if (!product.getUnitOfMeasure().isEmpty()) {
            viewHolder.txtQuantity.setText("Quantity: " + Integer.toString(product.getQuantity()) + " " + product.getUnitOfMeasure());
        } else {
            viewHolder.txtQuantity.setText("Quantity: " + Integer.toString(product.getQuantity()));
        }

        long dateDifference = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(product.getExpirationDate()));
        if (dateDifference < 0) {
            viewHolder.txtDate.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.txtDate.setText("Expired on " + product.getExpirationDate().toString() + " .");
        } else {
            viewHolder.txtDate.setTextColor(Color.parseColor("#55FF00"));
            viewHolder.txtDate.setText("Expires on " + product.getExpirationDate().toString() + " .");
        }
    }
}
