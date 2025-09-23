package com.coffencode.onlinestoreapplication.dto;

public class CustomerCreateDTO {
    private String name;
    private String email;
    private String password;

    public CustomerCreateDTO() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
