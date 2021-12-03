package com.example.usermobile.Storage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.usermobile.R;

import java.util.ArrayList;


public class StorageListView extends AppCompatActivity {

    private ListView storageListView;
    private Storage productStorage;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_list_view);

        storageListView = (ListView)findViewById(R.id.storageList);
        productStorage = new Storage();

        productStorage.addProduct(new Product("Tomato", 300, "2021-12-03", "vegetable",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Apple", 100, "2021-12-03", "fruit",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Meat", 300, "2021-12-03", "meat",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Meat", 300, "2021-12-03", "meat",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Meat", 300, "2021-12-03", "meat",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Meat", 300, "2021-12-03", "meat",
                new String[] {"da", "nu"}));
        productList = productStorage.getProductList();


        StorageListAdapter storageListAdapter = new StorageListAdapter(productList, R.layout.product_list_row, this );
        storageListView.setAdapter(storageListAdapter);
    }

    public void updateProductStorage (Storage productStorage){
        this.productStorage = productStorage;
    }
}