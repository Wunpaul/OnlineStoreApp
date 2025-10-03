package com.coffencode.onlinestoreapplication.dto;

public class AuthResponse {
    private String token;
    private CustomerAuthDTO customer;

    // ✅ No-args constructor (needed for Jackson serialization)
    public AuthResponse() {}

    // ✅ All-args constructor
    public AuthResponse(String token, CustomerAuthDTO customer) {
        this.token = token;
        this.customer = customer;
    }

    // Getters & Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public CustomerAuthDTO getCustomer() { return customer; }
    public void setCustomer(CustomerAuthDTO customer) { this.customer = customer; }
}
