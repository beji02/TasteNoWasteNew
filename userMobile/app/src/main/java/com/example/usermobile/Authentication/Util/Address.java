package com.example.usermobile.Authentication.Util;

public class Address {
    public String street;
    private String house;
    private String city;
    private String county;
    private String country;
    private String zipCode;

    public Address(String street, String house, String city, String county, String country, String zipCode) {
        this.street = street;
        this.house = house;
        this.city = city;
        this.county = county;
        this.country = country;
        this.zipCode = zipCode;
    }
}
