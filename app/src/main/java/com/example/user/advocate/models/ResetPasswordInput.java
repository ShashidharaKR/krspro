package com.example.user.advocate.models;

public class ResetPasswordInput {
    public String new_password;
    public String mobile;

    public ResetPasswordInput(String new_password, String mobile) {
        this.new_password = new_password;
        this.mobile = mobile;
    }
}
