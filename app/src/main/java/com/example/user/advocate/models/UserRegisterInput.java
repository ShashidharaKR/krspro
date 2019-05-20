package com.example.user.advocate.models;

public class UserRegisterInput {
    public String name;
    public String username;
    public String password;
    public String email;
    public String mobile;
    public String address;
    public String license;
    public String lattitude;
    public String longitude;
    public String type;

    public UserRegisterInput(String name, String username, String password, String email, String mobile, String address,
                             String license, String lattitude, String longitude, String type) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.license = license;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.type = type;
    }
}
