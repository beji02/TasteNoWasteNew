package com.example.usermobile.Storage;

import java.time.LocalDate;

public class Product {
    private String name;
    private int quantity;
    private LocalDate expirationDate;
    private String category;
    private String[] packages;

    /**
     * @param name
     * @param quantity
     * @param expirationDate
     * @param category
     * @param packages
     */
    public Product (final String name, final int quantity, final String expirationDate, final String category, final String[] packages){
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = LocalDate.parse(expirationDate);
        this.category = category;
        this.packages = packages;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public String[] getPackages() {
        return packages;
    }

}
