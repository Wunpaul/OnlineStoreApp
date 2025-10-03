package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.CustomerAuthDTO;
import com.coffencode.onlinestoreapplication.dto.CustomerCreateDTO;
import com.coffencode.onlinestoreapplication.dto.CustomerDTO;
import com.coffencode.onlinestoreapplication.dto.SignupRequest;

import java.util.List;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerCreateDTO dto);
    CustomerAuthDTO createCustomerForAuth(SignupRequest dto);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Long id);
    CustomerDTO updateCustomer(Long id, CustomerDTO dto);
    void deleteCustomer(Long id);
}
