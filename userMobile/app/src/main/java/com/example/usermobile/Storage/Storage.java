package com.example.usermobile.Storage;

import java.util.ArrayList;

public class Storage {
    ArrayList <Product> productList;

    public Storage (){
        productList = new ArrayList<>();
    }

    public Storage (final ArrayList<Product> productList){
        this.productList = productList;
    }

    public ArrayList<Product> getProductList (){
        return this.productList;
    }

    public void setProductList (ArrayList<Product> newProductList){
        this.productList = newProductList;
    }

    public void addProduct (Product product){
        productList.add(product);
    }
}
