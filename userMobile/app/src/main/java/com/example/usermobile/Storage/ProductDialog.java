package com.example.usermobile.Storage;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.usermobile.R;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ProductDialog extends DialogFragment {
    Button buttonDelete, buttonCancel;
    Product product;
    StorageListView storageParent;

    public ProductDialog(final Product product, StorageListView storageParent) {
        this.product = product;
        this.storageParent = storageParent;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_product, container);
        setInformation(view);

        buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(v -> Objects.requireNonNull(getDialog()).dismiss());


        buttonDelete.setOnClickListener(v -> {
            storageParent.deleteProduct(product);
            Objects.requireNonNull(getDialog()).dismiss();
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setInformation(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtName = (TextView) view.findViewById(R.id.nameDialog);
        viewHolder.txtQuantityValue = (EditText) view.findViewById(R.id.quantityEditDialog);
        viewHolder.txtQuantityUnitOfMeasure = (TextView) view.findViewById(R.id.quantityUnitOfMeasureDialog);
        viewHolder.txtDate = (TextView) view.findViewById(R.id.expirationDateDialog);
        viewHolder.txtCategory = (TextView) view.findViewById(R.id.categoryDialog);

        viewHolder.txtName.setText(product.getName());
        viewHolder.txtDate.setText("Expiration date: " + product.getExpirationDate());

        if (!product.getUnitOfMeasure().isEmpty()) {
            viewHolder.txtQuantityValue.setText(Integer.toString(product.getQuantity()));
            viewHolder.txtQuantityUnitOfMeasure.setText(" " + product.getUnitOfMeasure());
        } else {
            viewHolder.txtQuantityValue.setText(Integer.toString(product.getQuantity()));
        }

        if (!product.getCategory().isEmpty()) {
            viewHolder.txtCategory.setText("Category: " + product.getCategory());
        } else {
            viewHolder.txtCategory.setText("Category: " + "unknown");
        }

        long dateDifference = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.parse(product.getExpirationDate()));
        if (dateDifference < 0) {
            viewHolder.txtDate.setTextColor(Color.parseColor("#FF0000"));
            viewHolder.txtDate.setText("Expired on " + product.getExpirationDate() + ".");
        } else {
            viewHolder.txtDate.setTextColor(Color.parseColor("#55FF00"));
            viewHolder.txtDate.setText("Expires on " + product.getExpirationDate() + ".");
        }
    }

    private static class ViewHolder {
        TextView txtName;
        EditText txtQuantityValue;
        TextView txtQuantityUnitOfMeasure;
        TextView txtDate;
        TextView txtCategory;
    }
}
