package com.example.usermobile.Authentication.Util;

public class Address {
    private String address;
    private String city;
    private String county;
    private String country;
    private String zipCode;

    public Address(String address, String city, String county, String country, String zipCode) {
        this.address = address;
        this.city = city;
        this.county = county;
        this.country = country;
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
