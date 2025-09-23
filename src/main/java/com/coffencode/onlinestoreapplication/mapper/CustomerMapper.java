package com.coffencode.onlinestoreapplication.mapper;

import com.coffencode.onlinestoreapplication.dto.CustomerCreateDTO;
import com.coffencode.onlinestoreapplication.dto.CustomerDTO;
import com.coffencode.onlinestoreapplication.entities.Customer;

import java.util.stream.Collectors;

public class CustomerMapper {

    public static CustomerDTO toDTO(Customer customer) {
        if (customer == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        if (customer.getCart() != null) dto.setCartId(customer.getCart().getId());
        if (customer.getOrders() != null) {
            dto.setOrderIds(customer.getOrders().stream().map(o -> o.getId()).collect(Collectors.toList()));
        }
        return dto;
    }

    public static Customer toEntity(CustomerCreateDTO dto) {
        if (dto == null) return null;
        Customer c = new Customer();
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        // Do NOT set password here — service should encode and set it
        return c;
    }
}
