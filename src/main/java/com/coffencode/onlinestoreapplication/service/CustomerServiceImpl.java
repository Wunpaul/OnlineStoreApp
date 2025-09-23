package com.coffencode.onlinestoreapplication.service;

import com.coffencode.onlinestoreapplication.dto.CustomerCreateDTO;
import com.coffencode.onlinestoreapplication.dto.CustomerDTO;
import com.coffencode.onlinestoreapplication.entities.Cart;
import com.coffencode.onlinestoreapplication.entities.Customer;
import com.coffencode.onlinestoreapplication.mapper.CustomerMapper;
import com.coffencode.onlinestoreapplication.repositories.CartRepository;
import com.coffencode.onlinestoreapplication.repositories.CustomerRepository;
import com.coffencode.onlinestoreapplication.exceptions.CustomerNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CustomerServiceImpl(CustomerRepository customerRepository, CartRepository cartRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerCreateDTO dto) {
        Customer c = new Customer();
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        c.setPassword(passwordEncoder.encode(dto.getPassword()));
        Customer saved = customerRepository.save(c);

        Cart cart = new Cart();
        cart.setCustomer(saved);
        Cart savedCart = cartRepository.save(cart);
        saved.setCart(savedCart);
        // persist association
        customerRepository.save(saved);

        return CustomerMapper.toDTO(saved);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(CustomerMapper::toDTO)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer existing = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        // password updates handled separately (not via this DTO)
        Customer saved = customerRepository.save(existing);
        return CustomerMapper.toDTO(saved);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }
}
