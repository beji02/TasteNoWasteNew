package com.example.usermobile.Storage;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.usermobile.DatabaseManager.DatabaseStorageManager;
import com.example.usermobile.R;
import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ProductDialog extends DialogFragment {
    Button buttonDelete, buttonCancel;
    Product product;
    StorageListView storageParent;

    private DatabaseStorageManager databaseStorageManager;

    public ProductDialog(final Product product, StorageListView storageParent) {
        this.product = product;
        this.storageParent = storageParent;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_product, container);
        ViewHolder viewHolder = setInformation(view);

        databaseStorageManager = new DatabaseStorageManager(getContext());

        buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(v -> {
            Objects.requireNonNull(getDialog()).dismiss();
        });

        buttonDelete.setOnClickListener(v -> {
            storageParent.deleteProduct(product);
            Objects.requireNonNull(getDialog()).dismiss();
        });

        viewHolder.txtQuantityValue.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Abstract Method of TextWatcher Interface.
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Abstract Method of TextWatcher Interface.
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String quantity = viewHolder.txtQuantityValue.getText().toString();
                if (!quantity.isEmpty()) {
                    product.setQuantity(Integer.parseInt(quantity));
                } else {
                    product.setQuantity(Integer.parseInt("0"));
                }
                sendToDatabase(product);
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private ViewHolder setInformation(View view) {
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

        return viewHolder;
    }

    void sendToDatabase(Product product) {
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseStorageManager.modifyProductQuantity(userID, product);
    }

    private static class ViewHolder {
        TextView txtName;
        EditText txtQuantityValue;
        TextView txtQuantityUnitOfMeasure;
        TextView txtDate;
        TextView txtCategory;
    }
}
