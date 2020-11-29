package com.ledungcobra.cafo.models.user;

import java.util.ArrayList;

public class UserLogin {
    private String username;
    private String password;
    private String email;
    private ArrayList<String> roles;
    private String phone_number;

    public UserLogin(String username, String password, String email, ArrayList<String> roles, String phone_number) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.phone_number = phone_number;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
