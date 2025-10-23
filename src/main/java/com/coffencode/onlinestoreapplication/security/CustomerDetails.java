package com.coffencode.onlinestoreapplication.security;

import com.coffencode.onlinestoreapplication.entities.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomerDetails implements UserDetails {

    private final Customer customer;

    public CustomerDetails(Customer customer) {
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // If your Customer has roles, return them here
        return Collections.emptyList(); // or List.of(new SimpleGrantedAuthority(customer.getRole()))
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        // Spring Security uses this as the "login field"
        // If you log in with email, return email
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    // Optional helper getters (for convenience)
//    public String getFirstName() {
//        return customer.getFirstName();
//    }
//
//    public String getLastName() {
//        return customer.getLastName();
//    }

    public Long getId() {
        return customer.getId();
    }
}
