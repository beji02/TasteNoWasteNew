package com.example.usermobile.Storage;

public class Product {
    private String name;
    private int quantity;
    private String expirationDate;
    private String category;
    private String packages;
    private String idCode;
    private String photoLink;

    public Product() {

    }

    /**
     * @param name
     * @param quantity
     * @param expirationDate
     * @param category
     * @param packages
     */
    public Product (final String name, final int quantity, final String expirationDate, final String category, final String packages, final String photoLink){
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.category = category;
        this.photoLink = photoLink;
        this.packages = packages;
    }

    public Product (final String name, final int quantity, final String expirationDate, final String category, final String packages, final String photoLink, final String idCode){
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.category = category;
        this.packages = packages;
        this.photoLink = photoLink;
        this.idCode = idCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
