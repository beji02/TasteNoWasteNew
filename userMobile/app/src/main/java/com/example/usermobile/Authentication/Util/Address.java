package com.example.usermobile.Authentication.Util;

public class Address {
    private String Address;
    private String city;
    private String county;
    private String country;
    private String zipCode;

    public Address(String address, String city, String county, String country, String zipCode) {
        this.Address = address;
        this.city = city;
        this.county = county;
        this.country = country;
        this.zipCode = zipCode;
    }
}
