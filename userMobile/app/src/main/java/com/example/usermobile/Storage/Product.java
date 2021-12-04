package com.example.usermobile.Storage;

import java.time.LocalDate;

public class Product {
    private final String name;
    private final int quantity;
    private LocalDate expirationDate;
    private final String category;
    private final String[] packages;
    private final String imgURL;

    /**
     * @param name
     * @param quantity
     * @param expirationDate
     * @param category
     * @param packages
     */
    public Product(final String name, final int quantity, final String expirationDate, final String category, final String[] packages, final String imgURL) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = LocalDate.parse(expirationDate);
        this.category = category;
        this.packages = packages;
        this.imgURL = imgURL;
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

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public String[] getPackages() {
        return packages;
    }

    public String getImgURL() {
        return imgURL;
    }
}
