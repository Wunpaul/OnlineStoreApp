package com.coffencode.onlinestoreapplication.security;

import com.coffencode.onlinestoreapplication.entities.Customer;
import com.coffencode.onlinestoreapplication.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));

        // ✅ Use the actual role name from your Role entity
        String roleName = customer.getRole() != null ? customer.getRole().getName().replace("ROLE_", "") : "CUSTOMER";

        return new CustomerDetails(customer);
    }
}
