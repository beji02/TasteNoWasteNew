package com.example.usermobile.Authentication.Util;

public class User {
    private String email;
    private String name;
    private String phoneNumber;

    private Address address;
    private Location location;

    public User(String email, String name, String phoneNumber, Address address, Location location) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;

        this.address = address;
        this.location = location;
    }


}
