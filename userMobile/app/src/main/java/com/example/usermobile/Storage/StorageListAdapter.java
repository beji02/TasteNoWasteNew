package com.example.usermobile.Storage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.usermobile.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class StorageListAdapter extends ArrayAdapter<Product>{

    private ArrayList<Product> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtQuantity;
        TextView txtDate;
    }

    public StorageListAdapter(ArrayList<Product> data, int resource, Context context) {
        super(context, resource , data);
        this.dataSet = data;
        this.mContext=context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.product_list_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txtQuantity = (TextView) convertView.findViewById(R.id.quantity);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.expirationDate);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtName.setText("Item: " +  dataModel.getName());
        viewHolder.txtQuantity.setText("Quant: " + Integer.toString(dataModel.getQuantity()));
        viewHolder.txtDate.setText("Exp. date: " + dataModel.getExpirationDate().toString());

        // Return the completed view to render on screen
        return convertView;
    }
}