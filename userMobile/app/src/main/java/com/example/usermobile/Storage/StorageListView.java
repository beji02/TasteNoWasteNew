package com.example.usermobile.Storage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.usermobile.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class StorageListView extends AppCompatActivity {
    private ListView storageListView;
    private Storage productStorage;
    private ArrayList<Product> productList;
    StorageListAdapter storageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_list_view);

        storageListView = (ListView)findViewById(R.id.storageList);
        populateProductList();

        storageListAdapter = new StorageListAdapter(productList, R.layout.product_list_row, this );
        storageListView.setAdapter(storageListAdapter);

        storageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                Product product = (Product) parent.getItemAtPosition(position);

                ProductDialog productDialog = new ProductDialog(product, StorageListView.this);
                productDialog.show(getSupportFragmentManager(), "productDialog");

            }
        });
    }

    private void populateProductList () {
        productStorage = new Storage();

        productStorage.addProduct(new Product("Tomato", 300, "2021-12-03", "vegetable",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Apple", 100, "2021-11-03", "fruit",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Meat", 300, "2021-09-03", "meat",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Meat", 300, "2022-01-03", "meat",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Meat", 300, "2021-12-29", "meat",
                new String[] {"da", "nu"}));
        productStorage.addProduct(new Product("Meat", 300, "2022-10-03", "meat",
                new String[] {"da", "nu"}));
        productList = productStorage.getProductList();
        sortProductListAscending();
    }

    /** Updates the products in the list. */
    public void updateProductStorage (Storage productStorage){
        this.productStorage = productStorage;
        productList = productStorage.getProductList();
        sortProductListAscending();
    }

    /** Sorts the productList in ascending order after expiration date. */
    public void sortProductListAscending () {
        Collections.sort(productList, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getExpirationDate().compareTo(o2.getExpirationDate());
            }
        });
    }

    /** Deletes the product from the list and updates the listview. */
    public void deleteProduct (Product product) {
        productStorage.deleteProduct(product);
        storageListAdapter.remove(product);
    }
}