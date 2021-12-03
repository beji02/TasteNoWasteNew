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

    public User(String email, String name, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
